package comgeg0046.github.testapp;

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
public class SummonerDetailsFragment extends Fragment {

    private ArrayAdapter<String> mDetailsAdapter;
    private String summonerRegion;
    private String summonerName;
    private int summonerId;
    //private String[] summonerNames = new String[10];

    public SummonerDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.summonerdetailsfragment, menu);
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

        summonerRegion = this.getArguments().getString("region");
        summonerName = this.getArguments().getString("name");

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mDetailsAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_summoner_details, // The name of the layout ID.
                        R.id.list_summoner_details_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.summonerdetailsfragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_summoner_details);
        listView.setAdapter(mDetailsAdapter);

        //Uncomment this section if we want on-click functionality.
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        //@Override
        //public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //    String summoner = summonerNames[position];
        //    Intent intent = new Intent(getActivity(), SummonerDetailsActivity.class)
        //            .putExtra(Intent.EXTRA_TEXT, summoner);
        //    startActivity(intent);
        //}
        //});

        return rootView;
    }

    private void updateSummoner() {
        FetchSummonerId SummonerTask = new FetchSummonerId();
        SummonerTask.execute();
    }

    private void updateGame() {
        FetchCurrentGameDetails GameTask = new FetchCurrentGameDetails();
        GameTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateSummoner();
        updateGame();
    }

    public class FetchSummonerId extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchSummonerId.class.getSimpleName();

        private void getSummonerIdFromJson(String SummonerJsonStr) throws JSONException{
            String summonerStr = null;

            //Intent intent = getActivity().getIntent();
            //if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                //String str = intent.getStringExtra(Intent.EXTRA_TEXT);
                //summonerStr = str.split(" "); //will need to change how this works for summoner names like Annie Bot
            //}

            //strings to find summoner id
            final String SUM = summonerName.toLowerCase();
            final String SUMID = "id";

            JSONObject SummonerData = new JSONObject(SummonerJsonStr);
            JSONObject SummonerInfo = SummonerData.getJSONObject(SUM);

            int sumId = SummonerInfo.getInt(SUMID);

            summonerId = sumId;

            summonerStr = "Name: " + summonerName + ", ID: " + summonerId + ", Region: " + summonerRegion;

            Log.v(LOG_TAG, "Summoner Entry: " + summonerId);

            //return summonerStr;
        }

        @Override
        protected String doInBackground(String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String SummonerJsonStr = null;
            //String[] summonerStr = null;

            //Intent intent = getActivity().getIntent();
            //if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                //String str = intent.getStringExtra(Intent.EXTRA_TEXT);
                //summonerStr = str.split(" "); //will need to change how this works for summoner names like Annie Bot
            //}

            try {
                URL url = new URL("https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" + summonerName + "?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");

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
                mDetailsAdapter.clear();
                mDetailsAdapter.add(result);
                //New data is back from the server. Hooray!
            }
        }
    }

    public class FetchCurrentGameDetails extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchCurrentGameDetails.class.getSimpleName();

        private String getCurrentGameDataFromJson(String CurrentGameJsonStr) throws JSONException{

            //strings to find other players and game stats
            final String SUMMONERS = "participants";
            final String SUMMONERID = "summonerId";
            final String RUNES = "runes";
            final String RUNECOUNT = "count";
            final String RUNEID = "runeId";

            JSONObject GameData = new JSONObject(CurrentGameJsonStr);
            JSONArray Summoners = GameData.getJSONArray(SUMMONERS);

            String resultStr = null;
            String[] runesStr = new String[10];
            int sumId;

            //JSON object representing each summoner
            for (int i = 0; i < Summoners.length(); i++) {
                JSONObject SummonerGameInfo = Summoners.getJSONObject(i);

                sumId = SummonerGameInfo.getInt(SUMMONERID);

                Log.v(LOG_TAG, "sumId: " + sumId + ", summonerId: " + summonerId);
                if (sumId == summonerId) {
                    JSONArray RuneInfo = SummonerGameInfo.getJSONArray(RUNES);

                    Log.v(LOG_TAG, "sumId == summonerID, going into loop.");
                    for (int j = 0; j < RuneInfo.length(); j++){
                        int runeCount;
                        int runeId;

                        JSONObject Runes = RuneInfo.getJSONObject(j);

                        runeCount = Runes.getInt(RUNECOUNT);
                        runeId = Runes.getInt(RUNEID);

                        runesStr[j] = runeCount + "x\tRune ID: " + runeId;

                        Log.v(LOG_TAG, "runesStr[j]: " + runesStr[j]);
                    }

                    break;
                }
            }

            resultStr = runesStr[0] + "\n\n" + runesStr[1] + "\n\n" + runesStr[2] + "\n\n" + runesStr[3];

            return resultStr;//sumId + " " + summonerId;
        }

        @Override
        protected String doInBackground(String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String CurrentGameJsonStr = null;

            try {
                URL url = new URL("https://" + summonerRegion + ".api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/" + summonerRegion + "1/" + summonerId + "?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");
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
                return getCurrentGameDataFromJson(CurrentGameJsonStr);
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
                mDetailsAdapter.clear();
                mDetailsAdapter.add(result);
                //New data is back from the server. Hooray!
            }
        }
    }
}