package uz.jbnuu.tsc.parents.model.login.hemis

data class LoginHemisResponse(
    val success: Boolean?,
    val error: String?,
    val data: LoginHemisData?,
    val code: Int?,
)
