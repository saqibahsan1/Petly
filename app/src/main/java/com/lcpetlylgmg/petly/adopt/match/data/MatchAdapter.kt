package com.lcpetlylgmg.petly.adopt.match.data

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.organization.post.data.Post

class MatchAdapter(
    var clickListener: OnItemClickListener
) : RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    var arraylist = mutableListOf<Post>()
    fun setList(list: List<Post>) {
        this.arraylist = list.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onImageClickListener(
            post: Post
        )

        fun onSentRequestClickListener(
            post: Post,
            position: Int
        )

        fun onCancelClickListener(post: Post)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_card, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = arraylist[position]
        if (post != null) {
            if (post.link.isNullOrEmpty()) {
                holder.nameAge.text = "${post.dogName}" + " " + "${post.age}"
                val states = post.state?.joinToString(", ")
                holder.city.text = "$states" + " " + "${post.city}"
            }else{
                if (post.isBlog?.lowercase() == "ja")
                    holder.nameAge.text = "${post.productName}" + " " + "${post.price}"
                else
                    holder.nameAge.text = "${post.productName}"
            }

            Glide.with(holder.image)
                .load(post.imageUrl)
                .into(holder.image)

            holder.image.setOnClickListener {
                clickListener.onImageClickListener(post)
            }
            holder.requestButton.setOnClickListener {
                clickListener.onSentRequestClickListener(post, position)
                removeItem(position)
            }
            holder.cancelButton.setOnClickListener {
                removeItem(position)
                clickListener.onCancelClickListener(post)
            }
        }
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameAge: AppCompatTextView = view.findViewById(R.id.nameAge)
        var city: TextView = view.findViewById(R.id.stateCountry)
        var image: ShapeableImageView = view.findViewById(R.id.image)
        var requestButton: AppCompatImageView = view.findViewById(R.id.sendRequest)
        var cancelButton: AppCompatImageView = view.findViewById(R.id.cancelRequest)
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < arraylist.size) {
            arraylist.removeAt(position)
            notifyItemRemoved(position)
        }
    }

}