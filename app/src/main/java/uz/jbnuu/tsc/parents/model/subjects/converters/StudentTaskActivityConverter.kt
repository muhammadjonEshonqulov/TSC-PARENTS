package uz.jbnuu.tsc.parents.model.subjects.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import uz.jbnuu.tsc.parents.model.subjects.StudentTaskActivity

class StudentTaskActivityConverter {

    @TypeConverter
    fun dataStudentTaskActivityToJson(dataStudentTaskActivity: StudentTaskActivity?): String = Gson().toJson(dataStudentTaskActivity)

    @TypeConverter
    fun jsonToStudentTaskActivity(dataStudentTaskActivity: String): StudentTaskActivity? = Gson().fromJson(dataStudentTaskActivity, StudentTaskActivity::class.java)
}