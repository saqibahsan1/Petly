package com.lcpetlylgmg.petly.common.chat.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.utils.Global

class ChatAdapter(private val messages: List<Message>, private val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val INCOMING_MESSAGE = 1
    private val OUTGOING_MESSAGE = 2

    inner class IncomingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: AppCompatTextView? = itemView.findViewById(R.id.name)
        var message: AppCompatTextView? = itemView.findViewById(R.id.message)
        var time: AppCompatTextView? = itemView.findViewById(R.id.time)
    }

    inner class OutgoingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: AppCompatTextView? = itemView.findViewById(R.id.message)
        var time: AppCompatTextView? = itemView.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == INCOMING_MESSAGE) {
            val view = inflater.inflate(R.layout.item_receiving_message, parent, false)
            IncomingMessageViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_sending_message, parent, false)
            OutgoingMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is IncomingMessageViewHolder) {
            // Bind incoming message to the view holder
            holder.message?.text = message.messageText
            val time = Global.convertTimestampToReadableTime(message.time)
            holder.time?.text = time
        } else if (holder is OutgoingMessageViewHolder) {
            // Bind outgoing message to the view holder
            holder.message?.text = message.messageText
            val time = Global.convertTimestampToReadableTime(message.time)
            holder.time?.text = time
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) {
            OUTGOING_MESSAGE
        } else {
            INCOMING_MESSAGE
        }
    }
}
