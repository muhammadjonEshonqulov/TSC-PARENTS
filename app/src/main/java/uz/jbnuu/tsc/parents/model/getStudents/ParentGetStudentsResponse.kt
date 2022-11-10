package uz.jbnuu.tsc.parents.model.getStudents

import uz.jbnuu.tsc.parents.model.type_tarif.TarifData

data class ParentGetStudentsResponse(
    val status: Int?,
    val data: List<ParentGetStudentsData>?,
    val active: Boolean?,
    val tarif: TarifData?
)
