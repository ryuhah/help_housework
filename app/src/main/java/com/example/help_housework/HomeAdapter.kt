package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter (val homeWriteList : ArrayList<HomeWrite>) : RecyclerView.Adapter<HomeAdapter.CostomViewHolder>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.CostomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return CostomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return homeWriteList.size
    }

    override fun onBindViewHolder(holder: HomeAdapter.CostomViewHolder, position: Int) {
        holder.writer.text = homeWriteList.get(position).writer
        holder.contenth.text = homeWriteList.get(position).contenth
        holder.dateh.text = homeWriteList.get(position).dateh

    }

    class CostomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writer = itemView.findViewById<TextView>(R.id.tv_writer)
        val contenth = itemView.findViewById<TextView>(R.id.tv_content_h)
        val dateh = itemView.findViewById<TextView>(R.id.tv_date_h)

    }


}