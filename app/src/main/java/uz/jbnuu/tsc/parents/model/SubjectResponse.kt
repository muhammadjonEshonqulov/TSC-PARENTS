package uz.jbnuu.tsc.parents.model

import uz.jbnuu.tsc.parents.model.subjects.SubjectData

data class SubjectResponse(
    val success: Boolean?,
    val error: String?,
    val data: SubjectData?,
    val code: Int?,
)
