package uz.jbnuu.tsc.parents.model.reference

import uz.jbnuu.tsc.parents.model.schedule.TrainingType

data class Department(
    val id: Int?,
    val name: String?,
    val structureType: TrainingType?
)
