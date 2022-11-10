package uz.jbnuu.tsc.parents.model.register

data class RegisterBody(
    val familya: String?,
    val ism: String?,
    val otasi_ismi: String?,
    val pasport: String?,
    val password: String?,
    val tarif_id: Int? = null,
)
