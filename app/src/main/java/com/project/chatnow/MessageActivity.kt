package com.project.chatnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.chatnow.Model.Chat
import com.project.chatnow.Model.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.chat_item_left.view.*
import kotlinx.android.synthetic.main.chat_item_left.view.show_message
import kotlinx.android.synthetic.main.chat_item_right.view.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class MessageActivity : AppCompatActivity() {
    lateinit var fuser: FirebaseUser
    lateinit var userid:String
    lateinit var username:TextView
    lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var referecnce:DatabaseReference
    private val TAG = "messageactivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)




        //Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })
//        supportActionBar?.setTitle("")
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toolbar.setNavigationConten tDescription(object:View.OnClickListener{
//            override fun onClick(v: View?) {

//            }
//        })

        //Widgets for message activity
        val imageview = findViewById<ImageView>(R.id.image_view)
        username = findViewById<TextView>(R.id.profile_image2)
        val send_btn = findViewById<ImageView>(R.id.btn_send)
        val msg_edittext = findViewById<EditText>(R.id.text_send)
//        username.setOnClickListener(object:View.OnClickListener{
//            override fun onClick(v: View?) {
//                MessageAdapter.
//            }
//        })


        val intent = intent
        userid = intent.getStringExtra("userid")!!//getting the user id from the parent activity which called this activity
        fuser = FirebaseAuth.getInstance().currentUser!!
        referecnce = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid) // here we are refering to the person we want to talk to.

        val recycler_View = findViewById<RecyclerView>(R.id.recyclerView)
        recycler_View.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd=true
        recycler_View.layoutManager=linearLayoutManager
        referecnce.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) { // putting the username and profile pic of a sender i.e the person using the phone
                val user: Users? = snapshot.getValue(Users::class.java)

                if (user != null) {
                    username.text = user.getusername()
                    Log.d(TAG, user.getusername())
                } //putting the username in 'username' view of activity_message layout
                if (user != null) {
                    if(user.getimageURL().equals("default")) {
                        imageview.setImageResource(R.mipmap.ic_launcher)
                    } else {
                        Picasso.with(imageview.context)
                            .load(user.getimageURL())
                            .error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher)
                            .into(imageview)
                    }
                }
                if (user != null) {
                    readMessage(fuser.uid, userid,user.getimageURL())
                }

            }
        })
        send_btn.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val msg:String =  msg_edittext.text.toString()
                if(!msg.equals(""))
                {
                    sendMessage(fuser.uid, userid, msg)
                    msg_edittext.setText("")
                }
                else
                {
                    Toast.makeText(this@MessageActivity, "Empty Message", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun sendMessage(sender:String, receiver:String, message:String){

        val new_message:String = encrypt(message)
        referecnce = FirebaseDatabase.getInstance().getReference()
        val hashmap:HashMap<String, Any> = HashMap()
        hashmap.put("sender", sender)
        hashmap.put("receiver", receiver)
        hashmap.put("message", new_message)
        referecnce.child("Chats").push().setValue(hashmap)

        val chatref:DatabaseReference = FirebaseDatabase.getInstance().getReference("ChatList") //putting the other user in the chatlist to display in chat section
            .child(fuser.uid) //current user
            .child(userid) // other user
        val c:DatabaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(userid).child(fuser.uid)

        c.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

                }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(!snapshot.exists())
                        {
                    c.child("id").setValue(fuser.uid)
                }
            }
        })
        chatref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists())
                {
                    Log.d(TAG,"True")
                    chatref.child("id").setValue(userid)
                }
            }
        })

    }

    private fun encrypt(message: String): String {
        val encryptionKey: ByteArray = byteArrayOf(9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53)
        var cipher:Cipher = Cipher.getInstance("AES")

        val secretKeySpec: SecretKeySpec = SecretKeySpec(encryptionKey, "AES")
        val stringByte:ByteArray = message.toByteArray()

        var encryptedByte:ByteArray = ByteArray(stringByte.size)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        encryptedByte = cipher.doFinal(stringByte)
        val returnString:String = String(encryptedByte, charset("ISO-8859-1"))
        return returnString
    }



    private fun readMessage(myid:String, userid:String, imageurl:String){
        val mchat = ArrayList<Chat>()
        referecnce = FirebaseDatabase.getInstance().getReference("Chats")
        referecnce.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                mchat.clear()
                for(shot:DataSnapshot in snapshot.children){
                    val chat: Chat = shot.getValue(Chat::class.java)!!

                    if((chat.getSender() == userid && chat.getReceiver() ==myid)  || (chat.getSender() == myid && chat.getReceiver() == userid)){
                        mchat.add(chat)
                    }
                    val messageAdapter = MessageAdapter(this@MessageActivity,mchat,imageurl)
                    recyclerView?.adapter = messageAdapter
                    username.setOnClickListener(object :View.OnClickListener{
                        override fun onClick(v: View?) {
                            val itemView:View? =
                                linearLayoutManager.findViewByPosition(1)
                            if (itemView != null) {
//                                itemView.show_message.text = decrypt(itemView.show_message.toString())
                                val str = itemView.show_message.toString()
                                Log.d(TAG, str)
                                Toast.makeText(this@MessageActivity,str, Toast.LENGTH_SHORT).show()
                            }


//                            itemView.show_message.text = decrypt(itemView.show_message.toString())

                        }
                    })
                }

            }
        })

    }
    fun CheckStatus(status:String){
        referecnce = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser.uid)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("status", status)
        referecnce.updateChildren(hashMap as Map<String, String>)
    }

    override fun onResume() {
        super.onResume()
        CheckStatus("Online")
    }

    override fun onPause() {
        super.onPause()
        CheckStatus("Offline")
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

}


