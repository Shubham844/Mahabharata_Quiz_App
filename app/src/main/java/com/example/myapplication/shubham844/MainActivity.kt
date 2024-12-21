package com.example.myapplication.shubham844

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var btn1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //start quiz button
        btn1=findViewById(R.id.btn1)
        btn1.setOnClickListener {
            val intent= Intent(this,SecondActivity::class.java)
            startActivity(intent)

        }
    }
}