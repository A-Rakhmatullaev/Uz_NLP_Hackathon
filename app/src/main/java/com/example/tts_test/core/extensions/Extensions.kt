package com.example.tts_test.core.extensions

import android.app.Activity
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun updateText(textView: TextView, message: Any) {
    textView.text = message.toString()
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.showViews(vararg views: View){
    for(view in views) view.show()
}

fun View.hideViews(vararg views: View){
    for(view in views) view.hide()
}

fun View.isVisible(): Boolean{
    return visibility == View.VISIBLE
}

fun View.enable(){
    isEnabled = true
    isClickable = true
}

fun View.disable(){
    isEnabled = false
    isClickable = false
}

fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())

fun String.fromBase64(): String = String(Base64.getDecoder().decode(this))

fun time(): Long = System.currentTimeMillis()

fun log(tag: String, message: Any){
    Log.d(tag, "Log: $message")
}

fun Activity.toast(message: Any){
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
}

fun Activity.snackBar(message: Any){
    Snackbar.make(this, findViewById(android.R.id.content), message.toString(), Snackbar.LENGTH_SHORT).show()
}