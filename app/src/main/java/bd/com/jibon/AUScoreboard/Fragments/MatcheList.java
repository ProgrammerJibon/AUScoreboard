package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.GetMatchListAndSetToMainView;
import bd.com.jibon.AUScoreboard.R;

public class MatcheList extends Fragment {
    public ListView listView;
    public Activity activity;
    public LinearLayout progressBar;
    public SwipeRefreshLayout swipeRefreshLayout;
    public MatcheList() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Activity context) {
        this.activity = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        try {

            listView = view.findViewById(R.id.ListViewMatchList);
            progressBar = view.findViewById(R.id.progressBar);
            swipeRefreshLayout = view.findViewById(R.id.swiprefresh_match_list);


            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(false);
                new GetMatchListAndSetToMainView(activity, new Data(activity).urlGenerate("matches=1"), progressBar, listView).execute();
            });

            new GetMatchListAndSetToMainView(activity, new Data(activity).urlGenerate("matches=1"), progressBar, listView).execute();
        }catch (Exception error){
            Log.e("errnos_ma.java", error.toString());
        }

        return view;
    }

}