package uz.jbnuu.tsc.parents.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.ItemTarifTypeSpinnerBinding
import uz.jbnuu.tsc.parents.model.type_tarif.TarifData
import uz.jbnuu.tsc.parents.utils.MyDiffUtil


class ChangeTarifAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<ChangeTarifAdapter.MyViewHolder>() {

    var dataProduct = emptyList<TarifData>()
    var next: Int? = null

    fun setData(newData: List<TarifData>) {
        val diffUtil = MyDiffUtil(dataProduct, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        dataProduct = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(data: TarifData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTarifTypeSpinnerBinding = ItemTarifTypeSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)// DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_all_notification, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataProduct[position])
    }

    override fun getItemCount(): Int = dataProduct.size

    inner class MyViewHolder(private val binding: ItemTarifTypeSpinnerBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: TarifData) {
            if (data.select == true) {
                binding.selectIcon.visibility = View.VISIBLE
                binding.backItem.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.selected_color))
            } else {
                binding.selectIcon.visibility = View.INVISIBLE
                binding.backItem.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            }
            binding.nameTarif.text = data.name
            binding.namePrice.text = data.price + " so'm"
            binding.nameDefinition.text = data.definition
            binding.backItem.setOnClickListener {
                listener.onItemClick(data)
            }
        }
    }
}
