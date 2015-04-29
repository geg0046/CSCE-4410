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
public class AatroxFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    public AatroxFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.aatroxfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateAatrox();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create some dummy data for the ListView.  Here's a sample weekly forecast


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_aatrox_items, // The name of the layout ID.
                        R.id.list_aatrox_items_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.aatroxfragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return rootView;
    }


    private void updateAatrox() {
        FetchAatroxTask AatroxTask = new FetchAatroxTask();
        AatroxTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateAatrox();
    }

    public class FetchAatroxTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchAatroxTask.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
            so for convenience we're breaking it out into its own method now.
         */

       /* private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
            return format.format(date).toString();
        }*/

        /*
            Prepare the weather high/lows for presentation.
         */
      /*  private String formatHighLows(double high, double low){
            //For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }*/

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy: constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */

        private String getAatroxDataFromJson(String AatroxJsonStr)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String DD_DATA = "data";
            final String DD_INFO = "info";
            final String DD_AATROX = "Aatrox";
            final String DD_ATTACK = "attack";
            final String DD_DEFENSE = "defense";
            final String DD_MAGIC = "magic";
            final String DD_DIFFICULTY = "difficulty";
            final String OWM_DESCRIPTION = "main";

            JSONObject ChampionData = new JSONObject(AatroxJsonStr);
            JSONObject Aatrox = ChampionData.getJSONObject(DD_DATA);
            JSONObject AatroxStats = Aatrox.getJSONObject(DD_AATROX);

            String resultStrs = null;

            JSONObject infoObject = AatroxStats.getJSONObject(DD_INFO);
            int attack = infoObject.getInt(DD_ATTACK);
            int defense = infoObject.getInt(DD_DEFENSE);
            int magic = infoObject.getInt(DD_MAGIC);
            int difficulty = infoObject.getInt(DD_DIFFICULTY);

            resultStrs = "Attack: " + attack + " Defense: " + defense + " Magic: " + magic + " Difficulty: " + difficulty;
            //for(int i = 0; i < weatherArray.length(); i++){
            // For now, using the format "Day, description, hi/low"
            /*    String day;
                String description;
                String highAndLow;

                // Get the JSON Object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long. We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime = dayForecast.getLong(OWM_DATETIME);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                //Temperatures are in a child object called "temp". Try not to name variables
                //"temp" when working with temperature. It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs = day + " - " + description + " - " + highAndLow;*/
            //}


            Log.v(LOG_TAG, "Forecast entry: " + resultStrs);

            return resultStrs;

        }

        @Override
        protected String doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up. Verify size of params.
            /*if (params.length == 0)
                return null;
            }*/

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String AatroxJsonStr = null;

            // String format = "json";
            // String units = "metric";
            // int numDays = 7;

            try {

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast



                URL url = new URL("http://ddragon.leagueoflegends.com/cdn/5.7.2/data/en_US/champion/Aatrox.json");

                Log.v(LOG_TAG, "Built URL " + url.toString());

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
                AatroxJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast String: " + AatroxJsonStr);
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
                return getAatroxDataFromJson(AatroxJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            //This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void  onPostExecute(String result){
            if (result != null){
                mForecastAdapter.clear();
                mForecastAdapter.add(result);
                //New data is back from the server. Hooray!
            }
        }
    }
}
