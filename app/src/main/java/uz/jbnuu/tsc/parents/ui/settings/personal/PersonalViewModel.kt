package uz.jbnuu.tsc.parents.ui.settings.personal

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
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifMatnResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val repository: Repository, application: Application
) : AndroidViewModel(application) {

    private val _updatePersonalResponse = Channel<NetworkResult<TarifMatnResponse>>()
    var updatePersonalResponse = _updatePersonalResponse.receiveAsFlow()

    fun updatePersonal(registerBody: RegisterBody) = viewModelScope.launch {
        _updatePersonalResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.updateParent(registerBody)
                _updatePersonalResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _updatePersonalResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _updatePersonalResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _updatePersonalResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

}