package com.example.birdfriend

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.OutputStream


class CameraFragment : Fragment() {
    companion object {
        //image pick code
        val IMAGE_PICK_CODE = 1000;

        //Permission code
        val PERMISSION_CODE = 1001;
    }

    var youAndBird: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    var canvas: Canvas = Canvas(youAndBird)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.out_of_camera).setOnClickListener {
            findNavController().navigate(R.id.action_CameraFragment_to_SecondFragment)
        }

        val context = activity?.applicationContext
        view.findViewById<Button>(R.id.upload_button).setOnClickListener {
            Log.d("crash", "in button onSetClickListener")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("crash", "in version m section")
                if (context?.let { it1 ->
                        ContextCompat.checkSelfPermission(
                            it1,
                            Manifest.permission.CAMERA
                        )
                    } == PackageManager.PERMISSION_DENIED) {
                    Log.d("crash", "doesn't have camera permission")
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_CODE
                    )
                } else {
                    Log.d("crash", "has permission")
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            Log.d("crash", "leaving onSetClickListener")
        }

        val shareButton = view.findViewById<Button>(R.id.share_load_button).setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"

            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "title")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            val uri = requireActivity().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )!!

            val outstream: OutputStream
            try {
                outstream = requireActivity().contentResolver.openOutputStream(uri)!!
                youAndBird.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                outstream.close()
            } catch (e: Exception) {
                System.err.println(e.toString())
            }

            share.putExtra(Intent.EXTRA_STREAM, uri)
            share.putExtra(Intent.EXTRA_TEXT, "you and bird")
            startActivity(Intent.createChooser(share, "Send your image!"))
        }

    }

    private fun pickImageFromGallery() {
        Log.d("crash", "in pickImageFromGallery")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        ) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
        Log.d("crash", "leaving pickImageFromGallery")
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("crash", "in onRequestPermissionsResult")
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Log.d("crash", "leaving onRequestPermissionResult")
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("crash", "in onActivityResult")
        val loadimage = requireActivity().findViewById<ImageView>(R.id.load_image)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null && data.data != null) {
                Log.d("crash", "data is not null")
                val imageUri: Uri = data.data!!
                val original =
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            imageUri
                        )
                    )
                youAndBird = original.copy(Bitmap.Config.ARGB_8888, true)
                Log.d("photo", data.data.toString())
                Log.d("photo", "it worked here!")
            } else {
                Log.d("crash", "data is null")
            }
            canvas = Canvas(youAndBird)
            // Draw the image bitmap into the cavas
            canvas.drawBitmap(youAndBird, 0.0f, 0.0f, null)
            val bMap = BitmapFactory.decodeResource(resources, R.drawable.fly_p1)
            //resize bird
            val bMapScaled = Bitmap.createScaledBitmap(bMap, 1000, 1000, true)

            //position to top right corner
            val userWidth = youAndBird.width
            val userHeight = youAndBird.height

            canvas.drawBitmap(bMapScaled, userWidth - 1500f, userHeight - 3000f, null)

            // Attach the canvas to the ImageView
            loadimage?.setImageDrawable(BitmapDrawable(resources, youAndBird))
        }
        Log.d("crash", "leaving onActivityResult")
    }

}

