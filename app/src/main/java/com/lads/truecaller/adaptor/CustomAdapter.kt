package com.lads.truecaller.adaptor

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lads.truecaller.R
import com.lads.truecaller.model.ContactModel
import com.lads.truecaller.model.RecentContactsModel


class AllContactsCustomAdapter(private var contactModelArrayList: ArrayList<ContactModel>) :
    RecyclerView.Adapter<AllContactsCustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemsViewModel = contactModelArrayList[position]

//        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.textView.text = itemsViewModel.name
        holder.textViewNumber.text = itemsViewModel.number

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dailIntent = Intent(Intent.ACTION_CALL)
                dailIntent.data =
                    Uri.parse("tel:${contactModelArrayList[position].number}")
                v!!.context.startActivity(dailIntent)
            }
        })
    }

    override fun getItemCount(): Int {
        return contactModelArrayList.size
    }

    fun submitList(mRecentContactArrayList: java.util.ArrayList<ContactModel>) {
        contactModelArrayList = mRecentContactArrayList
        notifyDataSetChanged()
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val textViewNumber: TextView = itemView.findViewById(R.id.textView_number)
    }
}


