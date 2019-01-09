package br.com.componel.horaextra.api

import br.com.componel.horaextra.dto.LoginResponse
import br.com.componel.horaextra.model.Login
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface ComponelAPILogin {

    @POST("login_mobile.php")
    fun login(@Body login: Login): Single<LoginResponse>
}