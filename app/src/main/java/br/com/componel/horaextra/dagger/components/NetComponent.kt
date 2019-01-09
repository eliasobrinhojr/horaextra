package br.com.componel.horaextra.dagger.components

import br.com.componel.horaextra.dagger.modules.NetModule
import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import br.com.componel.horaextra.ui.viewmodel.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetModule::class)])
interface NetComponent {

    fun injectLoginVm(viewModel: LoginViewModel)
    fun injectExtraVm(viewModel: ExtraViewModel)
}