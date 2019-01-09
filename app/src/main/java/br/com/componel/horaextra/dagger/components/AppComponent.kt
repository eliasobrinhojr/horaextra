package br.com.componel.horaextra.dagger.components

import br.com.componel.horaextra.dagger.modules.AppModule
import br.com.componel.horaextra.dagger.modules.DbModule
import br.com.componel.horaextra.dagger.modules.NetModule
import br.com.componel.horaextra.helpers.interceptors.RedirectInterceptor
import br.com.componel.horaextra.helpers.interceptors.TokenInterceptor
import br.com.componel.horaextra.ui.activity.LoginActivity
import br.com.componel.horaextra.ui.activity.SplashScreenActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class, DbModule::class, AppModule::class])
interface AppComponent {
    fun injectRedirect(interceptor: RedirectInterceptor)
    fun injectTokenInterceptor(interceptor: TokenInterceptor)
    fun injectLoginActivity(activity: LoginActivity)

    //fun injectPerfilActivity(perfilActivity: PerfilActivity)
    fun injectSplashScreenActivity(splashScreenActivity: SplashScreenActivity)

    fun viewModelComponent(): ViewModelComponent
    fun fragmentComponent(): FragmentComponent
    fun activityComponent(): ActivityComponent
}