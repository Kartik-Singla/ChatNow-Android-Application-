package com.project.chatnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_activity.*

class login_activity : AppCompatActivity() {
    private val TAG = "loginactivity"
    override fun onStart() { // if the user accidently closes the app, the app goes in onPause state after opening the app it starts from onStart thats why we are puttin this code in it
        super.onStart()
        val firebaseUser = FirebaseAuth.getInstance().currentUser //saving the current user

        //checking for user existence

        if(firebaseUser!=null){  //the user should login in the app only once...without this piece of code user has to login again and again
            val intent = Intent(this@login_activity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activity)
        supportActionBar?.hide()

        val userETlogin = findViewById<EditText>(R.id.userlogin)
        val passETlogin = findViewById<EditText>(R.id.passlogin)
        val loginbtn = findViewById<Button>(R.id.loginbtn)

        //Forgot Password

        val btn_forgot = findViewById<Button>(R.id.btn_forget)

        btn_forgot.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(this@login_activity,ForgetPassword::class.java))

            }
        })
        //Firebase Authentication
        val auth = FirebaseAuth.getInstance()

        val rgstr_btn = findViewById<Button>(R.id.rgstr_nowbtn)

        rgstr_btn.setOnClickListener(object:View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@login_activity,RegisterActivity::class.java)
                startActivity(intent)
            }
        })
        //Login Button
        loginbtn.setOnClickListener(object:View.OnClickListener {

            override fun onClick(v: View?) {

                val email_text = userETlogin.text.toString()
                val pass_text = passETlogin.text.toString()

                if(email_text.isEmpty() or pass_text.isEmpty())
                {
                    Toast.makeText(this@login_activity, "Please fill all the fields", Toast.LENGTH_LONG).show()
                }
                else
                {

                    auth.signInWithEmailAndPassword(email_text, pass_text).addOnCompleteListener(object:OnCompleteListener<AuthResult>{

                        override fun onComplete(p0: Task<AuthResult>) {
                            Log.d(TAG,"Sucessfiljhhu")
                            if (p0.isSuccessful()){
                                Log.d(TAG,"Sucessfil")//if the user is found
                                val intent: Intent = Intent(this@login_activity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this@login_activity, "User doesn't exist",Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }

            }
        })
    }
}