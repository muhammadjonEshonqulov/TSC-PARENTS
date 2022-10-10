package uz.jbnuu.tsc.parents.model.performance

import uz.jbnuu.tsc.parents.model.schedule.TrainingType

data class Performance(
    val grade: Int?,
    val max_ball: Int?,
    val label: String?,
    val examType: TrainingType?
)
