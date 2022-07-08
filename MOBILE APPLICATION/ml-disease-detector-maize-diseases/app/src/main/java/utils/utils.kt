package utils

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*

private const val TIME_OUT = 60_000

val ktorHttpClient = HttpClient(Android) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.d("Inference Server", message)
            }
        }
        level = LogLevel.ALL
    }



    install(ResponseObserver) {
        onResponse { response ->
            Log.d("Inference Server", "${response.status.value}}")
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}