package br.com.componel.horaextra.ui.fragments


import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import br.com.componel.horaextra.R
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.dto.AprovarPost
import br.com.componel.horaextra.dto.AprovarResponse
import br.com.componel.horaextra.helpers.LoginHelper
import br.com.componel.horaextra.model.Empresa
import br.com.componel.horaextra.model.HoraExtra
import br.com.componel.horaextra.model.Setor
import br.com.componel.horaextra.repositorios.ExtraRepository
import br.com.componel.horaextra.ui.activity.LoginActivity
import br.com.componel.horaextra.ui.adapter.ListaExtrasAdapter
import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_aprovar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class AprovarFragment : Fragment() {

    init {
        MainApplication.fragmentComponent.aprovarFragInject(this)
    }

    val disposer = CompositeDisposable()

    lateinit var viewModel: ExtraViewModel

    @Inject
    lateinit var extraRepo: ExtraRepository

    @Inject
    lateinit var loginHelper: LoginHelper

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    lateinit var adapterExtras: ListaExtrasAdapter
    var listaExtras: MutableList<HoraExtra> = mutableListOf()
    var copyListaExtras: MutableList<HoraExtra> = mutableListOf()
    var listEmpresa: List<Empresa> = listOf()
    var listSetor: List<Setor> = listOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_aprovar, container, false)

        viewModel = ViewModelProviders.of(requireActivity()).get(ExtraViewModel::class.java)
        view.lista_agendadas.setHasFixedSize(false)

        inicializaLista(view)
        observaListaEmpresa(view)
        btnAprovar(view)
        btnRejeitar(view)


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu!!.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(s: String): Boolean {

                    if (s.isNotEmpty()) {
                        val filterList = listaExtras.filter { it.nome_funcionario.toLowerCase().contains(s) || it.drt_funcionario.toLowerCase().contains(s) }
                        copyListaExtras = filterList.toMutableList()
                    } else if (s.isEmpty()) {
                        copyListaExtras = listaExtras
                    }
                    atualizaLista()

                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.i("onQueryTextSubmit", query)

                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sair -> {
                loginHelper.logout()
                val intent = Intent(context, LoginActivity::class.java)
                activity!!.finish()
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observaListaEmpresa(view: View) {
        val sub = viewModel.listaEmpresaQuestSubject.subscribe {
            carregaSpinnerEmpresas(view, it)
        }
        disposer.add(sub)
    }

    private fun btnRejeitar(view: View) {
        view.btn_rejeitar.setOnClickListener { _ ->

            if (listSetor.isNotEmpty() && listEmpresa.isNotEmpty()) {
                showDialogConfirmAlteraSituacao(view, 3)
            } else {
                Toast.makeText(requireContext(), "Empresa ou setor nao informado !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnAprovar(view: View) {
        view.btn_aprovar.setOnClickListener { _ ->

            if (listSetor.isNotEmpty() && listEmpresa.isNotEmpty()) {
                showDialogConfirmAlteraSituacao(view, 2)
            } else {
                Toast.makeText(requireContext(), "Empresa ou setor nao informado !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogConfirmAlteraSituacao(view: View, situacao: Int) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(context!!)

        // Set the alert dialog title
        var title = ""
        title = if (situacao == 2) {
            "APROVAR EXTRAS"
        } else {
            "REJEITAR EXTRAS"
        }
        builder.setTitle(title)

        val emp = listEmpresa[view.spinner_empresa.selectedItemPosition].descricao
        val set = listSetor[view.spinner_setor.selectedItemPosition].descricao
        var cc = 0.0
        listaExtras.forEach { obj ->
            cc += obj.custo
        }

        // Display a message on alert dialog
        builder.setMessage("\n\nEmpresa: $emp \n\n\nSetor: $set \n\nTotal Custo: R$ ${cc.toFloat()} \n\n")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("CONFIRMAR") { _, _ ->

            val arry: Array<Int> = Array(listaExtras.size) { 0 }

            listaExtras.forEachIndexed { index, horaExtra ->
                arry[index] = horaExtra.id
            }

            extraRepo.API.aprovarExtras(AprovarPost(arry, situacao)).enqueue(
                    object : Callback<AprovarResponse> { //anonymous inner class. No lambda because there are two methods in Callback interface
                        override fun onResponse(call: Call<AprovarResponse>, response: Response<AprovarResponse>) {
                            val body = response.body()
                            Toast.makeText(requireContext(), body!!.msg, Toast.LENGTH_SHORT).show()
                            if (body.cod == 1) {
                                viewModel.atualizaTela.onNext(true)
                            }
                        }

                        override fun onFailure(call: Call<AprovarResponse>, t: Throwable) {
                            Log.d("retrofit", t.message)
                            viewModel.semConexao.onNext(true)
                        }
                    }
            )

        }


        builder.setNeutralButton("CANCELAR") { _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    fun inicializaLista(view: View) {
        adapterExtras = ListaExtrasAdapter(listaExtras, requireContext())
        adapterExtras.onItemClick = { extra ->
            extra?.let { _ ->
                val arry: Array<Int> = Array(1) { 0 }
                arry[0] = extra.id
                val builder = AlertDialog.Builder(context!!)

                // Set the alert dialog title
                val title = "Solicitação"
                builder.setTitle(title)

                val emp = listEmpresa[view.spinner_empresa.selectedItemPosition].descricao
                val set = listSetor[view.spinner_setor.selectedItemPosition].descricao
                val cc = extra.custo

                // Display a message on alert dialog
                builder.setMessage("\nColaborador: ${extra.nome_funcionario} \n\nEmpresa: $emp \n\n\nSetor: $set \n\nTotal Custo: R$ ${cc.toFloat()} \n\n")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("APROVAR") { _, _ ->

                    requestAlterarSituacao(arry, 2)
                }

                builder.setNegativeButton("REJEITAR") { _, _ ->
                    requestAlterarSituacao(arry, 3)
                }

                builder.setNeutralButton("CANCELAR") { _, _ ->

                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        view.lista_agendadas.adapter = adapterExtras
        view.lista_agendadas.setHasFixedSize(false)

    }

    private fun requestAlterarSituacao(arry: Array<Int>, situacao: Int) {
        extraRepo.API.aprovarExtras(AprovarPost(arry, situacao)).enqueue(
                object : Callback<AprovarResponse> { //anonymous inner class. No lambda because there are two methods in Callback interface
                    override fun onResponse(call: Call<AprovarResponse>, response: Response<AprovarResponse>) {
                        val body = response.body()
                        Toast.makeText(requireContext(), body!!.msg, Toast.LENGTH_SHORT).show()
                        if (body.cod == 1) {
                            viewModel.atualizaTela.onNext(true)
                        }
                    }

                    override fun onFailure(call: Call<AprovarResponse>, t: Throwable) {
                        Log.d("retrofit", t.message)
                        viewModel.semConexao.onNext(true)
                    }
                }
        )
    }

    fun atualizaLista() {
        adapterExtras.items = copyListaExtras
        adapterExtras.notifyDataSetChanged()
    }

    private fun carregaSpinnerEmpresas(viewFrag: View, lista: List<Empresa>) {

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lista)
        viewFrag.spinner_empresa.adapter = arrayAdapter

        viewFrag.spinner_empresa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                listEmpresa = lista
                //  carregaSetoresPorEmpresa(lista[position].id, viewFrag)
                carregaSetoresPorEmpresa(loginHelper.usuario.id, viewFrag)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun carregaSetoresPorEmpresa(id: Int, view: View) {
        val subs = extraRepo.getSetores(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { }
                .subscribe({ resp ->
                    carregaSpinnerSetores(resp.dados, view)

                }, { _ ->
                    viewModel.semConexao.onNext(true)
                })

        disposer.add(subs)
    }

    private fun carregaSpinnerSetores(setores: List<Setor>, viewFrag: View) {


        val filterList = setores.filter { it.id_empresa == listEmpresa[viewFrag.spinner_empresa.selectedItemPosition].id }


        listSetor = filterList
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listSetor)
        viewFrag.spinner_setor.adapter = arrayAdapter

        viewFrag.spinner_setor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val subsc = extraRepo.getExtras(loginHelper.usuario.id, listEmpresa[viewFrag.spinner_empresa.selectedItemPosition].id, listSetor[position].id)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate { atualizaLista() }
                        .subscribe({ resp ->


                            listaExtras = resp.dados.toMutableList()
                            copyListaExtras = listaExtras

                        }, { _ ->
                            viewModel.semConexao.onNext(true)
                        })
                disposer.add(subsc)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onDestroy() {
        disposer.dispose()
        super.onDestroy()
    }
}
