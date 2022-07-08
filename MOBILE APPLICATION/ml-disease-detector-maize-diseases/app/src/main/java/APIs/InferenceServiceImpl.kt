package APIs

import android.util.Log
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import models.InferenceRequest
import models.InferenceResponse

class InferenceServiceImpl(private val client: HttpClient): InferenceService {

    override suspend fun predict(inferenceRequest: InferenceRequest): InferenceResponse? {

        return try {
            client.post<InferenceResponse>{
                url(InferenceRoutes.predict)
                headers{
                    append(HttpHeaders.ContentType, "application/json")
                    append("x-api-key", "izXNbLDx5P1UEvhzPag1j5UxQ0oSkjQC7ot5VPBu")
                }
                contentType(ContentType.Application.Json)
                body = inferenceRequest
            }
        } catch (e: RedirectResponseException) {
            Log.d("Inference Error", e.response.status.description)
            null
        } catch (e: ClientRequestException) {
            Log.d("Inference Error", e.response.status.description)
            null
        }  catch (e: ServerResponseException) {
            Log.d("Inference Error", e.response.status.description)
            null
        }
        catch (e: Exception) {
            Log.d("Inference Error", "${e.message}")
            null
        }
    }
}