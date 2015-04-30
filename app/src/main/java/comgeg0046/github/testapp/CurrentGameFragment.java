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

                championName = getChampionName(championId);

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

    private String getChampionName(int championId){
        if (championId == 1)
            return "Annie";
        else if (championId == 2)
            return "Olaf";
        else if (championId == 3)
            return "Galio";
        else if (championId == 4)
            return "Twisted Fate";
        else if (championId == 5)
            return "Xin Zhao";
        else if (championId == 6)
            return "Urgot";
        else if (championId == 7)
            return "LeBlanc";
        else if (championId == 8)
            return "Vladimir";
        else if (championId == 9)
            return "Fiddlesticks";
        else if (championId == 10)
            return "Kayle";
        else if (championId == 11)
            return "Master Yi";
        else if (championId == 12)
            return "Alistar";
        else if (championId == 13)
            return "Ryze";
        else if (championId == 14)
            return "Sion";
        else if (championId == 15)
            return "Sivir";
        else if (championId == 16)
            return "Soraka";
        else if (championId == 17)
            return "Teemo";
        else if (championId == 18)
            return "Tristana";
        else if (championId == 19)
            return "Wawrwick";
        else if (championId == 20)
            return "Nunu";
        else if (championId == 21)
            return "Miss Fortune";
        else if (championId == 22)
            return "Ashe";
        else if (championId == 23)
            return "Tryndamere";
        else if (championId == 24)
            return "Jax";
        else if (championId == 25)
            return "Morgana";
        else if (championId == 26)
            return "Zilean";
        else if (championId == 27)
            return "Singed";
        else if (championId == 28)
            return "Evelynn";
        else if (championId == 29)
            return "Twitch";
        else if (championId == 30)
            return "Karthus";
        else if (championId == 31)
            return "Cho'Gath";
        else if (championId == 32)
            return "Amumu";
        else if (championId == 33)
            return "Rammus";
        else if (championId == 34)
            return "Anivia";
        else if (championId == 35)
            return "Shaco";
        else if (championId == 36)
            return "Dr. Mundo";
        else if (championId == 37)
            return "Sona";
        else if (championId == 38)
            return "Kassadin";
        else if (championId == 39)
            return "Irelia";
        else if (championId == 40)
            return "Janna";
        else if (championId == 41)
            return "Gangplank";
        else if (championId == 42)
            return "Corki";
        else if (championId == 43)
            return "Karma";
        else if (championId == 44)
            return "Taric";
        else if (championId == 45)
            return "Veigar";
        else if (championId == 48)
            return "Trundle";
        else if (championId == 50)
            return "Swain";
        else if (championId == 51)
            return "Caitlyn";
        else if (championId == 53)
            return "Blitzcrank";
        else if (championId == 54)
            return "Malphite";
        else if (championId == 55)
            return "Katarina";
        else if (championId == 56)
            return "Nocturne";
        else if (championId == 57)
            return "Maokai";
        else if (championId == 58)
            return "Renekton";
        else if (championId == 59)
            return "Jarvan IV";
        else if (championId == 60)
            return "Elise";
        else if (championId == 61)
            return "Orianna";
        else if (championId == 62)
            return "Wukong";
        else if (championId == 63)
            return "Brand";
        else if (championId == 64)
            return "Lee Sin";
        else if (championId == 67)
            return "Vayne";
        else if (championId == 68)
            return "Rumble";
        else if (championId == 69)
            return "Cassiopeia";
        else if (championId == 72)
            return "Skarner";
        else if (championId == 74)
            return "Heimerdinger";
        else if (championId == 75)
            return "Nasus";
        else if (championId == 76)
            return "Nidalee";
        else if (championId == 77)
            return "Udyr";
        else if (championId == 78)
            return "Poppy";
        else if (championId == 79)
            return "Gragas";
        else if (championId == 80)
            return "Pantheon";
        else if (championId == 81)
            return "Ezreal";
        else if (championId == 82)
            return "Mordekaiser";
        else if (championId == 83)
            return "Yorick";
        else if (championId == 84)
            return "Akali";
        else if (championId == 85)
            return "Kennen";
        else if (championId == 86)
            return "Garen";
        else if (championId == 89)
            return "Leona";
        else if (championId == 90)
            return "Malzahar";
        else if (championId == 91)
            return "Talon";
        else if (championId == 92)
            return "Riven";
        else if (championId == 96)
            return "Kog'Maw";
        else if (championId == 98)
            return "Shen";
        else if (championId == 99)
            return "Lux";
        else if (championId == 101)
            return "Xerath";
        else if (championId == 102)
            return "Shyvana";
        else if (championId == 103)
            return "Ahri";
        else if (championId == 104)
            return "Graves";
        else if (championId == 105)
            return "Fizz";
        else if (championId == 106)
            return "Volibear";
        else if (championId == 107)
            return "Rengar";
        else if (championId == 110)
            return "Varus";
        else if (championId == 111)
            return "Nautilus";
        else if (championId == 112)
            return "Viktor";
        else if (championId == 113)
            return "Sejuani";
        else if (championId == 114)
            return "Fiora";
        else if (championId == 115)
            return "Ziggs";
        else if (championId == 117)
            return "Lulu";
        else if (championId == 119)
            return "Draven";
        else if (championId == 120)
            return "Hecarim";
        else if (championId == 121)
            return "Kha'Zix";
        else if (championId == 122)
            return "Darius";
        else if (championId == 126)
            return "Jayce";
        else if (championId == 127)
            return "Lissandra";
        else if (championId == 131)
            return "Diana";
        else if (championId == 133)
            return "Quinn";
        else if (championId == 134)
            return "Syndra";
        else if (championId == 143)
            return "Zyra";
        else if (championId == 150)
            return "Gnar";
        else if (championId == 154)
            return "Zac";
        else if (championId == 157)
            return "Yasuo";
        else if (championId == 161)
            return "Vel'Koz";
        else if (championId == 201)
            return "Braum";
        else if (championId == 222)
            return "Jinx";
        else if (championId == 236)
            return "Lucian";
        else if (championId == 238)
            return "Zed";
        else if (championId == 254)
            return "Viktor";
        else if (championId == 266)
            return "Aatrox";
        else if (championId == 267)
            return "Nami";
        else if (championId == 268)
            return "Azir";
        else if (championId == 412)
            return "Thresh";
        else if (championId == 421)
            return "Rek'Sai";
        else if (championId == 429)
            return "Kalista";
        else if (championId == 432)
            return "Bard";
        else
            return null;
    }
}