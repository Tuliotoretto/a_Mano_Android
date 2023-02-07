package dgtic.unam.amano

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import dgtic.unam.amano.databinding.ActivityEditProfileBinding
import dgtic.unam.amano.firebase.AuthManager
import dgtic.unam.amano.firebase.StorageManager

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private var selectedPhotoUri: Uri? = null
    private var selectedPhotoBitMap: BitmapDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // load name from auth storage
        if (AuthManager.userName != null) {
            binding.textName.setText(AuthManager.userName)
        }

        // load image
        if (AuthManager.userPhotoPath != null) {
            StorageManager.downloadImage(
                AuthManager.userPhotoPath!!
            ) { bitmap ->
                binding.imageButton.setImageBitmap(bitmap)
            }
        }

        binding.imageButton.setOnClickListener {
            pickImageGallery()
        }

        binding.buttonDone.setOnClickListener {
            if (binding.textName.text!!.isEmpty()) return@setOnClickListener
            it.isEnabled = false

            // update user name and photo
            if (selectedPhotoUri != null && selectedPhotoBitMap != null) {
                val photoUri = selectedPhotoUri!!
                StorageManager.uploadImage(
                    photoUri
                ) { path ->
                    if ( path == null)  {
                        it.isEnabled = true
                        return@uploadImage
                    }

                    val name = binding.textName.text!!.toString()
                    AuthManager.setUser(
                        name,
                        Uri.parse(path),
                    ) { res ->
                        if (res) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        it.isEnabled = true
                    }
                }
            }

            // update only user name
            else {
                val name = binding.textName.text!!.toString()

                AuthManager.setUser(
                    name,
                    null,
                ) { res ->
                    if (res) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    it.isEnabled = true
                }
            }
        }
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //Toast.makeText(this, "foto seleccionada", Toast.LENGTH_SHORT).show()
            selectedPhotoUri = data.data!!

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectedPhotoBitMap = BitmapDrawable(bitmap)

            binding.imageButton.setImageDrawable(selectedPhotoBitMap)

            Log.d("EditProfileActivity", selectedPhotoUri.toString())
        }
    }
}