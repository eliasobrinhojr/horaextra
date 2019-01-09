package br.com.componel.horaextra.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import br.com.componel.horaextra.model.Empresa
import br.com.componel.horaextra.model.HoraExtra

@Dao
interface EmpresaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateEmpresa(extra: List<Empresa>)
}