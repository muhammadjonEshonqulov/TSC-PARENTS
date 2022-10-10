package uz.jbnuu.tsc.parents.model.subjects


data class SubjectsResponse(
    val success: Boolean?,
    val error: String?,
    val data: List<SubjectsData>?,
    val code: Int?,
)
