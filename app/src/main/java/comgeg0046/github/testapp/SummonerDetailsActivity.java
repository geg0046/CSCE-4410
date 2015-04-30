package comgeg0046.github.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SummonerDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_details);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String str = intent.getStringExtra(Intent.EXTRA_TEXT);
                String[] summonerStr = str.split("-");

                Bundle bundle = new Bundle();
                bundle.putString("region", summonerStr[0]);
                bundle.putString("name", summonerStr[1]);
                SummonerDetailsFragment fragInfo = new SummonerDetailsFragment();
                fragInfo.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragInfo)
                        .commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summoner_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
