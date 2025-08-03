package com.example.pokescanner.ar

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.example.pokescanner.R
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session

/**
 * Renders a price overlay aligned with a detected card.
 * Uses ARCore when available; otherwise falls back to a 2D overlay.
 */
class PriceOverlayRenderer(
    private val context: Context,
    private val container: ViewGroup,
    private val onFavorite: (() -> Unit)? = null,
    private val onShare: (() -> Unit)? = null
) {

    private var arSession: Session? = null
    private var overlayView: View? = null

    init {
        setupSession()
    }

    private fun setupSession() {
        val availability = ArCoreApk.getInstance().checkAvailability(context)
        if (availability.isSupported) {
            try {
                arSession = Session(context)
            } catch (_: Exception) {
                arSession = null
            }
        }
    }

    /** Aligns overlay to [cardBounds] and displays [price]. */
    fun render(cardBounds: Rect, price: String) {
        val overlay = overlayView ?: createOverlay()
        overlay.findViewById<TextView>(R.id.price_text).text = price

        val params = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = cardBounds.left
            topMargin = cardBounds.top
        }
        overlay.layoutParams = params
        overlay.visibility = View.VISIBLE

        arSession?.let {
            try {
                // ARCore session update to keep tracking
                it.update()
            } catch (_: Exception) {
                // Ignore update failures; overlay still renders in 2D
            }
        }
    }

    /** Updates only the price value on the overlay. */
    fun updatePrice(newPrice: String) {
        overlayView?.findViewById<TextView>(R.id.price_text)?.text = newPrice
    }

    fun hide() {
        overlayView?.visibility = View.GONE
    }

    private fun createOverlay(): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_price_overlay, container, false)
        view.setOnClickListener { showPopup(it) }
        container.addView(view)
        overlayView = view
        return view
    }

    private fun showPopup(anchor: View) {
        val popup = PopupMenu(context, anchor)
        popup.menu.add("Favoritar")
        popup.menu.add("Compartilhar")
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Favoritar" -> onFavorite?.invoke()
                "Compartilhar" -> onShare?.invoke()
            }
            true
        }
        popup.show()
    }
}

