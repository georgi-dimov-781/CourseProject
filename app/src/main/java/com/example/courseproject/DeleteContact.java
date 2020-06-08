package com.example.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteContact extends DialogFragment {
    public interface OnCompleteListener {
        void onCompleteDeleteDialog(Bundle callbackData);
    }

    public DeleteContact() {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final OnCompleteListener dialogCallbackListener = (OnCompleteListener) getActivity();

        final int contactId = getArguments().getInt("contactId");

        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
        deleteDialog.setTitle("Delete Contact");
        deleteDialog.setMessage("Are you sure?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle callbackData = new Bundle();
                callbackData.putString("response", "Yes");
                callbackData.putInt("contactId", contactId);
                dialogCallbackListener.onCompleteDeleteDialog(callbackData);
            }
        });
        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Bundle callbackData = new Bundle();
                callbackData.putString("response", "No");
                dialogCallbackListener.onCompleteDeleteDialog(callbackData);
            }
        });

        return deleteDialog.create();
    }
}
