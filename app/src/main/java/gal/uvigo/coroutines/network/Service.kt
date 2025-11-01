package gal.uvigo.coroutines.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Service {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://example.com/") // replace with your static JSON
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: NewsApi = retrofit.create(NewsApi::class.java)
}