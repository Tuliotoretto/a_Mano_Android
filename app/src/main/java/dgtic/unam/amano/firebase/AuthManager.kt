package dgtic.unam.amano.firebase

import android.content.Context
import android.net.Uri
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

object AuthManager {
    private val auth = FirebaseAuth.getInstance()

    val isCurrentUserActive: Boolean
        get() = auth.currentUser != null

    val userId: String
        get() = auth.currentUser!!.uid

    val userName: String?
        get() = auth.currentUser?.displayName

    val userPhotoPath: Uri?
        get() = auth.currentUser?.photoUrl

    fun setUser(name: String, profilePhotoUri: Uri?, completion: ((result: Boolean) -> Unit)?) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            if (profilePhotoUri != null) {
                photoUri = profilePhotoUri
            }
        }
        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                completion?.invoke(task.isSuccessful)
                return@addOnCompleteListener
            }

    }

    fun logOut(context: Context, completion: ((result: Boolean) -> Unit)? = null) {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion?.invoke(true)
                    return@addOnCompleteListener
                }
                completion?.invoke(false)
                return@addOnCompleteListener
            }
    }
}