package com.example.myapplication.Model


//    var masv: String? = null;
//    // kieu int
//    var class_id:Int ? = null;
//    lateinit var name:String;
//    var monhoc_id:Int ? = null;
//    lateinit var email:String;
//    lateinit var password:String;
//    var status:String ? = null;
//    var  khoa:String ? = null;
//    lateinit var role:String;

    data class User(
        val id: String?,
        val name: String,
        val email: String,
        val password: String?,
        val code: String,
        val role: String

        //
    )

