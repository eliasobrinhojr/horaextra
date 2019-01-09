package br.com.componel.horaextra.api

import br.com.componel.horaextra.dto.EmpresaResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ComponelAPIEmpresa {

    @GET("empresas.php")
    fun getEmpresas(@Query("id") id: Int): Single<EmpresaResponse>
}