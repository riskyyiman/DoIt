package project.riskyiman.doit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.riskyiman.doit.viewmodel.AppDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView // Tambahan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.nextButton)
        signupText = findViewById(R.id.signupText) // Inisialisasi TextView

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        loginButton.setOnClickListener {
            val emailInput = emailEditText.text.toString()
            val passwordInput = passwordEditText.text.toString()

            if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = withContext(Dispatchers.IO) {
                        userDao.getUserByUsername(emailInput)
                    }
                    if (user != null && user.password == passwordInput) {
                        // Simpan username ke SharedPreferences
                        val sharedPref = getSharedPreferences("DoItPrefs", Context.MODE_PRIVATE)
                        sharedPref.edit().putString("loggedInUsername", user.username).apply()

                        Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Username atau Password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigasi ke halaman signup saat TextView diklik
        signupText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
