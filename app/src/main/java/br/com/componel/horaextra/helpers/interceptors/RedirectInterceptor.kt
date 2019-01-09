package br.com.componel.horaextra.helpers.interceptors

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.helpers.LoginHelper
import br.com.componel.horaextra.ui.activity.LoginActivity
import br.com.componel.horaextra.utilities.UnauthorizedExtra
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class RedirectInterceptor : Interceptor {
    @Inject
    lateinit var contexto: Context

    @Inject
    lateinit var loginHelper: LoginHelper

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
        MainApplication.appComponent.injectRedirect(this)
        val response = chain.proceed(newRequest)
        Log.d("Redirect", "Codigo: ${response.code()}")
        if (response.code() == 401) {
            val intent = Intent(contexto, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(UnauthorizedExtra, "Login expirou!")
            response.close()
            loginHelper.logout()
            contexto.startActivity(intent)
            (contexto as Activity).finish()

        }
        return response
    }
}