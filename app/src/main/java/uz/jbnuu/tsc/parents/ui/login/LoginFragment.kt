package uz.jbnuu.tsc.parents.ui.login

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.LoginFragmentBinding
import uz.jbnuu.tsc.parents.model.login.parents.LoginParentBody
import uz.jbnuu.tsc.parents.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate), View.OnClickListener {

    private val vm: LoginViewModel by viewModels()
    var progressDialog: ProgressDialog? = null

    @Inject
    lateinit var prefs: Prefs

    var whichOne = 0

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.loginBtn.setOnClickListener(this)
        binding.registerBtn.setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.loginBtn -> {
                hideKeyboard()
                val login = binding.loginAuth.text.toString()
                val password = binding.passwordAuth.text.toString()
                if (login.isNotEmpty() && password.isNotEmpty()) {
                    loginParents(LoginParentBody(login, password))
                } else {
                    if (login.isEmpty()) {
                        binding.loginAuth.error = "Loginingizni kiriting"
                    }
                    if (password.isEmpty()) {
                        binding.passwordAuth.error = "Passwordni kiriting"
                    }
                }
            }
            binding.registerBtn -> {
                hideKeyboard()
                findNavController().navigateSafe(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun loginParents(loginParentBody: LoginParentBody) {
        vm.loginParents(loginParentBody)
        vm.loginResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    closeLoader()
                    if (it.data?.status == 1) {
                        it.data.apply {
                            token?.let {
                                prefs.save(prefs.token, it)
                            }
                            data?.ism?.let {
                                prefs.save(prefs.name, it)
                            }
                            data?.familya?.let {
                                prefs.save(prefs.fam, it)
                            }
                            data?.otasi_ismi?.let {
                                prefs.save(prefs.lastname, it)
                            }
                            tarif?.apply {
                                person_count?.let {
                                    prefs.save(prefs.person_count, it)
                                }
                                id?.let {
                                    prefs.save(prefs.tarif_id, it)
                                }
                            }
                            prefs.save(prefs.login, "" + loginParentBody.login)
                            prefs.save(prefs.password, "" + loginParentBody.password)

                            findNavController().navigateSafe(R.id.action_loginFragment_to_mainFragment)
                        }
                    } else {
                        snackBar( "status " + it.data?.status)
                    }
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    snackBar( it.message.toString())
                }
            }
        }
    }

    private fun showLoader() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(binding.root.context, "Iltimos kuting...")
        }
        progressDialog?.show()
    }

    private fun closeLoader() {
        progressDialog?.dismiss()
    }
}