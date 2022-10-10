package uz.jbnuu.tsc.parents.model.me

data class MeResponse(
    val success: Boolean?,
    val error: String?,
    val data: MeData?,
    val code: Int?,
)
