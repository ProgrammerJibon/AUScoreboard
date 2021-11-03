package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.GetPlayerListInternet;
import bd.com.jibon.AUScoreboard.R;


public class PlayerList extends Fragment {
    Activity activity;
    LinearLayout progressBar;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView deletePlayer;


    public PlayerList() {
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

       View view = inflater.inflate(R.layout.fragment_player_list, container, false);
       progressBar = view.findViewById(R.id.progressBar);
        listView = view.findViewById(R.id.listViewPlayerList);
        deletePlayer = view.findViewById(R.id.deletePlayer);
        swipeRefreshLayout = view.findViewById(R.id.listViewRefreshPlayer);
          swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            new GetPlayerListInternet(activity, new Data(activity).urlGenerate("players=1"), progressBar, listView, deletePlayer).execute();
        });
        new GetPlayerListInternet(activity, new Data(activity).urlGenerate("players=1"), progressBar, listView, deletePlayer).execute();
        return view;
    }
}