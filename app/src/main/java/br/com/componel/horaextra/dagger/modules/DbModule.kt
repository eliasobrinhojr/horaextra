package br.com.componel.horaextra.dagger.modules

import android.app.Application
import android.arch.persistence.room.Room
import br.com.componel.horaextra.dao.EmpresaDAO
import br.com.componel.horaextra.dao.ExtraDAO
import br.com.componel.horaextra.dao.UsuarioDAO
import br.com.componel.horaextra.data.AppDatabase
import br.com.componel.horaextra.utilities.DbName
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule(private val context: Application) {

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DbName).build()
    }

    @Provides
    fun provideUsuarioDao(db: AppDatabase): UsuarioDAO = db.usuarioDAO()

    @Provides
    fun provideExtraDao(db: AppDatabase): ExtraDAO = db.extraDAO()

    @Provides
    fun provideEmpresaDao(db: AppDatabase): EmpresaDAO = db.empresaDAO()
}