package br.com.componel.horaextra.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Empresa (
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        var cod_fpw: Int,
        var descricao: String,
        var conexao_base: String
){
        override fun toString(): String {
                return descricao
        }
}