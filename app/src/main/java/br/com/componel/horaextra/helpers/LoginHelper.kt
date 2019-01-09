package br.com.componel.horaextra.helpers

import android.content.Context
import br.com.componel.horaextra.dao.UsuarioDAO
import br.com.componel.horaextra.dto.LoginResponse
import br.com.componel.horaextra.model.Usuario
import br.com.componel.horaextra.utilities.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LoginHelper @Inject constructor(
    var context: Context, var usuarioDAO: UsuarioDAO
){
    var APP_TOKEN = ""
        private set
    var usuario: Usuario = Usuario(0,"","", "","")


    fun login(login: LoginResponse) {
        insereUsuarioPreferences(login)
        inicializaUsuario()
        insereUsuarioBanco()
        APP_TOKEN = login.chave
    }

    private fun insereUsuarioPreferences(login: LoginResponse) {
        val pref = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString(TokenKey, login.chave)
            putString(UsuarioNomeKey, login.nome_completo)
            putInt(UsuarioCodKey, login.id)
            putString(UsuarioStatusKey, login.status_registro)
            putString("UsuarioSenha", login.senha)
            putString(UsuarioTipoAuthKey, login.tipo_autenticacao)
            apply()
        }
    }

    private fun inicializaUsuario() {
        val pref = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
        with(pref) {
            usuario = Usuario(
                nome_completo = getString(UsuarioNomeKey, "")!!,
                senha = getString("UsuarioSenha", "")!!,
                id = getInt(UsuarioCodKey, 0),
                status_registro = getString(UsuarioStatusKey, "")!!,
                tipo_autenticacao = getString(UsuarioTipoAuthKey, "")!!
            )
        }

    }

    private fun insereUsuarioBanco() {
        runOnIoThread {
            usuarioDAO.insertOrUpdateUsuario(usuario)
        }
    }

    fun logout() {
        val pref = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
        with(pref.edit()) {
            remove(TokenKey)
            remove(UsuarioNomeKey)
            remove(UsuarioCodKey)
            remove(UsuarioStatusKey)
            remove(UsuarioTipoAuthKey)
            apply()
        }
        APP_TOKEN = ""
    }

    fun inicializaLogin(): Boolean {
        val pref = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE)
        val token = pref.getString(TokenKey, "")
        return if (token != "") {
            inicializaUsuario()
            APP_TOKEN = token!!
            true
        } else {
            false
        }
    }
}