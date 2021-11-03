package bd.com.jibon.AUScoreboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import bd.com.jibon.AUScoreboard.Fragments.AdminPage;
import bd.com.jibon.AUScoreboard.Fragments.MatcheList;
import bd.com.jibon.AUScoreboard.Fragments.MyAccount;
import bd.com.jibon.AUScoreboard.Fragments.PlayerList;
import bd.com.jibon.AUScoreboard.Fragments.ProfilePage;
import bd.com.jibon.AUScoreboard.Fragments.TeamList;


public class MainActivity extends AppCompatActivity {
    public Activity activity = this;
    public View fragmentMainActivity;
    public TextView mainActivityTitle;
    public TabLayout tabLayoutMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            tabLayoutMainActivity = findViewById(R.id.MainActivityTabLayout);
            fragmentMainActivity = findViewById(R.id.fragmentMainActivity);
            mainActivityTitle = findViewById(R.id.MainActivityTitle);

            Intent intentList = getIntent();
            Bundle bundle = intentList.getExtras();
            String user_role = "";
            if (bundle != null){
                user_role = bundle.getString("user_role");
            }
            if (!user_role.equals("ADMIN")){
                tabLayoutMainActivity.removeTabAt(4);
            }

            String finalUser_role = user_role;
            tabLayoutMainActivity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @SuppressLint("SetTextI18n")
                public void onTabSelected(TabLayout.Tab tab) {
                    Fragment fragment = null;
                    if (tab.getPosition() == 0) {
                        fragment = new MatcheList();
                        MainActivity.this.mainActivityTitle.setText("All Matches");
                    } else if (tab.getPosition() == 1) {
                        fragment = new PlayerList();
                        MainActivity.this.mainActivityTitle.setText("All Player");
                    } else if (tab.getPosition() == 2) {
                        fragment = new TeamList();
                        MainActivity.this.mainActivityTitle.setText("All Temas");
                    } else if (tab.getPosition() == 3) {
                        if (finalUser_role.equals("ADMIN") || finalUser_role.equals("USER")){
                            fragment = new ProfilePage();
                            MainActivity.this.mainActivityTitle.setText("Profile");
                        }else{
                            fragment = new MyAccount();
                            MainActivity.this.mainActivityTitle.setText("My Account");
                        }
                    } else if (tab.getPosition() == 4) {
                        fragment = new AdminPage();
                        MainActivity.this.mainActivityTitle.setText("Admin Area");
                    }
                    if (fragment != null) {
                        FragmentTransaction fragmentTransaction1 = MainActivity.this.getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        fragmentTransaction1.replace(R.id.fragmentMainActivity, fragment);
                        fragmentTransaction1.commit();
                    }
                }

                public void onTabUnselected(TabLayout.Tab tab) {
                }

                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        } catch (Exception error) {
            Log.e("errnos_main", error.toString());
        }
    }


    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }else{
            new Settings(this.activity).exitApp(true);
        }
    }
}
