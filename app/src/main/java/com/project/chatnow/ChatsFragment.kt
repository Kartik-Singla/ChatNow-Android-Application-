 package com.project.chatnow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.chatnow.Model.ChatList
import com.project.chatnow.Model.Users
import kotlinx.android.synthetic.main.fragment_chats.*



/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {


    val userlist:ArrayList<ChatList> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        val recyclerView_chat = view.findViewById<RecyclerView>(R.id.recycler_vew_chat)
        recyclerView_chat.setHasFixedSize(true)
        recyclerView_chat.layoutManager = LinearLayoutManager(context)

        val fuser = FirebaseAuth.getInstance().currentUser


        val reference = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser!!.uid)

        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for(shot:DataSnapshot in snapshot.children)
                {
                    val chatlist: ChatList = shot.getValue(ChatList::class.java)!!

                        userlist.add(chatlist)
                }
                chatList()
            }
        })
        return view
    }

    private fun chatList() {
        //getting all previous chats and users
            var muser:ArrayList<Users> = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("MyUsers")
        ref.addValueEventListener(object:ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                muser.clear() //clearing the list and filling it again with data
                for(shot:DataSnapshot in snapshot.children)
                {
                    val user: Users? = shot.getValue(Users::class.java)
                    for(chatlist:ChatList in userlist){
                        if (user != null) {
                            if(user.getId() == chatlist.getId())
                            {
                                muser.add(user)
                            }

                        }
                    }
                }
                if(context!=null) {
                    val userAdapter = UserAdapter(context!!, muser,true)
                    recycler_vew_chat.adapter = userAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}