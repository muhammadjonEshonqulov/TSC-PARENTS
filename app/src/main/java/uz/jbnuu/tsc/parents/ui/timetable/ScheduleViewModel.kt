package uz.jbnuu.tsc.parents.ui.timetable

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
import uz.jbnuu.tsc.parents.model.schedule.ScheduleResponse
import uz.jbnuu.tsc.parents.model.semester.SemestersResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    var landscape = false

    private val _semestersResponse = Channel<NetworkResult<SemestersResponse>>()
    var semestersResponse = _semestersResponse.receiveAsFlow()

    fun semesters() = viewModelScope.launch {
        _semestersResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.semesters()
                _semestersResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _semestersResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _semestersResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _semestersResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _scheduleResponse = Channel<NetworkResult<ScheduleResponse>>()
    var scheduleResponse = _scheduleResponse.receiveAsFlow()

    fun schedule(week: Int) = viewModelScope.launch {
        _scheduleResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.schedule(week)
                _scheduleResponse.send(handleResponse(response))
            } catch (e: SocketTimeoutException) {
                _scheduleResponse.send(NetworkResult.Error(App.context.getString(R.string.bad_network_message)))
            } catch (e: Exception) {
                _scheduleResponse.send(NetworkResult.Error(App.context.getString(R.string.onother_error) + e.message))
            }
        } else {
            _scheduleResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }
}