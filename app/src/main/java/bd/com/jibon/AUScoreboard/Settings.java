package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;

public class Settings  {
    public SharedPreferences preferences;
    public SharedPreferences.Editor preferencesEditor;
    public Activity activity;
    public Settings(Activity applicationContext) {
        this.activity = applicationContext;
        this.preferences = activity.getApplicationContext().getSharedPreferences("USER", Context.MODE_PRIVATE);
    }

    public void exitApp(Boolean... showAlertBox){
        if (showAlertBox[0] == null || showAlertBox[0]){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Are you sure to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Exit", (dialog, which) -> {
                        Process.killProcess(Process.myPid());
                        activity.finish();
                        System.exit(0);
                    })
                    .setNegativeButton("Later", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setIcon(R.drawable.main_logo);
            builder.create().show();
        }else{
            Process.killProcess(Process.myPid());
            activity.finish();
            System.exit(0);
        }

    }



}
