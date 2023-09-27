package com.lcpetlylgmg.petly.organization.post.data

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

class PostAdapter(var clickListener: OnItemClickListener) :
    RecyclerView.Adapter<PostAdapter.DataObjectHolder>() {

    lateinit var context: Context

    var arraylist = mutableListOf<Post>()
    fun setList(list: List<Post>) {
        this.arraylist = list.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            post: Post
        )

    }


    class DataObjectHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var name: AppCompatTextView? = itemView?.findViewById(R.id.name)
        var age: AppCompatTextView? = itemView?.findViewById(R.id.age)
        var breed: AppCompatTextView? = itemView?.findViewById(R.id.breed)
        var image: ShapeableImageView? = itemView?.findViewById(R.id.image)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataObjectHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return DataObjectHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        val post = arraylist[position]

        holder.name?.text = post.dogName
        holder.age?.text = "Age : "+post.age
        holder.breed?.text = post.breed

        holder.image?.let { Glide.with(context).load(post.imageUrl).into(it) }

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(post)
        }

    }

    override fun getItemCount(): Int {
        return arraylist.size
    }


}