package com.example.mylibrary.model

data class Book(
    var id:String, var name:String, var desciption:String, var categoryName:String, var rating: Any?, var authorName:String,
    var yearRelease: Any?, var isFavorite: Boolean, var image:String
)