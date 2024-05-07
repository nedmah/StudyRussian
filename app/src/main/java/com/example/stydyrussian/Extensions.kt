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


//открываем фрагмент с бэкстеком из фрагмента
fun Fragment.openFragmentWithBackStack(fragment: Fragment){
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .replace(R.id.frag_container,fragment)
        .addToBackStack(null)
        .commit()
}


fun Fragment.openActivity(activity: AppCompatActivity){
    val intent = Intent(getActivity(),activity::class.java)
    startActivity(intent)
}

fun AppCompatActivity.openActivity(activity: AppCompatActivity){
    val intent = Intent(this,activity::class.java)
    startActivity(intent)
    finish()
}