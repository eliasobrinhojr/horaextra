package br.com.componel.horaextra.repositorios

import br.com.componel.horaextra.api.ComponelAPIExtra
import br.com.componel.horaextra.dao.ExtraDAO
import br.com.componel.horaextra.dto.AprovarPost
import br.com.componel.horaextra.model.HoraExtra
import io.reactivex.Completable
import javax.inject.Inject

class ExtraRepository @Inject constructor(
        val extraDao: ExtraDAO,
        val API: ComponelAPIExtra
) {

    fun getExtras(id_login: Int, id_empresa: Int, id_setor: Int) = API.getExtrasHj(id_login, id_empresa, id_setor)

    fun getEmpresas(id: Int) = API.getEmpresas(id)

    fun getSetores(id: Int) = API.getSetores(id)

    fun aprovarExtras(ids: AprovarPost) = API.aprovarExtras(ids)

}