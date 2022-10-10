package uz.jbnuu.tsc.parents.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import uz.jbnuu.tsc.parents.model.StatisticResponse
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.attendance.AttendanceResponse
import uz.jbnuu.tsc.parents.model.examTable.ExamTableResponse
import uz.jbnuu.tsc.parents.model.group.GroupResponse
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryResponse
import uz.jbnuu.tsc.parents.model.login.LogoutResponse
import uz.jbnuu.tsc.parents.model.login.admin.AdminResponse
import uz.jbnuu.tsc.parents.model.login.hemis.LoginHemisResponse
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentResponse
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterBody
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterResponse
import uz.jbnuu.tsc.parents.model.me.MeResponse
import uz.jbnuu.tsc.parents.model.performance.PerformanceResponse
import uz.jbnuu.tsc.parents.model.reference.ReferenceResponse
import uz.jbnuu.tsc.parents.model.schedule.ScheduleResponse
import uz.jbnuu.tsc.parents.model.semester.SemestersResponse
import uz.jbnuu.tsc.parents.model.send_location.SendLocationArrayBody
import uz.jbnuu.tsc.parents.model.send_location.SendLocationBody
import uz.jbnuu.tsc.parents.model.send_location.SendLocationResponse
import uz.jbnuu.tsc.parents.model.student.PushNotification
import uz.jbnuu.tsc.parents.model.student.StudentResponse
import uz.jbnuu.tsc.parents.model.subjects.SubjectsResponse
import uz.jbnuu.tsc.parents.model.tutors.GetTutorsResponse
import uz.jbnuu.tsc.parents.utils.Constants.Companion.CONTENT_TYPE
import uz.jbnuu.tsc.parents.utils.Constants.Companion.SERVER_KEY

interface ApiService {

    @POST("login_student")
    suspend fun loginStudent(@Body loginStudentBody: LoginStudentBody): Response<LoginStudentResponse>

    @POST("login_tyutor")
    suspend fun loginTyuter(@Body loginTyuterBody: LoginTyuterBody): Response<LoginTyuterResponse>

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST
    suspend fun postNotification(@Url full_url: String, @Body notification: PushNotification): Response<ResponseBody>

    @POST("auth/login")
    suspend fun loginHemis(@Body loginHemisBody: LoginStudentBody): Response<LoginHemisResponse>

    @GET("account/me")
    suspend fun me(): Response<MeResponse>

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

    @GET("get_groups")
    suspend fun getGroups(): Response<GroupResponse>

    @GET("get_students")
    suspend fun getStudents(@Query("group_id") group_id: Int?, @Query("key") key: String?, @Query("value") value: String?): Response<StudentResponse>

    @GET("get_history_locations")
    suspend fun getLocationHistory(@Query("student_id") student_id: Int?, @Query("pagination") pagination: Int?, @Query("page") page: Int?): Response<LocationHistoryResponse>

    @POST("send_location")
    suspend fun sendLocation(@Body sendLocationBody: SendLocationBody): Response<SendLocationResponse>

    @POST("send_location1")
    suspend fun sendLocation1(@Body sendLocationBody: SendLocationBody): Response<SendLocationResponse>

    @POST("send_location_array")
    suspend fun sendLocationArray(@Body sendLocationArrayBody: SendLocationArrayBody): Response<LogoutResponse>

    @POST("send_location_array1")
    suspend fun sendLocationArray1(@Body sendLocationArrayBody: SendLocationArrayBody): Response<LogoutResponse>

    @POST("login_admin")
    suspend fun loginAdmin(@Body loginTyuterBody: LoginTyuterBody): Response<AdminResponse>

    @GET("get_tutors")
    suspend fun getTutors(): Response<GetTutorsResponse>

    @GET("get_history_locations1")
    suspend fun getAdminLocationHistory(@Query("employe_id") employe_id: Int?, @Query("pagination") pagination: Int?, @Query("page") page: Int?): Response<LocationHistoryResponse>

    @GET("statistics")
    suspend fun statistics(@Query("employe_id") employe_id: Int?): Response<StatisticResponse>

    @GET("education/exam-table")
    suspend fun examTable(@Query("semester") semester: String?): Response<ExamTableResponse>

//    @POST("orders")
//    suspend fun orders(@Query("table_id") table_id: Int, @Query("waiter_id") waiter_id: Int, @Query("lang") lang: Int, @Body orders: OrderBody): Response<OrderResponse>
}