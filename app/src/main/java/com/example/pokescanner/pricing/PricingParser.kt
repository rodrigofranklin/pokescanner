package com.example.pokescanner.pricing

import org.jsoup.Jsoup

class PricingParser {
    fun parse(html: String): CardPricing? {
        val document = Jsoup.parse(html)
        val price = document.selectFirst(".price")?.text()
        val condition = document.selectFirst(".condition")?.text()
        return if (price != null && condition != null) {
            CardPricing(price, condition)
        } else {
            null
        }
    }
}
