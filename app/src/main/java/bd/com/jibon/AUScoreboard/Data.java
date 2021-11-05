package bd.com.jibon.AUScoreboard;

import android.app.Activity;

public class Data {
    public Activity activity;
    public String website = "https://newjson.000webhostapp.com/auscorecard";
    public Data(Activity activity) {
        this.activity = activity;
    }
    public String urlGenerate(String... getAction){
        return website+"/?"+getAction[0];
    }
}
