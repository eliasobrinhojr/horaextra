package br.com.componel.horaextra.ui.viewmodel

import android.arch.lifecycle.ViewModel
import br.com.componel.horaextra.model.Empresa
import br.com.componel.horaextra.model.HoraExtra
import br.com.componel.horaextra.model.Setor
import io.reactivex.subjects.BehaviorSubject

class ExtraViewModel : ViewModel() {
    val listaQuestSubject = BehaviorSubject.create<List<HoraExtra>>()
    val listaEmpresaQuestSubject = BehaviorSubject.create<List<Empresa>>()
    val listaSetoresSubject = BehaviorSubject.create<List<Setor>>()

    val atualizaTela = BehaviorSubject.create<Boolean>()

    val semConexao = BehaviorSubject.create<Boolean>()
}