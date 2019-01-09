package br.com.componel.horaextra.dagger.components

import br.com.componel.horaextra.ui.fragments.AprovarFragment
import br.com.componel.horaextra.ui.fragments.MainFragment
import br.com.componel.horaextra.ui.fragments.SemConexaoFragment
import br.com.componel.horaextra.ui.fragments.SemExtraFragment
import dagger.Subcomponent

@Subcomponent
interface FragmentComponent {
    fun mainFragInject(mainFrag: MainFragment)
    fun aprovarFragInject(aprovarFrag: AprovarFragment)
    fun semConexaoFragInject(semConexaoFrag: SemConexaoFragment)
    fun semExtraFragInject(semExtraFrag: SemExtraFragment)
    //fun novaAnotacaoFragInject(novaAnotacaoFragment: NovaAnotacaoFragment)
}