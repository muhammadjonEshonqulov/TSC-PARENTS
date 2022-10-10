package uz.jbnuu.tsc.parents.model.login.student

import uz.jbnuu.tsc.parents.model.me.MeData

data class LoginStudentResponse(
    val status: Int?,
    val token: String?,
    val hemins_token: String?,
    val getme: MeData?
)
