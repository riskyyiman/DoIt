package project.riskyiman.doit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.riskyiman.doit.viewmodel.AppDatabase

class SettingFragment : Fragment() {

    private lateinit var accountNameTextView: TextView
    private lateinit var darkModeSwitch: Switch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        accountNameTextView = view.findViewById(R.id.accountNameTextView)
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)

        // Ambil username dari SharedPreferences
        val sharedPref = activity?.getSharedPreferences("DoItPrefs", Context.MODE_PRIVATE)
        val username = sharedPref?.getString("loggedInUsername", null)

        // Tampilkan nama akun
        if (username != null) {
            val db = AppDatabase.getDatabase(requireContext())
            val userDao = db.userDao()

            lifecycleScope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserByUsername(username)
                }
                user?.let {
                    accountNameTextView.text = it.fullName
                }
            }
        }

        // Inisialisasi dark mode dari SharedPreferences
        val isDarkModeEnabled = sharedPref?.getBoolean("darkMode", false) ?: false
        darkModeSwitch.isChecked = isDarkModeEnabled

        // Listener untuk switch dark mode
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPref?.edit()?.putBoolean("darkMode", isChecked)?.apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Handle logout button
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Hapus username dari SharedPreferences
            sharedPref?.edit()?.remove("loggedInUsername")?.apply()

            // Kembali ke LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}
