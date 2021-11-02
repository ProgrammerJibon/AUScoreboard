package bd.com.jibon.AUScoreboard;

/*import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionSelectViewAdapter extends BaseAdapter {
    public ArrayList<String>  NAMES;
    public ArrayList<String>  ID_LIST;
    public Activity activity;
    public OptionSelectViewAdapter(Activity activity, ArrayList<String> ID_LIST, ArrayList<String> NAMES) {
        this.ID_LIST = ID_LIST;
        this.NAMES = NAMES;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return NAMES.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.sample_match_list, parent, false);
            }
            TextView StringOfListItem = convertView.findViewById(R.id.StringOfListItem);
            TextView IdOfListItem = convertView.findViewById(R.id.IdOfListItem);

            IdOfListItem.setText(ID_LIST.get(position));
            StringOfListItem.setText(NAMES.get(position));
            return convertView;
        }catch (Exception e){
            Log.e("errnos_optionadapter", e.toString());
            return null;
        }
    }
}*/
