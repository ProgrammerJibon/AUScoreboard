package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.GetTeamListInternet;
import bd.com.jibon.AUScoreboard.R;


public class TeamList extends Fragment {
    Activity activity;
    ListView listView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    public TeamList() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_team_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.listViewRefresh);
        listView = view.findViewById(R.id.listViewTeamList);
        progressBar = view.findViewById(R.id.progressBarTeams);
        new GetTeamListInternet(activity, new Data(activity).urlGenerate("teams=1"), progressBar, listView).execute();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            new GetTeamListInternet(activity, new Data(activity).urlGenerate("teams=1"), progressBar, listView).execute();
        });
        return view;
    }
}