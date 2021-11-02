package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.LoginForAccount;
import bd.com.jibon.AUScoreboard.Internet.RegisterForAccount;
import bd.com.jibon.AUScoreboard.OptionSelectView;
import bd.com.jibon.AUScoreboard.R;


public class MyAccount extends Fragment {
    Activity activity;
    EditText fname, lname, dd, mm, yyyy, reg_email, fpass, cpass, email, password;
    Spinner sex, country;
    Button register, toggle_login, login, toggle_signup;
    ScrollView signup_view, login_view;
    public MyAccount() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        signup_view = view.findViewById(R.id.signupView);
        login_view = view.findViewById(R.id.loginView);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        dd = view.findViewById(R.id.day);
        mm = view.findViewById(R.id.month);
        yyyy = view.findViewById(R.id.year);
        reg_email = view.findViewById(R.id.reg_email);
        fpass = view.findViewById(R.id.fpass);
        cpass = view.findViewById(R.id.cpass);
        sex = view.findViewById(R.id.sex);
        country = view.findViewById(R.id.country);
        register = view.findViewById(R.id.register);
        login = view.findViewById(R.id.login);
        toggle_login = view.findViewById(R.id.toggle_login);
        toggle_signup = view.findViewById(R.id.toggle_signup);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);



        register.setOnClickListener(v->{
            new RegisterForAccount(activity,fname, lname, dd, mm, yyyy, reg_email, country, fpass, cpass, sex, (LinearLayout) view.findViewById(R.id.progressBar)).execute();
        });

        login.setOnClickListener(v->{
            new LoginForAccount(activity, view.findViewById(R.id.progressBar), email, password).execute();
        });

        ArrayList<String> sexList = new ArrayList<>();
        sexList.add("Boy");sexList.add("Girl");sexList.add("Other");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, sexList);
        sex.setAdapter(adapter);

        new OptionSelectView(activity, new Data(activity).urlGenerate("countries=1"), view.findViewById(R.id.progressBar), country, "countries").execute();

        toggle_signup.setOnClickListener(v->{
            signup_view.setVisibility(View.VISIBLE);
            login_view.setVisibility(View.GONE);
        });

        toggle_login.setOnClickListener(v->{
            signup_view.setVisibility(View.GONE);
            login_view.setVisibility(View.VISIBLE);
        });


        return  view;
    }
}