package br.com.componel.horaextra.dagger.components

import br.com.componel.horaextra.ui.activity.MainActivity
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {
    fun mainActInject(mainAct: MainActivity)
}