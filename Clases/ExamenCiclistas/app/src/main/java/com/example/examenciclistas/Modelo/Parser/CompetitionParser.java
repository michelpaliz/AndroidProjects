package com.example.examenciclistas.Modelo.Parser;

import android.content.Context;
import android.util.Log;

import com.example.examenciclistas.Modelo.Competition;
import com.example.examenciclistas.Modelo.Cyclist;
import com.example.examenciclistas.Modelo.Result;
import com.example.examenciclistas.Modelo.Stage;
import com.example.examenciclistas.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompetitionParser {
    private Competition competition;
    private final InputStream competitionFile;

    public CompetitionParser(Context c) {
        this.competitionFile = c.getResources().openRawResource(R.raw.lavuelta);
    }

    public boolean parse() {
        boolean parsed = false;
        String json = null;
        competition = null;
        try {
            int size = competitionFile.available();
            byte[] buffer = new byte[size];
            competitionFile.read(buffer);
            competitionFile.close();
            json = new String(buffer, "UTF-8");
            JSONTokener tokener = new JSONTokener(json);
            JSONObject jsonGlobal = new JSONObject(tokener);
            JSONObject jsonCompetition = jsonGlobal.getJSONObject("competition");
            String competitionName = jsonCompetition.getString("name");
            JSONArray jsonArrayCyclists = jsonCompetition.getJSONArray("cyclists");
            Cyclist[] cyclists = new Cyclist[jsonArrayCyclists.length()];
            for (int i = 0; i < jsonArrayCyclists.length(); i++) {
                JSONObject jsonCyclist = jsonArrayCyclists.getJSONObject(i);
                int cyclistId = jsonCyclist.getInt("cyclistId");
                String name = jsonCyclist.getString("name");
                String surname = jsonCyclist.getString("surname");
                String team = jsonCyclist.getString("team");
                cyclists[i] = new Cyclist(cyclistId, name, surname, team);
            }

            JSONArray jsonArrayStages = jsonCompetition.getJSONArray("stages");
            Stage[] stages = new Stage[jsonArrayStages.length()];
            for (int i = 0; i < jsonArrayStages.length(); i++) {
                JSONObject jsonStage = jsonArrayStages.getJSONObject(i);
                String startCity = jsonStage.getString("startCity");
                String finishCity = jsonStage.getString("finishCity");
                double distance = jsonStage.getDouble("distance");
                JSONArray jsonArrayResults = jsonStage.getJSONArray("results");

                Result[] results = new Result[jsonArrayResults.length()];
//                List<Result> resultList = new ArrayList<>(jsonArrayResults.length());
                for (int j = 0; j < jsonArrayResults.length(); j++) {
                    JSONObject jsonResult = jsonArrayResults.getJSONObject(j);
                    int cyclistId = jsonResult.getInt("cyclistId");
                    int hours = jsonResult.getInt("hours");
                    int minutes = jsonResult.getInt("minutes");
                    int seconds = jsonResult.getInt("seconds");
//                    resultList.add(new Result(cyclistId, hours, minutes, seconds));
                    results[j] = new Result(cyclistId, hours, minutes, seconds);
                }
                stages[i] = new Stage(startCity, finishCity, distance, Arrays.asList(results));
            }
            competition = new Competition(competitionName, cyclists, stages);


            parsed = true;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Cannot parse lavuelta.json. " + e.getMessage());
        }
        return parsed;
    }

    public Competition getCompetition() {
        return this.competition;
    }
}
