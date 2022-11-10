package uz.jbnuu.tsc.parents.ui.reference

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.downloader.PRDownloader
import com.tonyodev.fetch2.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import uz.jbnuu.tsc.parents.base.BaseFragment
import uz.jbnuu.tsc.parents.databinding.ReferenceFragmentBinding
import uz.jbnuu.tsc.parents.model.login.student.LoginStudentBody
import uz.jbnuu.tsc.parents.utils.NetworkResult
import uz.jbnuu.tsc.parents.utils.Prefs
import uz.jbnuu.tsc.parents.utils.collectLA
import uz.jbnuu.tsc.parents.utils.lg
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ReferenceFragment : BaseFragment<ReferenceFragmentBinding>(ReferenceFragmentBinding::inflate) {

    private val vm: ReferenceViewModel by viewModels()

    lateinit var fetch: Fetch

    @Inject
    lateinit var prefs: Prefs

    override fun onViewCreatedd(view: View, savedInstanceState: Bundle?) {
        reference()
        download()
    }


    private fun download() {
        val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(requireContext())
            .setDownloadConcurrentLimit(3)
            .build()

        fetch = Fetch.Impl.getInstance(fetchConfiguration)

        val url = "https://student.jbnuu.uz/rest/v1/student/reference-download?id=2159"
        val file = "/downloads/test.pdf"

        val request = Request(url, file)
        request.priority = (Priority.HIGH)
        request.networkType = (NetworkType.ALL)
        request.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ2MVwvYXV0aFwvbG9naW4iLCJhdWQiOiJ2MVwvYXV0aFwvbG9naW4iLCJleHAiOjE2NjUxMTY5MzEsImp0aSI6IjQwMTIwMTEwMDAxMiIsInN1YiI6IjEyIn0.iF5jgbzyACU2wfOU1lEFu0SMkYuIzOpLeCWnJ8v_reE")

        fetch.enqueue(request, { updatedRequest ->
            lg("updatedRequest->" + updatedRequest)
        }) { error ->
            lg("error request -> " + error)
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            // todo change the file location/name according to your needs
            val downloadFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/TSC JBNUU/") // + body.string()
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                if (!downloadFile.exists()) {
                    downloadFile.mkdirs()
                }
                PRDownloader.download("https://student.jbnuu.uz/rest/v1/student/reference-download?id=2159", downloadFile.absolutePath, "file.pdf")
//                val fileReader = ByteArray(4096)
//                val fileSize = body.contentLength()
//                var fileSizeDownloaded: Long = 0
//                inputStream = body.byteStream()
//                outputStream = FileOutputStream(downloadFile)
//                while (true) {
//                    val read: Int = inputStream.read(fileReader)
//                    if (read == -1) {
//                        break
//                    }
//                    outputStream.write(fileReader, 0, read)
//                    fileSizeDownloaded += read.toLong()
//                    lg("file download: $fileSizeDownloaded of $fileSize")
//                }
//                outputStream.flush()
                true
            } catch (e: IOException) {
                lg("error write to storage " + e.message.toString())
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    private fun reference() {
        vm.reference()
        vm.referenceResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        it.data.data?.forEach {
                            it.file?.let {

                            }
                            it.file?.split("download?id=")?.last()?.let {
                                referenceDownload(it.toInt())
                            }
                        }
                    } else {
                        it.data?.error?.let {
                            snackBar( " " + it)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    if (it.code == 401) {
                        loginHemis()
                    } else {
                        snackBar( it.message.toString())
                    }
                }
            }
        }
    }

    private fun referenceDownload(id: Int) {
        vm.referenceDownload(id)
        vm.referenceDownloadResponse.collectLA(lifecycleScope) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    lg("response file " + it.data.toString())

                    it.data?.let { it1 -> writeResponseBodyToDisk(it1) }
                }
                is NetworkResult.Error -> {
                    closeLoader()
                    snackBar( it.message.toString())
                }
            }
        }
    }

    private fun loginHemis() {
        vm.loginHemis(LoginStudentBody("",""))
        vm.loginHemisResponse.collectLA(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoader()
                }
                is NetworkResult.Success -> {
                    if (it.data?.success == true) {
                        it.data.apply {
                            data?.token?.let {
                                prefs.save(prefs.hemisToken, it)
                                reference()
                            }
                        }
                    } else {
                        it.data?.error?.let {
                            snackBar( " " + it)
                        }
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

    }

    private fun closeLoader() {

    }
}