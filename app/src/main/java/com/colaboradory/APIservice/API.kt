package com.colaboradory.APIservice

import com.colaboradory.model.Colaborador
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @POST("/")
    fun cadastroColaborador(@Body body: Map<String, String>): Call<ResponseBody>

    @POST("/associateBoss")
    fun associaChefe(@Body body: Map<String, Integer?>): Call<ResponseBody>

    @GET("/")
    fun listaColaboradores(): Call<List<Colaborador>>
}