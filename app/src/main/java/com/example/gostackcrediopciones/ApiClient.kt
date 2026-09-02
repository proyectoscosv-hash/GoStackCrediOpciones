package com.example.gostackcrediopciones

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

// Modelos de datos para enviar y recibir
data class LoginRequest(val identificador: String, val password: String)
data class LoginResponse(val status: String, val message: String, val user: User?)
data class User(val id_user: Int, val username: String)

// Interfaz de Retrofit
interface ApiService {
    @POST("Logi_CO.php") // La ruta a tu archivo PHP
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

// Cliente de Retrofit
object RetrofitClient {
    // Reemplaza con la IP de tu servidor local o dominio real
    // Si usas el emulador y tu servidor local está en tu misma PC, usa "http://10.0.2.2/"
    private const val BASE_URL = "https://api-chasis-dsg8gabxbkfve4d5.canadacentral-01.azurewebsites.net/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}