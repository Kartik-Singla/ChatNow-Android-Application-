package com.project.chatnow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.solver.widgets.Snapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.chatnow.Model.Users
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_users.*



/**
 * A simple [Fragment] subclass.
 * Use the [UsersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsersFragment : Fragment() {
    private var TAG = "usersfragment"
    private var musers:ArrayList<Users> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        ReadUsers()
        return view
    }
    fun ReadUsers() // this function is to read all users and add them in out app
    {

        val firebaseuser =  FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("MyUsers")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                musers.clear()

                for(shot:DataSnapshot in snapshot.children){
                    Log.d(TAG, "HEllo")
                    var users:Users = shot.getValue(Users::class.java)!!
                    if(firebaseuser!!.uid!=users.getId())  // we ousrselves should not be shown in our users

                    {
                        musers.add(users)
                    }
                }
                val useradapter = context?.let { UserAdapter(it, musers,false) }
                recycler_view?.adapter = useradapter // this line will only be executed if recycler_view is not null
            }
        })
    }

}