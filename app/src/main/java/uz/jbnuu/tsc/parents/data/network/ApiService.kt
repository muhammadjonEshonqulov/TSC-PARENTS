package uz.jbnuu.tsc.parents.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.attendance.AttendanceResponse
import uz.jbnuu.tsc.parents.model.examTable.ExamTableResponse
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsResponse
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryResponse
import uz.jbnuu.tsc.parents.model.login.LoginParentsResponse
import uz.jbnuu.tsc.parents.model.login.LogoutResponse
import uz.jbnuu.tsc.parents.model.login.hemis.LoginHemisResponse
import uz.jbnuu.tsc.parents.model.login.parents.ConnectParentResponse
import uz.jbnuu.tsc.parents.model.login.parents.LoginParentBody
import uz.jbnuu.tsc.parents.model.login.parents.LoginStudentToConnectParentBody
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.me.MeResponse
import uz.jbnuu.tsc.parents.model.performance.PerformanceResponse
import uz.jbnuu.tsc.parents.model.reference.ReferenceResponse
import uz.jbnuu.tsc.parents.model.register.RegisterBody
import uz.jbnuu.tsc.parents.model.register.RegisterResponse
import uz.jbnuu.tsc.parents.model.remove.RemoveStudentBody
import uz.jbnuu.tsc.parents.model.remove.RemoveStudentResponse
import uz.jbnuu.tsc.parents.model.schedule.ScheduleResponse
import uz.jbnuu.tsc.parents.model.semester.SemestersResponse
import uz.jbnuu.tsc.parents.model.student.PushNotification
import uz.jbnuu.tsc.parents.model.subjects.SubjectsResponse
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifBody
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifMatnResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifResponse
import uz.jbnuu.tsc.parents.utils.Constants.Companion.CONTENT_TYPE
import uz.jbnuu.tsc.parents.utils.Constants.Companion.SERVER_KEY

interface ApiService {

    @POST("login_parent")
    suspend fun loginParents(@Body loginParentBody: LoginParentBody): Response<LoginParentsResponse>

    @POST("connect_parent_student")
    suspend fun connectParentStudent(@Body loginStudentToConnectParentBody: LoginStudentToConnectParentBody): Response<ConnectParentResponse>

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST
    suspend fun postNotification(@Url full_url: String, @Body notification: PushNotification): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginHemis(@Body loginHemisBody: LoginStudentBody): Response<LoginHemisResponse>

    @POST("create_parent")
    suspend fun createParent(@Body registerBody: RegisterBody): Response<RegisterResponse>

    @GET("account/me")
    suspend fun me(): Response<MeResponse>

    @GET("tarif_matn")
    suspend fun tarifMatn(): Response<TarifMatnResponse>

    @GET("parent_get_students")
    suspend fun parentGetStudents(): Response<ParentGetStudentsResponse>

    @GET("get_tarifs")
    suspend fun getTarifsme(): Response<TarifResponse>

    @POST("update_tarif")
    suspend fun updateTarif(@Body changeTarifBody: ChangeTarifBody): Response<ChangeTarifResponse>

    @POST("update_parent")
    suspend fun updateParent(@Body registerBody: RegisterBody): Response<TarifMatnResponse>

    @POST("disconnect_parent_student")
    suspend fun disconnectParentStudent(@Body removeStudentBody: RemoveStudentBody): Response<RemoveStudentResponse>

    @GET("student/reference")
    suspend fun studentReference(): Response<ReferenceResponse>

    @GET("student/reference-download")
    suspend fun studentReferenceDownload(@Query("id") id: Int): Response<ResponseBody>

    @GET("education/schedule")
    suspend fun schedule(@Query("week") week: Int): Response<ScheduleResponse>

    @GET("education/subjects")
    suspend fun subjects(): Response<SubjectsResponse>

    @GET("education/subject")
    suspend fun subject(@Query("subject") subject: Int?, @Query("semester") semester: String): Response<SubjectResponse>

    @GET("education/semesters")
    suspend fun semesters(): Response<SemestersResponse>

    @GET("education/performance")
    suspend fun performance(): Response<PerformanceResponse>

    @GET("education/attendance")
    suspend fun attendance(@Query("semester") semester: String?): Response<AttendanceResponse>

    @GET("logout")
    suspend fun logout(): Response<LogoutResponse>

    @GET("parent_get_history_locations")
    suspend fun getLocationHistory(@Query("student_id") student_id: Int?, @Query("pagination") pagination: Int?, @Query("page") page: Int?): Response<LocationHistoryResponse>

    @GET("education/exam-table")
    suspend fun examTable(@Query("semester") semester: String?): Response<ExamTableResponse>

//    @POST("orders")
//    suspend fun orders(@Query("table_id") table_id: Int, @Query("waiter_id") waiter_id: Int, @Query("lang") lang: Int, @Body orders: OrderBody): Response<OrderResponse>
}