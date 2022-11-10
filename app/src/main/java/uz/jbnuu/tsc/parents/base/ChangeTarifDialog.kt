package uz.jbnuu.tsc.parents.base

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.adapters.ChangeTarifAdapter
import uz.jbnuu.tsc.parents.databinding.DialogChangeTarifBinding
import uz.jbnuu.tsc.parents.model.type_tarif.TarifData
import uz.jbnuu.tsc.parents.utils.Prefs
import uz.jbnuu.tsc.parents.utils.lg

class ChangeTarifDialog : AlertDialog, ChangeTarifAdapter.OnItemClickListener {
    private var text: TextView? = null


    var submit_listener_onclick: ((Int) -> Unit)? = null
    var cancel_listener_onclick: (() -> Unit)? = null
    var binding: DialogChangeTarifBinding
    var tarif_id = -1
    var prefs: Prefs
    lateinit var changeTarifAdapter: ChangeTarifAdapter
    fun setOnSubmitClick(l: ((Int) -> Unit)?) {
        submit_listener_onclick = l
    }

    fun setOnCancelClick(l: (() -> Unit)?) {
        cancel_listener_onclick = l
    }

    constructor(context: Context, data: List<TarifData>) : super(context) {
        this.setCancelable(true)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_change_tarif, null, false)
        binding = DialogChangeTarifBinding.bind(view)
        prefs = Prefs(binding.root.context)


        view?.apply {

            changeTarifAdapter = ChangeTarifAdapter(this@ChangeTarifDialog)
            binding.listTarif.layoutManager = LinearLayoutManager(binding.root.context)
            binding.listTarif.adapter = changeTarifAdapter
            data.forEach {
                it.select = it.id == prefs.get(prefs.tarif_id, 0)
            }
            tarif_id = prefs.get(prefs.tarif_id, -1)

            changeTarifAdapter.setData(data)

            binding.save.setOnClickListener {
                lg("tarif_id in onclick->" + tarif_id)
                submit_listener_onclick?.invoke(tarif_id)
            }
            binding.cancel.setOnClickListener {
                cancel_listener_onclick?.invoke()
            }
        }
        setView(view)
    }

    override fun onItemClick(data2: TarifData) {
        data2.id?.let {
            tarif_id = it
            lg("tarif_id in onItemClick->" + tarif_id)
//            prefs.save(prefs.tarif_id, it)
            var newIndex = 0
            var oldIndex = 0
            changeTarifAdapter.dataProduct.forEachIndexed { index, tarifData ->
                if (tarifData.select == true) {
                    oldIndex = index
                }
                if (data2.id == tarifData.id) {
                    tarifData.select = true
                    newIndex = index
                } else {
                    tarifData.select = false
                }
            }
            changeTarifAdapter.notifyItemChanged(newIndex)
            changeTarifAdapter.notifyItemChanged(oldIndex)
        }
    }
}