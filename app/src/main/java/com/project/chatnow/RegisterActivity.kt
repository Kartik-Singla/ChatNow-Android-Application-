package com.project.chatnow

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity:AppCompatActivity() {

    private var TAG = "register"
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        progressBar_r.visibility=View.INVISIBLE
        val userET = findViewById<EditText>(R.id.userEditText)
        val passET = findViewById<EditText>(R.id.passEditText)
        val emailET = findViewById<EditText>(R.id.emailEditText)
        val registerbtn = findViewById<Button>(R.id.btnregister)

        auth = FirebaseAuth.getInstance()
        registerbtn.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                val user_text = userET.text.toString()
                val pass_text = passET.text.toString()
                val email_text = emailET.text.toString()
                if (user_text.isEmpty() or pass_text.isEmpty() or email_text.isEmpty())

                    Toast.makeText(this@RegisterActivity, "Please fill all the required fields", Toast.LENGTH_SHORT).show()
            else
            {
                registerNow(user_text, email_text, pass_text)
            }
        }
        })
    }
    fun registerNow(username:String , email:String, password:String){  // registering a user
        progressBar_r.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(object: OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                if(p0.isSuccessful()){
                    progressBar_r.visibility=View.INVISIBLE
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid
                    val myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId!!)
                    var hashmap : HashMap<String, String> = HashMap()
                    hashmap.put("id",userId)
                    hashmap.put("username",username)
                    hashmap.put("imageURL", "default")
                    hashmap.put("status", "Offline")

                    // opening main file after successful registeration

                    myref.setValue(hashmap).addOnCompleteListener(object:OnCompleteListener<Void>{

                        override fun onComplete(p0: Task<Void>) {
                            if (p0.isSuccessful()){
                                val intent = Intent(this@RegisterActivity, login_activity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)// we add this to start new fresh activity by closing all other activities. if we wont add this then on pressing back button user will see the login page again
                                startActivity(intent)
                                finish()
                            }
                        }
                    })

                }
                else
                {
                    progressBar_r.visibility=View.INVISIBLE
                    Toast.makeText(this@RegisterActivity, "Invalid Email/Password or User already exists",Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}