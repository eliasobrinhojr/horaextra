package br.com.componel.horaextra.api

import br.com.componel.horaextra.dto.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ComponelAPIExtra {

    @GET("extras_aprovar.php")
    fun getExtrasHj(@Query("id_login") id_login: Int,
                    @Query("id_empresa") id_empresa: Int,
                    @Query("id_setor") id_setor: Int): Single<HoraExtraResponse>

    @GET("empresas.php")
    fun getEmpresas(@Query("id") id: Int): Single<EmpresaResponse>

    @GET("setores.php")
    fun getSetores(@Query("id") id: Int): Single<SetorResponse>

    @POST("altera_situacao.php")
    fun aprovarExtras(@Body ids: AprovarPost): Call<AprovarResponse>

}