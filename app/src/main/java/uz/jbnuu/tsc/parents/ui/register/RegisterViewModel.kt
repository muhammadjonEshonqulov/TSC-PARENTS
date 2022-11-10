package uz.jbnuu.tsc.parents.ui.register

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
import uz.jbnuu.tsc.parents.model.register.RegisterBody
import uz.jbnuu.tsc.parents.model.register.RegisterResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _tarifTypeResponse = Channel<NetworkResult<TarifResponse>>()
    var tarifTypeResponse = _tarifTypeResponse.receiveAsFlow()

    fun getTarifsme() = viewModelScope.launch {
        _tarifTypeResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getTarifsme()
                _tarifTypeResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _tarifTypeResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _tarifTypeResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _tarifTypeResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _createParentResponse = Channel<NetworkResult<RegisterResponse>>()
    var createParentResponse = _createParentResponse.receiveAsFlow()

    fun createParent(registerBody: RegisterBody) = viewModelScope.launch {
        _createParentResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.createParent(registerBody)
                _createParentResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _createParentResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _createParentResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _createParentResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }
}