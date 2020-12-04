package com.android.samples.donuttracker.firestore

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.android.samples.donuttracker.R

class SingInErrorDialogFragment: DialogFragment() {

    companion object {
        private const val MESSAGE_KEY = "message"
        fun getInstance(@StringRes message: Int): SingInErrorDialogFragment{
            val fragment = SingInErrorDialogFragment()
            val args = Bundle()
            args.putInt(MESSAGE_KEY, message)
            return fragment
        }
    }

    interface Listener{
        fun onRetry()
        fun onExit()
    }
    private var listener: Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireActivity())
                .setTitle(R.string.title_sign_in_error)
                .setMessage(arguments?.getInt(MESSAGE_KEY)!!)
                .setCancelable(false)
                .setPositiveButton(R.string.option_retry) { _, _ -> listener?.onRetry() }
                .setNegativeButton(R.string.option_exit) { _, _ -> listener?.onExit() }
                .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listener)
            listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}