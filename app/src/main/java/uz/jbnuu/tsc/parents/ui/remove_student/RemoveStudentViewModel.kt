package uz.jbnuu.tsc.parents.ui.remove_student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.Repository
import uz.jbnuu.tsc.parents.model.SubjectResponse
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsResponse
import uz.jbnuu.tsc.parents.model.login.LogoutResponse
import uz.jbnuu.tsc.parents.model.login.parents.ConnectParentResponse
import uz.jbnuu.tsc.parents.model.login.parents.LoginStudentToConnectParentBody
import uz.jbnuu.tsc.parents.model.remove.RemoveStudentBody
import uz.jbnuu.tsc.parents.model.remove.RemoveStudentResponse
import uz.jbnuu.tsc.parents.model.subjects.SubjectsResponse
import uz.jbnuu.tsc.parents.model.subjects.Task
import uz.jbnuu.tsc.parents.model.type_tarif.TarifMatnResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RemoveStudentViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _getParentStudentResponse = Channel<NetworkResult<ParentGetStudentsResponse>>()
    var getParentStudentResponse = _getParentStudentResponse.receiveAsFlow()

    fun getParentStudent() = viewModelScope.launch {
        _getParentStudentResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.parentGetStudents()
                _getParentStudentResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _getParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _getParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _getParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _disconnectParentStudentResponse = Channel<NetworkResult<RemoveStudentResponse>>()
    var disconnectParentStudentResponse = _disconnectParentStudentResponse.receiveAsFlow()

    fun disconnectParentStudent(removeStudentBody: RemoveStudentBody) = viewModelScope.launch {
        _disconnectParentStudentResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.disconnectParentStudent(removeStudentBody)
                _disconnectParentStudentResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _disconnectParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _disconnectParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _disconnectParentStudentResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

}