package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BuymeAdapter(val buymeWriteList : MutableList<BuymeWrite>) : RecyclerView.Adapter<BuymeAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ) : BuymeAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buyme_item, parent,false)

        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return buymeWriteList.size
    }

    override fun onBindViewHolder(holder: BuymeAdapter.CustomViewHolder, position: Int) {
        val buymeWrite = buymeWriteList[position]

        holder.fromUser.text = buymeWrite.fromUser
        holder.toUser.text = buymeWrite.toUser
        holder.content.text = buymeWrite.content
        holder.hyperlink.text = buymeWrite.hyperlink
        holder.status.text = buymeWrite.status
        holder.date.text = buymeWrite.date

        if (buymeWrite.status =="구매완료"){
            holder.itemView.findViewById<TextView>(R.id.tv_purchaseStatus_b).setBackgroundResource(R.drawable.tv_purchase_y)
        } else {
            holder.itemView.findViewById<TextView>(R.id.tv_purchaseStatus_b).setBackgroundResource(R.drawable.tv_purchase_n)
        }
    }
    fun setBuymeList(buymeList : MutableList<BuymeWrite>){
        buymeWriteList.clear()
        buymeWriteList.addAll(buymeList)
        notifyDataSetChanged()
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fromUser = itemView.findViewById<TextView>(R.id.tv_fromUser)
        val toUser = itemView.findViewById<TextView>(R.id.tv_toUser)
        val content = itemView.findViewById<TextView>(R.id.tv_content_b)
        val hyperlink = itemView.findViewById<TextView>(R.id.tv_hyperlink_b)
        val status = itemView.findViewById<TextView>(R.id.tv_purchaseStatus_b)
        val date = itemView.findViewById<TextView>(R.id.tv_date_b)

    }


}