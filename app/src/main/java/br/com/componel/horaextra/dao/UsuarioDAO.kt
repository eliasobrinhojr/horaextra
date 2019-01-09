package br.com.componel.horaextra.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import br.com.componel.horaextra.model.Usuario

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateUsuario(usuario: Usuario)
}