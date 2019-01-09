package br.com.componel.horaextra.helpers.enums

enum class LoginStatus(val valor: Int) {
    Ok(0),
    SenhaIncorreta(1),
    UsuarioInexistente(2)
}