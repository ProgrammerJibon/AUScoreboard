package bd.com.jibon.AUScoreboard.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.R;

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

            name.setText(jsonObject.getString("name"));
            id.setText(jsonObject.getString("id"));
            team.setText(jsonObject.getString("team_name"));

        }catch (Exception error){
            Log.e("errnos_teamada", error.toString());
        }
        return convertView;
    }
}
