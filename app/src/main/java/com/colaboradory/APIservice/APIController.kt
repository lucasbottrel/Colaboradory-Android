package com.colaboradory.APIservice

import com.colaboradory.model.Colaborador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class APIController {

    interface ColaboradoresCallback {
        fun onColaboradoresReceived(colaboradores: List<Colaborador>)
        fun onErroResponse(code: Int)
        fun onFalha(throwable: Throwable)
    }

    fun ListaColaboradores(callback: ColaboradoresCallback){
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.2.105:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(API::class.java)

            apiService.listaColaboradores().enqueue(object : Callback<List<Colaborador>> {
                override fun onResponse(call: Call<List<Colaborador>>, response: Response<List<Colaborador>>) {
                    if (response.isSuccessful) {
                        val colaboradores = response.body()
                        if (colaboradores != null) {
                            callback.onColaboradoresReceived(colaboradores)
                        } else {
                            callback.onErroResponse(response.code())
                        }
                    } else {
                        callback.onErroResponse(response.code())
                    }
                }

                override fun onFailure(call: Call<List<Colaborador>>, t: Throwable) {
                    callback.onFalha(t)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}