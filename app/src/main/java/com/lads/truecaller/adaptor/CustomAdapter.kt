package com.lads.truecaller.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.model.ContactModel


class CustomAdapter(private val contactModelArrayList: ArrayList<ContactModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = contactModelArrayList[position]

        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.textView.text = ItemsViewModel.name
        holder.textViewNumber.text = ItemsViewModel.number
    }

    override fun getItemCount(): Int {
        return contactModelArrayList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val textViewNumber: TextView = itemView.findViewById(R.id.textView_number)
    }
}


