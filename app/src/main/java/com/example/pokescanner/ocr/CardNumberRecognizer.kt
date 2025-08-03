package com.example.pokescanner.ocr

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * Uses ML Kit to recognize the card number located at the bottom of a frame.
 * The number must match the pattern \d{1,3}/\d{1,3}.
 */
class CardNumberRecognizer(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val onCardNumberFound: (String) -> Unit
) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val pattern = Regex("\\d{1,3}/\\d{1,3}")

    fun start() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { image ->
                processImage(image)
            }
            val selector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, analysis)
        }, ContextCompat.getMainExecutor(context))
    }

    fun stop() {
        val provider = ProcessCameraProvider.getInstance(context).get()
        provider.unbindAll()
    }

    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotation = imageProxy.imageInfo.rotationDegrees
            val input = InputImage.fromMediaImage(mediaImage, rotation)
            val bitmap = input.bitmapInternal
            if (bitmap != null) {
                val number = detectNumberFromBitmap(bitmap)
                if (number != null) {
                    onCardNumberFound(number)
                }
            }
        }
        imageProxy.close()
    }

    private fun cropBottom(bitmap: Bitmap): Bitmap {
        val cropHeight = (bitmap.height * 0.25).toInt()
        return Bitmap.createBitmap(bitmap, 0, bitmap.height - cropHeight, bitmap.width, cropHeight)
    }

    /**
     * Detects a card number from the provided bitmap.
     * This method is synchronous and blocks until ML Kit finishes.
     */
    fun detectNumberFromBitmap(bitmap: Bitmap): String? {
        val cropped = cropBottom(bitmap)
        val result = Tasks.await(
            recognizer.process(InputImage.fromBitmap(cropped, 0))
        )
        return pattern.find(result.text)?.value
    }

    companion object {
        /**
         * Utility function for tests: recognize card number from a bitmap without instantiating CameraX.
         */
        fun recognize(bitmap: Bitmap): String? {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val pattern = Regex("\\d{1,3}/\\d{1,3}")
            val cropHeight = (bitmap.height * 0.25).toInt()
            val cropped = Bitmap.createBitmap(bitmap, 0, bitmap.height - cropHeight, bitmap.width, cropHeight)
            val result = Tasks.await(
                recognizer.process(InputImage.fromBitmap(cropped, 0))
            )
            return pattern.find(result.text)?.value
        }
    }
}

