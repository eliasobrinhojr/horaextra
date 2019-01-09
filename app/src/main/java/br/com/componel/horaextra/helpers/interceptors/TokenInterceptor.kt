package br.com.componel.horaextra.helpers.interceptors

import android.util.Log
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.helpers.LoginHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor : Interceptor {
    init {
        MainApplication.appComponent.injectTokenInterceptor(this)
    }

    @Inject
    lateinit var loginHelper: LoginHelper

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("TokenInterceptor", loginHelper.usuario.toString())
        val request = chain.request().newBuilder().addHeader("Token", loginHelper.APP_TOKEN).build()
        return chain.proceed(request)
    }
}