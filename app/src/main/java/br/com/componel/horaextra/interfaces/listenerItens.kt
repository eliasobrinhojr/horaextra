package br.com.componel.horaextra.interfaces

import br.com.componel.horaextra.model.HoraExtra


interface listenerItens {

    var items: MutableList<HoraExtra>
    fun notifyDataSetChanged()
}