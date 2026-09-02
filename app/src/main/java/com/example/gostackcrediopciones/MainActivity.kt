package com.example.gostackcrediopciones


import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegúrate de que tu tema principal extienda de Theme.MaterialComponents en themes.xml
        setContentView(R.layout.activity_main)

        // Referencias a las vistas
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        // Evento Iniciar Sesión
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(username, password)
            }
        }

        // Evento Olvidé mi contraseña
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Redirigiendo a recuperación de contraseña...", Toast.LENGTH_SHORT).show()
            // Aquí iría el Intent para tu Activity de recuperación
        }

        // Evento Registro
        tvRegister.setOnClickListener {
            Toast.makeText(this, "Redirigiendo a registro...", Toast.LENGTH_SHORT).show()
            // Aquí iría el Intent para tu Activity de registro
        }
    }

    private fun iniciarSesion(identificador: String, contrasena: String) {
        // Deshabilitar el botón mientras carga (opcional pero recomendado)
        findViewById<MaterialButton>(R.id.btnLogin).isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = LoginRequest(identificador, contrasena)
                val response = RetrofitClient.instance.login(request)

                withContext(Dispatchers.Main) {
                    findViewById<MaterialButton>(R.id.btnLogin).isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        if (loginResponse.status == "success") {
                            Toast.makeText(this@MainActivity, "¡Bienvenido ${loginResponse.user?.username}!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, loginResponse.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error en el servidor", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    findViewById<MaterialButton>(R.id.btnLogin).isEnabled = true
                    Toast.makeText(this@MainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}