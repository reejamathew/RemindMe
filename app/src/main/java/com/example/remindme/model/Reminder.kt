package com.example.remindme.model

data class Reminder(
    var title: String = "",
    var description: String = "",
    var dateTime: String = "",
    var img_location: String = "",
    var email: String = ""
){}
