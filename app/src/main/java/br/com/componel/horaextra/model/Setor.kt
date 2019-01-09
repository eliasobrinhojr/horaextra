package br.com.componel.horaextra.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Setor (
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        var id_empresa: Int,
        var descricao: String
){
        override fun toString(): String {
                return descricao
        }
}