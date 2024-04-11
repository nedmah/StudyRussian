package com.example.stydyrussian

import android.content.Intent
import android.text.TextUtils.replace
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


//открываем фрагмент из активити
fun AppCompatActivity.openFragment(fragment: Fragment){
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.frag_container,fragment)
        .commit()
}


//открываем фрагмент из фрагмента
fun Fragment.openFragment(fragment: Fragment){
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .replace(R.id.frag_container,fragment)
        .commit()
}

fun AppCompatActivity.openActivity(activity: AppCompatActivity){
    val intent = Intent(this,activity::class.java)
    startActivity(intent)
    finish()
}