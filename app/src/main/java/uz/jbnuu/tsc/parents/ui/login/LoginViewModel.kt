package uz.jbnuu.tsc.parents.ui.login

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
import uz.jbnuu.tsc.parents.model.login.LoginParentsResponse
import uz.jbnuu.tsc.parents.model.login.parents.LoginParentBody
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _loginResponse = Channel<NetworkResult<LoginParentsResponse>>()
    var loginResponse = _loginResponse.receiveAsFlow()

    fun loginParents(loginParentBody: LoginParentBody) = viewModelScope.launch {
        _loginResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginParents(loginParentBody)
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

}