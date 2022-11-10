package uz.jbnuu.tsc.parents.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.CustomSpinnerItemBinding
import uz.jbnuu.tsc.parents.databinding.ItemTarifTypeSpinnerBinding
import uz.jbnuu.tsc.parents.model.type_tarif.TarifData

class GetTypeAdapter(val context: Context, var dataSource: List<TarifData>) : BaseAdapter() {

    lateinit var binding: ItemTarifTypeSpinnerBinding

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_tarif_type_spinner, parent, false)
        binding = ItemTarifTypeSpinnerBinding.bind(view)
        var vh = ItemHolder(binding.root)
        vh.name_tarif.text = dataSource[position].name
        vh.name_price.text = dataSource[position].price + " so'm"
        vh.name_definition.text = dataSource[position].definition
        return binding.root
    }

    private class ItemHolder(row: View?) {
        val name_tarif: TextView = row?.findViewById(R.id.name_tarif) as TextView
        val name_price: TextView = row?.findViewById(R.id.name_price) as TextView
        val name_definition: TextView = row?.findViewById(R.id.name_definition) as TextView
    }
}
