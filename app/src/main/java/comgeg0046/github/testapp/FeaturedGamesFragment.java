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
 * Created by CyberPowerPC on 4/30/2015.
 */
public class FeaturedGamesFragment extends Fragment {

    private ArrayAdapter<String> mFeaturedGamesAdapter;
    private String[] summonerNames = new String[5];
    private String[] summonerStr = null;
    private String summonerName = "";
    private String region;

    public FeaturedGamesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.featuredgamesfragment, menu);
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
            region = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mFeaturedGamesAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_game, // The name of the layout ID.
                        R.id.list_game_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.featuredgamesfragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_featured_games);
        listView.setAdapter(mFeaturedGamesAdapter);

        //Uncomment this section if we want on-click functionality.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String test = mSummonersAdapter.getItem(position);
                String summoner = summonerNames[position];
                Intent intent = new Intent(getActivity(), CurrentGame.class)
                        .putExtra(Intent.EXTRA_TEXT, summoner);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateFeaturedGames() {
        FetchFeaturedGames GameTask = new FetchFeaturedGames();
        GameTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateFeaturedGames();
    }

    public class FetchFeaturedGames extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchFeaturedGames.class.getSimpleName();

        private String[] getCurrentGameDataFromJson(String CurrentGameJsonStr, int numFeaturedGames) throws JSONException{

            //strings to find other players and game stats
            final String FEATURED = "gameList";
            final String SUMMONERS = "participants";
            final String TIME = "gameLength";
            final String SUMMONERNAME = "summonerName";

            JSONObject JSONData = new JSONObject(CurrentGameJsonStr);
            JSONArray FeaturedGames = JSONData.getJSONArray(FEATURED);

            String resultStrs[] = new String[numFeaturedGames];
            for (int i = 0; i < numFeaturedGames; i++) {
                String name;

                //JSON object representing each summoner
                JSONObject FeaturedGameInfo = FeaturedGames.getJSONObject(i);
                JSONArray Game = FeaturedGameInfo.getJSONArray(SUMMONERS);

                name = Game.getJSONObject(0).getString(SUMMONERNAME);

                String nameArray[] = name.split(" ");

                String actualName = "";
                for (int j = 0; j < nameArray.length; j++)
                    actualName = actualName + nameArray[j];



                //teamId = SummonerGameInfo.getInt(TEAM);
                //championId = SummonerGameInfo.getInt(CHAMPION);


                //if (teamId == 100) team = "Blue Team";
                //else if (teamId == 200) team = "Red Team";


                summonerNames[i] = region + " " + name;

                resultStrs[i] = region + " Featured Game " + (i + 1);
            }

            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params){

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String CurrentGameJsonStr = null;

            int numFeaturedGames = 5;

            try {
                URL url = new URL("https://" + region.toLowerCase() + ".api.pvp.net/observer-mode/rest/featured?api_key=a7e48163-f846-4502-b7c6-2cd202df5d3a");

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
                return getCurrentGameDataFromJson(CurrentGameJsonStr, numFeaturedGames);
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
                mFeaturedGamesAdapter.clear();
                for (String gameResult : result)
                    mFeaturedGamesAdapter.add(gameResult);
                //New data is back from the server. Hooray!
            }
        }
    }
}