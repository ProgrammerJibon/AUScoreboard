package bd.com.jibon.AUScoreboard.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bd.com.jibon.AUScoreboard.R;

public class MatchBallerAdapter extends BaseAdapter {
    ArrayList<JSONObject> jsonObjects = new ArrayList<>();
    Activity activity;
    public MatchBallerAdapter(Activity context, ArrayList<JSONObject> jsonObjects) {
        this.activity = context;
        this.jsonObjects = jsonObjects;
    }

    @Override
    public int getCount() {
        return jsonObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return jsonObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        try {
            return jsonObjects.get(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return Integer.parseInt("0."+position);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            JSONObject teamdatass = jsonObjects.get(position);
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.baller_status, parent, false);
            }
            TextView ballerName = convertView.findViewById(R.id.ballerName);
            TextView run = convertView.findViewById(R.id.run);
            TextView ball = convertView.findViewById(R.id.ball);
            TextView six = convertView.findViewById(R.id.six);
            TextView four = convertView.findViewById(R.id.four);
            TextView wicket = convertView.findViewById(R.id.wicket);
            TextView byes = convertView.findViewById(R.id.byes);
            TextView status = convertView.findViewById(R.id.status);
            JSONObject baller_data = teamdatass.getJSONArray("baller_data").getJSONObject(0);

            ballerName.setText(baller_data.getString("player_name"));
            run.setText(baller_data.getString("ball_run"));
            ball.setText(baller_data.getString("ball_ball"));
            six.setText(baller_data.getString("six"));
            four.setText(baller_data.getString("four"));
            byes.setText(baller_data.getString("byes"));
            wicket.setText(baller_data.getString("ball_wicket"));
            status.setText(baller_data.getString("status"));



        } catch (Exception e) {
            e.printStackTrace();
            Log.e("errnos_p_d", e.toString());
        }


        return convertView;
    }
}
