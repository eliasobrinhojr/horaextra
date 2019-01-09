package br.com.componel.horaextra.dto

import br.com.componel.horaextra.model.Setor

data class SetorResponse(
        var cod: Int,
        var dados: List<Setor>,
        var msg: String
) {
    override fun toString(): String {
        return "EmpresaResponse(cod=$cod, dados=$dados, msg='$msg')"
    }
}