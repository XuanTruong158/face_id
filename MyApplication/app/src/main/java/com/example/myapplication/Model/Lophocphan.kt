package com.example.myapplication.Model

data class Lophocphan(
    var id: String? = null,
    var courseId: String,
    var lecturerId: String,
    var name: String? = null,
    var semester: String? = null,
    var year: Int? = null,

    var  lecturer: User? = null,
    var  course: Monhoc? = null,
)