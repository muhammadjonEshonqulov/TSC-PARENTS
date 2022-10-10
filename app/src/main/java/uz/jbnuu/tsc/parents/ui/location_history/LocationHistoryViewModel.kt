package uz.jbnuu.tsc.parents.ui.location_history

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
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryBody
import uz.jbnuu.tsc.parents.model.history_location.LocationHistoryResponse
import uz.jbnuu.tsc.parents.model.login.admin.AdminResponse
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterBody
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import javax.inject.Inject

@HiltViewModel
class LocationHistoryViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _getLocationHistoryResponse = Channel<NetworkResult<LocationHistoryResponse>>()
    var getLocationHistoryResponse = _getLocationHistoryResponse.receiveAsFlow()

    fun getLocationHistory(locationHistoryBody: LocationHistoryBody) = viewModelScope.launch {
        _getLocationHistoryResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getLocationHistory(locationHistoryBody)
                _getLocationHistoryResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _getLocationHistoryResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _getLocationHistoryResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _getAdminLocationHistoryResponse = Channel<NetworkResult<LocationHistoryResponse>>()
    var getAdminLocationHistoryResponse = _getAdminLocationHistoryResponse.receiveAsFlow()

    fun getAdminLocationHistory(adminlocationHistoryBody: LocationHistoryBody) = viewModelScope.launch {
        _getAdminLocationHistoryResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getAdminLocationHistory(adminlocationHistoryBody)
                _getAdminLocationHistoryResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _getAdminLocationHistoryResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _getAdminLocationHistoryResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _loginAdminResponse = Channel<NetworkResult<AdminResponse>>()
    var loginAdminResponse = _loginAdminResponse.receiveAsFlow()

    fun loginAdmin(loginAdminBody: LoginTyuterBody) = viewModelScope.launch {
        _loginAdminResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginAdmin(loginAdminBody)
                _loginAdminResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _loginAdminResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _loginAdminResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _loginTyuterResponse = Channel<NetworkResult<LoginTyuterResponse>>()
    var loginTyuterResponse = _loginTyuterResponse.receiveAsFlow()

    fun loginTutor(loginTyuterBody: LoginTyuterBody) = viewModelScope.launch {
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
}