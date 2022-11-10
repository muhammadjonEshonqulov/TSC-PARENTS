package uz.jbnuu.tsc.parents.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.jbnuu.tsc.parents.R
import uz.jbnuu.tsc.parents.databinding.ItemStudentBinding
import uz.jbnuu.tsc.parents.model.getStudents.ParentGetStudentsData
import uz.jbnuu.tsc.parents.utils.MyDiffUtil
import uz.jbnuu.tsc.parents.utils.Prefs


class StudentAdapter(val listener: OnItemClickListener) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>() {

    var dataProduct = emptyList<ParentGetStudentsData>()
    var next: Int? = null

    fun setData(newData: List<ParentGetStudentsData>) {
        val diffUtil = MyDiffUtil(dataProduct, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        dataProduct = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(data: ParentGetStudentsData, type: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemStudentBinding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)// DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_all_notification, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataProduct[position])
    }

    override fun getItemCount(): Int = dataProduct.size

    inner class MyViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: ParentGetStudentsData) {
            binding.name.text = data.familya + " " + data.ism + " " + data.otasi_ismi
            binding.group.text = data.get_me?.group?.name + " guruh talabasi"
            val prefs = Prefs(binding.root.context)
            val active = prefs.get(prefs.active, false)
            if (active) {
                binding.itemBack.alpha = 1f
            } else {
                binding.itemBack.alpha = 0.5f
            }
            binding.itemBack.setOnClickListener {
                if (active) {
                    listener.onItemClick(data, 1)
                } else {
                    listener.onItemClick(data, 2)
                }
            }
            data.get_me?.image?.let {
                Glide.with(binding.root.context)
                    .load(it)
                    .placeholder(R.drawable.ic_baseline_person_outline_24)
                    .into(binding.imageUser)
            }
//            binding.listLessons.adapter = lessonsAdapter
//            binding.listLessons.layoutManager = LinearLayoutManager(binding.root.context)
//            data.schedules?.let {
//                lessonsAdapter.setData(it)
//            }
//
//            data.lesson_date?.let {
//                binding.dateWeek.text = getDateTime(it)
//                binding.weekDay.text = getDayOfWeek(it)
//            }
        }
    }
}
