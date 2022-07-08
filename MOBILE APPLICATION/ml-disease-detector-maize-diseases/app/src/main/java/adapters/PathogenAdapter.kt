import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mangoesquality.databinding.PathogenItemBinding
import com.squareup.picasso.Picasso
import models.Pathogen
import org.jetbrains.anko.layoutInflater


@Suppress("DEPRECATION")
class PathogenAdapter (
    private val context: Context,
    private val amenities: List<Pathogen>,
    private val onClickProperty: (Pathogen) -> Unit
) : RecyclerView.Adapter<PathogenAdapter.PathogenViewHolder>() {

    lateinit var binding: PathogenItemBinding
    lateinit var mView: View
    override fun getItemCount(): Int = amenities.size


    override fun onBindViewHolder(holder: PathogenViewHolder, position: Int) {

        holder.bindData(amenities[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PathogenViewHolder {
        binding = PathogenItemBinding.inflate(
            context.layoutInflater, parent, false
        )
        mView = binding.root
        return  PathogenViewHolder(mView, onClickProperty)
    }

    inner  class PathogenViewHolder(view: View, private val onClickProperty: (Pathogen) -> Unit) : RecyclerView.ViewHolder(
        view
    ) {

        @SuppressLint("SetTextI18n")
        fun bindData(pathogen: Pathogen) {
            binding.pathogen.text = pathogen.name
            Picasso.get().load(pathogen.image).into(binding.pathogenImage)

            binding.pathogenCard.setOnClickListener { onClickProperty(pathogen) }
        }
    }
}