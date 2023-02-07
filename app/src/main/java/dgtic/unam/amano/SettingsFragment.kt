package dgtic.unam.amano

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dgtic.unam.amano.firebase.AuthManager

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val logOut: Preference? = findPreference("preference_log_out")

        logOut?.setOnPreferenceClickListener {
            AuthManager.logOut(requireContext()) { res ->
                if (res) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
            return@setOnPreferenceClickListener true
        }
    }
}