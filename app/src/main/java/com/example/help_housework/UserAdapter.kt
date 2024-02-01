package com.example.help_housework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.help_housework.R.*
import com.example.help_housework.R.layout.activity_join

class UserAdapter (list: ArrayList<UserAccount>) : RecyclerView.Adapter<CustomViewHolder>(){
    var mList : ArrayList<UserAccount> = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(activity_join, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val p = mList.get(position)
        holder.setHolder(p)
    }
}

class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    fun setHolder(userAccount: UserAccount) {
        itemView.findViewById<EditText>(R.id.et_id_j).setText(userAccount.id)
        itemView.findViewById<EditText>(R.id.et_pw_j).setText(userAccount.pw)
        itemView.findViewById<EditText>(R.id.et_name_j).setText(userAccount.name)

        val spinner = itemView.findViewById<Spinner>(R.id.spinner)
        val spinnerAdapter = ArrayAdapter<String>(itemView.context, R.layout.spinner_item, listOf(userAccount.selectedRelation) )
        spinner.adapter = spinnerAdapter

    }
}