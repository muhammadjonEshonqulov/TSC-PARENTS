package uz.jbnuu.tsc.parents.model.me

import uz.jbnuu.tsc.parents.model.group.GroupData
import uz.jbnuu.tsc.parents.model.schedule.TrainingType
import uz.jbnuu.tsc.parents.model.semester.SemestersData

data class MeData(
    val first_name: String?,
    val second_name: String?,
    val third_name: String?,
    val student_id_number: String?,
    val image: String?,
    val birth_date: Long?,
    val phone: String?,
    val group: GroupData?,
    val faculty: GroupData?,
    val level: TrainingType?,
    val specialty: TrainingType?,
    val educationForm: TrainingType?,
    val educationType: TrainingType?,
    val educationLang: TrainingType?,
    val semester: SemestersData?,
)
