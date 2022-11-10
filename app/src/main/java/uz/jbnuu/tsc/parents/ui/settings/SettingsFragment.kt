package uz.jbnuu.tsc.parents.ui.settings

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.ChangeTarifDialog
import uz.jbnuu.tsc.parents.base.MessageDialog
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.SettingsFragmentBinding
import uz.jbnuu.tsc.parents.model.type_tarif.ChangeTarifBody
import uz.jbnuu.tsc.parents.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsFragmentBinding>(SettingsFragmentBinding::inflate), OnClickListener {

    val vm: SettingsViewModel by viewModels()

    @Inject
    lateinit var prefs: Prefs
    var progressDialog: ProgressDialog? = null
    private var changeTarifDialog: ChangeTarifDialog? = null
    var tarif_id = 0


    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        binding.changeTarif.setOnClickListener(this)
        binding.changeOwnData.setOnClickListener(this)
        binding.removeStudent.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)

    }

    private fun getTarifs() {
        vm.getTarifsme()
        vm.changeTarifResponse.collectLA(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    closeLoader()
                    snackBar(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    closeLoader()
                    it.data?.data?.let {
                        if (changeTarifDialog == null)
                            changeTarifDialog = ChangeTarifDialog(binding.root.context, it)
                        changeTarifDialog?.show()
                        changeTarifDialog?.setOnCancelClick {
                            changeTarifDialog?.dismiss()
                            changeTarifDialog = null
                        }
                        changeTarifDialog?.setOnSubmitClick {
                            tarif_id = it
                            updateTarifsme()
                            changeTarifDialog?.dismiss()
                            changeTarifDialog = null
                        }
                    }
                }
            }
        }
    }

    private fun updateTarifsme() {
        vm.updateTarifsme(ChangeTarifBody(tarif_id))
        vm.updateTarifResponse.collectLatestLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Error -> {
                    closeLoader()
                    if (it.errorCode == 1) {
                        it.message?.let {
                            val messageDialog = MessageDialog(requireContext(), "Tarifni almashtirish xatoligi", it)
                            messageDialog.show()
                            messageDialog.setOnSubmitClick {
                                messageDialog.dismiss()
                            }
                        }
                    } else if (it.errorCode == 0) {
                        snackBar(it.message.toString())
                    }
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    closeLoader()
                    if (it.data?.status == 1) {
                        prefs.save(prefs.tarif_id, tarif_id)
                        snackBar("${it.data.message}")
                    }

                }
            }
        }
    }

    private fun tarifMatnStudent() {
        vm.tarifMatnStudent()
        vm.tarifMatnResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    it.data?.data?.let {
                        val messageDialog = MessageDialog(requireContext(), "Tarifni almashtirish", it)
                        messageDialog.show()
                        messageDialog.setOnSubmitClick {
                            messageDialog.dismiss()
                            getTarifs()
                        }
                    }
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
            binding.backBtn -> {
                finish()
            }
            binding.changeTarif -> {
                tarifMatnStudent()
            }
            binding.changeOwnData -> {
                findNavController().navigateSafe(R.id.action_settingsFragment_to_personalFragment)
            }
            binding.removeStudent -> {
                findNavController().navigateSafe(R.id.action_settingsFragment_to_removeStudentFragment)
            }
        }
    }
}