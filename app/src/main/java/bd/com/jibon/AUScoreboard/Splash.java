package bd.com.jibon.AUScoreboard;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.security.Permission;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    public String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    int PERMISSIONS_ALL = 1;
    public Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;

        if (hasPermission(activity, PERMISSIONS)){
            runMain();
        }else{
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSIONS_ALL);
            new CustomTools(activity).toast("Give permission to Vibrate, Location and Internet...", R.drawable.ic_baseline_warning_24);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Boolean inter = true;
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                Log.e("errnos", permission+" not granted");
                inter = false;
            }
        }
        if (inter){
            runMain();
        }else{
            new CustomTools(activity).toast("We need Vibrate, Location and Internet permission...", R.drawable.ic_baseline_warning_24);
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
    public static boolean hasPermission(Activity activity, String... PERMISSIONS){
        for (String permission : PERMISSIONS){
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new Settings(activity).exitApp(true);
    }
}