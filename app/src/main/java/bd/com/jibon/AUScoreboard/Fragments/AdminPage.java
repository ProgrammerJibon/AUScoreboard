package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import bd.com.jibon.AUScoreboard.MainActivity;
import bd.com.jibon.AUScoreboard.R;


public class AdminPage extends Fragment {
    public Activity activity;
    EditText editText;
    ImageButton imageButton;
    public AdminPage() {
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
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        editText = view.findViewById(R.id.profile_id);
        view.findViewById(R.id.profile_go).setOnClickListener(v->{
            String text = editText.getText().toString();
            ProfilePage fragment = new ProfilePage();
            fragment.profile_id = text;
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentMainActivity, fragment)
                    .commit();
        });
        return view;
    }
}