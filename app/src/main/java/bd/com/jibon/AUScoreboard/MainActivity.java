package bd.com.jibon.AUScoreboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bd.com.jibon.AUScoreboard.Fragments.AdminPage;
import bd.com.jibon.AUScoreboard.Fragments.MatcheList;
import bd.com.jibon.AUScoreboard.Fragments.MyAccount;
import bd.com.jibon.AUScoreboard.Fragments.PlayerList;
import bd.com.jibon.AUScoreboard.Fragments.ProfilePage;
import bd.com.jibon.AUScoreboard.Fragments.TeamList;


public class MainActivity extends AppCompatActivity {
    public Activity activity = this;
//    public View fragmentMainActivity;
    public ViewPager2 viewPager2;
    public TextView mainActivityTitle;
    public TabLayout tabLayoutMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            tabLayoutMainActivity = findViewById(R.id.MainActivityTabLayout);
//            fragmentMainActivity = findViewById(R.id.fragmentMainActivity);
            viewPager2 = findViewById(R.id.viewPager2);
            mainActivityTitle = findViewById(R.id.MainActivityTitle);
            tabLayoutMainActivity.addTab(tabLayoutMainActivity.newTab().setIcon(R.drawable.ic_baseline_sports_cricket_24));
            tabLayoutMainActivity.addTab(tabLayoutMainActivity.newTab().setIcon(R.drawable.ic_outline_people_24));
            tabLayoutMainActivity.addTab(tabLayoutMainActivity.newTab().setIcon(R.drawable.ic_outline_emoji_flags_24));
            tabLayoutMainActivity.addTab(tabLayoutMainActivity.newTab().setIcon(R.drawable.ic_baseline_account_circle_24));
            tabLayoutMainActivity.addTab(tabLayoutMainActivity.newTab().setIcon(R.drawable.ic_outline_admin_panel_settings_24));

            Intent intentList = getIntent();
            Bundle bundle = intentList.getExtras();
            String user_role = "";
            ArrayList<Fragment> fragments = new ArrayList<>();
            FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragments);

            if (bundle != null){
                user_role = bundle.getString("user_role");
            }

            String finalUser_role = user_role;

            fragments.add(new MatcheList());
            fragments.add(new PlayerList());
            fragments.add(new TeamList());
            if (finalUser_role.equals("ADMIN") || finalUser_role.equals("USER")) {
                fragments.add(new ProfilePage());
            } else {
                fragments.add(new MyAccount());
            }
            if (finalUser_role.equals("ADMIN")) {
                fragments.add(new AdminPage());
            }

            viewPager2.setAdapter(fragmentAdapter);
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabLayoutMainActivity.selectTab(tabLayoutMainActivity.getTabAt(position));
                }
            });
            tabLayoutMainActivity.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @SuppressLint("SetTextI18n")
                public void onTabSelected(TabLayout.Tab tab) {
                    try {
                        viewPager2.setCurrentItem(tab.getPosition(), false);
                    }catch (Exception e){
                        Log.e("errnos_fragment", e.toString());
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
        new Settings(this.activity).exitApp(true);
    }





    public static class FragmentAdapter extends FragmentStateAdapter{
        ArrayList<Fragment> fragments;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ArrayList<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
