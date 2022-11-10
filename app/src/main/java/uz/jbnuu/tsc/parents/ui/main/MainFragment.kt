package uz.jbnuu.tsc.parents.ui.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.adapters.StudentAdapter
import uz.jbnuu.tsc.parents.base.AddStudentDialog
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.LogoutDialog
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.HeaderLayoutBinding
import uz.jbnuu.tsc.parents.databinding.MainFragmentBinding
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsData
import uz.jbnuu.tsc.parents.model.login.parents.LoginStudentToConnectParentBody
import uz.jbnuu.tsc.parents.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<MainFragmentBinding>(MainFragmentBinding::inflate), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, StudentAdapter.OnItemClickListener {

    lateinit var bindNavHeader: HeaderLayoutBinding
    var progressDialog: ProgressDialog? = null

    private val vm: MainViewModel by viewModels()
    private var dialog: AddStudentDialog? = null

    private val studentAdapter: StudentAdapter by lazy { StudentAdapter(this) }

    @Inject
    lateinit var prefs: Prefs

    @SuppressLint("SetTextI18n")
    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        getParentStudent()
        setupRecycler()
        bindNavHeader = HeaderLayoutBinding.bind(LayoutInflater.from(requireContext()).inflate(R.layout.header_layout, null, false))
        binding.navView.addHeaderView(bindNavHeader.root)
        bindNavHeader.userNameHeader.text = prefs.get(prefs.name, " ") + " " + prefs.get(prefs.fam, "") + " " + prefs.get(prefs.lastname, "")

        binding.menu.setOnClickListener(this)
        binding.addStudent.setOnClickListener(this)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.swipeRefreshLayout.setOnRefreshListener {
            getParentStudent()
        }
    }

    private fun setupRecycler() {
        binding.listStudent.apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.menu -> {
                binding.drawer.openDrawer(GravityCompat.START)
            }
            binding.addStudent -> {
                if (dialog == null) {
                    dialog = AddStudentDialog(requireContext())
                }
                dialog?.setOnCancelClick {
                    dialog?.dismiss()
                }
                dialog?.setOnSubmitClick { login, parol ->
                    hideKeyboard()
                    dialog?.dismiss()
                    addStudent(LoginStudentToConnectParentBody(login, parol))
                }
                dialog?.show()
            }
        }
    }

    private fun addStudent(loginStudentToConnectParentBody: LoginStudentToConnectParentBody) {
        vm.connectParentStudentStudent(loginStudentToConnectParentBody)
        vm.connectParentStudentResponse.collectLA(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    dialog?.dismiss()
                    dialog = null
                    getParentStudent()

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


    private fun getParentStudent() {
        vm.getParentStudent()
        vm.getParentStudentResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    closeLoader()
                    it.data?.active?.let {
                        prefs.save(prefs.active, it)
                    }
                    it.data?.tarif?.person_count?.let {
                        prefs.save(prefs.person_count, it)
                    }
                    it.data?.data?.let {
                        if (it.size >= prefs.get(prefs.person_count, 0)) {
                            binding.addStudentCard.visibility = View.GONE
                        } else if (it.size < prefs.get(prefs.person_count, 0)) {
                            binding.addStudentCard.visibility = View.VISIBLE
                        }
                        if (it.isNotEmpty()) {
                            binding.listStudent.visibility = View.VISIBLE
                            binding.notFoundLesson.visibility = View.GONE
                        } else {
                            binding.listStudent.visibility = View.GONE
                            binding.notFoundLesson.visibility = View.VISIBLE
                        }
                        studentAdapter.setData(it)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                snackBar("Profile")
            }
            R.id.settings -> {
//                snackBar( "Sozlamalar")
                findNavController().navigateSafe(R.id.action_mainFragment_to_settingsFragment)

            }
            R.id.logout -> {
                val logoutDialog = LogoutDialog(requireContext())
                logoutDialog.show()
                logoutDialog.setOnCancelClick {
                    logoutDialog.dismiss()
                }
                logoutDialog.setOnSubmitClick {
                    logoutDialog.dismiss()
                    logout()
                    prefs.clear()
                    findNavController().navigateSafe(R.id.action_mainFragment_to_loginFragment)
                }
            }
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        vm.logout()
        vm.logoutResponse.collectLA(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
//                    showLoader()
                }
                is NetworkResult.Success -> {
//                    closeLoader()
//                    if (it.data?.status == 1) {
//                        it.data.apply {
//                            prefs.clear()
//                            sendDataToActivity?.send("Stop")
//                            findNavController().navigateSafe(R.id.action_studentMainFragment_to_loginFragment)
//                        }
//                    } else {
//                        snackBar( "status " + it.data?.status)
//                    }
                }
                is NetworkResult.Error -> {
//                    closeLoader()
//                    if (it.code == 401) {
//                        prefs.clear()
//                        sendDataToActivity?.send("Stop")
//                        findNavController().navigateSafe(R.id.action_studentMainFragment_to_loginFragment)
//                    } else {
//                        snackBar( it.message.toString())
//                    }
                }
            }
        }
    }

    private fun showLoader() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(binding.root.context, "Iltimos kuting...")
        }
        binding.swipeRefreshLayout.isRefreshing = true
        progressDialog?.show()
    }

    private fun closeLoader() {
        progressDialog?.dismiss()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onItemClick(data: ParentGetStudentsData, type: Int) {
        if (type == 1) {
            data.auth_id?.let {
                prefs.save(prefs.loginHemis, it)
            }
            data.first_name?.let {
                prefs.save(prefs.passwordHemis, it.decode())
            }
            data.get_me?.semester?.code?.let {
                prefs.save(prefs.semester, it)
            }
            val bundle = bundleOf("student_id" to data.id)
            findNavController().navigateSafe(R.id.action_mainFragment_to_studentMainFragment, bundle)
        } else {
            snackBar("Sizning tarifingiz faol emas. Faol qilish uchun to'lov qilishingiz kerak bo'ladi.")
        }
    }

}