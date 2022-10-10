package uz.jbnuu.tsc.parents.ui.deadlines

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.Repository
import uz.jbnuu.tsc.parents.model.group.GroupResponse
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterBody
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterResponse
import uz.jbnuu.tsc.parents.model.subjects.Task
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import javax.inject.Inject

@HiltViewModel
class DeadlinesViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    var landscape = false

    private val _getGroupsResponse = Channel<NetworkResult<GroupResponse>>()
    var getGroupsResponse = _getGroupsResponse.receiveAsFlow()

    fun getGroups() = viewModelScope.launch {
        _getGroupsResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getGroups()
                _getGroupsResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _getGroupsResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _getGroupsResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _loginTyuterResponse = Channel<NetworkResult<LoginTyuterResponse>>()
    var loginTyuterResponse = _loginTyuterResponse.receiveAsFlow()

    fun loginTyuter(loginTyuterBody: LoginTyuterBody) = viewModelScope.launch {
        _loginTyuterResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginTyuter(loginTyuterBody)
                _loginTyuterResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _loginTyuterResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _loginTyuterResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _taskDataResponse = Channel<List<Task>>()
    var taskDataResponse = _taskDataResponse.receiveAsFlow()

    fun getTaskData() = viewModelScope.launch {
        _taskDataResponse.send(repository.local.getTaskData().stateIn(this).value)
    }

//    val tasks = repository.local.getTaskData()

}