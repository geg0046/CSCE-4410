package comgeg0046.github.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * Created by CyberPowerPC on 4/30/2015.
 */
public class FeaturedGamesLookup extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_games_lookup);

        Spinner dropdown = (Spinner)findViewById(R.id.featuredSpinner);
        String[] items = new String[]{"NA", "EUW", "KR", "OCE", "EUNE", "BR", "LAN", "LAS", "RU", "TR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_featured_games_lookup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        //return true;
        return super.onOptionsItemSelected(item);
    }

    public void displayFeaturedGames(View view) {
        Intent intent = new Intent(this, FeaturedGamesActivity.class); //change to FeaturedGame.class
        Spinner dropdown = (Spinner)findViewById(R.id.featuredSpinner);
        String region = (String) dropdown.getSelectedItem();
        intent.putExtra(Intent.EXTRA_TEXT, region);
        startActivity(intent);
    }
}
