package com.heron.constructmanager;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

public class Utils {

    public static void showToastMsg(Context context, Task task, String action) {
        if(task.isSuccessful()){
            Toast.makeText(context, action + Constants.SUCCESS, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, Constants.FAILURE + action, Toast.LENGTH_LONG).show();
        }
    }

    public void uploadFile(){}
}