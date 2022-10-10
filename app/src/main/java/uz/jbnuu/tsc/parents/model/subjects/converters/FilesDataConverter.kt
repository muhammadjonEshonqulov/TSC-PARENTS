package uz.jbnuu.tsc.parents.model.subjects.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import uz.jbnuu.tsc.parents.model.subjects.FilesData

class FilesDataConverter {

    @TypeConverter
    fun dataFilesDataToJson(dataFilesData: List<FilesData>?): String = Gson().toJson(dataFilesData)

    @TypeConverter
    fun jsonToFilesData(dataFilesData: String): List<FilesData> = Gson().fromJson(dataFilesData, Array<FilesData>::class.java).toList()
}