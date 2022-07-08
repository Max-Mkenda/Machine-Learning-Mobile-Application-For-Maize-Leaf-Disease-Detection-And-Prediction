package fragments.Inference

import APIs.InferenceService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Base64
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.amazonaws.amplify.generated.graphql.GetItemQuery
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.mangoesquality.R

import com.example.mangoesquality.databinding.PredictionFragmentBinding
import com.jpegkit.Jpeg
import kotlinx.coroutines.launch
import models.Disease
import models.InferenceRequest
import org.jetbrains.anko.sdk25.coroutines.onClick

import org.jetbrains.anko.support.v4.runOnUiThread



class Prediction : Fragment() {

    private var _binding: PredictionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var appSyncClient: AWSAppSyncClient
    private var awsConfiguration: AWSConfiguration? = null
    private val inferenceService = InferenceService.create()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PredictionFragmentBinding.inflate(inflater, container, false)
        awsConfiguration = AWSConfiguration(requireContext())
        appSyncClient = AWSAppSyncClient.builder()
            .context(requireContext())
            .awsConfiguration(awsConfiguration).build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.onClick {
            activity?.onBackPressed()
        }

        val imageBase64 = arguments?.getString("image")
        val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
        Log.d("Camera", imageBytes.toString())

        binding.inferenceResults.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE

        lifecycleScope.launch {
            val responsePayload = inferenceService.predict(
                InferenceRequest(
                    application = "maize-diseases", image = imageBase64!!
                )
            )
            if (responsePayload != null) {

                /*
                0 -> leaf spot gray
                1 -> common rust
                2 -> northern leaf blight
                3 -> healthy
                4 -> Neither
                 */

                val _disease = responsePayload.results
                var name: String? = null
                if (_disease != 4) {
                    if (_disease == 0) {
                        name = "Leaf Spot of Maize"
                    }
                    else if (_disease == 1) {
                        name = "Common Rust of Maize"
                    }
                    else if (_disease == 2) {
                        name = "Southern Leaf Blight of Maize"
                    }

                    else if (_disease == 3){
                        name = "Healthy"
                    }

                    runOnUiThread {
                        binding.loader.visibility = View.GONE
                        binding.predictionConfidence.visibility = View.VISIBLE
                        val confidence = responsePayload.confidence * 100.00
                        binding.predictionConfidence.setProgress(confidence.toFloat(), true)
                        binding.predictionResults.text = name
                        binding.inferenceResults.visibility = View.VISIBLE
                    }

                   if (_disease != 3) {
                       appSyncClient.query(
                           GetItemQuery.builder().disease(name as String).build()
                       ).responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK).enqueue(
                           object: GraphQLCall.Callback<GetItemQuery.Data> () {
                               override fun onFailure(e: ApolloException) {

                               }

                               override fun onResponse(response: Response<GetItemQuery.Data>) {

                                   if(response.data() !== null) {
                                       Log.d("Diseases Predicted", "${response.data()!!.item}")
                                       val disease = Disease(
                                           disease = response.data()!!.item!!.disease(),
                                           image = response.data()!!.item!!.image()!!,
                                           chemicalControl = response.data()!!.item!!.chemicalControl()!!,
                                           organicControl = response.data()!!.item!!.organicControl()!!,
                                           hosts = response.data()!!.item!!.hosts()!!,
                                           symptoms = response.data()!!.item!!.symptoms()!!,
                                           trigger = response.data()!!.item!!.trigger()!!,
                                           pathogen = response.data()!!.item!!.pathogen()!!,
                                           measures = response.data()!!.item!!.measures()!!
                                       )

                                       val diseaseBundle = bundleOf("disease" to disease)
                                       runOnUiThread {
                                           binding.inferenceMeasures.visibility = View.VISIBLE
                                           binding.inferenceMeasures.onClick {
                                               view.findNavController().navigate(R.id.toDiseaseMeasures, diseaseBundle)
                                           }
                                       }
                                   }
                               }
                           }
                       )
                   }
                }
                else {
                    runOnUiThread {
                        binding.inferenceResults.visibility = View.VISIBLE
                        binding.inferenceMeasures.visibility = View.GONE
                        binding.loader.visibility = View.GONE
                        binding.predictionConfidence.visibility = View.GONE
                        binding.predictionResults.text = "Sorry we could not make any prediction at the moment, the image could not identified"
                    }
                }




            } else {
                runOnUiThread {
                    binding.loader.visibility = View.VISIBLE
                    binding.inferenceResults.visibility = View.GONE
                }
            }

        }
        val jpeg = Jpeg(imageBytes)
//
        binding.capturedImage.setJpeg(jpeg)

    }

}