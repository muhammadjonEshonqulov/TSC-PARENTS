package uz.jbnuu.tsc.parents.ui.reference

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.app.App
import uz.jbnuu.tsc.parents.data.Repository
import uz.jbnuu.tsc.parents.model.login.hemis.LoginHemisResponse
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.reference.ReferenceResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ReferenceViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _referenceResponse = Channel<NetworkResult<ReferenceResponse>>()
    var referenceResponse = _referenceResponse.receiveAsFlow()

    fun reference() = viewModelScope.launch {
        _referenceResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.studentReference()
                _referenceResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _referenceResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _referenceResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _referenceResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _referenceDownloadResponse = Channel<NetworkResult<ResponseBody>>()
    var referenceDownloadResponse = _referenceDownloadResponse.receiveAsFlow()

    fun referenceDownload(id: Int) = viewModelScope.launch {
        _referenceDownloadResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.studentReferenceDownload(id)
                _referenceDownloadResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _referenceDownloadResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _referenceDownloadResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _referenceDownloadResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _loginHemisResponse = Channel<NetworkResult<LoginHemisResponse>>()
    var loginHemisResponse = _loginHemisResponse.receiveAsFlow()

    fun loginHemis(loginHemisBody: LoginStudentBody) = viewModelScope.launch {
        _loginHemisResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginHemis(loginHemisBody)
                _loginHemisResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _loginHemisResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _loginHemisResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _loginHemisResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }
}