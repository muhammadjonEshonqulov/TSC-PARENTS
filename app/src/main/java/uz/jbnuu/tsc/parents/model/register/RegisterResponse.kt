package uz.jbnuu.tsc.parents.model.register

data class RegisterResponse(
    val status: Int?,
    val error: Int?,
    val errors: Errors?,
    val data: Int?
)
