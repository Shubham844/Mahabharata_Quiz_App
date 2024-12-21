package com.example.myapplication.shubham844

data class QuestionData (
    var question:String,
//    var id:Int,
//    var option_one:String,
//    var option_two:String,
//    var option_three:String,
//    var option_four:String,
//    var ans:Int,
    val options: List<String>,
    val answer: Int

        ) {

}
