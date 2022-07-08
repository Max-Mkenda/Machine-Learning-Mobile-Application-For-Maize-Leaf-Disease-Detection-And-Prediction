package fragments.diagnose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mangoesquality.databinding.DiseaseDetailsBinding
import com.squareup.picasso.Picasso
import models.Disease
import org.jetbrains.anko.sdk25.coroutines.onClick

class DiseaseDetails: Fragment() {

    private var _binding: DiseaseDetailsBinding? = null
    private val binding get() = _binding!!

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
        _binding = DiseaseDetailsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disease = arguments?.get("disease") as Disease

        binding.backArrow.onClick {
            activity?.onBackPressed()
        }

        Picasso.get().load(disease.image).into(binding.diseaseImage)
        binding.diseaseName.text = disease.disease
        binding.diseaseSymptoms.text = disease.symptoms
        binding.chemicalControl.text = disease.chemicalControl
        binding.organicControl.text = disease.organicControl
        binding.triggers.text = disease.trigger
        binding.hosts.text = disease.hosts
        binding.pathogen.text = disease.pathogen
    }
}