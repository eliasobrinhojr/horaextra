package br.com.componel.horaextra.dagger.components

import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import br.com.componel.horaextra.ui.viewmodel.LoginViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelComponent {
    fun vmInject(viewModel: LoginViewModel)

    fun extraInject(extraVm: ExtraViewModel)
    fun loginVmInject(loginVm: LoginViewModel)
    //fun questSecoesInject(qstSec: SecoesQuestionarioViewModel)
}