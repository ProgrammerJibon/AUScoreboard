package bd.com.jibon.AUScoreboard.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import bd.com.jibon.AUScoreboard.Internet.GetPlayerListInternet;
import bd.com.jibon.AUScoreboard.Internet.OpenImageFromLink;
import bd.com.jibon.AUScoreboard.Internet.WebActivity;
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

    public static class PlayerListAdapter extends BaseAdapter {
        public ArrayList<JSONObject> arrayList;
        public Activity activity;
        public String user_role;

        public PlayerListAdapter(ArrayList<JSONObject> arrayList, Activity activity, String user_role) {
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                PlayersViewHolder playersViewHolder ;
                if (convertView == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = layoutInflater.inflate(R.layout.sample_player_list, parent, false);
                    playersViewHolder = new PlayersViewHolder();
                    playersViewHolder.name = convertView.findViewById(R.id.name);
                    playersViewHolder.team = convertView.findViewById(R.id.team);
                    playersViewHolder.deletePlayer = convertView.findViewById(R.id.deletePlayer);
                    convertView.setTag(playersViewHolder);
                }else{
                    playersViewHolder = (PlayersViewHolder) convertView.getTag();
                }
                JSONObject jsonObject = arrayList.get(position);

                TextView name = playersViewHolder.name;
                TextView team = playersViewHolder.team;
                TextView deletePlayer = playersViewHolder.deletePlayer;

                ImageView imageView = convertView.findViewById(R.id.pics);
                String xxId = jsonObject.getString("id");
                String picLink = new Data(activity).urlGenerateGeneral(jsonObject.getString("pic"));
                String xxname = jsonObject.getString("name");

                convertView.setOnClickListener(v -> {
                    try {
                        new CustomTools(activity).toast(jsonObject.getString("name"), R.drawable.ic_outline_people_24);
                        if(user_role == "ADMIN"){
                            Intent intent = new Intent(activity, WebActivity.class);
                            intent.putExtra("link", new Data(activity).urlGenerateGeneral("/players.php?id="+xxId));
                            activity.startActivity(intent);
                        }
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
                            builder.setTitle("Delete")
                                    .setCancelable(true)
                                    .setMessage("Delete " + xxname)
                                    .setIcon(R.drawable.ic_baseline_admin_panel_settings_24)
                                    .setPositiveButton("DELETE", (dialog, which) -> {
                                        new DeleteTargetedWithId(activity, "PLAYER", xxId).execute();
                                        finalConvertView.setAlpha(0.5F);
                                        deletePlayer.setText("");
                                    })
                                    .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()))
                                    .show();
                        });
                    }


                }else{
                    deletePlayer.setVisibility(View.GONE);
                }

                name.setText(jsonObject.getString("name"));
                team.setText(jsonObject.getString("team_name"));
                new OpenImageFromLink(picLink, imageView).execute();

            }catch (Exception error){
                Log.e("errnos_teamada", error.toString());
            }
            return convertView;
        }
    }
    public static class PlayersViewHolder{
        public TextView name = null;
        public TextView team = null;
        public TextView deletePlayer = null;
    }
}