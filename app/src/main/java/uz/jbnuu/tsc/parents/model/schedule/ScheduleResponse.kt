package uz.jbnuu.tsc.parents.model.schedule

data class ScheduleResponse(
    val success: Boolean?,
    val error: String?,
    val data: List<ScheduleData>?,
    val code: Int?,
)
