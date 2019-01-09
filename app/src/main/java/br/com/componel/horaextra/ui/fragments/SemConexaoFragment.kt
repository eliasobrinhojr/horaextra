package br.com.componel.horaextra.ui.fragments


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import br.com.componel.horaextra.R
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.helpers.LoginHelper
import br.com.componel.horaextra.ui.activity.LoginActivity
import br.com.componel.horaextra.ui.viewmodel.ExtraViewModel
import kotlinx.android.synthetic.main.fragment_sem_conexao.view.*
import javax.inject.Inject


class SemConexaoFragment : Fragment() {

    init {
        MainApplication.fragmentComponent.semConexaoFragInject(this)
    }

    lateinit var viewModel: ExtraViewModel

    @Inject
    lateinit var loginHelper: LoginHelper


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sem_conexao, container, false)

        viewModel = ViewModelProviders.of(requireActivity()).get(ExtraViewModel::class.java)

        view.button2.setOnClickListener {
            viewModel.atualizaTela.onNext(true)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_frags, menu)


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


}
