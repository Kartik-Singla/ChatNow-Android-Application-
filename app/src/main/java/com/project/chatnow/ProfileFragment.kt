package com.project.chatnow

import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.project.chatnow.Model.Users
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.user_item.*
import java.util.*


class ProfileFragment : Fragment() {
    private val TAG = "profilefrag"
   lateinit var fileReference:StorageReference
    lateinit var storagereference:StorageReference
    private var IMAGE_REQUEST = 1
    lateinit var imageUri:Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val imgv = view.findViewById<ImageView>(R.id.imageView3)
        val username = view.findViewById<TextView>(R.id.usernamer)


        //Profile Image Reference in storage

        storagereference = FirebaseStorage.getInstance().getReference("uploads")
        val fuser = FirebaseAuth.getInstance().currentUser
        val refer = FirebaseDatabase.getInstance().getReference("MyUsers").child(fuser!!.uid)
        refer.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user: Users? =snapshot.getValue(Users::class.java)
                username.text = user!!.getusername()

                if(user.getimageURL().equals("default"))
                {
                    imgv.setImageResource(R.mipmap.ic_launcher)
                }
                else
                {
                    Picasso.with(imgv.context)
                        .load(user.getimageURL())
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(imgv)
                }
            }
        })

        imgv.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                selectImage( )
            }
        })

        return view
    }

    fun selectImage(){
        val i: Intent = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i, IMAGE_REQUEST)

    }

    fun getFileExtension(uri:Uri):String?{
        val contentResolver: ContentResolver = context!!.getContentResolver()
        val mimeTypeMap:MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

//    fun UploadMyImage(){
//       val progressDialog = ProgressDialog(context)
//        progressDialog.setMessage("Uploading")
//        progressDialog.show()
//
//        if(imageUri!=null){
//            fileReference = storagereference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri) )
//            val uploadTask = fileReference.getFile(imageUri)
//            uploadTask!!.continueWith(object :
//                Continuation<UploadTask.TaskSnapshot?, Task<Uri>> {
//                @Throws(Exception::class)
//                 override fun then(p0: Task<UploadTask.TaskSnapshot?>): Task<Uri> {
//                    if(!p0.isSuccessful){
//                        throw Objects.requireNonNull(p0.exception)!!
//                    }
//                    return fileReference.downloadUrl
//                }
//
//            })
//        }
//
//
//    }
}





