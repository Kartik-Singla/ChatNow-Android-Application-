package com.project.chatnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {
    private val TAG = "forget"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

//        val fireuser = FirebaseAuth.getInstance().currentUser
        val email_text = findViewById<EditText>(R.id.email_text)

        val btn_verify = findViewById<Button>(R.id.btn_getvarify)
        btn_verify.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val str_email:String? = email_text.text.toString()
                if(str_email!=null) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(str_email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG,"Successful")
                                Toast.makeText(this@ForgetPassword,"Email sent!!! Check your Mailbox",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@ForgetPassword,login_activity::class.java))
                                finish()
                            }
                        }
                }
            }
        })

    }
}