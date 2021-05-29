package com.project.chatnow

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.project.chatnow.Model.Chat
import com.squareup.picasso.Picasso
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class MessageAdapter(private var context: Context, private var mchats: List<Chat>, private var imgURL:String) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    private var flag:Int = 1
    var hashMapflag: HashMap<Int, Int> = HashMap()
    var hashMapmessage: HashMap<Int, String> = HashMap()
    var hashMapdecmessage: HashMap<Int, String> = HashMap()

    private val TAG = "messageadapter"
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {  // Holding the views...ViewHolder is compulsory for Recycler Views
        var img_view: ImageView = view.findViewById(R.id.profile_image)
        var show_message = view.findViewById<TextView>(R.id.show_message)
    }

    val MSG_TYPE_LEFT=0
    val MSG_TYPE_RIGHT=1

   private lateinit var fuser: FirebaseUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType==MSG_TYPE_RIGHT){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false) //put the message to the right
            return ViewHolder(view)
        }

        else
        {
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return mchats.size
    }
//     fun abc(){
//         val a:RecyclerView.ViewHolder? =
//
//    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat:Chat=mchats.get(position)
        holder.show_message.text =chat.getMessage()

            if(imgURL.equals("default"))
            holder.img_view.setImageResource(R.mipmap.ic_launcher)
        else
        {
            Picasso.with(holder.img_view.context)
                .load(imgURL)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img_view)
        }
        holder.itemView.setOnClickListener(object :View.OnClickListener{

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onClick(v: View?) {

                Log.d(TAG, holder.layoutPosition.toString())

                if(holder.layoutPosition !in hashMapflag.keys){
                    val message:String = holder.show_message.text.toString()
                    val newMessage:String = decrypt(message)
                    Log.d(TAG, "if called")
                    holder.show_message.text = newMessage
                    hashMapmessage.put(holder.layoutPosition, message)
                    hashMapdecmessage.put(holder.layoutPosition, newMessage)
                    hashMapflag.put(holder.layoutPosition, 0)
                }
                else if(hashMapflag.get(holder.layoutPosition) == 0){
                    Log.d(TAG, "second if called")
                    holder.show_message.text = hashMapmessage.get(holder.layoutPosition)
                    hashMapflag.replace(holder.layoutPosition, 1)
                }
                else if(hashMapflag.get(holder.layoutPosition) == 1){
                    Log.d(TAG, "third if called")
                    holder.show_message.text = hashMapdecmessage.get(holder.layoutPosition)
                    hashMapflag.replace(holder.layoutPosition, 0)
                }


            }
        })
    }

    private fun decrypt(message: String): String {
        val encryptedByte:ByteArray = message.toByteArray(charset("ISO-8859-1"))

        val encryptionKey: ByteArray = byteArrayOf(9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53)
        var decipher:Cipher = Cipher.getInstance("AES")

        val secretKeySpec: SecretKeySpec = SecretKeySpec(encryptionKey, "AES")
        decipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        val decryption:ByteArray = decipher.doFinal(encryptedByte)
        val decryptedString:String = String(decryption)
        return decryptedString

    }

    override fun getItemViewType(position: Int): Int {
        fuser = FirebaseAuth.getInstance().currentUser!!
        if(mchats.get(position).getSender().equals(fuser.uid)) // if the id is same as current id then it means we are the sender and we have to display the message on the right
            return MSG_TYPE_RIGHT
        else
        {
            return MSG_TYPE_LEFT
        }

    }
}