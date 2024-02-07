package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BuymeAdapter(val buymeWriteList : ArrayList<BuymeWrite>) : RecyclerView.Adapter<BuymeAdapter.CustomViewHolder>(){

    private var filteredList : ArrayList<BuymeWrite> = ArrayList()

    init {
        filteredList.addAll(buymeWriteList)
    }

    fun getFilteredList() : ArrayList<BuymeWrite>{
        return filteredList
    }

    fun filterByStatus(status : String){
        filteredList.clear()
        for(item in buymeWriteList){
            if( status.isEmpty() || item.status == status){
                filteredList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ) : BuymeAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buyme_item, parent,false)

        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: BuymeAdapter.CustomViewHolder, position: Int) {
        val currentItem = filteredList[position]

        holder.fromUser.text = currentItem.fromUser
        holder.toUser.text = currentItem.toUser
        holder.content.text = currentItem.content
        holder.hyperlink.text = currentItem.hyperlink
        holder.status.text = currentItem.status
        holder.date.text = currentItem.date

        if(currentItem.status == "구매완료"){
            holder.itemView.findViewById<TextView>(R.id.tv_purchaseStatus_b).setBackgroundResource(R.drawable.tv_purchase_y)
        } else {
            holder.itemView.findViewById<TextView>(R.id.tv_purchaseStatus_b).setBackgroundResource(R.drawable.tv_purchase_n)
        }
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