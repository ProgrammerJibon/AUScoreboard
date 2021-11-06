package bd.com.jibon.AUScoreboard;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    public String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.VIBRATE
    };
    int PERMISSIONS_ALL = 125;
    public Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        if (hasPermission(activity)){
            runMain();
        }else{
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_ALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean results = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                results = false;
                break;
            }
        }
        if (results){
            runMain();
        }else{
            new CustomTools(activity).toast("We need Vibrate and Internet permission...", R.drawable.ic_baseline_warning_24);
            finish();
        }
    }

    private void runMain(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new CheckUserSigned(activity, new Data(activity).urlGenerate("")).execute();
            }
        }, 1500);
    }
    public boolean hasPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null){
            for (String permission : PERMISSIONS){
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new Settings(activity).exitApp(true);
    }
}