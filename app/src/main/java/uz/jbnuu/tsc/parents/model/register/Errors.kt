package uz.jbnuu.tsc.parents.model.register

data class Errors(
    val familya: List<String>?,
    val ism: List<String>?,
    val otasi_ismi: List<String>?,
    val pasport: List<String>?,
    val password: List<String>?,
    val tarif_id: List<String>?,
)
