package br.com.componel.horaextra.ui.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.componel.horaextra.R
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.model.HoraExtra
import br.com.componel.horaextra.repositorios.ExtraRepository
import br.com.componel.horaextra.ui.adapter.ListaExtrasAdapter
import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.view.*
import javax.inject.Inject

class MainFragment : Fragment() {

    lateinit var viewModel: ExtraViewModel
    val disposer = CompositeDisposable()
    lateinit var adapterExtras: ListaExtrasAdapter
    var listaExtras: MutableList<HoraExtra> = mutableListOf()
    var copyListaExtras: MutableList<HoraExtra> = mutableListOf()

    @Inject
    lateinit var extraRepo: ExtraRepository

    init {
        MainApplication.fragmentComponent.mainFragInject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.lista_agendadas.setHasFixedSize(false)
        viewModel = ViewModelProviders.of(requireActivity()).get(ExtraViewModel::class.java)

        inicializaLista(view)
        actionSearch(view)

        val subs = viewModel.listaQuestSubject
                .subscribe {
                    view.lb_extras.text.clear()
                    listaExtras = it.toMutableList()
                    copyListaExtras = listaExtras
                    atualizaLista()
                }

        disposer.add(subs)

        return view

    }

    private fun actionSearch(view: View) {
        view.lb_extras.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    val filterList = listaExtras.filter { it.nome_funcionario.toLowerCase().contains(s) || it.drt_funcionario.toLowerCase().contains(s) }
                    copyListaExtras = filterList.toMutableList()
                } else if (s.isEmpty()) {
                    copyListaExtras = listaExtras
                }
                atualizaLista()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


    fun atualizaLista() {
        adapterExtras.items = copyListaExtras
        adapterExtras.notifyDataSetChanged()
    }

    fun inicializaLista(view: View) {
        adapterExtras = ListaExtrasAdapter(listaExtras, requireContext())
        adapterExtras.onItemClick = { extra ->
            extra?.let { _ ->
                //TODO lista item click, action
            }
        }
        view.lista_agendadas.adapter = adapterExtras
        view.lista_agendadas.setHasFixedSize(false)

    }

    override fun onDestroy() {
        disposer.dispose()
        super.onDestroy()
    }


}
