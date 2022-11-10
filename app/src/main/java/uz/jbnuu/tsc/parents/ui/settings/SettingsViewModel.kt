package uz.jbnuu.tsc.parents.ui.settings

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
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifBody
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifMatnResponse
import uz.jbnuu.tsc.parents.model.type_tarif.TarifResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: Repository, application: Application
) : AndroidViewModel(application) {

    private val _changeTarifResponse = Channel<NetworkResult<TarifResponse>>()
    var changeTarifResponse = _changeTarifResponse.receiveAsFlow()

    fun getTarifsme() = viewModelScope.launch {
        _changeTarifResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.getTarifsme()
                _changeTarifResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _changeTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _changeTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _changeTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _updateTarifResponse = Channel<NetworkResult<ChangeTarifResponse>>()
    var updateTarifResponse = _updateTarifResponse.receiveAsFlow()

    fun updateTarifsme(changeTarifBody: ChangeTarifBody) = viewModelScope.launch {
        _updateTarifResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.updateTarif(changeTarifBody)
                _updateTarifResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _updateTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _updateTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _updateTarifResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }


    private val _tarifMatnResponse = Channel<NetworkResult<TarifMatnResponse>>()
    var tarifMatnResponse = _tarifMatnResponse.receiveAsFlow()

    fun tarifMatnStudent() = viewModelScope.launch {
        _tarifMatnResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.tarifMatn()
                _tarifMatnResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _tarifMatnResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _tarifMatnResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _tarifMatnResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

}