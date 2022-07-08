package fragments.menu

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mangoesquality.R
import com.example.mangoesquality.databinding.HomeFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONObject


class Home: Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val  REQUEST_LOCATION = 1


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return  binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        Picasso.get().load("https://content.peat-cloud.com/w400/manganese-deficiency-gram-1581515473.jpg").into(binding.diseaseImage)


        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activity?.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION
            )
        }

        doAsync {

            fusedLocationProviderClient.lastLocation.addOnSuccessListener {


                doAsync {

                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url("https://dark-sky.p.rapidapi.com/${it.latitude},${it.longitude}?units=auto&lang=en")
                        .get()
                        .addHeader("X-RapidAPI-Host", "dark-sky.p.rapidapi.com")
                        .addHeader("X-RapidAPI-Key", "BqpBS9gXgcmshFXGa5h0xEU32EYQp1TVRzXjsnO6CQIcdyfn1F")
                        .build()

                    val response = client.newCall(request).execute()

                    val weatherData = JSONObject(response.body!!.string())
                    val currentWeather = JSONObject(weatherData.get("currently").toString())

                    runOnUiThread {
                        val temp = currentWeather.getString("temperature")
                        binding.temperature.text = temp.toString()
                        binding.windValue.text = "${currentWeather.getString("windSpeed")} KM/H"
                        binding.humidValue.text = currentWeather.getString("humidity")
                        binding.precipValue.text = currentWeather.getString("precipProbability")
                        binding.summary.text = currentWeather.getString("summary")

                        if (currentWeather.getString("icon") == "partly-cloudy-day") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_cloudy)
                            )
                        }

                        else if (currentWeather.getString("icon") == "partly-cloudy-night") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_cloudy_night)
                            )
                        }

                        else if (currentWeather.getString("icon") == "fog") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_fog)
                            )
                        }

                        else if (currentWeather.getString("icon") == "clear-day") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_sky)
                            )
                        }

                        else if (currentWeather.getString("icon") == "clear-night") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_crescent_moon)
                            )
                        }

                        else if (currentWeather.getString("icon") == "cloudy") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_cloudy)
                            )
                        }

                        else if (currentWeather.getString("icon") == "rain") {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_rain)
                            )
                        }
                        else {
//
                            binding.icon.setImageDrawable(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_sun)
                            )
                        }



                    }
                    Log.d("Weather Data", "${currentWeather}")

                }


            }
        }


//        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
//            Log.d("Weather Data", "${it.longitude}")
//            doAsync {
//                val client = OkHttpClient()
//

//
//            }
//        }



    }
}