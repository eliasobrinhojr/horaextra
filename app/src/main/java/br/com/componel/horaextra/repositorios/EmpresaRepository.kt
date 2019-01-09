package br.com.componel.horaextra.repositorios

import br.com.componel.horaextra.api.ComponelAPIEmpresa
import br.com.componel.horaextra.dao.EmpresaDAO
import br.com.componel.horaextra.model.Empresa
import io.reactivex.Completable
import javax.inject.Inject

class EmpresaRepository @Inject constructor(
        val empresaDao: EmpresaDAO,
        val API: ComponelAPIEmpresa
) {
    fun getEmpresas(id: Int) = API.getEmpresas(id)

    fun addOrUpdateEmpresaInterno(empresas: List<Empresa>): Completable {
        return Completable.fromAction { empresaDao.insertOrUpdateEmpresa(empresas) }
    }
}