package com.lcpetlylgmg.petly.common.requests.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.utils.Global

class RequestAdapter(var clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RequestAdapter.DataObjectHolder>() {

    lateinit var context: Context

    var arraylist = mutableListOf<Request>()
    fun setList(list: List<Request>) {
        this.arraylist = list.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            request: Request
        )

    }


    class DataObjectHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var name: AppCompatTextView? = itemView?.findViewById(R.id.name)
        var date: AppCompatTextView? = itemView?.findViewById(R.id.date)
        var image: ShapeableImageView? = itemView?.findViewById(R.id.image)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataObjectHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return DataObjectHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        val request = arraylist[position]

        holder.name?.text = request.dogName
        holder.date?.text = Global.convertTimestampToReadableDate(request.requestTime!!)

        holder.image?.let { Glide.with(context).load(request.postImgUrl).into(it) }

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(request)
        }

    }

    override fun getItemCount(): Int {
        return arraylist.size
    }


}