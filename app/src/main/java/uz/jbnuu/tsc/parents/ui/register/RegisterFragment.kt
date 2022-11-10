package uz.jbnuu.tsc.parents.ui.register

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.adapters.GetTypeAdapter
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.RegisterFragmentBinding
import uz.jbnuu.tsc.parents.model.register.RegisterBody
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.collectLA

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterFragmentBinding>(RegisterFragmentBinding::inflate), OnClickListener {

    private val vm: RegisterViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null

    private lateinit var tarifAdapter: GetTypeAdapter
    var tarif_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTarifs()
    }

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener(this)
        binding.registerBtn.setOnClickListener(this)
    }

    private fun getTarifs() {
        vm.getTarifsme()
        vm.tarifTypeResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Error -> {
                    snackBar( it.message.toString())
                }
                is NetworkResult.Success -> {
                    if (it.data?.status == 1) {
                        it.data.data?.let {
                            tarifAdapter = GetTypeAdapter(requireContext(), it)
                            binding.spinnerType.adapter = tarifAdapter
                            binding.spinnerType.onItemSelectedListener = object : OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    it[position].id?.let {
                                        tarif_id = it
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun createParent(registerBody: RegisterBody) {
        vm.createParent(registerBody)
        vm.createParentResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    closeLoader()
                    if (it.data?.status == 1) {
                        snackBar( "Siz muvaffaqiyatli ro'yhatdan o'tdingiz! Tizimdan foydalanishingiz mumkin.")
                        finish()
                    } else {
                        snackBar( "Xatolik api " + it.data?.error)
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
            progressDialog = ProgressDialog(requireContext(), "Iltimos kuting...")
        }
        progressDialog?.show()
    }

    private fun closeLoader() {
        progressDialog?.dismiss()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                finish()
            }
            binding.registerBtn -> {
                hideKeyBoard()
                val surname = binding.surname.text.toString()
                val name = binding.name.text.toString()
                val lastname = binding.lastname.text.toString()
                val login = binding.login.text.toString()
                val password = binding.password.text.toString()
                val rePassword = binding.rePassword.text.toString()

                if (surname.isNotEmpty() && name.isNotEmpty() && lastname.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                    if (password == rePassword && password.length >= 6 && login.length >= 6) {
                        createParent(RegisterBody(surname, name, lastname, login, password, tarif_id))
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
        }
    }
}