package uz.jbnuu.tsc.parents.ui.remove_student

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.adapters.RemoveStudentAdapter
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.MessageDialog
import uz.jbnuu.tsc.parents.databinding.RemoveStudentFragmentBinding
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsData
import uz.jbnuu.tsc.parents.model.remove.RemoveStudentBody
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.collectLA
import uz.jbnuu.tsc.parents.utils.navigateSafe

@AndroidEntryPoint
class RemoveStudentFragment : BaseFragment<RemoveStudentFragmentBinding>(RemoveStudentFragmentBinding::inflate), RemoveStudentAdapter.OnItemClickListener, OnClickListener {

    private val studentAdapter: RemoveStudentAdapter by lazy { RemoveStudentAdapter(this) }
    private val vm: RemoveStudentViewModel by viewModels()

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        getParentStudent()
        setupRecycler()
        binding.backBtn.setOnClickListener(this)
        binding.swipeRefreshLayout.setOnRefreshListener {
            getParentStudent()
        }
    }

    override fun onItemClick(data: ParentGetStudentsData, type: Int) {
        val messageDialog = MessageDialog(requireContext(), "Talabani uzish", "Siz rostdan ham ${data.familya} ${data.ism}ni talabalar ro'yxatidan chiqarmoqchimisiz ?", okMessage = "Ha")
        messageDialog.show()
        messageDialog.setOnSubmitClick {
            messageDialog.dismiss()
            data.id?.let {
                disconnectParentStudent(RemoveStudentBody(it))
            }
        }
        messageDialog.setOnCancelClick {
            messageDialog.dismiss()
        }
    }

    private fun setupRecycler() {
        binding.listStudent.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getParentStudent() {
        vm.getParentStudent()
        vm.getParentStudentResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    it.data?.data?.let {
                        if (it.isNotEmpty()) {
                            binding.listStudent.visibility = View.VISIBLE
                            binding.notFoundLesson.visibility = View.GONE
                            studentAdapter.setData(it)
                        } else {
                            binding.notFoundLesson.visibility = View.VISIBLE
                            binding.listStudent.visibility = View.GONE
                        }
                    }
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    if (it.code == 401) {
                        findNavController().navigateSafe(R.id.action_mainFragment_to_loginFragment)
                    } else {
                        snackBar("" + it.message.toString())
                    }
                }
            }
        }
    }

    private fun disconnectParentStudent(removeStudentBody: RemoveStudentBody) {
        vm.disconnectParentStudent(removeStudentBody)
        vm.disconnectParentStudentResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    it.data?.message?.let {
                        snackBar(it)
                        getParentStudent()
                    }
                }
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    if (it.code == 401) {
                        findNavController().navigateSafe(R.id.action_mainFragment_to_loginFragment)
                    } else {
                        snackBar("" + it.message.toString())
                    }
                }
            }
        }
    }

    private fun showLoader() {
        binding.swipeRefreshLayout.isRefreshing = true
    }

    private fun closeLoader() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                finish()
            }
        }
    }
}