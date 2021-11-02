package bd.com.jibon.AUScoreboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomTools {
    public Activity activity;
    public CustomTools(Activity applicationContext) {
        this.activity = applicationContext;
    }
    public boolean toast(String text, int drawable){
        try {
            View view = activity.getLayoutInflater().inflate(R.layout.toast, activity.findViewById(R.id.Custom_toast), false);
            ((TextView) view.findViewById(R.id.Custom_toast_text)).setText(text);
            ((ImageView) view.findViewById(R.id.Custom_toast_icon)).setImageResource(drawable);
            Toast toast = new Toast(activity.getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
            new CustomTools(activity).vibrate(50);
            return true;
        }catch (Exception e){
            Log.e("errnos_ctool_a", "Custom Toast Problem: "+e.toString());
            return false;
        }
    }

    public boolean vibrate(int milliseconds){
        try{
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(milliseconds);
            vibrator.vibrate(Vibrator.VIBRATION_EFFECT_SUPPORT_YES);
            return true;
        }catch (Exception e){
            Log.e("errnos_ctool_b","Vibrate Problem: "+e.toString());
            return false;
        }
    }

    public void alert(String title, String messages, int icon){
        AlertDialog.Builder  builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(messages)
                .setIcon(icon)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        builder.create().show();
    }
}