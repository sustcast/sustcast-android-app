package com.chameleon.sustcast.chatHandler;

/**
 * Created by Yashasvi on 16-04-2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteMessage extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder theDialog= new AlertDialog.Builder(getActivity());
        theDialog.setTitle("message deleted");
        theDialog.setPositiveButton("Yeah", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ChatActvity)getActivity()).delMessageBool();
                    }
                });
        theDialog.setNegativeButton("Nope", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().cancel();
                    }
                });
        return theDialog.create();
    }
}
