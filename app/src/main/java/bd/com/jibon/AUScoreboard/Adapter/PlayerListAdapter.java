package bd.com.jibon.AUScoreboard.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.Internet.DeleteTargetedWithId;
import bd.com.jibon.AUScoreboard.R;
import bd.com.jibon.AUScoreboard.RestoreWithId;

public class PlayerListAdapter extends BaseAdapter {
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
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.sample_player_list, parent, false);

            }
            JSONObject jsonObject = arrayList.get(position);

            TextView name = convertView.findViewById(R.id.name);
            TextView team = convertView.findViewById(R.id.team);
            TextView id = convertView.findViewById(R.id.id);
            TextView deletePlayer = convertView.findViewById(R.id.deletePlayer);
            String xxId = jsonObject.getString("id");
            String xxname = jsonObject.getString("name");

            if (user_role.equals("ADMIN")){

                if (jsonObject.getString("status").equals("DELETED")){
                    deletePlayer.setText("Restore");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        deletePlayer.setTextColor(activity.getColor(R.color.teaser_blue));
                    }
                }else{
                    deletePlayer.setText("Delete");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        deletePlayer.setTextColor(activity.getColor(R.color.red));
                    }
                }

                if (deletePlayer.getText().equals("Restore")){
                    deletePlayer.setText("Restore");
                    deletePlayer.setOnClickListener(v -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Restore")
                                .setCancelable(true)
                                .setMessage("Restore " + xxname)
                                .setIcon(R.drawable.ic_baseline_admin_panel_settings_24)
                                .setPositiveButton("RESTORE", (dialog, which) -> {
                                    new RestoreWithId(activity, "PLAYER", xxId).execute();
                                    deletePlayer.setText("Delete");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        deletePlayer.setTextColor(activity.getColor(R.color.red));
                                    }
                                })
                                .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()))
                                .show();
                    });
                }else{
                    deletePlayer.setText("Delete");
                    deletePlayer.setOnClickListener(v -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Delete")
                                .setCancelable(true)
                                .setMessage("Delete " + xxname)
                                .setIcon(R.drawable.ic_baseline_admin_panel_settings_24)
                                .setPositiveButton("DELETE", (dialog, which) -> {
                                    new DeleteTargetedWithId(activity, "PLAYER", xxId).execute();
                                    deletePlayer.setText("Restore");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        deletePlayer.setTextColor(activity.getColor(R.color.teaser_blue));
                                    }
                                })
                                .setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()))
                                .show();
                    });
                }

            }else{
                deletePlayer.setVisibility(View.GONE);
            }

            name.setText(jsonObject.getString("name"));
            id.setText(jsonObject.getString("id"));
            team.setText(jsonObject.getString("team_name"));

        }catch (Exception error){
            Log.e("errnos_teamada", error.toString());
        }
        return convertView;
    }
}
