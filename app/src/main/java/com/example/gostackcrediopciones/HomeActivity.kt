package com.example.gostackcrediopciones

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var etCodigoCliente: TextInputEditText
    private lateinit var btnConsultar: MaterialButton
    private lateinit var cardResultados: MaterialCardView
    private lateinit var tvNombreCliente: TextView
    private lateinit var tvTotalMotos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializamos las vistas
        etCodigoCliente = findViewById(R.id.etCodigoCliente)
        btnConsultar = findViewById(R.id.btnConsultar)
        cardResultados = findViewById(R.id.cardResultados)
        tvNombreCliente = findViewById(R.id.tvNombreCliente)
        tvTotalMotos = findViewById(R.id.tvTotalMotos)

        btnConsultar.setOnClickListener {
            val codigo = etCodigoCliente.text.toString().trim()

            if (codigo.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un código de cliente", Toast.LENGTH_SHORT).show()
                cardResultados.visibility = View.GONE
            } else {
                realizarConsultaAPI(codigo)
            }
        }
    }

    private fun realizarConsultaAPI(codigo: String) {
        // Deshabilitamos el botón para evitar múltiples clics
        btnConsultar.isEnabled = false

        // Ocultamos la tarjeta por si había un resultado anterior
        cardResultados.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Hacemos la petición a la API
                val response = RetrofitClient.instance.buscarCliente(codigo)

                withContext(Dispatchers.Main) {
                    btnConsultar.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        val clienteResponse = response.body()!!

                        if (clienteResponse.status == "success" && clienteResponse.data != null) {

                            val nombre = clienteResponse.data.nombre
                            val totalCompras = clienteResponse.data.total_compras
                            val listaMotos = clienteResponse.data.motocicletas

                            tvNombreCliente.text = "Cliente: $nombre"
                            tvTotalMotos.text = "Total de compras: $totalCompras"

                            // Limpiamos el contenedor por si había consultas previas
                            val llMotosContainer = findViewById<android.widget.LinearLayout>(R.id.llMotosContainer)
                            llMotosContainer.removeAllViews()

                            // Dibujamos una tarjeta por cada compra
                            listaMotos?.forEach { moto ->
                                val motoView = layoutInflater.inflate(R.layout.item_motocicleta, llMotosContainer, false)

                                motoView.findViewById<TextView>(R.id.tvChasis).text = "Chasis: ${moto.chasis}"
                                motoView.findViewById<TextView>(R.id.tvFecha).text = "Fecha: ${moto.fecha}"
                                motoView.findViewById<TextView>(R.id.tvSector).text = "Sector: ${moto.sector}"
                                motoView.findViewById<TextView>(R.id.tvMaterial).text = "Material: ${moto.material}"
                                motoView.findViewById<TextView>(R.id.tvAgencia).text = "Agencia: ${moto.agencia}"

                                llMotosContainer.addView(motoView)
                            }

                            cardResultados.alpha = 0f
                            cardResultados.visibility = View.VISIBLE
                            cardResultados.animate().alpha(1f).setDuration(300).start()


                        } else {
                            Toast.makeText(this@HomeActivity, clienteResponse.message ?: "Cliente no encontrado", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@HomeActivity, "Error en el servidor al consultar", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnConsultar.isEnabled = true
                    Toast.makeText(this@HomeActivity, "Fallo de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}