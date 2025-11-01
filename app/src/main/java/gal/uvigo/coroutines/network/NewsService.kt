package gal.uvigo.coroutines.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NewsService {
    private val authInterceptor = Interceptor { chain ->
        // Resolve the API key reflectively so the source file doesn't require a compile-time
        // dependency on the generated BuildConfig class (avoids unresolved reference errors).
        val apiKey: String = try {
            val cls = Class.forName("gal.uvigo.coroutines.BuildConfig")
            val field = cls.getField("NEWSAPI_KEY")
            field.get(null) as? String ?: ""
        } catch (t: Throwable) {
            ""
        }

        val reqBuilder = chain.request().newBuilder()
        if (apiKey.isNotBlank()) {
            reqBuilder.addHeader("X-Api-Key", apiKey)
        }
        val req = reqBuilder.build()
        chain.proceed(req)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()

    val api: NewsApi = retrofit.create(NewsApi::class.java)
}