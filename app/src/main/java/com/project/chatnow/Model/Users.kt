package com.project.chatnow.Model

class Users(private var id: String, private var imageURL: String, private var username: String, private var status: String) {

    constructor() : this("default","default","default", "default" ){ // no arguement constructor is necessary

    }

    fun getId():String{
        return id
    }

    fun getimageURL():String{
        return imageURL
    }

    fun getusername():String{
        return username
    }


    fun getStatus():String{
        return status
    }
    fun setId(id:String):Unit{
        this.id=id
    }


    fun setStatus(status:String):Unit{
        this.status=status
    }

    fun setimageURL(emailURL:String):Unit{
        this.imageURL=emailURL
    }

    fun setusername(username:String):Unit{
        this.username=username
    }

}