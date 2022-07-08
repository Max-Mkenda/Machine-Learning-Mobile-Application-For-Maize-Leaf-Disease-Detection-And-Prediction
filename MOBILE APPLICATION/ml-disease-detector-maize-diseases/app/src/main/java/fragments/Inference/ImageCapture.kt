package fragments.Inference

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mangoesquality.R
import com.example.mangoesquality.databinding.TakePictureFragmentBinding
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ImageCapture: Fragment() {

    private var _binding: TakePictureFragmentBinding? = null
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
        _binding = TakePictureFragmentBinding.inflate(inflater, container, false)
        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraView = binding.camera
        cameraView.setLifecycleOwner(viewLifecycleOwner)

        binding.backArrow.onClick {
            activity?.onBackPressed()
        }

        cameraView.addCameraListener(object: CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                Log.d("Camera Taken", "Camera ${result}")

                result.toBitmap(1000, 1000) {
                    doAsync {
                        val stream = ByteArrayOutputStream()
                        it?.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                        val image = stream.toByteArray()
                        val base64 = Base64.encodeToString(
                            image, Base64.DEFAULT
                        )
                        Log.d("Camera Capture", "Camera $base64")
                        uiThread {
                            val bundle = bundleOf("image" to base64)
                            view.findNavController().navigate(R.id.inferenceResults, bundle)
                        }
                    }
                }

            }
        })

        cameraView.mapGesture(
            Gesture.PINCH, GestureAction.ZOOM
        )
        cameraView.mapGesture(
            Gesture.TAP, GestureAction.AUTO_FOCUS
        )
        cameraView.mapGesture(
            Gesture.LONG_TAP, GestureAction.TAKE_PICTURE
        )


        binding.flashOn.setOnClickListener {

            if (cameraView.flash !=  Flash.ON) {
                cameraView.flash= Flash.TORCH
                binding.flashOn.visibility = View.GONE
                binding.flashOff.visibility = View.VISIBLE
            }
        }

        binding.flashOff.setOnClickListener {
            if(cameraView.flash !=  Flash.OFF) {
                cameraView.flash= Flash.OFF
                binding.flashOn.visibility = View.VISIBLE
                binding.flashOff.visibility = View.GONE
            }
        }

        binding.gallery.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, 1234)
        }

        binding.capture.setOnClickListener {
            cameraView.takePicture()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1234) {
            if(resultCode == RESULT_OK) {
                val selectedImage: Uri? = data!!.data
                val imageStream: InputStream? = requireContext().contentResolver!!.openInputStream(selectedImage!!)

                val bitmapImage = BitmapFactory.decodeStream(imageStream)
                val byteArrayOutputStream =  ByteArrayOutputStream()
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

                val bundle = bundleOf("image" to imageBase64)
                runOnUiThread {
                    view?.findNavController()!!.navigate(R.id.inferenceResults, bundle)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}