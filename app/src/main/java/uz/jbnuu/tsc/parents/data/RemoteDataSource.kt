package uz.jbnuu.tsc.parents.data

import okhttp3.ResponseBody
import retrofit2.Response
import uz.jbnuu.tsc.parents.data.network.ApiService
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.attendance.AttendanceResponse
import uz.jbnuu.tsc.parents.model.examTable.ExamTableResponse
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsResponse
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryBody
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
import javax.inject.Inject
import javax.inject.Named

class RemoteDataSource @Inject constructor(@Named("provideApiService") val apiService: ApiService, @Named("provideApiServiceHemis") val apiServiceHemis: ApiService) {

    suspend fun connectParentStudent(loginStudentToConnectParentBody: LoginStudentToConnectParentBody): Response<ConnectParentResponse> {
        return apiService.connectParentStudent(loginStudentToConnectParentBody)
    }

    suspend fun loginParents(loginParentBody: LoginParentBody): Response<LoginParentsResponse> {
        return apiService.loginParents(loginParentBody)
    }

    suspend fun postNotification(full_url: String, notification: PushNotification): Response<ResponseBody> {
        return apiService.postNotification(full_url, notification)
    }

    suspend fun me(): Response<MeResponse> {
        return apiServiceHemis.me()
    }

    suspend fun tarifMatn(): Response<TarifMatnResponse> {
        return apiService.tarifMatn()
    }


    suspend fun parentGetStudents(): Response<ParentGetStudentsResponse> {
        return apiService.parentGetStudents()
    }

    suspend fun getTarifsme(): Response<TarifResponse> {
        return apiService.getTarifsme()
    }

    suspend fun updateTarif(changeTarifBody: ChangeTarifBody): Response<ChangeTarifResponse> {
        return apiService.updateTarif(changeTarifBody)
    }

    suspend fun updateParent(registerBody: RegisterBody): Response<TarifMatnResponse> {
        return apiService.updateParent(registerBody)
    }

    suspend fun disconnectParentStudent(removeStudentBody: RemoveStudentBody): Response<RemoveStudentResponse> {
        return apiService.disconnectParentStudent(removeStudentBody)
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

    suspend fun createParent(registerBody: RegisterBody): Response<RegisterResponse> {
        return apiService.createParent(registerBody)
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

    suspend fun getLocationHistory(locationHistoryBody: LocationHistoryBody): Response<LocationHistoryResponse> {
        return apiService.getLocationHistory(locationHistoryBody.student_id, 50, locationHistoryBody.page)
    }

    suspend fun examTable(semester: String?): Response<ExamTableResponse> {
        return apiServiceHemis.examTable(semester)
    }

}