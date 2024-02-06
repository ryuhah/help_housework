package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BuymeAdapter(val buymeWriteList : ArrayList<BuymeWrite>) : RecyclerView.Adapter<BuymeAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ) : BuymeAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buyme_item, parent,false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return buymeWriteList.size
    }

    override fun onBindViewHolder(holder: BuymeAdapter.CustomViewHolder, position: Int) {
        holder.fromUser.text = buymeWriteList.get(position).fromUser
        holder.toUser.text = buymeWriteList.get(position).toUser
        holder.content.text = buymeWriteList.get(position).content
        holder.hyperlink.text = buymeWriteList.get(position).hyperlink
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fromUser = itemView.findViewById<TextView>(R.id.tv_fromUser)
        val toUser = itemView.findViewById<TextView>(R.id.tv_toUser)
        val content = itemView.findViewById<TextView>(R.id.tv_content_b)
        val hyperlink = itemView.findViewById<TextView>(R.id.tv_hyperlink_b)

    }


}