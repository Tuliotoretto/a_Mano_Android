package dgtic.unam.amano.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

object StorageManager {

    private val storage = Firebase.storage
    private val storageRef = Firebase.storage.reference


    fun uploadImage(photoUri: Uri, completion: ((path: String?) -> Unit)?) {
        val fileName = UUID.randomUUID().toString()
        val imagesRef = storageRef.child("images/$fileName")

        imagesRef.putFile(photoUri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener {
                    completion?.invoke(it.toString())
                }
            }
            .addOnFailureListener {
                completion?.invoke(null)
                return@addOnFailureListener
            }
            .addOnCanceledListener {
                completion?.invoke(null)
                return@addOnCanceledListener
            }
    }

    fun downloadImage(photoUri: Uri, completion: ((bitmap: Bitmap?) -> Unit)?) {
        val httpRef = storage.getReferenceFromUrl(photoUri.toString())

        httpRef.getBytes(1024 * 1024)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it,0, it.size)
                completion?.invoke(bitmap)
            }
            .addOnFailureListener {
                completion?.invoke(null)
            }
            .addOnCanceledListener {
                completion?.invoke(null)
            }

    }
}