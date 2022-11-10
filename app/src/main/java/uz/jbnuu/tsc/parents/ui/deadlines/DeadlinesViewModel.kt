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

    private val _taskDataResponse = Channel<List<Task>>()
    var taskDataResponse = _taskDataResponse.receiveAsFlow()

    fun getTaskData(student_id: Int) = viewModelScope.launch {
        _taskDataResponse.send(repository.local.getTaskData(student_id).stateIn(this).value)
    }

//    val tasks = repository.local.getTaskData()

}