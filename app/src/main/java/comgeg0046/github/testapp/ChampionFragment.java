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
 * Created by Callan on 4/6/2015.
 */
public class ChampionFragment extends Fragment {

    private ArrayAdapter<String> mChampionAdapter;
    private String championName = null;

    public ChampionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.championfragment, menu);
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

        championName = this.getArguments().getString("name");

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mChampionAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_champion_items, // The name of the layout ID.
                        R.id.list_champion_items_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.championfragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mChampionAdapter);

        //uncomment this if we decide we want to have the stats clickable; otherwise KEEP THESE ~10 LINES COMMENTED
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            //@Override
            //public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
            //    String forecast = mChampionAdapter.getItem(position);
            //    Intent intent = new Intent(getActivity(), DetailActivity.class)
            //            .putExtra(Intent.EXTRA_TEXT, forecast);
            //    startActivity(intent);
            //}
        //});

        return rootView;
    }


    private void updateChampion() {
        FetchChampionTask ChampionTask = new FetchChampionTask();
        ChampionTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateChampion();
    }

    public class FetchChampionTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchChampionTask.class.getSimpleName();

        private String getChampionDataFromJson(String ChampionJsonStr)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String DD_DATA = "data";
            final String DD_INFO = "info";
            final String DD_NAME = championName;
            final String DD_ID = "id";
            final String DD_ATTACK = "attack";
            final String DD_DEFENSE = "defense";
            final String DD_MAGIC = "magic";
            final String DD_DIFFICULTY = "difficulty";
            final String DD_LORE = "lore";

            //Champion "stats"
            final String DD_HP = "hp";
            final String DD_HPPL = "hpperlevel";
            final String DD_MP = "mp";
            final String DD_MPPL = "mpperlevel";
            final String DD_MOVESPD = "movespeed";
            final String DD_ARMOR = "armor";
            final String DD_ARMORPL = "armorperlevel";
            final String DD_MR = "spellblock";
            final String DD_MRPL = "spellblockperlevel";
            final String DD_AR = "attackrange";
            final String DD_HPR = "hpregen";
            final String DD_HPRPL = "hpregenperlevel";
            final String DD_MPR = "mpregen";
            final String DD_MPRPL = "mpregenperlevel";
            final String DD_AD = "attackdamage";
            final String DD_ADPL = "attackdamageperlevel";
            final String DD_AS = "attackspeedoffset"; //ASbase = 0.625 / (1 + attackspeedoffset)
            final String DD_ASPL = "attackspeedperlevel";

            //Champion "spells[]"; Spells are inserted into an array, and read off by their
            final String DD_SNAME = "name";
            final String DD_SDESCRIPTION = "description";
            final String DD_TOOLTIP = "tooltip"; //This might be worth using over the description, but will take some code to synergize with what e1/a1 mean, etc.


            final String OWM_DESCRIPTION = "main";

            JSONObject ChampionData = new JSONObject(ChampionJsonStr);
            JSONObject Champion = ChampionData.getJSONObject(DD_DATA);
            JSONObject ChampionStats = Champion.getJSONObject(DD_NAME);

            String name = ChampionStats.getString(DD_ID);
            String resultStrs = null;

            JSONObject infoObject = ChampionStats.getJSONObject(DD_INFO);
            int attack = infoObject.getInt(DD_ATTACK);
            int defense = infoObject.getInt(DD_DEFENSE);
            int magic = infoObject.getInt(DD_MAGIC);
            int difficulty = infoObject.getInt(DD_DIFFICULTY);

            resultStrs = "Champion: " + name + "\nAttack: " + attack + " Defense: " + defense + " Magic: " + magic + " Difficulty: " + difficulty;

            Log.v(LOG_TAG, "Champion entry: " + resultStrs);

            return resultStrs;

        }

        @Override
        protected String doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String ChampionJsonStr = null;

            try {

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/5.7.2/data/en_US/champion/" + championName + ".json");

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
                ChampionJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Champion String: " + ChampionJsonStr);
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
                return getChampionDataFromJson(ChampionJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if (result != null){
                mChampionAdapter.clear();
                mChampionAdapter.add(result);
                //New data is back from the server. Hooray!
            }
        }
    }
}
