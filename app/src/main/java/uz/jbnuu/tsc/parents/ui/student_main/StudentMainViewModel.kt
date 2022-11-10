package uz.jbnuu.tsc.parents.ui.student_main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.Repository
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.login.LogoutResponse
import uz.jbnuu.tsc.parents.model.login.hemis.LoginHemisResponse
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentResponse
import uz.jbnuu.tsc.parents.model.subjects.SubjectsResponse
import uz.jbnuu.tsc.parents.model.subjects.Task
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class StudentMainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _loginResponse = Channel<NetworkResult<LoginHemisResponse>>()
    var loginResponse = _loginResponse.receiveAsFlow()

    fun loginHemis(loginStudentBody: LoginStudentBody) = viewModelScope.launch {
        _loginResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginHemis(loginStudentBody)
                _loginResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _loginResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _loginResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _loginResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _logoutResponse = Channel<NetworkResult<LogoutResponse>>()
    var logoutResponse = _logoutResponse.receiveAsFlow()

    fun logout() = viewModelScope.launch {
        _logoutResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.logout()
                _logoutResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _logoutResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _logoutResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _logoutResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _subjectsResponse = Channel<NetworkResult<SubjectsResponse>>()
    var subjectsResponse = _subjectsResponse.receiveAsFlow()

    fun subjects() = viewModelScope.launch {
        _subjectsResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.subjects()
                _subjectsResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _subjectsResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _subjectsResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _subjectsResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _subjectResponse = Channel<NetworkResult<SubjectResponse>>()
    var subjectResponse = _subjectResponse.receiveAsFlow()

    fun subject(subject: Int?, semester: String) = viewModelScope.launch {
        _subjectResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.subject(subject, semester)
                _subjectResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _subjectResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _subjectResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _subjectResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _taskDataResponse = Channel<List<Task>>()
    var taskDataResponse = _taskDataResponse.receiveAsFlow()

//    fun getTaskData() = viewModelScope.launch {
//        _taskDataResponse.send(repository.local.getTaskData().stateIn(this).value)
//    }
//
//    fun insertCategoryData(data: SendLocationBody) = viewModelScope.launch(Dispatchers.IO) {
//        repository.local.insertSendLocationBody(data)
//    }
//
//    private val _getSendLocationsResponse = Channel<List<SendLocationBody>>()
//    var getSendLocationsResponse = _getSendLocationsResponse.receiveAsFlow()
//
//    fun getSendLocationBodyData() = viewModelScope.launch(Dispatchers.IO) {
//        _getSendLocationsResponse.send(getLocationHistory())
//    }
//
//    suspend fun getLocationHistory(): List<SendLocationBody> {
//        return repository.local.getSendLocationBodyData().stateIn(viewModelScope).value
//    }
//
//    fun clearSendLocationBodyData() = viewModelScope.launch {
//        repository.local.clearSendLocationBodyData()
//    }
//
    fun clearTaskData() = viewModelScope.launch {
        repository.local.clearTaskData()
    }
//
    fun insertTaskData(data: List<Task>) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertTaskData(data)
    }


}