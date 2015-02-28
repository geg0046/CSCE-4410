package comgeg0046.github.testapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class InGameLookup extends ActionBarActivity {
    public final static String GAME_TO_DISPLAY = "comgeg0046.github.com.GAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game_lookup);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String[] items = new String[]{"BR", "EUNE", "EUW", "KR", "LAN", "LAS", "NA", "OCE", "RU", "TR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_in_game_lookup, menu);
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

    public void displayGame(View view) {
        Intent intent = new Intent(this, DisplayGame.class);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        String region = (String) dropdown.getSelectedItem();
        EditText summonerName = (EditText)findViewById(R.id.summoner_name);
        String string = summonerName.getText().toString() + " " + region;
        intent.putExtra(GAME_TO_DISPLAY, string);
        startActivity(intent);
    }
}
