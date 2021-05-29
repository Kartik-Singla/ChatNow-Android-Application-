package com.project.chatnow.Model

class ChatList(private var id:String) {
    constructor() : this("default"){ // no arguement constructor is necessary
    }

    fun getId():String{
        return id
    }

    fun setId(id:String){
        this.id = id
    }
}