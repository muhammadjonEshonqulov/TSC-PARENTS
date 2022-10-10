package uz.jbnuu.tsc.parents.model.examTable

data class ExamTableResponse(
    val success: Boolean?,
    val error: String?,
    val data: List<ExamTableData>?,
    val code: Int?,
)
