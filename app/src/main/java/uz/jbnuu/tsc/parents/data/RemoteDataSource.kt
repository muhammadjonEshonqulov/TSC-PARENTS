package uz.jbnuu.tsc.parents.data

import okhttp3.ResponseBody
import retrofit2.Response
import uz.jbnuu.tsc.parents.data.network.ApiService
import uz.jbnuu.tsc.parents.model.StatisticResponse
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.attendance.AttendanceResponse
import uz.jbnuu.tsc.parents.model.examTable.ExamTableResponse
import uz.jbnuu.tsc.parents.model.group.GroupResponse
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryBody
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
import uz.jbnuu.tsc.parents.model.student.StudentBody
import uz.jbnuu.tsc.parents.model.student.StudentResponse
import uz.jbnuu.tsc.parents.model.subjects.SubjectsResponse
import uz.jbnuu.tsc.parents.model.tutors.GetTutorsResponse
import javax.inject.Inject
import javax.inject.Named

class RemoteDataSource @Inject constructor(@Named("provideApiService") val apiService: ApiService, @Named("provideApiServiceHemis") val apiServiceHemis: ApiService) {

    suspend fun loginStudent(loginStudentBody: LoginStudentBody): Response<LoginStudentResponse> {
        return apiService.loginStudent(loginStudentBody)
    }

    suspend fun loginTyuter(loginTyuterBody: LoginTyuterBody): Response<LoginTyuterResponse> {
        return apiService.loginTyuter(loginTyuterBody)
    }

    suspend fun postNotification(full_url: String, notification: PushNotification): Response<ResponseBody> {
        return apiService.postNotification(full_url, notification)
    }

    suspend fun me(): Response<MeResponse> {
        return apiServiceHemis.me()
    }

    suspend fun studentReference(): Response<ReferenceResponse> {
        return apiServiceHemis.studentReference()
    }

    suspend fun studentReferenceDownload(id: Int): Response<ResponseBody> {
        return apiServiceHemis.studentReferenceDownload(id)
    }

    suspend fun loginHemis(loginHemisBody: LoginStudentBody): Response<LoginHemisResponse> {
        return apiServiceHemis.loginHemis(loginHemisBody)
    }

    suspend fun subjects(): Response<SubjectsResponse> {
        return apiServiceHemis.subjects()
    }

    suspend fun subject(subject: Int?, semester: String): Response<SubjectResponse> {
        return apiServiceHemis.subject(subject, semester)
    }

    suspend fun semesters(): Response<SemestersResponse> {
        return apiServiceHemis.semesters()
    }

    suspend fun schedule(week: Int): Response<ScheduleResponse> {
        return apiServiceHemis.schedule(week)
    }

    suspend fun performance(): Response<PerformanceResponse> {
        return apiServiceHemis.performance()
    }

    suspend fun attendance(semester: String?): Response<AttendanceResponse> {
        return apiServiceHemis.attendance(semester)
    }

    suspend fun logout(): Response<LogoutResponse> {
        return apiService.logout()
    }


    suspend fun getGroups(): Response<GroupResponse> {
        return apiService.getGroups()
    }

    suspend fun getStudents(studentBody: StudentBody?): Response<StudentResponse> {
        return apiService.getStudents(studentBody?.group_id, studentBody?.key, studentBody?.value)
    }

    suspend fun getLocationHistory(locationHistoryBody: LocationHistoryBody): Response<LocationHistoryResponse> {
        return apiService.getLocationHistory(locationHistoryBody.student_id, 50, locationHistoryBody.page)
    }

    suspend fun getAdminLocationHistory(locationHistoryBody: LocationHistoryBody): Response<LocationHistoryResponse> {
        return apiService.getAdminLocationHistory(locationHistoryBody.student_id, 50, locationHistoryBody.page)
    }

    suspend fun sendLocation(sendLocationBody: SendLocationBody): Response<SendLocationResponse> {
        return apiService.sendLocation(sendLocationBody)
    }

    suspend fun sendLocation1(sendLocationBody: SendLocationBody): Response<SendLocationResponse> {
        return apiService.sendLocation1(sendLocationBody)
    }

    suspend fun sendLocationArray(sendLocationArrayBody: SendLocationArrayBody): Response<LogoutResponse> {
        return apiService.sendLocationArray(sendLocationArrayBody)
    }

    //   99 180 03 37
    suspend fun sendLocationArray1(sendLocationArrayBody: SendLocationArrayBody): Response<LogoutResponse> {
        return apiService.sendLocationArray1(sendLocationArrayBody)
    }

    suspend fun loginAdmin(loginTyuterBody: LoginTyuterBody): Response<AdminResponse> {
        return apiService.loginAdmin(loginTyuterBody)
    }

    suspend fun getTutors(): Response<GetTutorsResponse> {
        return apiService.getTutors()
    }

    suspend fun statistics(employe_id: Int?): Response<StatisticResponse> {
        return apiService.statistics(employe_id)
    }

    suspend fun examTable(semester: String?): Response<ExamTableResponse> {
        return apiServiceHemis.examTable(semester)
    }

}