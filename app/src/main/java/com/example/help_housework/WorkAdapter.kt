package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkAdapter (val workWriteList : ArrayList<WorkWrite>) : RecyclerView.Adapter<WorkAdapter.ConstomViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkAdapter.ConstomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.work_item, parent, false)
        return ConstomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return workWriteList.size
    }
    override fun onBindViewHolder(holder: WorkAdapter.ConstomViewHolder, position: Int) {
        holder.contentwork.text = workWriteList.get(position).contentwork
        holder.imgwork.setImageResource(workWriteList.get(position).imgwork)
    }




    class ConstomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentwork = itemView.findViewById<TextView>(R.id.tv_contentwork)
        val imgwork = itemView.findViewById<ImageView>(R.id.img_contentwork)
    }


}