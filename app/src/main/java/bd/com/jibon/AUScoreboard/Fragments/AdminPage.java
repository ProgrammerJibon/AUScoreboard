package bd.com.jibon.AUScoreboard.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import bd.com.jibon.AUScoreboard.Data;
import bd.com.jibon.AUScoreboard.Internet.AddDataToInternetThenHide;
import bd.com.jibon.AUScoreboard.OptionSelectView;
import bd.com.jibon.AUScoreboard.R;


public class AdminPage extends Fragment {
    public Activity activity;
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
        try {

            // add player
            EditText new_player_name = view.findViewById(R.id.playerName);
            Spinner new_player_country = view.findViewById(R.id.countryList);
            LinearLayout show_add_player = view.findViewById(R.id.addPlayer);
            LinearLayout add_player_view = view.findViewById(R.id.addPlayerShow);
            Button close_add_player = view.findViewById(R.id.closeAddplayer);
            Button addPlayerSubmit = view.findViewById(R.id.addPlayerSubmit);
            LinearLayout progressBar = view.findViewById(R.id.progressBar);
            TextView showCountryId = view.findViewById(R.id.showCountryId);







            //add team
            OptionSelectView optionSelectView = new OptionSelectView(activity, new Data(activity).urlGenerate("teams=1"), progressBar, new_player_country, "teams");
            optionSelectView.execute();
            new_player_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    showCountryId.setText((optionSelectView.countryId).get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            close_add_player.setOnClickListener(v -> {
                add_player_view.setVisibility(View.GONE);
            });
            show_add_player.setOnClickListener(v -> {
                add_player_view.setVisibility(View.VISIBLE);
            });

            addPlayerSubmit.setOnClickListener(v -> {
                String url = new Data(activity).urlGenerate("add_player=1&name="+new_player_name.getText()+"&team_id="+showCountryId.getText());
                new AddDataToInternetThenHide(activity, url, progressBar).execute();
                new_player_name.setText("");
                add_player_view.setVisibility(View.GONE);
            });


            LinearLayout add_team_btn = view.findViewById(R.id.addTeams);
            LinearLayout add_team_view = view.findViewById(R.id.addTeamsView);
            EditText team_name = view.findViewById(R.id.team_name);
            Spinner team_type = view.findViewById(R.id.showTeamType);
            Button submit_team_name = view.findViewById(R.id.submitTeamName);
            Button closeAddTeamView = view.findViewById(R.id.closeAddteamView);
            add_team_view.setVisibility(View.GONE);

            closeAddTeamView.setOnClickListener(v->{
                add_team_view.setVisibility(View.GONE);
            });
            add_team_btn.setOnClickListener(v -> {
                add_team_view.setVisibility(View.VISIBLE);
            });
            submit_team_name.setOnClickListener(v->{
                String url = new Data(activity).urlGenerate("add_team=1&name="+team_name.getText()+"&type="+team_type.getSelectedItem());
                new AddDataToInternetThenHide(activity, url, progressBar).execute();
                team_name.setText("");
                add_team_view.setVisibility(View.GONE);
            });

            new OptionSelectView(activity, new Data(activity).urlGenerate("team_type=1"), progressBar, team_type, "team_type").execute();



            //new match
            LinearLayout addMatchView = view.findViewById(R.id.newMatchView);
            Spinner selectTeam1 = view.findViewById(R.id.selectTeam1), selectTeam2 = view.findViewById(R.id.selectTeam2);

            OptionSelectView team1Names = new OptionSelectView(activity, new Data(activity).urlGenerate("teams=1"), progressBar, selectTeam1, "teams");
            team1Names.execute();
            OptionSelectView team2Names = new OptionSelectView(activity, new Data(activity).urlGenerate("teams=1"), progressBar, selectTeam2, "teams");
            team2Names.execute();

            view.findViewById(R.id.newMatch).setOnClickListener(v->{
                addMatchView.setVisibility(View.VISIBLE);
            });




        }catch (Exception e){
            Log.e("errnos", e.toString());
        }

        return view;
    }
}