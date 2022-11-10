package uz.jbnuu.tsc.parents.base

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.DialogMessageBinding

class MessageDialog : AlertDialog {
    private var text: TextView? = null


    var submit_listener_onclick: (() -> Unit)? = null
    var cancel_listener_onclick: (() -> Unit)? = null
    var binding: DialogMessageBinding

    fun setOnSubmitClick(l: (() -> Unit)?) {
        submit_listener_onclick = l
    }

    fun setOnCancelClick(l: (() -> Unit)?) {
        cancel_listener_onclick = l
    }


    constructor(context: Context, title: String, message: String, okMessage: String? = null) : super(context) {
        this.setCancelable(false)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_message, null, false)
        binding = DialogMessageBinding.bind(view)
        view?.apply {

            if (okMessage != null) {
                binding.cancel.visibility = View.VISIBLE
                binding.okMessage.text = okMessage
            } else {
                binding.cancel.visibility = View.GONE
            }
            binding.messageTittle.text = title

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.messageText.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.messageText.text = Html.fromHtml(message)
            }

            binding.okMessage.setOnClickListener {
                submit_listener_onclick?.invoke()
            }
            binding.cancel.setOnClickListener {
                cancel_listener_onclick?.invoke()
            }
        }
        setView(view)
    }
}