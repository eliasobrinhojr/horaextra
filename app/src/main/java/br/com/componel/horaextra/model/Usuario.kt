package br.com.componel.horaextra.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Usuario(
        @PrimaryKey(autoGenerate = false) var id: Int,
        var nome_completo: String,
        var senha: String,
        var tipo_autenticacao: String,
        var status_registro: String
)