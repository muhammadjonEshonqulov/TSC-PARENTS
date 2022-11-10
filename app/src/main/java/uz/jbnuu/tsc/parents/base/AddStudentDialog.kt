package uz.jbnuu.tsc.parents.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.DialogAddStudentBinding

class AddStudentDialog : AlertDialog {
    private var text: TextView? = null


    var submit_listener_onclick: ((String, String) -> Unit)? = null
    var cancel_listener_onclick: (() -> Unit)? = null
    var binding: DialogAddStudentBinding

    fun setOnSubmitClick(l: ((String, String) -> Unit)?) {
        submit_listener_onclick = l
    }

    fun setOnCancelClick(l: (() -> Unit)?) {
        cancel_listener_onclick = l
    }

    constructor(context: Context) : super(context) {
        this.setCancelable(true)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_student, null, false)
        binding = DialogAddStudentBinding.bind(view)
        view?.apply {

            binding.submit.setOnClickListener {
                val login = binding.loginStudent.text.toString()
                val password = binding.passwordStudent.text.toString()
                if (login.isNotEmpty() && password.isNotEmpty()) {
                    submit_listener_onclick?.invoke(login, password)
                } else {
                    if (login.isEmpty()) {
                        binding.loginStudent.error = "Talaba loginini kiritng"
                    }
                    if (password.isEmpty()) {
                        binding.passwordStudent.error = "Talaba parolini kiriting"
                    }
                }

            }
            binding.cancel.setOnClickListener {
                cancel_listener_onclick?.invoke()
            }
        }
        setView(view)
    }
}