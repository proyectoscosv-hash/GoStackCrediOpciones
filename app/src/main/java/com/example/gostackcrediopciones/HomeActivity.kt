package com.example.gostackcrediopciones

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val etCodigoCliente = findViewById<TextInputEditText>(R.id.etCodigoCliente)
        val btnConsultar = findViewById<MaterialButton>(R.id.btnConsultar)
        val cardResultados = findViewById<MaterialCardView>(R.id.cardResultados)
        val tvNombreCliente = findViewById<TextView>(R.id.tvNombreCliente)
        val tvTotalMotos = findViewById<TextView>(R.id.tvTotalMotos)

        btnConsultar.setOnClickListener {
            val codigo = etCodigoCliente.text.toString().trim()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un código de cliente", Toast.LENGTH_SHORT).show()
                cardResultados.visibility = View.GONE
            } else {
                // Aquí en el futuro llamarás a la API con Retrofit pasando el "codigo" (idcliente)
                // Por ahora simularemos que encontró al cliente y muestra la tarjeta.

                realizarConsultaCliente(codigo, cardResultados, tvNombreCliente, tvTotalMotos)
            }
        }
    }

    private fun realizarConsultaCliente(
        idcliente: String,
        cardResultados: MaterialCardView,
        tvNombre: TextView,
        tvTotal: TextView
    ) {
        // Simulación de respuesta exitosa de la base de datos
        val nombreClienteSimulado = "Juan Pérez"
        val totalComprasSimuladas = 3

        tvNombre.text = "Cliente: $nombreClienteSimulado"
        tvTotal.text = "Total de compras: $totalComprasSimuladas"

        // Hacemos visible la tarjeta con una pequeña animación
        cardResultados.alpha = 0f
        cardResultados.visibility = View.VISIBLE
        cardResultados.animate().alpha(1f).setDuration(300).start()
    }
}