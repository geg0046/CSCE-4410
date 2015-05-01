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


            Log.v(LOG_TAG, "Summoner Entry: " + summonerStr);

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
            final String MASTERIES = "masteries";
            final String MASTERYID = "masteryId";
            final String MASTERYCOUNT = "rank";
            final String SPELLONE = "spell1Id";
            final String SPELLTWO = "spell2Id";

            JSONObject GameData = new JSONObject(CurrentGameJsonStr);
            JSONArray Summoners = GameData.getJSONArray(SUMMONERS);

            String resultStr = null;
            String[] runesStr = new String[10];
            int[] masteriesStr = { 0, 0, 0 };
            String[] summonerSpells = new String[2];
            int sumId;
            int length = 0;

            //JSON object representing each summoner
            for (int i = 0; i < Summoners.length(); i++) {
                JSONObject SummonerGameInfo = Summoners.getJSONObject(i);

                sumId = SummonerGameInfo.getInt(SUMMONERID);

                Log.v(LOG_TAG, "sumId: " + sumId + ", summonerId: " + summonerId);
                if (sumId == summonerId) {
                    Log.v(LOG_TAG, "sumId == summonerID, going into loop.");
                    int j;

                    int spellOne = SummonerGameInfo.getInt(SPELLONE);
                    int spellTwo = SummonerGameInfo.getInt(SPELLTWO);

                    String spellOneName = getSummonerSpellName(spellOne);
                    String spellTwoName = getSummonerSpellName(spellTwo);

                    summonerSpells[0] = spellOneName;
                    summonerSpells[1] = spellTwoName;

                    JSONArray MasteryInfo = SummonerGameInfo.getJSONArray(MASTERIES);
                    for (j = 0; j < MasteryInfo.length(); j++){
                        JSONObject Masteries = MasteryInfo.getJSONObject(j);
                        int mastery = Masteries.getInt(MASTERYID);
                        int masteryCount = Masteries.getInt(MASTERYCOUNT);

                        if (mastery < 4200)
                            masteriesStr[0] += masteryCount;
                        else if (mastery > 4200 && mastery < 4300)
                            masteriesStr[1] += masteryCount;
                        else if (mastery > 4300)
                            masteriesStr[2] += masteryCount;
                    }

                    JSONArray RuneInfo = SummonerGameInfo.getJSONArray(RUNES);
                    length = RuneInfo.length();
                    for (j = 0; j < RuneInfo.length(); j++){
                        int runeCount;
                        int runeId;
                        String runeName;

                        JSONObject Runes = RuneInfo.getJSONObject(j);

                        runeCount = Runes.getInt(RUNECOUNT);
                        runeId = Runes.getInt(RUNEID);

                        runeName = getRuneName(runeId);

                        runesStr[j] = runeCount + "x\t" + runeName;

                        Log.v(LOG_TAG, "runesStr[j]: " + runesStr[j]);
                    }

                    break;
                }
            }

            String[] fullRuneStr = new String[length];

            int k;
            for (k = 0; k < fullRuneStr.length; k++){
                fullRuneStr[k] = runesStr[k];
            }

            String allRunes = "Runes:\n\n" + fullRuneStr[0];
            for (k = 1; k < fullRuneStr.length; k++){
                allRunes = allRunes + "\n\n" + fullRuneStr[k];
            }

            resultStr = "Summoner Spells:\n\nD: " + summonerSpells[0] + "\n\nF: " + summonerSpells[1] + "\n\n\n\n\n" +
                    "Masteries:\n\n" + masteriesStr[0] + " Offense\n\n" + masteriesStr[1] + " Defense\n\n" + masteriesStr[2] + " Utility\n\n\n\n\n" +
                    allRunes;

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

    private String getSummonerSpellName(int summonerSpellId){
        if (summonerSpellId == 1)
            return "Cleanse";
        else if (summonerSpellId == 2)
            return "Clairvoyance";
        else if (summonerSpellId == 3)
            return "Exhaust";
        else if (summonerSpellId == 4)
            return "Flash";
        else if (summonerSpellId == 6)
            return "Ghost";
        else if (summonerSpellId == 7)
            return "Heal";
        else if (summonerSpellId == 11)
            return "Smite";
        else if (summonerSpellId == 12)
            return "Teleport";
        else if (summonerSpellId == 13)
            return "Clarity";
        else if (summonerSpellId == 14)
            return "Ignite";
        else if (summonerSpellId == 17)
            return "Garrison";
        else if (summonerSpellId == 21)
            return "Barrier";
        else
            return null;
    }

    private String getRuneName(int runeId){
        if (runeId == 5245)
            return "Greater Mark of Attack Damage";
        else if (runeId == 5246)
            return "Greater Mark of Scaling Attack Damage";
        else if (runeId == 5247)
            return "Greater Mark of Attack Speed";
        else if (runeId == 5249)
            return "Greater Mark of Critical Damage";
        else if (runeId == 5251)
            return "Greater Mark of Critical Chance";
        else if (runeId == 5253)
            return "Greater Mark of Armor Penetration";
        else if (runeId == 5255)
            return "Greater Mark of Health";
        else if (runeId == 5256)
            return "Greater Mark of Scaling Health";
        else if (runeId == 5257)
            return "Greater Mark of Armor";
        else if (runeId == 5259)
            return "Greater Mark of Magic Resist";
        else if (runeId == 5260)
            return "Greater Mark of Scaling Magic Resist";
        else if (runeId == 5265)
            return "Greater Mark of Cooldown Reduction";
        else if (runeId == 5267)
            return "Greater Mark of Ability Power";
        else if (runeId == 5268)
            return "Greater Mark of Scaling Ability Power";
        else if (runeId == 5269)
            return "Greater Mark of Mana";
        else if (runeId == 5270)
            return "Greater Mark of Scaling Mana";
        else if (runeId == 5271)
            return "Greater Mark of Mana Regeneration";
        else if (runeId == 5273)
            return "Greater Mark of Magic Penetration";
        else if (runeId == 5275)
            return "Greater Glyph of Attack Speed";
        else if (runeId == 5276)
            return "Greater Glyph of Scaling Attack Damage";
        else if (runeId == 5277)
            return "Greater Glyph of Attack Speed";
        else if (runeId == 5279)
            return "Greater Glyph of Critical Damage";
        else if (runeId == 5281)
            return "Greater Glyph of Critical Chance";
        else if (runeId == 5285)
            return "Greater Glyph of Health";
        else if (runeId == 5286)
            return "Greater Glyph of Scaling Health";
        else if (runeId == 5287)
            return "Greater Glyph of Armor";
        else if (runeId == 5289)
            return "Greater Glyph of Magic Resist";
        else if (runeId == 5290)
            return "Greater Glyph of Scaling Magic Resist";
        else if (runeId == 5291)
            return "Greater Glyph of Health Regeneration";
        else if (runeId == 5295)
            return "Greater Glyph of Cooldown Reduction";
        else if (runeId == 5296)
            return "Greater Glyph of Scaling Cooldown Reduction";
        else if (runeId == 5297)
            return "Greater Glyph of Ability Power";
        else if (runeId == 5299)
            return "Greater Glyph of Mana";
        else if (runeId == 5300)
            return "Greater Glyph of Scaling Mana";
        else if (runeId == 5301)
            return "Greater Glyph of Mana Regeneration";
        else if (runeId == 5302)
            return "Greater Glyph of Scaling Mana Regeneration";
        else if (runeId == 5303)
            return "Greater Glyph of Magic Penetration";
        else if (runeId == 5305)
            return "Greater Seal of Attack Damage";
        else if (runeId == 5306)
            return "Greater Seal of Scaling Attack Damage";
        else if (runeId == 5307)
            return "Greater Seal of Attack Speed";
        else if (runeId == 5309)
            return "Greater Seal of Critical Damage";
        else if (runeId == 5311)
            return "Greater Seal of Critical Chance";
        else if (runeId == 5315)
            return "Greater Seal of Health";
        else if (runeId == 5316)
            return "Greater Seal of Scaling Health";
        else if (runeId == 5317)
            return "Greater Seal of Armor";
        else if (runeId == 5318)
            return "Greater Seal of Scaling Armor";
        else if (runeId == 5319)
            return "Greater Seal of Magic Resist";
        else if (runeId == 5320)
            return "Greater Seal of Scaling Magic Resist";
        else if (runeId == 5321)
            return "Greater Seal of Health Regeneration";
        else if (runeId == 5322)
            return "Greater Seal of Scaling Health Regeneration";
        else if (runeId == 5325)
            return "Greater Seal of Cooldown Reduction";
        else if (runeId == 5327)
            return "Greater Seal of Ability Power";
        else if (runeId == 5328)
            return "Greater Seal of Scaling Ability Power";
        else if (runeId == 5329)
            return "Greater Seal of Mana";
        else if (runeId == 5330)
            return "Greater Seal of Scaling Mana";
        else if (runeId == 5331)
            return "Greater Seal of Mana Regeneration";
        else if (runeId == 5332)
            return "Greater Seal of Scaling Mana Regeration";
        else if (runeId == 5335)
            return "Greater Quintessence of Attack Damage";
        else if (runeId == 5336)
            return "Greater Quintessence of Scaling Attack Damage";
        else if (runeId == 5337)
            return "Greater Quintessence of Attack Speed";
        else if (runeId == 5339)
            return "Greater Quintessence of Critical Damage";
        else if (runeId == 5341)
            return "Greater Quintessence of Critical Chance";
        else if (runeId == 5343)
            return "Greater Quintessence of Armor Penetration";
        else if (runeId == 5345)
            return "Greater Quintessence of Health";
        else if (runeId == 5346)
            return "Greater Quintessence of Scaling Health";
        else if (runeId == 5347)
            return "Greater Quintessence of Armor";
        else if (runeId == 5348)
            return "Greater Quintessence of Scaling Armor";
        else if (runeId == 5349)
            return "Greater Quintessence of Magic Resist";
        else if (runeId == 5350)
            return "Greater Quintessence of Scaling Magic Resist";
        else if (runeId == 5351)
            return "Greater Quintessence of Health Regeneration";
        else if (runeId == 5352)
            return "Greater Quintessence of Scaling Health Regeneration";
        else if (runeId == 5355)
            return "Greater Quintessence of Cooldown Reduction";
        else if (runeId == 5356)
            return "Greater Quintessence of Scaling Cooldown Reduction";
        else if (runeId == 5357)
            return "Greater Quintessence of Ability Power";
        else if (runeId == 5358)
            return "Greater Quintessence of Scaling Ability Power";
        else if (runeId == 5359)
            return "Greater Quintessence of Mana";
        else if (runeId == 5360)
            return "Greater Quintessence of Scaling Mana";
        else if (runeId == 5361)
            return "Greater Quintessence of Mana Regeneration";
        else if (runeId == 5362)
            return "Greater Quintessence of Scaling Mana Regeneration";
        else if (runeId == 5363)
            return "Greater Quintessence of Magic Penetration";
        else if (runeId == 5365)
            return "Greater Quintessence of Movement Speed";
        else if (runeId == 5366)
            return "Greater Quintessence of Revival";
        else if (runeId == 5367)
            return "Greater Quintessence of Gold";
        else if (runeId == 5368)
            return "Greater Quintessence of Experience";
        else if (runeId == 5369)
            return "Greater Seal of Energy Regeneration";
        else if (runeId == 5370)
            return "Greater Seal of Scaling Energy Regeneration";
        else if (runeId == 5371)
            return "Greater Glyph of Energy";
        else if (runeId == 5372)
            return "Greater Glyph of Scaling Energy";
        else if (runeId == 5373)
            return "Greater Quintessence of Energy Regeneration";
        else if (runeId == 5374)
            return "Greater Quintessence of Energy";
        else if (runeId == 5398)
            return "Greater Glyph of Scaling Ability Power";
        else if (runeId == 5402)
            return "Greater Mark of Hybrid Penetration";
        else if (runeId == 5403)
            return "Greater Seal of Gold";
        else if (runeId == 5406)
            return "Greater Quintessence of Percent Health";
        else if (runeId == 5409)
            return "Greater Quintessence of Spell Vamp";
        else if (runeId == 5412)
            return "Greater Quintessence of Life Steal";
        else if (runeId == 5415)
            return "Greater Seal of Percent Health";
        else if (runeId == 5418)
            return "Greater Quintessence of Hybrid Penetration";
        else if (runeId == 8019)
            return "Greater Quintessence of the Piercing Present";
        else if (runeId == 8020)
            return "Greater Quintessence of the Deadly Wreath";
        else if (runeId == 8021)
            return "Greater Quintessence of Frosty Health";
        else if (runeId == 8022)
            return "Greater Quintessence of Sugar Rush";
        else if (runeId == 8035)
            return "Greater Quintessence of Studio Rumble";
        else
            return null;
    }
}