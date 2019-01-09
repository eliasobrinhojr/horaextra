package br.com.componel.horaextra.dto

import br.com.componel.horaextra.model.Empresa

data class EmpresaResponse(
        var cod: Int,
        var dados: List<Empresa>,
        var msg: String
){
    override fun toString(): String {
        return "EmpresaResponse(cod=$cod, dados=$dados, msg='$msg')"
    }
}