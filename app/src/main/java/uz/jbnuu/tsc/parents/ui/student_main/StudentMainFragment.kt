package uz.jbnuu.tsc.parents.ui.student_main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.base.ProgressDialog
import uz.jbnuu.tsc.parents.databinding.HeaderLayoutBinding
import uz.jbnuu.tsc.parents.databinding.StudentMainFragmentBinding
import uz.jbnuu.tsc.parents.ui.ReturnDataFromActivity
import uz.jbnuu.tsc.parents.ui.SendDataToActivity
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.Prefs
import uz.jbnuu.tsc.parents.utils.collectLA
import uz.jbnuu.tsc.parents.utils.navigateSafe
import javax.inject.Inject


@AndroidEntryPoint
class StudentMainFragment : BaseFragment<StudentMainFragmentBinding>(StudentMainFragmentBinding::inflate), View.OnClickListener, ReturnDataFromActivity {


    lateinit var bindNavHeader: HeaderLayoutBinding

    private val vm: StudentMainViewModel by viewModels()
    var sendDataToActivity: SendDataToActivity? = null
    var student_id = 0

    var progressDialog: ProgressDialog? = null

    @Inject
    lateinit var prefs: Prefs

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SourceLockedOrientationActivity", "SetTextI18n")
    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        arguments?.getInt("student_id")?.let {
            student_id = it
            prefs.save("student_id", student_id)
        }

        bindNavHeader = HeaderLayoutBinding.bind(
            LayoutInflater.from(requireContext()).inflate(R.layout.header_layout, null, false)
        )
        bindNavHeader.userNameHeader.text = prefs.get(prefs.fam, " ") + " " + prefs.get(prefs.name, "")
        bindNavHeader.organizationUserHeader.text = prefs.get(prefs.group, "") + " guruh"

        prefs.get(prefs.image, "").let {
            Glide.with(requireContext()).load(it).placeholder(R.drawable.logo_main).into(bindNavHeader.imgUserDrawer)
        }


        binding.scheduleLesson.setOnClickListener(this)
        binding.attendance.setOnClickListener(this)
        binding.tableOfExams.setOnClickListener(this)
        binding.performance.setOnClickListener(this)
        binding.locationHistory.setOnClickListener(this)
        binding.deadlines.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        vm.clearTaskData()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            sendDataToActivity = activity as SendDataToActivity
            sendDataToActivity?.send("login_student_hemis#${prefs.get(prefs.loginHemis, "")}&&${prefs.get(prefs.passwordHemis, "")}", this)
            sendDataToActivity?.send("Deadline", this)
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity error")
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.scheduleLesson -> {
                val extras = FragmentNavigatorExtras(binding.scheduleLessonImage to "schedule_lesson_image")

                findNavController().navigateSafe(
                    R.id.action_studentMainFragment_to_scheduleFragment, null, null, extras
                )
            }
            binding.attendance -> {
                val extras = FragmentNavigatorExtras(binding.attendanceImage to "attendance_image")

                findNavController().navigateSafe(R.id.action_studentMainFragment_to_attendanceFragment, extras = extras)
            }

            binding.tableOfExams -> {
//                snackBar( "Hozirda ishlab chiqishda")

                val extras = FragmentNavigatorExtras(binding.tableOfExamsName to "exam_table_image")

                findNavController().navigateSafe(R.id.action_studentMainFragment_to_examTableFragment, extras = extras)
            }
            binding.performance -> {
//                snackBar( "Hozirda ishlab chiqishda")
                val extras = FragmentNavigatorExtras(binding.performanceImage to "performance_image")
                findNavController().navigateSafe(R.id.action_studentMainFragment_to_performanceFragment, extras = extras)
            }

            binding.deadlines -> {
                val extras = FragmentNavigatorExtras(binding.deadlinesImage to "deadlines_image")
                findNavController().navigateSafe(R.id.action_studentMainFragment_to_deadlineFragment, extras = extras)
            }
            binding.backBtn -> {
                finish()
            }
            binding.locationHistory -> {
                val bundle = bundleOf("student_id" to student_id)
                val extras = FragmentNavigatorExtras(binding.locationHistoryImage to "location_history_image")
                findNavController().navigateSafe(R.id.action_studentMainFragment_to_locationHistoryFragment, bundle, extras = extras)
            }
        }
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

    override fun returnData(value: String) {

    }
}