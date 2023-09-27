package com.lcpetlylgmg.petly.admin.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.FirebaseFirestore
import com.lcpetlylgmg.petly.R
import com.lcpetlylgmg.petly.user.data.User
import com.lcpetlylgmg.petly.utils.GlobalKeys
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.mail.internet.InternetAddress

class UserAdminListAdapter(context: Context) :
    RecyclerView.Adapter<UserAdminListAdapter.DataObjectHolder>() {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }
    var nextMsgId = 1
    lateinit var context: Context
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAA-sYgmYc:APA91bEtUz5oJ2OY3-b0AnDOX5-US2f2QzoDM2LHXbbxnBfpHRE67T7U3LgWOxZv_g_sp3o9WNmzXZzGU6803yfn0H-nkk9_OZBBcyHdCLgSd26CJKb2DA5gD1mz-jgVOyhLll8TspEt"
    private val contentType = "application/json"


    var arraylist = mutableListOf<User>()
    fun setList(list: List<User>) {
        this.arraylist = list.toMutableList()
        notifyDataSetChanged()
    }


    class DataObjectHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        var name: AppCompatTextView? = itemView?.findViewById(R.id.name)
        var email: AppCompatTextView? = itemView?.findViewById(R.id.tvEmail)
        var image: CircleImageView? = itemView?.findViewById(R.id.image)
        var imageAction: AppCompatImageView? = itemView?.findViewById(R.id.image_action)
        var type: AppCompatTextView? = itemView?.findViewById(R.id.type)
        var button: AppCompatTextView? = itemView?.findViewById(R.id.buttonPending)

    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): DataObjectHolder {
        context = parent.context
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false)
        return DataObjectHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataObjectHolder, position: Int) {
        val user = arraylist[position]
        val isApproved = user.isApproved

        if (isApproved == true) {
            holder.button?.text = context.getString(R.string.approved)
            holder.imageAction?.setImageResource(R.drawable.round_dangerous_24)
        } else {
            holder.button?.text = context.getString(R.string.pending)
            holder.imageAction?.setImageResource(R.drawable.baseline_check_circle_24)
        }

        holder.name?.text = user.name
        holder.email?.text = user.email
        holder.type?.text = context.getString(R.string.type) + " :" + user.type

        holder.button?.setOnClickListener {
            val newIsApproved = !isApproved!!

            user.userId?.let { userId ->
                val userRef = FirebaseFirestore.getInstance().collection(GlobalKeys.USER_TABLE)
                    .document(userId)
                userRef.update("isApproved", newIsApproved).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update the user object in the dataset
                        user.isApproved = newIsApproved
                        sendNotificationToUser(user.isApproved!!, user.fcmToken!!, user.email!!)
                        // Notify the adapter that this item has changed
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private fun sendNotificationToUser(approved: Boolean, fcmToken: String, email: String?) {
        val notification = JSONObject()
        val notificationBody = JSONObject()
        var message: String = ""

        try {
            notificationBody.put("title", "Admin Notification")
            if (approved) {
                notificationBody.put("message", "Your account status is now approved.")
                message = "Your account status is now approved."
            } else {
                notificationBody.put("message", "Your account status is not approved.")
                message = "Your account status is not approved."
            }
            notification.put("to", fcmToken) // Use the user's FCM registration token
            notification.put("data", notificationBody)
        } catch (e: JSONException) {
            Log.e("TAG", "Error creating notification: ${e.message}")
        }
        sendEmail(email, message)
        sendNotification(notification)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")

            },
            Response.ErrorListener {
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }


    // Function to generate a unique message ID
    fun generateUniqueMessageId(): String {
        val messageId = nextMsgId.toString()
        nextMsgId++
        return messageId
    }


    override fun getItemCount(): Int {
        return arraylist.size
    }

    fun sendEmail(email: String?, message: String) {
        val auth = com.lcpetlylgmg.petly.EmailService.UserPassAuthenticator("petlyshelter@gmail.com", "yjojmhrpitdjybnv")
        val to = listOf(InternetAddress(email))
        val from = InternetAddress("admin@petly.com")
        val email = com.lcpetlylgmg.petly.EmailService.Email(auth, to, from, "Petly Admin", message)
        val emailService = com.lcpetlylgmg.petly.EmailService("smtp.gmail.com", 587)

        GlobalScope.launch {
            emailService.send(email)
        }
    }

}