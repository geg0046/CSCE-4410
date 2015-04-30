package comgeg0046.github.testapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by CyberPowerPC on 4/28/2015.
 */
public class CurrentGameFragment extends Fragment {

    private ArrayAdapter<String> mSummonersAdapter;
    private String summonerId;
    private String[] summonerNames = new String[10];
    private String[] summonerStr = null;

    public CurrentGameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.currentgamefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if(id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String str = intent.getStringExtra(Intent.EXTRA_TEXT);
            summonerStr = str.split(" "); //will need to change how this works for summoner names like Annie Bot
        }

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mSummonersAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_game, // The name of the layout ID.
                        R.id.list_game_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.currentgamefragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_current_game);
        listView.setAdapter(mSummonersAdapter);

        //Uncomment this section if we want on-click functionality.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            //String test = mSummonersAdapter.getItem(position);
            String summoner = summonerNames[position];
            Intent intent = new Intent(getActivity(), SummonerDetailsActivity.class)
                    .putExtra(Intent.EXTRA_TEXT, summoner);
            startActivity(intent);
        }
        });

        return rootView;
    }

    private void updateSummoner() {
        FetchSummonerId SummonerTask = new FetchSummonerId();
        SummonerTask.execute();
    }

    private void updateGame() {
        FetchCurrentGame GameTask = new FetchCurrentGame();
        GameTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateSummoner();
        updateGame();
    }

    public class FetchSummonerId extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchCurrentGame.class.getSimpleName();

        private void getSummonerIdFromJson(String SummonerJsonStr) throws JSONException{



            summonerNames[0] = summonerStr[0].toLowerCase();

            //strings to find summoner id
            final String SUM = summonerStr[0].toLowerCase();
            final String SUMID = "id";

            JSONObject SummonerData = new JSONObject(SummonerJsonStr);
            JSONObject SummonerInfo = SummonerData.getJSONObject(SUM);

            String sumId = SummonerInfo.getString(SUMID);

            summonerId = sumId;

            Log.v(LOG_TAG, "Summoner Entry: " + summonerId);
        }

        @Override
        protected String doInBackground(String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String SummonerJsonStr = null;

            try {
                URL url = new URL("https://" + summonerStr[1].toLowerCase() + ".api.pvp.net/api/lol/" + summonerStr[1].toLowerCase() + "/v1.4/summoner/by-name/" + summonerStr[0] + "?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");

                Log.v(LOG_TAG, "Built URL: " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                SummonerJsonStr = buffer.toString();

                Log.v(LOG_TAG, "SummonerJsonStr: " + SummonerJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOG_TAG", "Error closing stream", e);
                    }
                }
            }
            try {
                getSummonerIdFromJson(SummonerJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing the summoner or game
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if (result != null){
                mSummonersAdapter.clear();
                mSummonersAdapter.add(result);
                //New data is back from the server. Hooray!
            }
        }
    }

    public class FetchCurrentGame extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchCurrentGame.class.getSimpleName();

        private String[] getCurrentGameDataFromJson(String CurrentGameJsonStr, int numSummoners) throws JSONException{

            //strings to find other players and game stats
            final String SUMMONERS = "participants";
            final String TIME = "gameLength";
            final String TEAM = "teamId";
            final String CHAMPION = "championId";
            final String SUMMONERNAME = "summonerName";

            JSONObject GameData = new JSONObject(CurrentGameJsonStr);
            JSONArray Summoners = GameData.getJSONArray(SUMMONERS);

            String resultStrs[] = new String[numSummoners];
            for (int i = 0; i < Summoners.length(); i++) {
                String name;
                String championName = null;
                String team = null;
                int teamId;
                int championId;

                //JSON object representing each summoner
                JSONObject SummonerGameInfo = Summoners.getJSONObject(i);

                name = SummonerGameInfo.getString(SUMMONERNAME);

                teamId = SummonerGameInfo.getInt(TEAM);
                championId = SummonerGameInfo.getInt(CHAMPION);

                if (championId == 1) championName = "Annie";
                else if (championId == 2) championName = "Olaf";
                else if (championId == 3) championName = "Galio";
                else if (championId == 4) championName = "Twisted Fate";
                else if (championId == 5) championName = "Xin Zhao";
                else if (championId == 6) championName = "Urgot";
                else if (championId == 7) championName = "LeBlanc";
                else if (championId == 8) championName = "Vladimir";
                else if (championId == 9) championName = "Fiddlesticks";
                else if (championId == 10) championName = "Kayle";
                else if (championId == 11) championName = "Master Yi";
                else if (championId == 12) championName = "Alistar";
                else if (championId == 13) championName = "Ryze";
                else if (championId == 14) championName = "Sion";
                else if (championId == 15) championName = "Sivir";
                else if (championId == 16) championName = "Soraka";
                else if (championId == 17) championName = "Teemo";
                else if (championId == 18) championName = "Tristana";
                else if (championId == 19) championName = "Wawrwick";
                else if (championId == 20) championName = "Nunu";
                else if (championId == 21) championName = "Miss Fortune";
                else if (championId == 22) championName = "Ashe";
                else if (championId == 23) championName = "Tryndamere";
                else if (championId == 24) championName = "Jax";
                else if (championId == 25) championName = "Morgana";
                else if (championId == 26) championName = "Zilean";
                else if (championId == 27) championName = "Singed";
                else if (championId == 28) championName = "Evelynn";
                else if (championId == 29) championName = "Twitch";
                else if (championId == 30) championName = "Karthus";
                else if (championId == 31) championName = "Cho'Gath";
                else if (championId == 32) championName = "Amumu";
                else if (championId == 33) championName = "Rammus";
                else if (championId == 34) championName = "Anivia";
                else if (championId == 35) championName = "Shaco";
                else if (championId == 36) championName = "Dr. Mundo";
                else if (championId == 37) championName = "Sona";
                else if (championId == 38) championName = "Kassadin";
                else if (championId == 39) championName = "Irelia";
                else if (championId == 40) championName = "Janna";
                else if (championId == 41) championName = "Gangplank";
                else if (championId == 42) championName = "Corki";
                else if (championId == 43) championName = "Karma";
                else if (championId == 44) championName = "Taric";
                else if (championId == 45) championName = "Veigar";
                else if (championId == 48) championName = "Trundle";
                else if (championId == 50) championName = "Swain";
                else if (championId == 51) championName = "Caitlyn";
                else if (championId == 53) championName = "Blitzcrank";
                else if (championId == 54) championName = "Malphite";
                else if (championId == 55) championName = "Katarina";
                else if (championId == 56) championName = "Nocturne";
                else if (championId == 57) championName = "Maokai";
                else if (championId == 58) championName = "Renekton";
                else if (championId == 59) championName = "Jarvan IV";
                else if (championId == 60) championName = "Elise";
                else if (championId == 61) championName = "Orianna";
                else if (championId == 62) championName = "Wukong";
                else if (championId == 63) championName = "Brand";
                else if (championId == 64) championName = "Lee Sin";
                else if (championId == 67) championName = "Vayne";
                else if (championId == 68) championName = "Rumble";
                else if (championId == 69) championName = "Cassiopeia";
                else if (championId == 72) championName = "Skarner";
                else if (championId == 74) championName = "Heimerdinger";
                else if (championId == 75) championName = "Nasus";
                else if (championId == 76) championName = "Nidalee";
                else if (championId == 77) championName = "Udyr";
                else if (championId == 78) championName = "Poppy";
                else if (championId == 79) championName = "Gragas";
                else if (championId == 80) championName = "Pantheon";
                else if (championId == 81) championName = "Ezreal";
                else if (championId == 82) championName = "Mordekaiser";
                else if (championId == 83) championName = "Yorick";
                else if (championId == 84) championName = "Akali";
                else if (championId == 85) championName = "Kennen";
                else if (championId == 86) championName = "Garen";
                else if (championId == 89) championName = "Leona";
                else if (championId == 90) championName = "Malzahar";
                else if (championId == 91) championName = "Talon";
                else if (championId == 92) championName = "Riven";
                else if (championId == 96) championName = "Kog'Maw";
                else if (championId == 98) championName = "Shen";
                else if (championId == 99) championName = "Lux";
                else if (championId == 101) championName = "Xerath";
                else if (championId == 102) championName = "Shyvana";
                else if (championId == 103) championName = "Ahri";
                else if (championId == 104) championName = "Graves";
                else if (championId == 105) championName = "Fizz";
                else if (championId == 106) championName = "Volibear";
                else if (championId == 107) championName = "Rengar";
                else if (championId == 110) championName = "Varus";
                else if (championId == 111) championName = "Nautilus";
                else if (championId == 112) championName = "Viktor";
                else if (championId == 113) championName = "Sejuani";
                else if (championId == 114) championName = "Fiora";
                else if (championId == 115) championName = "Ziggs";
                else if (championId == 117) championName = "Lulu";
                else if (championId == 119) championName = "Draven";
                else if (championId == 120) championName = "Hecarim";
                else if (championId == 121) championName = "Kha'Zix";
                else if (championId == 122) championName = "Darius";
                else if (championId == 126) championName = "Jayce";
                else if (championId == 127) championName = "Lissandra";
                else if (championId == 131) championName = "Diana";
                else if (championId == 133) championName = "Quinn";
                else if (championId == 134) championName = "Syndra";
                else if (championId == 143) championName = "Zyra";
                else if (championId == 150) championName = "Gnar";
                else if (championId == 154) championName = "Zac";
                else if (championId == 157) championName = "Yasuo";
                else if (championId == 161) championName = "Vel'Koz";
                else if (championId == 201) championName = "Braum";
                else if (championId == 222) championName = "Jinx";
                else if (championId == 236) championName = "Lucian";
                else if (championId == 238) championName = "Zed";
                else if (championId == 254) championName = "Viktor";
                else if (championId == 266) championName = "Aatrox";
                else if (championId == 267) championName = "Nami";
                else if (championId == 268) championName = "Azir";
                else if (championId == 412) championName = "Thresh";
                else if (championId == 421) championName = "Rek'Sai";
                else if (championId == 429) championName = "Kalista";
                else if (championId == 432) championName = "Bard";

                if (teamId == 100) team = "Blue Team";
                else if (teamId == 200) team = "Red Team";


                summonerNames[i] = summonerStr[1] + "-" + name;

                resultStrs[i] = name + " - " + team + " - " + championName;
            }

            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String CurrentGameJsonStr = null;

            int numSummoners = 10;

            try {
                URL url = new URL("https://" + summonerStr[1].toLowerCase() + ".api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/" + summonerStr[1] + "1/" + summonerId + "?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");
                //URL url = new URL("https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/20672928?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");

                Log.v(LOG_TAG, "Built URL: " + url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                CurrentGameJsonStr = buffer.toString();

                Log.v(LOG_TAG, "CurrentGameJsonStr: " + CurrentGameJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOG_TAG", "Error closing stream", e);
                    }
                }
            }
            try {
                return getCurrentGameDataFromJson(CurrentGameJsonStr, numSummoners);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing the summoner or game
            return null;
        }

        @Override
        protected void onPostExecute(String[] result){
            if (result != null){
                mSummonersAdapter.clear();
                for (String gameResult : result)
                    mSummonersAdapter.add(gameResult);
                //New data is back from the server. Hooray!
            }
        }
    }
}