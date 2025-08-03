package com.example.pokescanner.ocr

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardNumberRecognizerInstrumentedTest {
    @Test
    fun recognizeNumberFromGeneratedBitmap() {
        val width = 200
        val height = 200
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 48f
        }
        canvas.drawText("123/456", 20f, height - 10f, paint)

        val result = CardNumberRecognizer.recognize(bitmap)
        assertEquals("123/456", result)
    }
}

