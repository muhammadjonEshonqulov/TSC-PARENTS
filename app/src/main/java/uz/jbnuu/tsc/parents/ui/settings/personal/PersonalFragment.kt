package uz.jbnuu.tsc.parents.ui.settings.personal

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.PersonalFragmentBinding
import uz.jbnuu.tsc.parents.model.register.RegisterBody
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.Prefs
import uz.jbnuu.tsc.parents.utils.collectLA
import javax.inject.Inject

@AndroidEntryPoint
class PersonalFragment : BaseFragment<PersonalFragmentBinding>(PersonalFragmentBinding::inflate), OnClickListener {

    @Inject
    lateinit var prefs: Prefs
    private val vm: PersonalViewModel by viewModels()
    var progressDialog: ProgressDialog? = null


    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.surname.setText(prefs.get(prefs.fam, ""))
        binding.name.setText(prefs.get(prefs.name, ""))
        binding.lastname.setText(prefs.get(prefs.lastname, ""))
        binding.login.setText(prefs.get(prefs.login, ""))
        binding.login.setText(prefs.get(prefs.login, ""))
        binding.backBtn.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)

    }

    private fun updatePersonal(registerBody: RegisterBody) {
        vm.updatePersonal(registerBody)
        vm.updatePersonalResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    snackBar(""+it.data?.data)
                    finish()
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    snackBar("" + it.message.toString())
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

    override fun onClick(v: View?) {
        when (v) {
            binding.saveBtn -> {
                val name = binding.name.text.toString()
                val surname = binding.surname.text.toString()
                val lastname = binding.lastname.text.toString()
                val login = binding.login.text.toString()
                val password = binding.password.text.toString()
                val rePassword = binding.rePassword.text.toString()

                if (surname.isNotEmpty() && name.isNotEmpty() && lastname.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                    if (password == rePassword && password.length >= 6 && login.length >= 6) {
                        updatePersonal(RegisterBody(surname, name, lastname, login, password))
                    } else {
                        if (password.length < 6) {
                            binding.password.error = "Parol kamida 6 ta simvoldan iborat bo'lishi kerak"
                        }
                        if (login.length < 6) {
                            binding.login.error = "Login kamida 6 ta simvoldan iborat bo'lishi kerak"
                        }
                        if (password != rePassword) {
                            binding.rePassword.error = "Parollar mos emas"
                        }
                    }
                } else {
                    if (surname.isEmpty()) {
                        binding.surname.error = "Familiyani kiriting"
                    }
                    if (name.isEmpty()) {
                        binding.name.error = "Ismni kiriting"
                    }
                    if (lastname.isEmpty()) {
                        binding.lastname.error = "Otasining ismini kiriting"
                    }
                    if (login.isEmpty()) {
                        binding.login.error = "Loginni kiriting"
                    }
                    if (password.isEmpty()) {
                        binding.password.error = "Parolni kiriting"
                    }
                    if (rePassword.isEmpty()) {
                        binding.rePassword.error = "Parolni takroran kiriting"
                    }
                }
            }
            binding.backBtn -> {
                finish()
            }
        }
    }

}