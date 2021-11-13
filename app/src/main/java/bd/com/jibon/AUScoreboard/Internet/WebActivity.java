package bd.com.jibon.AUScoreboard.Internet;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import bd.com.jibon.AUScoreboard.R;

public class WebActivity extends AppCompatActivity {
    public TextView titleView;
    public ImageView imageView;
    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        titleView = findViewById(R.id.titleHere);
        imageView = findViewById(R.id.closeWebView);
        webView = findViewById(R.id.webView);

        imageView.setOnClickListener(v->{
            this.finish();
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            String link = bundle.getString("link");
            webView.loadUrl(link);

        }else{
            this.finish();
        }
    }
}