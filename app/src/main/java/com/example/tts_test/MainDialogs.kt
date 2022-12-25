package com.example.tts_test

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog

open class BaseAlertDialogBuilder(context: Context,
                                  title: String,
                                  message: String,
                                  isCancelable: Boolean,
                                  @StyleRes
                                  style: Int) {

    private val builder: AlertDialog.Builder = AlertDialog.Builder(context, style)
    init {
        builder.setTitle(title)
        if(message.isNotBlank()) builder.setMessage(message)
        builder.setCancelable(isCancelable)
    }

    fun builder(): AlertDialog.Builder = builder
}

class MainDialogBuilder(
    private val context: Context,
    title: String,
    private val message: String,
    isCancelable: Boolean,
    @StyleRes
    style: Int): BaseAlertDialogBuilder(context, title, message, isCancelable, style) {

    @SuppressLint("ResourceAsColor")
    fun textDialogBuilder(): AlertDialog.Builder {
        /**
         * Attach EditText
         */
        val textView = TextView(context)
        val linearLayout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        textView.layoutParams = linearLayout
        textView.setPadding(4, 4, 4, 4)
        textView.text = message
        textView.setTextColor(R.color.black)

        super.builder().setView(textView)
        super.builder().setPositiveButton(R.string.got_it) { dialog, which ->
            //hide()
            dialog.cancel()
        }
        return super.builder()
    }
}

class MainAlertDialog {

    private var alertDialog: AlertDialog? = null

    fun showTextDialog(
        context: Context,
        title: String,
        message: String,
        isCancelable: Boolean,
        @StyleRes
        style: Int,
    ) {
        alertDialog = MainDialogBuilder(context, title, message, isCancelable, style)
            .textDialogBuilder().create()
        alertDialog?.show() ?: return
        //TODO Delete below
        //alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(R.color.black)
    }
}