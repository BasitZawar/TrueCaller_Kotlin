package com.lads.truecaller.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.lads.truecaller.R
import com.lads.truecaller.model.ContactModel


class AllContactsCustomAdapter(
    private var contactModelArrayList: ArrayList<ContactModel>,
    ctx: Context
) :
    RecyclerView.Adapter<AllContactsCustomAdapter.ViewHolder>() {
    private var context = ctx

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemsViewModel = contactModelArrayList[position]

//        holder.imageView.setImageResource(itemsViewModel[position)
        holder.textView.text = itemsViewModel.name
        holder.textViewNumber.text = itemsViewModel.number
        if (itemsViewModel.image != null) {
            holder.imageView.setImageBitmap(contactModelArrayList[position].image)
        } else {
            val drawable = TextDrawable.builder()
                .buildRound("${contactModelArrayList[position].name?.get(0)}", Color.RED)
            holder.imageView.setImageDrawable(drawable)
//            holder.imageView.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.mipmap.ic_launcher_round
//                )
//            )
        }
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


