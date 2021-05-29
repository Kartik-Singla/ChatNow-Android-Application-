package com.project.chatnow
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.project.chatnow.Model.Users

class MainActivity : AppCompatActivity() {
    private val TAG = "mainactivity"
    private lateinit var firebaseUser:FirebaseUser
    private lateinit var myref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseUser = FirebaseAuth.getInstance().currentUser!! // instance of the current user
         myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.uid) // instance to the database

        myref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var users: Users? = snapshot.getValue(Users::class.java)
            }
        })
        //tab layout and viewpager
        val tablayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewpager = findViewById<ViewPager>(R.id.view_pager) // instantiating a view pager

        var viewpagerddapter = ViewPAdapter(supportFragmentManager) // object of ViewPagerAdapter

        // adding fragments to viewpager
        viewpagerddapter.addFragments(ChatsFragment(),"Chats")
        viewpagerddapter.addFragments(UsersFragment(),"Users")
        viewpagerddapter.addFragments(ProfileFragment(),"Profile")

        //connecting to adapter
        viewpager.adapter = viewpagerddapter
        tablayout.setupWithViewPager(viewpager)//connecting tablayout and viewpager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.logout -> {
               FirebaseAuth.getInstance().signOut()
               startActivity(Intent(this, login_activity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
               finish()
           }
        }
        return true
    }
    fun CheckStatus(status:String){
       myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.uid)
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("status", status)
        myref.updateChildren(hashMap as Map<String, String>)
    }

    override fun onResume() {
        super.onResume()
        CheckStatus("Online")
    }

    override fun onPause() {
        super.onPause()
        CheckStatus("Offline")
    }
}