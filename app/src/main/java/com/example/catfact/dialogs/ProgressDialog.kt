package com.example.catfact.dialogs

import android.app.Dialog
import android.content.Context
import com.example.catfact.R

object ProgressDialog {

    private var dialog: Dialog? = null

    fun show(context: Context) {
        dialog = Dialog(context)

        dialog!!.let {
            it.setContentView(R.layout.dialog_progress_bar)
            it.show()
        }
    }

    fun hide() = dialog?.dismiss()
}