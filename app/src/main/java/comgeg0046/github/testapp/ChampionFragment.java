package comgeg0046.github.testapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import java.text.DecimalFormat;
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
        ListView listView = (ListView) rootView.findViewById(R.id.listview_stats);
        listView.setAdapter(mChampionAdapter);


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

    /**
     * Returns a CharSequence that concatenates the specified array of CharSequence
     * objects and then applies a list of zero or more tags to the entire range.
     *
     * @param content an array of character sequences to apply a style to
     * @param tags the styled span objects to apply to the content
     *        such as android.text.style.StyleSpan
     *
     */
    private static CharSequence apply(CharSequence[] content, Object... tags) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }

    /**
     * Iterates over an array of tags and applies them to the beginning of the specified
     * Spannable object so that future text appended to the text will have the styling
     * applied to it. Do not call this method directly.
     */
    private static void openTags(Spannable text, Object[] tags) {
        for (Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }

    /**
     * "Closes" the specified tags on a Spannable by updating the spans to be
     * endpoint-exclusive so that future text appended to the end will not take
     * on the same styling. Do not call this method directly.
     */
    private static void closeTags(Spannable text, Object[] tags) {
        int len = text.length();
        for (Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }

    /**
     * Returns a CharSequence that applies a foreground color to the
     * concatenation of the specified CharSequence objects.
     */
    public static CharSequence color(int color, CharSequence... content) {
        return apply(content, new ForegroundColorSpan(color));
    }

    public class FetchChampionTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = FetchChampionTask.class.getSimpleName();

        private String getChampionDataFromJson(String ChampionJsonStr)
                throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String DD_DATA = "data";
            final String DD_INFO = "info";
            final String DD_STATS = "stats";
            final String DD_PASSIVE = "passive";
            final String DD_SPELLS = "spells";
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

            //Champion "spells[4]"; Spells are inserted into an array, and read off by their
            final String DD_SNAME = "name";
            final String DD_TOOLTIP = "tooltip"; //This might be worth using over the description, but will take some code to synergize with what e1/a1 mean, etc.
            final String DD_EFFECTBURN = "effectBurn";
            final String DD_COOLDOWNBURN = "cooldownBurn"; //Array
            final String DD_COSTBURN = "costBurn"; //Array
            final String DD_VARS = "vars"; //Array; "link," "coeff," "key."
                final String DD_LINK = "link";
                final String DD_COEFF = "coeff";
                final String DD_KEY = "key";
            final String DD_COSTTYPE = "costType";
            final String DD_RANGEBURN = "rangeBurn"; //Array
            final String DD_RESOURCE = "resource";

            //Champion "passive"
            final String DD_PDESCRIPTION = "description";
            final String DD_IMAGE = "image";


            final String OWM_DESCRIPTION = "main";

            JSONObject ChampionData = new JSONObject(ChampionJsonStr);
            JSONObject Champion = ChampionData.getJSONObject(DD_DATA);
            JSONObject ChampionStats = Champion.getJSONObject(DD_NAME);

            String name = ChampionStats.getString(DD_ID);
            String resultStrs = null;

            //Info
            JSONObject infoObject = ChampionStats.getJSONObject(DD_INFO);
            int attack = infoObject.getInt(DD_ATTACK);
            int defense = infoObject.getInt(DD_DEFENSE);
            int magic = infoObject.getInt(DD_MAGIC);
            int difficulty = infoObject.getInt(DD_DIFFICULTY);

            //Lore
           /* String lore = ChampionStats.getString(DD_LORE);
            lore = lore.replace("<br>", "\n"); */

            //Stats
            JSONObject statsObject = ChampionStats.getJSONObject(DD_STATS);
            double hp = statsObject.getDouble(DD_HP);
            double hppl = statsObject.getDouble(DD_HPPL);
            double mp = statsObject.getDouble(DD_MP);
            double mppl = statsObject.getDouble(DD_MPPL);
            double armor = statsObject.getDouble(DD_ARMOR);
            double armorpl = statsObject.getDouble(DD_ARMORPL);
            double mr = statsObject.getDouble(DD_MR);
            double mrpl = statsObject.getDouble(DD_MRPL);
            double movespd = statsObject.getDouble(DD_MOVESPD);
            double ar = statsObject.getDouble(DD_AR);
            double hpr = statsObject.getDouble(DD_HPR);
            double hprpl = statsObject.getDouble(DD_HPRPL);
            double mpr = statsObject.getDouble(DD_MPR);
            double mprpl = statsObject.getDouble(DD_MPRPL);
            double ad = statsObject.getDouble(DD_AD);
            double adpl = statsObject.getDouble(DD_ADPL);
            double as = statsObject.getDouble(DD_AS);
            double aspl = statsObject.getDouble(DD_ASPL);

            double asBase = (0.625 / (1 + as));
            DecimalFormat newASBase = new DecimalFormat("#.###");





            resultStrs = name + "\nGeneral Rating (out of 10):\n\nAttack: " + attack + " Defense: " + defense + " Magic: " + magic + " Difficulty: " + difficulty + "\n\n\n " +

                    "STATS:" +
                    "\n\nHealth: " + hp + " (+" + hppl + " per level)\n" +
                    "Health Regen: " + hpr + " (+" + hprpl + " per level)\n" +
                    "Mana: " + mp + " (+" + mppl + " per level)\n" +
                    "Mana Regen: " + mpr + " (+" + mprpl + " per level)\n" +
                    "Attack Damage: " + ad + " (+" + adpl + " per level)\n" +
                    "Attack Speed: " + newASBase.format(asBase) + " (+" + aspl + "% per level)\n" +
                    "Attack Range: " + ar + "\n" +
                    "Armor: " + armor + " (+" + armorpl + " per level)\n" +
                    "Magic Resistance: " + mr + " (+" + mrpl + " per level)\n" +
                    "Movement Speed: " + movespd + "\n\n\n";


            JSONObject passiveObject = ChampionStats.getJSONObject(DD_PASSIVE);
            String passive = passiveObject.getString(DD_PDESCRIPTION);
            resultStrs = resultStrs + "PASSIVE:\n\n" + passive + "\n\n\nABILITIES:\n\n";



            JSONArray spellsArray = ChampionStats.getJSONArray(DD_SPELLS);
                for(int i = 0; i < spellsArray.length(); i++){

                JSONObject sDetails = spellsArray.getJSONObject(i);
                String sName = sDetails.getString(DD_SNAME);
                String sCostType = sDetails.getString(DD_COSTTYPE);
                String sRange = sDetails.getString(DD_RANGEBURN);
                String sCost = sDetails.getString(DD_COSTBURN);
                String sCooldown = sDetails.getString(DD_COOLDOWNBURN);
                String sResource = sDetails.getString(DD_RESOURCE);

                JSONArray effectBurn = sDetails.getJSONArray(DD_EFFECTBURN);
                int eBurnLength = effectBurn.length();

                String[] eBurn = new String[eBurnLength];
                for (int j = 1; j < eBurnLength; j++){
                    eBurn[j] = effectBurn.getString(j);
                }

                resultStrs = resultStrs + sName;
                if(i == 0)
                    resultStrs = resultStrs + " (Q):\n\n";
                if(i == 1)
                    resultStrs = resultStrs + " (W):\n\n";
                if(i == 2)
                    resultStrs = resultStrs + " (E):\n\n";
                if(i == 3)
                    resultStrs = resultStrs + " (R):\n\n";


                if (sResource.contains("{{ e"))
                {
                    for(int j = 1; j < effectBurn.length(); j++) {
                        if (sResource.contains("{{ e" + j)) {
                            int startIndex = sResource.indexOf("{{ e" + j);
                            int endIndex = sResource.indexOf("e"+ j + " }}");
                            String replacement = eBurn[j];
                            String toBeReplaced = sResource.substring(startIndex, endIndex + 5);
                            sResource = sResource.replace(toBeReplaced, replacement);
                            resultStrs = resultStrs + sResource + "\n";
                        }
                    }
                }
                else{
                    if(sCostType.equals("NoCost"))
                    {
                        resultStrs = resultStrs + "No Cost\n";
                    }
                    else {
                        resultStrs = resultStrs + sCost + " " + sCostType + "\n";
                    }
                }

                resultStrs = resultStrs + sCooldown + " second(s) cooldown\n" +
                            sRange + " range\n\n";


                String sTooltip = sDetails.getString(DD_TOOLTIP);
                while(sTooltip.contains("<")){
                    int startIndex = sTooltip.indexOf("<");
                    int endIndex = sTooltip.indexOf(">");
                    String replacement = "";
                    String toBeReplaced = sTooltip.substring(startIndex, endIndex+1);
                    sTooltip = sTooltip.replace(toBeReplaced, replacement);
                }





                for(int j = 1; j < effectBurn.length(); j++) {
                    if (sTooltip.contains("{{ e" + j)) {
                        int startIndex = sTooltip.indexOf("{{ e" + j);
                        int endIndex = sTooltip.indexOf("e"+ j + " }}");
                        String replacement = eBurn[j];
                        String toBeReplaced = sTooltip.substring(startIndex, endIndex + 5);
                        sTooltip = sTooltip.replace(toBeReplaced, replacement);

                    }
                }

                JSONArray vars = sDetails.getJSONArray(DD_VARS);
                if(!vars.equals(null)) {
                    String[] coeff = new String[vars.length() + 5];
                    String[] coefff = new String[vars.length() + 5];

                    for (int j = 0; j < vars.length(); j++) {
                        JSONObject var = vars.getJSONObject(j);
                        String coeffa = var.getString(DD_COEFF);
                        String key = var.getString(DD_KEY);
                        String link = var.getString(DD_LINK);
                        if (link.equals("bonusattackdamage"))
                            link = "Bonus AD";
                        else
                        if (link.equals("spelldamage"))
                            link = "AP";
                        else
                        if (link.equals("attackdamage"))
                            link = "AD";
                        else
                        if (link.equals("@cooldownchampion"))
                            link = " ";
                        else
                        if (link.equals("@dynamic.abilitypower"))
                            link = "AP Ratio";
                        if(key.contains("a")) {
                            key = key.replace("a", "");
                            int index = Integer.parseInt(key);
                            coeff[index] = coeffa + " " + link;
                        }
                        else
                        if(key.contains("f")) {
                            key = key.replace("f", "");
                            int index = Integer.parseInt(key);
                            coefff[index] = coeffa + " " + link;
                        }
                    }


                    for (int j = 1; j < vars.length() + 1; j++) {

                        if (sTooltip.contains("{{ a" + j)) {

                            //for(int k = 0; k < vars.length(); k++) {
                            //JSONObject eleVar = vars.getJSONObject(k);
                            //String key = eleVar.getString(DD_KEY);


                            String replacement = coeff[j];
                            int startIndex = sTooltip.indexOf("{{ a" + j);
                            int endIndex = sTooltip.indexOf("a" + j + " }}");
                            String toBeReplaced = sTooltip.substring(startIndex, endIndex + 5);
                            sTooltip = sTooltip.replace(toBeReplaced, replacement);


                            //}
                        }
                        if (sTooltip.contains("{{ f" + j)) {

                            //for(int k = 0; k < vars.length(); k++) {
                            //JSONObject eleVar = vars.getJSONObject(k);
                            //String key = eleVar.getString(DD_KEY);
                            if(coefff[j] != null) {
                                String replacement = coefff[j];
                                int startIndex = sTooltip.indexOf("{{ f" + j);
                                int endIndex = sTooltip.indexOf("f" + j + " }}");
                                String toBeReplaced = sTooltip.substring(startIndex, endIndex + 5);
                                sTooltip = sTooltip.replace(toBeReplaced, replacement);
                            }
                            else
                            {
                                String replacement = "";
                                int startIndex = sTooltip.indexOf("{{ f" + j);
                                int endIndex = sTooltip.indexOf("f" + j + " }}");
                                String toBeReplaced = sTooltip.substring(startIndex, endIndex + 5);
                                sTooltip = sTooltip.replace(toBeReplaced, replacement);
                            }
                        }


                            //}

                    }
                }

                if (sTooltip.contains("{")) {
                    while (sTooltip.contains("{")) {
                        String replacement = "";
                        int startIndex = sTooltip.lastIndexOf("{");
                        int endIndex = sTooltip.lastIndexOf("}");
                        String toBeReplaced = sTooltip.substring(startIndex-1, endIndex + 1);
                        sTooltip = sTooltip.replace(toBeReplaced, replacement);
                    }
                }
                resultStrs = resultStrs + sTooltip + "\n\n\n";

            }





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
