package uz.jbnuu.tsc.parents.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.jbnuu.tsc.parents.model.send_location.SendLocationBody
import uz.jbnuu.tsc.parents.model.subjects.Task


@Dao
interface MyDao {
    // SendLocationBody
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSendLocationBodyData(data: SendLocationBody)

    @Query("select *  from SendLocationBody")
    fun getSendLocationBodyData(): Flow<List<SendLocationBody>>

    @Query("delete from SendLocationBody")
    suspend fun clearSendLocationBodyData()

    // Task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskData(data: List<Task>)

    @Query("select *  from Task where student_id=:student_id order by deadline ASC")
    fun getTaskData(student_id: Int): Flow<List<Task>>

    @Query("delete from Task")
    suspend fun clearTaskData()
}