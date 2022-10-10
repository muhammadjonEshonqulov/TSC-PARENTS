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
import uz.jbnuu.tsc.parents.model.login.admin.AdminResponse
import uz.jbnuu.tsc.parents.model.login.hemis.LoginHemisResponse
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentResponse
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterBody
import uz.jbnuu.tsc.parents.model.login.tyuter.LoginTyuterResponse
import uz.jbnuu.tsc.parents.model.me.MeResponse
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.handleResponse
import uz.jbnuu.tsc.parents.utils.hasInternetConnection
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _loginResponse = Channel<NetworkResult<LoginStudentResponse>>()
    var loginResponse = _loginResponse.receiveAsFlow()

    fun loginStudent(loginStudentBody: LoginStudentBody) = viewModelScope.launch {
        _loginResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginStudent(loginStudentBody)
                _loginResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _loginResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _loginResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
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

    private val _loginHemisResponse = Channel<NetworkResult<LoginHemisResponse>>()
    var loginHemisResponse = _loginHemisResponse.receiveAsFlow()

    fun loginHemis(loginHemisBody: LoginStudentBody) = viewModelScope.launch {
        _loginHemisResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.loginHemis(loginHemisBody)
                _loginHemisResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _loginHemisResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _loginHemisResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }

    private val _meHemisResponse = Channel<NetworkResult<MeResponse>>()
    var meHemisResponse = _meHemisResponse.receiveAsFlow()

    fun meHemis() = viewModelScope.launch {
        _meHemisResponse.send(NetworkResult.Loading())
        if (hasInternetConnection(getApplication())) {
            try {
                val response = repository.remote.me()
                _meHemisResponse.send(handleResponse(response))
            } catch (e: Exception) {
                _meHemisResponse.send(NetworkResult.Error("Xatolik : " + e.message))
            }
        } else {
            _meHemisResponse.send(NetworkResult.Error(App.context.getString(R.string.connection_error)))
        }
    }
//
//    fun clearCategoryData() = viewModelScope.launch {
//        repository.local.clearCategoryData()
//        clearProductsData()
//    }
//
//    private fun clearProductsData() = viewModelScope.launch {
//        repository.local.clearProductsData()
//    }
}