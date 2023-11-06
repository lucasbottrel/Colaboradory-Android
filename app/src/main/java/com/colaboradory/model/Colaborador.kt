package com.colaboradory.model

import com.google.gson.annotations.SerializedName

data class Colaborador(
    @SerializedName("id")
    var id: Integer?,

    @SerializedName("nome")
    var nome: String?,

    @SerializedName("senha")
    var senha: String?,

    @SerializedName("complexidade")
    var complexidade: String?,

    @SerializedName("score")
    var score: String?,

    @SerializedName("chefe")
    var chefe: Colaborador?,

)
