package com.project.chatnow.Model

class Chat(private var sender:String,private  var receiver:String, private var message:String) {
    constructor() : this("default","default","default" ){ // no arguement constructor is necessary

    }

    fun getSender():String{
        return sender
    }

    fun getReceiver():String{
        return receiver
    }

    fun getMessage():String{
        return message
    }

    fun setSender(sender:String):Unit{
        this.sender=sender
    }

    fun setReceiver(receiver:String):Unit{
        this.receiver= receiver
    }

    fun setusername(message:String):Unit{
        this.message=message
    }

}