package uz.jbnuu.tsc.parents.model.attendance


data class AttendanceResponse(
    val success: Boolean?,
    val error: String?,
    val data: List<AttendanceData>?,
    val code: Int?,
)
