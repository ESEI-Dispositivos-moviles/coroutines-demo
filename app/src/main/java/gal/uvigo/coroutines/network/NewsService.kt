package gal.uvigo.coroutines.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import gal.uvigo.coroutines.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NewsService {
    // TODO: Retrofit already supports suspend functions out of the box when using Kotlin.

    private val authInterceptor = Interceptor { chain ->
        val apiKey = BuildConfig.NEWSAPI_KEY
        val reqBuilder = chain.request().newBuilder()
        if (apiKey.isNotBlank()) {
            reqBuilder.addHeader("X-Api-Key", apiKey)
        }
        chain.proceed(reqBuilder.build())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    val api: NewsApi = retrofit.create(NewsApi::class.java)
}