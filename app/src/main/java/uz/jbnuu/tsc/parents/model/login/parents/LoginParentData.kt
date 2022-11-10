package uz.jbnuu.tsc.parents.model.login.parents

import uz.jbnuu.tsc.parents.model.type_tarif.TarifData

data class LoginParentData(
    val id: Int?,//  4,
    val familya: String?,//  "Karimov",
    val ism: String?,//  "Sherozjon",
    val otasi_ismi: String?,//  "Shavkat o'g'li",
    val pasport: String?,//  "AB123456811",
    val active: String?,//  "Faol",
    val active_time: String?,//  "--/--/----",
    val deadline: String?,//  "--/--/----",
    val summa_qoldiq: String?,//  "0",
    val role_id: Int?,//  5,
    val tarif_id: Int?,//  2,
    val other_data: String?,//  null,
    val created_at: String?,//  "2022-10-10T03:47:26.000000Z",
    val updated_at: String?,//  "2022-10-17T07:20:55.000000Z",
    val tarif: TarifData?,
)