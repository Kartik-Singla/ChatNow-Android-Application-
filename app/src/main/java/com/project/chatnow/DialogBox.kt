package com.project.chatnow

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.app.AppCompatDialogFragment

//import androidx.appcompat.app.AppCompatDialogFragment

class DialogBox : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Alert").setMessage("Do you want to archive the chat?")
            .setPositiveButton("Yes",object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            }).show()
        return builder.create()
    }
}