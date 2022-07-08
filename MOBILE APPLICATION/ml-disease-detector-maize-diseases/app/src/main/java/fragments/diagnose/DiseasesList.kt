package fragments.diagnose

import DiseasesAdapter
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.amplify.generated.graphql.ListItemsQuery
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers
import com.apollographql.apollo.GraphQLCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.mangoesquality.R
import com.example.mangoesquality.databinding.DiseasesListBinding
import com.google.gson.Gson
import models.Disease
import models.Pathogen
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread
import type.TableDiseaseFilterInput
import type.TableStringFilterInput

class DiseasesList: Fragment() {
    private var _binding: DiseasesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var appSyncClient: AWSAppSyncClient
    private var awsConfiguration: AWSConfiguration? = null

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
        _binding = DiseasesListBinding.inflate(inflater, container, false)
        awsConfiguration = AWSConfiguration(requireContext())
        appSyncClient = AWSAppSyncClient.builder()
            .context(requireContext())
            .awsConfiguration(awsConfiguration).build()
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pathogen = Gson().fromJson(requireActivity().intent.extras!!.getString("pathogen"), Pathogen::class.java)
        val diseasesList = binding.diseasesList
        binding.loader.visibility = View.VISIBLE
        diseasesList.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.backArrow.onClick {
            activity?.onBackPressed()
        }

        val pathogenFilter = TableDiseaseFilterInput.builder().pathogen(
            TableStringFilterInput.builder().contains(pathogen.name).build()
        ).build()

        appSyncClient.query(
            ListItemsQuery.builder().limit(500).filter(pathogenFilter).build()
        ).responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK).enqueue(
            object: GraphQLCall.Callback<ListItemsQuery.Data> () {
                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<ListItemsQuery.Data>) {

                    val diseases: MutableList<Disease> = ArrayList()
                    for (item in response.data()!!.listItems()!!.items()!!.shuffled()
                        .iterator()) {
                        val disease = Disease(
                            disease = item.disease(),
                            chemicalControl = item.chemicalControl()!!,
                            pathogen =  pathogen.name,
                            trigger = item.trigger()!!,
                            symptoms = item.symptoms()!!,
                            image = item.image()!!,
                            organicControl = item.organicControl()!!,
                            hosts = item.hosts()!!,
                            measures =  item.measures()!!

                        )
                        diseases.add(disease)
                    }

                    runOnUiThread {
                        binding.loader.visibility = View.GONE
                        val adapter = DiseasesAdapter(requireContext(), diseases) {
                            val disease = bundleOf("disease" to it)
                            view.findNavController().navigate(R.id.toDiseaseDetails, disease)
                        }
                        diseasesList.adapter = adapter
                    }
                }
            }
        )

    }
}