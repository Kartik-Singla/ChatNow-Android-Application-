package com.project.chatnow

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.chatnow.Model.Chat
import com.project.chatnow.Model.ChatList
import com.project.chatnow.Model.Users
import com.squareup.picasso.Picasso
import java.util.*

class ViewHolder(view:View): RecyclerView.ViewHolder(view) {  // Holding the views...ViewHolder is compulsory for Recycler Views
    var img_view: ImageView = view.findViewById(R.id.imageView)
    var text_view = view.findViewById<TextView>(R.id.textView3)
    var imageON = view.findViewById<ImageView>(R.id.statusimageON)
    var imageOFF = view.findViewById<ImageView>(R.id.statusimageOFF)
}

class UserAdapter(private var context: Context, private var musers: List<Users>, private var isChat:Boolean) : RecyclerView.Adapter<ViewHolder>() {

    private val TAG = "useradapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = musers.get(position)
        Log.d(TAG, "${position}")
        holder.text_view.text = users.getusername()
        if(users.getimageURL().equals("default"))
            holder.img_view.setImageResource(R.mipmap.ic_launcher)
        else
        { 
            Picasso.with(holder.img_view.context)
                .load(users.getimageURL())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img_view)
        }

        if(isChat){
            if(users.getStatus().equals("Online"))
            {
                holder.imageON.visibility = View.VISIBLE
                holder.imageOFF.visibility = View.GONE
//                holder.time.visibility = View.GONE
//                holder.date.visibility = View.GONE
            }
            else
            {
                holder.imageON.visibility = View.GONE
                holder.imageOFF.visibility = View.VISIBLE
                val calendar:Calendar = Calendar.getInstance()
//                holder.time.visibility = View.VISIBLE
//                holder.time.text = calendar.time.toString()
//                holder.date.text = calendar.setWeekDate().toString()

            }
        }
        else
        {
            holder.imageON.visibility = View.GONE
            holder.imageOFF.visibility = View.GONE
        }

        holder.itemView.setOnClickListener(object:View.OnClickListener{// an itemview can be described as a single view in list view or recycle view
            override fun onClick(v: View?) {
                val i = Intent(context, MessageActivity::class.java)
            Log.d(TAG, "CAlled")
                i.putExtra("userid", users.getId())
                context.startActivity(i)

            }
        })

        holder.itemView.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {

                openDialog(users.getId())

                return true
            }
        })

    }

    private fun openDialog(id:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Archive Chat").setMessage("Do you want to archive the chat?")
            .setNegativeButton("No",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            .setPositiveButton("Yes",object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                    val fuser = FirebaseAuth.getInstance().currentUser!!
                    val ref = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.uid)

                    ref.addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(shot:DataSnapshot in snapshot.children){
                                val c:ChatList = shot.getValue(ChatList::class.java)!!
                                if(c.getId() == id){
                                    val rem = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.uid).child(id)
                                    rem.removeValue()
                                    break
                                }
                            }

                        }
                    })

                }
            }).show()
    }

}