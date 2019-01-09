package br.com.componel.horaextra.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import br.com.componel.horaextra.dao.EmpresaDAO
import br.com.componel.horaextra.dao.ExtraDAO
import br.com.componel.horaextra.dao.UsuarioDAO
import br.com.componel.horaextra.model.Empresa
import br.com.componel.horaextra.model.HoraExtra
import br.com.componel.horaextra.model.Usuario

@Database(entities = [Usuario::class, HoraExtra::class, Empresa::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun extraDAO(): ExtraDAO
    abstract fun empresaDAO(): EmpresaDAO
}

