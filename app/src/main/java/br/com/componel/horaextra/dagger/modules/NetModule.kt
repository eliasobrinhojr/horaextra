package br.com.componel.horaextra.dagger.modules

import br.com.componel.horaextra.BuildConfig
import br.com.componel.horaextra.api.ComponelAPIEmpresa
import br.com.componel.horaextra.api.ComponelAPIExtra
import br.com.componel.horaextra.api.ComponelAPILogin
import br.com.componel.horaextra.helpers.interceptors.RedirectInterceptor
import br.com.componel.horaextra.helpers.interceptors.TokenInterceptor
import br.com.componel.horaextra.utilities.componelApiUrl
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
open class NetModule {

    @Provides
    @Singleton
    open fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .addInterceptor(RedirectInterceptor())
        val gson = GsonBuilder()
            .setDateFormat("dd/MM/yyyy HH:mm:ss")
            .setLenient()
            .create()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return Retrofit.Builder()
            .baseUrl(componelApiUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }

    @Provides
    open fun provideLoginApi(retrofit: Retrofit): ComponelAPILogin {
        return retrofit.create(ComponelAPILogin::class.java)
    }

    @Provides
    open fun provideExtraApi(retrofit: Retrofit): ComponelAPIExtra {
        return retrofit.create(ComponelAPIExtra::class.java)
    }

    @Provides
    open fun provideEmpresaApi(retrofit: Retrofit): ComponelAPIEmpresa {
        return retrofit.create(ComponelAPIEmpresa::class.java)
    }

}