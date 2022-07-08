package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.example.mangoesquality.R
import com.example.mangoesquality.databinding.ActivityDiagnoseBinding

class Diagnose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDiagnoseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Navigation.findNavController(this, R.id.diagnoseHostView)
    }
}