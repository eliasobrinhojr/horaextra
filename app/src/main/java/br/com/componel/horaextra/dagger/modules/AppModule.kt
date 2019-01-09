package br.com.componel.horaextra.dagger.modules

import android.app.Application
import android.content.Context
import br.com.componel.horaextra.helpers.enums.RunMode
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideRunMode() = RunMode.NORMAL
}