package br.com.componel.horaextra.ui.activity

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.componel.horaextra.R
import br.com.componel.horaextra.api.ComponelAPILogin
import br.com.componel.horaextra.dagger.MainApplication
import br.com.componel.horaextra.databinding.ActivityLoginBinding
import br.com.componel.horaextra.dto.LoginResponse
import br.com.componel.horaextra.helpers.LoginHelper
import br.com.componel.horaextra.helpers.enums.LoginStatus
import br.com.componel.horaextra.model.Login
import br.com.componel.horaextra.ui.viewmodel.LoginViewModel
import br.com.componel.horaextra.utilities.UnauthorizedExtra
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    init {
        MainApplication.appComponent.injectLoginActivity(this)
    }

    private lateinit var viewModel: LoginViewModel
    private val disposer = CompositeDisposable()

    @Inject
    lateinit var loginHelper: LoginHelper

    @Inject
    lateinit var service: ComponelAPILogin




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configuraViewModel()
        val binding = configuraBinding()
        observaCamposFormulario()
        configuraBotao(binding)
        verificaExtra()
    }

    private fun verificaExtra() {
        if (intent.hasExtra(UnauthorizedExtra)) {
            Toast.makeText(this, intent.getStringExtra(UnauthorizedExtra), Toast.LENGTH_SHORT).show()
        }
    }

    private fun configuraBinding(): ActivityLoginBinding {
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.viewModel = viewModel
        return binding
    }

    private fun configuraBotao(binding: ActivityLoginBinding) {
        binding.loginButton.setOnClickListener { entrar() }
    }

    private fun configuraViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private fun observaCamposFormulario() {
        val subs = viewModel.formularioValido.subscribe {
            validaCamposFormulario(it)
        }
        disposer.add(subs)
    }

    private fun validaCamposFormulario(valido: Boolean) {
        login_button.isEnabled = valido
        limpaErros()
    }

    private fun limpaErros() {
        login_username_layout.error = ""
        login_password_layout.error = ""
    }

    private fun entrar() {
        login_button.isSending = true
        if (viewModel.campoSenha.get() != null && viewModel.campoUsuario.get() != null) {
            val login = Login(viewModel.campoUsuario.get() as String, viewModel.campoSenha.get() as String)
            val sub = enviaLogin(login)
            disposer.add(sub)
        }
    }

    private fun enviaLogin(login: Login): Disposable {
        return service.login(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate { login_button.isSending = false }
                .subscribe({ resp ->
                    tratarResp(resp)
                }, { _ ->
                    //TODO tratar erros
                    Toast.makeText(this, "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show()
                })
    }

    private fun tratarResp(resp: LoginResponse) {
        when (resp.cod) {
            LoginStatus.Ok.valor -> {
                loginHelper.login(resp)
                vaiParaMain()
            }
            LoginStatus.SenhaIncorreta.valor -> login_password_layout.error = "Senha Incorreta"
            LoginStatus.UsuarioInexistente.valor -> login_username_layout.error = "Usuário Inexistente"
        }
    }

    private fun vaiParaMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        disposer.dispose()
        super.onDestroy()
    }
}
