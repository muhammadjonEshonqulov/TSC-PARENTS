package uz.jbnuu.tsc.parents.model.examTable

import uz.jbnuu.tsc.parents.model.schedule.LessonPair
import uz.jbnuu.tsc.parents.model.schedule.Subject
import uz.jbnuu.tsc.parents.model.schedule.TrainingType
import uz.jbnuu.tsc.parents.model.semester.EducationYear

data class ExamTableData(
    val id: Int?,
    val subject: Subject?,
    val semester: TrainingType?,
    val educationYear: EducationYear?,
    val group: Subject?,
    val faculty: Subject?,
    val department: Subject?,
    val examType: TrainingType?,
    val finalExamType: TrainingType?,
    val employee: Subject?,
    val auditorium: TrainingType?,
    val lessonPair: LessonPair?,
    val examDate: Long?
)
