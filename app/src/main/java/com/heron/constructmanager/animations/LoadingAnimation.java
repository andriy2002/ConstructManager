package com.heron.constructmanager.animations;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.heron.constructmanager.R;

public class LoadingAnimation {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingAnimation(Activity a) {
        activity = a;
    }

    public void loadingAnimationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissLoading() {
        dialog.dismiss();
    }
}

