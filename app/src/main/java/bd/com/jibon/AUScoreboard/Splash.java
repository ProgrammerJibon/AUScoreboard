package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    public Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new CheckUserSigned(activity, new Data(activity).urlGenerate("")).execute();
            }
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        new Settings(activity).exitApp(true);
    }
}