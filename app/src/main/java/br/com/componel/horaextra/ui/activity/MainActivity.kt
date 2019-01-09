package br.com.componel.horaextra.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import br.com.componel.horaextra.R
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.helpers.LoginHelper
import br.com.componel.horaextra.model.Empresa
import br.com.componel.horaextra.repositorios.ExtraRepository
import br.com.componel.horaextra.ui.fragments.AprovarFragment
import br.com.componel.horaextra.ui.fragments.SemConexaoFragment
import br.com.componel.horaextra.ui.fragments.SemExtraFragment
import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    init {
        MainApplication.actComponent.mainActInject(this)
    }

    @Inject
    lateinit var loginHelper: LoginHelper

    lateinit var viewModel: ExtraViewModel

    @Inject
    lateinit var extraRepo: ExtraRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ExtraViewModel::class.java)
        configuraToolbar()
        swipeRefreshAction()
        observaLista()

        val subscribe = viewModel.atualizaTela.subscribe {
            if (it) {
                sincronizaDados()
            }
        }
        disposer.add(subscribe)

        val subsConexao = viewModel.semConexao.subscribe {
            semConexao()
        }
        disposer.add(subsConexao)
    }

    private fun observaLista() {
        val subs = viewModel.listaEmpresaQuestSubject.subscribe {
            if (it.isNotEmpty()) {
                addFragment(AprovarFragment(), true, "0")
            } else {
                addFragment(SemExtraFragment(), true, "0")
            }

        }
        disposer.add(subs)
    }


    private fun configuraToolbar() {
        val actionBar = supportActionBar
        actionBar!!.title = "Hora Extra"
        actionBar.subtitle = loginHelper.usuario.nome_completo
        actionBar.elevation = 4.0F
    }


    override fun sincronizaDados() {

        disposer.add(sincronizaListaEmpresas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sincronizaConsumerEmpresa()))

    }

    fun sincronizaListaEmpresas(): Single<List<Empresa>> {
        return extraRepo.getEmpresas(loginHelper.usuario.id) // Requisição p/ servidor
                .map { resp -> resp.dados }
                .flatMapObservable { lista -> Observable.fromIterable(lista) }
                .toList()
    }

    fun sincronizaConsumerEmpresa(): BiConsumer<List<Empresa>?, Throwable?> {
        return BiConsumer { lista, erro ->
            main_swipe.isRefreshing = false
            lista?.let { viewModel.listaEmpresaQuestSubject.onNext(it) }
            erro?.let { semConexao() }
        }
    }

    private fun semConexao() {
        Toast.makeText(this, "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show()
        addFragment(SemConexaoFragment(), false, "")
    }


    private fun swipeRefreshAction() {
        main_swipe.setOnRefreshListener {
            sincronizaDados()
            main_swipe.isRefreshing = false
        }
    }

    private fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.replace(R.id.container_frame_back, fragment, tag)
        ft.commitAllowingStateLoss()
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.toolbar_menu, menu)
//
//        return super.onCreateOptionsMenu(menu)
//    }


    override fun onBackPressed() {
        finish()
    }
}
