package uz.jbnuu.tsc.parents.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.ActivityMainBinding
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.model.subjects.SubjectsData
import uz.jbnuu.tsc.parents.ui.student_main.StudentMainViewModel
import uz.jbnuu.tsc.parents.utils.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SendDataToActivity {

    private val vm: StudentMainViewModel by viewModels()
    private val timeTest = (6 * 1000).toLong() + 100
    private var timer: CountDownTimer? = null
    private val MYREQUESTCODE = 100
    private var returnDataFromActivity: ReturnDataFromActivity? = null

    @Inject
    lateinit var prefs: Prefs

    private var appUpdateManager: AppUpdateManager? = null

    var task: Task<LocationSettingsResponse>? = null
    var locationSettingsRequestBuilder: LocationSettingsRequest.Builder? = null

    lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_CHECK_SETTINGS = 0x1

    var subjectTasks: ArrayList<uz.jbnuu.tsc.parents.model.subjects.Task>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (appUpdateManager == null) {
            appUpdateManager = AppUpdateManagerFactory.create(this)
        }
        checkUpdate()

        FirebaseMessaging.getInstance().subscribeToTopic("jbnuu_tsc_channel")
    }


    private fun subjects() {
        vm.subjects()
        vm.subjectsResponse.collectLatestLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.data?.let {
                        val currentSemesterSubjects = java.util.ArrayList<SubjectsData>()
                        currentSemesterSubjects.clear()
                        it.forEachIndexed { index, subjectsData ->
                            if (prefs.get(prefs.semester, "") == subjectsData._semester) {
                                currentSemesterSubjects.add(subjectsData)
                                subject(subjectsData.subject?.id, subjectsData._semester)
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
//                    if (it.code == 401) {
//                        navigateToLogin()
//                    }
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private fun subject(subject: Int?, semester: String) {
        vm.subject(subject, semester)
        vm.subjectResponse.collectLatestLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    lg("subject 2 ")

                    if (subjectTasks == null) {
                        subjectTasks = java.util.ArrayList()
                    }

                    it.data?.data?.tasks?.let { tasks ->
                        tasks.forEach {
                            it.student_id = prefs.get("student_id", 0)
                        }
                        vm.insertTaskData(tasks)
                    }
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                appUpdateManager?.registerListener(listener)
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, MYREQUESTCODE)
            } else {
                lg("No Update available")
            }
        }
    }

    private val listener: InstallStateUpdatedListener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            lg("An update has been downloaded")
            showSnackBarForCompleteUpdate()
        }
    }

    private fun showSnackBarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            binding.root, "New app is ready!", Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Install") { view: View? ->
            appUpdateManager?.completeUpdate()
        }
        snackbar.setActionTextColor(ContextCompat.getColor(binding.root.context, R.color.cl_color_primary))
        snackbar.show()
    }

    override fun onStop() {
        appUpdateManager?.unregisterListener(listener)
        super.onStop()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            MYREQUESTCODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        lg("" + "Result Ok")
                    }
                    Activity.RESULT_CANCELED -> {
                        lg("" + "Result Cancelled")
                        checkUpdate()
                    }
                    ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                        lg("" + "Update Failure")
                        checkUpdate()

                    }
                }
            }
        }
    }

    private fun loginStudent(loginStudentBody: LoginStudentBody) {
        vm.loginHemis(loginStudentBody)
        vm.loginResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.data?.token?.let {
                        prefs.save(prefs.hemisToken, it)
                    }
                    returnDataFromActivity?.returnData("success")
                }
                is NetworkResult.Error -> {
                    snackBar(binding, "" + it.data?.error)
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, MYREQUESTCODE)
            }
        }
    }

    override fun send(value: String, returnDataFromActivity: ReturnDataFromActivity?) {
        this.returnDataFromActivity = returnDataFromActivity
        if (value.split("#").first() == "login_student_hemis") {
            loginStudent(LoginStudentBody(value.split("#").last().split("&&").first(), value.split("#").last().split("&&").last()))
        } else if (value == "Deadline") {
            subjects()
        }
    }

}