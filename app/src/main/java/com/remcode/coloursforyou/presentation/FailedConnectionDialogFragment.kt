package com.remcode.coloursforyou.presentation

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.remcode.coloursforyou.R

/**
 * DialogFragment to inform the user of connectivity issues
 */
class FailedConnectionDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.possible_causes))
                .setPositiveButton(getString(R.string.ok)) { dialog, id -> dialog.dismiss() }
                .setTitle(getString(R.string.unable_to_connect))
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}