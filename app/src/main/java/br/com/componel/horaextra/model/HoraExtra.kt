package br.com.componel.horaextra.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class HoraExtra(
        @PrimaryKey(autoGenerate = false) var id: Int,
        @ColumnInfo(name = "nome_funcionario") var nome_funcionario: String,
        @ColumnInfo(name = "drt_funcionario") var drt_funcionario: String,
        @ColumnInfo(name = "hora_incial") var hora_incial: String,
        @ColumnInfo(name = "hora_final") var hora_final: String,
        @ColumnInfo(name = "nome_empresa") var nome_empresa: String,
        @ColumnInfo(name = "custo") var custo: Double
): Serializable