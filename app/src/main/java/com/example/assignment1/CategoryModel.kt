package com.example.assignment1

data class CategoryModel(
    val id:String,
    val image:Int,
    val name:String,


)

data class CategoryResponse(
    val triviaCategories: List<CategoryModel>
)
