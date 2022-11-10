package uz.jbnuu.tsc.parents.model.login.parents

import uz.jbnuu.tsc.parents.model.me.MeData

data class ConnectParentResponse(
    val status: Int?,
    val hemins_token: String?,
    val getme: MeData?,
)
