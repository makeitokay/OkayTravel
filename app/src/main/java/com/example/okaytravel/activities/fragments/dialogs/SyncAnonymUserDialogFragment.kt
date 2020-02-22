package com.example.okaytravel.activities.fragments.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.okaytravel.R

class SyncAnonymUserDialogFragment: DialogFragment() {

    interface ChooseListener {
        fun syncAnonymUserButtonClicked()
        fun cancelSyncAnonymUserButtonClicked()
    }

    private lateinit var listener: ChooseListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = activity as ChooseListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setMessage(R.string.syncAnonymUserAsk)
            .setPositiveButton(R.string.yesButton) { _, _ -> listener.syncAnonymUserButtonClicked() }
            .setNegativeButton(R.string.noButton) { _, _ -> listener.cancelSyncAnonymUserButtonClicked() }
            .create()
    }
}