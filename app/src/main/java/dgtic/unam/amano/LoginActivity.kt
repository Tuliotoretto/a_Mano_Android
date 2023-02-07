package dgtic.unam.amano

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dgtic.unam.amano.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashScreen.setKeepOnScreenCondition { false }

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            showAuthUI()
        }
    }

    private fun showAuthUI() {
        val phoneBuilder = AuthUI.IdpConfig.PhoneBuilder()

        // TODO DELETE THIS
        phoneBuilder.setDefaultNumber("+16505551111")

        val providers = arrayListOf(
            phoneBuilder.build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Theme_AMano)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // sign in failed
            if (response != null) {
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    makeToast(getString(com.firebase.ui.auth.R.string.fui_no_internet))
                    return
                }
            }
            makeToast("Fallo algo")
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}