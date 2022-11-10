package uz.jbnuu.tsc.parents.model.type_tarif

data class TarifData(
    val id: Int?,
    val name: String?,
    val price: String?,
    val definition: String?,
    val person_count: Int?,
    val other_data: Any?,
    var select: Boolean? = false
)
