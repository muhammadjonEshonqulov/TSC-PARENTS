package uz.jbnuu.tsc.parents.model.login

import uz.jbnuu.tsc.parents.model.login.parents.LoginParentData
import uz.jbnuu.tsc.parents.model.type_tarif.TarifData

data class LoginParentsResponse(
    val status: Int?,
    val token: String?,
    val name: String?,
    val surname: String?,
    val lastname: String?,
    val role_id: Int?,
    val data: LoginParentData?,
    val tarif: TarifData?
)
