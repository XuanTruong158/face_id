package com.example.face_id.teacher.model

data class ClassItem(
    val id: String,            // _id của class_models
    val courseName: String,    // "Android" -> bind tvTitle
    val classCode: String,     // "CSE123_02" -> bind tvCode
    val desc: String? = null   // mô tả tùy chọn -> bind tvDesc (nếu null thì ẩn)
)
