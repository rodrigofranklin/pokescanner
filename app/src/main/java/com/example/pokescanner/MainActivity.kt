package com.example.pokescanner

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pokescanner.pricing.PricingParser
import com.example.pokescanner.pricing.PricingService
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var resultText: TextView

    private val service: PricingService by lazy {
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(OkHttpClient())
            .build()
            .create(PricingService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)
        resultText = findViewById(R.id.result_text)

        fetchPricing("https://example.com/card")
    }

    private fun fetchPricing(url: String) {
        progressBar.visibility = View.VISIBLE
        resultText.text = ""
        service.fetchPricingPage(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val html = response.body()?.string().orEmpty()
                    val pricing = PricingParser().parse(html)
                    resultText.text = pricing?.let { "${'$'}{it.price} - ${'$'}{it.condition}" }
                        ?: getString(R.string.error_parsing)
                } else {
                    resultText.text = getString(R.string.error_network)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressBar.visibility = View.GONE
                resultText.text = getString(R.string.error_network)
            }
        })
    }
}
