import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mangoesquality.databinding.DiseaseItemBinding
import com.squareup.picasso.Picasso
import models.Disease
import org.jetbrains.anko.layoutInflater


@Suppress("DEPRECATION")
class DiseasesAdapter (
    private val context: Context,
    private val diseases: List<Disease>,
    private val onClickProperty: (Disease) -> Unit
) : RecyclerView.Adapter<DiseasesAdapter.DiseaseViewHolder>() {

    lateinit var binding: DiseaseItemBinding
    lateinit var mView: View
    override fun getItemCount(): Int = diseases.size


    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {

        holder.bindData(diseases[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiseaseViewHolder {
        binding = DiseaseItemBinding.inflate(
            context.layoutInflater, parent, false
        )
        mView = binding.root
        return  DiseaseViewHolder(mView, onClickProperty)
    }

    inner  class DiseaseViewHolder(view: View, private val onClickProperty: (Disease) -> Unit) : RecyclerView.ViewHolder(
        view
    ) {

        @SuppressLint("SetTextI18n")
        fun bindData(disease: Disease) {
            binding.diseaseName.text = disease.disease
            binding.diseaseSymptoms.text = disease.symptoms
            binding.diseasePathogen.text = disease.pathogen
            Picasso.get().load(disease.image).into(binding.diseaseImage)

            binding.diseaseCard.setOnClickListener { onClickProperty(disease) }
        }
    }
}