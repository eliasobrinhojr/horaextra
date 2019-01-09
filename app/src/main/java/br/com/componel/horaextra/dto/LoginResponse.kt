package br.com.componel.horaextra.dto

data class LoginResponse(var cod: Int,
                         var id: Int,
                         var chave: String,
                         var nome_completo: String,
                         var senha: String,
                         var tipo_autenticacao: String,
                         var status_registro: String,
                         var msg: String)