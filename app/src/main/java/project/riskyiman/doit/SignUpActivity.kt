package project.riskyiman.doit

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
import project.riskyiman.doit.model.User
import project.riskyiman.doit.viewmodel.AppDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginText: TextView  // Declare login TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        fullNameEditText = findViewById(R.id.fullNameEditText)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signUpButton = findViewById(R.id.signupButton)
        loginText = findViewById(R.id.loginText)  // Initialize login TextView

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        signUpButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (fullName.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val existingUser = withContext(Dispatchers.IO) {
                        userDao.getUserByUsername(username)
                    }
                    if (existingUser != null) {
                        Toast.makeText(this@SignUpActivity, "Username sudah digunakan!", Toast.LENGTH_SHORT).show()
                    } else {
                        withContext(Dispatchers.IO) {
                            userDao.insertUser(User(fullName = fullName, username = username, password = password))
                        }
                        Toast.makeText(this@SignUpActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()

                        // Pindah ke LoginActivity setelah registrasi sukses
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up the OnClickListener for the Login TextView
        loginText.setOnClickListener {
            // Navigate to LoginActivity
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
