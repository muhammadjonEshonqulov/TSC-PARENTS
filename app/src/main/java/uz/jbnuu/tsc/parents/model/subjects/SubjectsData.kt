package uz.jbnuu.tsc.parents.model.subjects

import uz.jbnuu.tsc.parents.model.schedule.Subject
import uz.jbnuu.tsc.parents.model.schedule.TrainingType

data class SubjectsData(
    val subject: Subject?,
    val subjectType: TrainingType?,
    val _semester: String?,
    val total_acload: Int?,
    val credit: Int?
)
