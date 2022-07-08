package activities

import adapters.MenuAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.mangoesquality.R
import com.example.mangoesquality.databinding.ActivityMainBinding
import com.example.mangoesquality.databinding.CameraFragmentBinding
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.capture.onClick {
            val intent = Intent(this@MainActivity, Camera::class.java)
            startActivity(intent)
        }

        val menuAdapter = MenuAdapter(
            supportFragmentManager, 2
        )
        binding.menuViewPager.adapter = menuAdapter

        binding.bottomBar.setOnItemSelectedListener {
            when(it) {
                R.id.home -> binding.menuViewPager.currentItem = 0
                R.id.diagnose -> binding.menuViewPager.currentItem = 1
//                R.id.more -> binding.menuViewPager.currentItem = 2
            }
        }

        binding.menuViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> binding.bottomBar.setItemSelected(R.id.home)
                    1 -> binding.bottomBar.setItemSelected(R.id.diagnose)
//                    2 -> binding.bottomBar.setItemSelected(R.id.more)
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })

    }
}