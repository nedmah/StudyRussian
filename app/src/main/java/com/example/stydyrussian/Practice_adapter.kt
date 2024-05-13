package com.example.stydyrussian

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
//import com.example.stydyrussian.Theory_adapter.Companion.theoryList
import com.example.stydyrussian.databinding.PracticeItemBinding
import com.example.stydyrussian.databinding.TheoryItemBinding

class Practice_adapter(val listener: PracticeListener) :
    RecyclerView.Adapter<Practice_adapter.Practice_VH>() {


    val practiceList = ArrayList<Practice>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Practice_VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.practice_item, parent, false)
        return Practice_VH(view)
    }

    override fun getItemCount(): Int {
        return practiceList.size
    }

    override fun onBindViewHolder(holder: Practice_VH, position: Int) {
        holder.bind(practiceList[position], listener)
    }


    class Practice_VH(item: View) : RecyclerView.ViewHolder(item) {
        val binding = PracticeItemBinding.bind(item)
        fun bind(practice: Practice, listener: PracticeListener) = with(binding) {
            tvTaskTitle.text = practice.title
            if (practice.isCompletedTest) pracCW.strokeColor =
                ContextCompat.getColor(itemView.context, R.color.green)
            else pracCW.strokeColor = ContextCompat.getColor(itemView.context, R.color.ef_stroke)
            itemView.setOnClickListener {
                listener.onClick(practice)
            }
        }
    }

    fun addItems(list: List<Practice>) {
        //practiceList.clear() // очищаем список перед добавлением новых элементов
        practiceList.addAll(list)
        notifyDataSetChanged()
    }

    interface PracticeListener {
        fun onClick(practice: Practice)
    }

}