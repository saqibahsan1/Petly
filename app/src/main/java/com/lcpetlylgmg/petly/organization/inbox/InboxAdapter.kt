package com.lcpetlylgmg.petly.organization.inbox

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.common.chat.data.ChatThread
import com.lcpetlylgmg.petly.prefs.PreferenceHelper
import com.lcpetlylgmg.petly.utils.Global
import com.lcpetlylgmg.petly.utils.GlobalKeys
import de.hdodenhof.circleimageview.CircleImageView

class InboxAdapter(var clickListener: OnItemClickListener) :
    RecyclerView.Adapter<InboxAdapter.DataObjectHolder>() {

    lateinit var context: Context

    var arraylist = mutableListOf<ChatThread>()
    fun setList(list: List<ChatThread>) {
        this.arraylist = list.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            chatThread: ChatThread,
            position: Int,
            relativeLayout: RelativeLayout?
        )

    }


    class DataObjectHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var name: AppCompatTextView? = itemView?.findViewById(R.id.name)
        var date: AppCompatTextView? = itemView?.findViewById(R.id.date)
        var image: CircleImageView? = itemView?.findViewById(R.id.image)
        var lastMsg: AppCompatTextView? = itemView?.findViewById(R.id.desc)
        var relativeLayout: RelativeLayout? = itemView?.findViewById(R.id.relative)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataObjectHolder {
        context = parent.context
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_inbox, parent, false)
        return DataObjectHolder(view)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {

        val chatThread = arraylist[position]


        if (PreferenceHelper.getPref(context)
                .getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_ADOPT || PreferenceHelper.getPref(
                context
            ).getCurrentUser()?.accountType == GlobalKeys.ACCOUNT_TYPE_AGENT
        ) {
            holder.name?.text = chatThread.shelterName
            if (chatThread.isShelterNewMessage) {

                holder.relativeLayout?.background =
                    context.resources.getDrawable(R.drawable.bg_unread_msg)
            }
        } else {
            holder.name?.text = chatThread.userName
            if (chatThread.isUserNewMessage) {
                holder.relativeLayout?.background =
                    context.resources.getDrawable(R.drawable.bg_unread_msg)
            }
        }


        holder.date?.text = Global.convertTimestampToReadableDate(chatThread.modifiedDate)

        holder.image?.let { Glide.with(context).load(chatThread.postImg).into(it) }

        holder.lastMsg?.text = chatThread.lastMsg

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(chatThread, position, holder.relativeLayout)
        }

    }

    override fun getItemCount(): Int {
        return arraylist.size
    }


}