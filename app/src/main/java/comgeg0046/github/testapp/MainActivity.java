package comgeg0046.github.testapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity /*implements View.OnClickListener*/{
    public final static String EXTRA_MESSAGE = "comgeg0046.github.testapp.MESSAGE";

    //@Override
  /*  public void onClick(View x){
        switch (x.getId()){
           // case R.id.Aatrox: Aatrox();
            break;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  //      Button bttn = (Button) findViewById(R.id.button_id);
    //    bttn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.IGL:
                lookUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the In-Game Lookup button */
    public void lookUp() {
        Intent LookUp = new Intent(this, InGameLookup.class);
        startActivity(LookUp);
    }
    /*public void Aatrox(){
        Intent Aatrox = new Intent(this, Aatrox.class);
        startActivity(Aatrox);
    }*/
    public void Aatrox(View view){
        Intent Aatrox = new Intent(this, Aatrox.class);
        startActivity(Aatrox);
    }
}