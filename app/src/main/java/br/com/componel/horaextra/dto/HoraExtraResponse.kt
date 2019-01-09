package br.com.componel.horaextra.dto

import br.com.componel.horaextra.model.HoraExtra

data class HoraExtraResponse(
        var cod: Int,
        var dados: List<HoraExtra>,
        var msg: String
)