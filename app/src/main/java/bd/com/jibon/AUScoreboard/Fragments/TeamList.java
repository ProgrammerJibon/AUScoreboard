package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.CustomTools;
import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.DeleteTargetedWithId;
import bd.com.jibon.AUScoreboard.Internet.GetTeamListInternet;
import bd.com.jibon.AUScoreboard.R;


public class TeamList extends Fragment {
    Activity activity;
    ListView listView;
    LinearLayout progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView actionBar;
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
        progressBar = view.findViewById(R.id.progressBar);
        actionBar = view.findViewById(R.id.deleteTeam);
        new GetTeamListInternet(activity, new Data(activity).urlGenerate("teams=1"), progressBar, listView, actionBar).execute();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            new GetTeamListInternet(activity, new Data(activity).urlGenerate("teams=1"), progressBar, listView, actionBar).execute();
        });
        return view;
    }

    public static class TeamListAdapter extends BaseAdapter {
        public ArrayList<JSONObject> arrayList;
        public Activity activity;
        public String user_role;


        public TeamListAdapter(ArrayList<JSONObject> arrayList, Activity activity, String user_role) {
            this.arrayList = arrayList;
            this.activity = activity;
            this.user_role = user_role;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            try {
                return (long) arrayList.get(position).get("id");
            } catch (Exception e) {
                e.printStackTrace();
                return Long.parseLong(("0."+position));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.sample_team_list, parent, false);

                }
                JSONObject jsonObject = arrayList.get(position);

                TextView name = convertView.findViewById(R.id.name);
                TextView type = convertView.findViewById(R.id.type);
                TextView id = convertView.findViewById(R.id.id);

                TextView deletePlayer = convertView.findViewById(R.id.deleteTeam);
                String xxId = jsonObject.getString("id");
                String xxname = jsonObject.getString("name");

                convertView.setOnClickListener(v -> {
                    try {
                        new CustomTools(activity).toast(jsonObject.getString("name"), R.drawable.ic_outline_people_24);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                if (user_role.equals("ADMIN")){

                    if (!jsonObject.getString("status").equals("DELETED")) {
                        deletePlayer.setText("Delete");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            deletePlayer.setTextColor(activity.getColor(R.color.red));
                        }
                        View finalConvertView = convertView;
                        deletePlayer.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Delete");
                            builder.setCancelable(true);
                            builder.setMessage("Delete " + xxname);
                            builder.setIcon(R.drawable.ic_baseline_admin_panel_settings_24);
                            builder.setPositiveButton("DELETE", (dialog, which) -> {
                                new DeleteTargetedWithId(activity, "TEAM", xxId).execute();
                                finalConvertView.setAlpha(0.5F);
                                deletePlayer.setText("");
                            });
                            builder.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
                            builder.show();
                        });
                    }

                }else{
                    deletePlayer.setVisibility(View.GONE);
                }


                name.setText(jsonObject.getString("name"));
                type.setText(jsonObject.getString("type"));
                id.setText(jsonObject.getString("id"));

            }catch (Exception error){
                Log.e("errnos_teamada", error.toString());
            }
            return convertView;
        }
    }
}