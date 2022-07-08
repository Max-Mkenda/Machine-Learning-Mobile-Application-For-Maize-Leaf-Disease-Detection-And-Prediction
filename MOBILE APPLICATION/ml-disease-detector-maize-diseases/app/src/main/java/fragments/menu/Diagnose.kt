package fragments.menu

import PathogenAdapter
import activities.Diagnose
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mangoesquality.databinding.DiagnoseBinding
import com.google.gson.Gson
import models.Pathogen

class Diagnose: Fragment() {

    private var _binding: DiagnoseBinding? = null
    private val binding get() = _binding!!

    private val pathogens: MutableList<Pathogen> = ArrayList()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DiagnoseBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        pathogens.add(Pathogen("Fungus", "https://content.peat-cloud.com/w400/mal-secco-citrus-1.jpg"))
        pathogens.add(Pathogen("Insect", "https://content.peat-cloud.com/w400/shoot-flies-1550421205.jpg"))
        pathogens.add(Pathogen("Bacteria", "https://content.peat-cloud.com/w400/stunt-of-maize-1.jpg"))
        pathogens.add(Pathogen("Mite", "https://content.peat-cloud.com/w400/brown-mite-1.jpg"))
        pathogens.add(Pathogen("Deficiency", "https://content.peat-cloud.com/w400/manganese-deficiency-gram-1581515473.jpg"))
        pathogens.add(Pathogen("Virus", "https://content.peat-cloud.com/w400/grapevine-leafroll-grape-1580126562.jpg"))
        pathogens.add(Pathogen("Other", "https://content.peat-cloud.com/w400/fruit-cracking-melon-1.jpg"))


        val pathogenList = binding.pathogenList
        pathogenList.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)


        val adapter = PathogenAdapter(requireContext(), pathogens) {

            val intent =
                Intent(requireContext(), Diagnose::class.java)
            intent.putExtra("pathogen", Gson().toJson(it))
            startActivity(intent)
//            requireActivity().finish()
        }

        pathogenList.adapter = adapter

    }
}