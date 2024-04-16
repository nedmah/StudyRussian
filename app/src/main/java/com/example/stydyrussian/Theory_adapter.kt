package com.example.stydyrussian

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.stydyrussian.databinding.TheoryItemBinding

class Theory_adapter(val listener: TheoryListener): RecyclerView.Adapter<Theory_adapter.Theory_VH>() {


//companion object{
    var theoryList = ArrayList<Theory>()
//}




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Theory_VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.theory_item,parent,false)
        return Theory_VH(view)
    }

    override fun getItemCount(): Int {
        return theoryList.size
    }

    override fun onBindViewHolder(holder: Theory_VH, position: Int) {
        holder.bind(theoryList[position],listener)
    }



    class Theory_VH(item : View) : RecyclerView.ViewHolder(item) {
        val binding = TheoryItemBinding.bind(item)
        fun bind(theory : Theory, listener: TheoryListener) = with(binding){
            tvTaskTitle.text = theory.title
            imgTheory.setImageResource(theory.imageID)
            if (theory.isCompleted) theoryCW.strokeColor = ContextCompat.getColor(itemView.context, R.color.green)
            else theoryCW.strokeColor = ContextCompat.getColor(itemView.context, R.color.ef_stroke)
            itemView.setOnClickListener {
                listener.onClick(theory)
            }
        }
    }

    fun addItems(list: List<Theory>) {
        //theoryList.clear()
        theoryList.addAll(list)
        notifyDataSetChanged()
    }

    interface TheoryListener{
        fun onClick(theory: Theory)
    }


}

