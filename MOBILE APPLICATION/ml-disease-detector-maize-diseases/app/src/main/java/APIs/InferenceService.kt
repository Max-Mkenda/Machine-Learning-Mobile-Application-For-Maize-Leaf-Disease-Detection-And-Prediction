package APIs

import models.InferenceRequest
import models.InferenceResponse
import utils.ktorHttpClient


interface InferenceService {
    suspend fun predict(inferenceRequest: InferenceRequest): InferenceResponse?

    companion object {
        fun create(): InferenceService {
            return InferenceServiceImpl(
                client = ktorHttpClient
            )
        }
    }

}