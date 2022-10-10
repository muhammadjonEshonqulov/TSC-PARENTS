package uz.jbnuu.tsc.parents.model.login.admin

data class AdminResponse(
    val status: Int?,
    val token: String?,
    val name: String?,
    val role_id: Int?
)
