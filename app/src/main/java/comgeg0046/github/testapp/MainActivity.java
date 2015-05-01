package comgeg0046.github.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {

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
            case R.id.InGameLookup:
                currentGameLookUp();
                return true;
            case R.id.FeaturedGamesLookUp:
                featuredGamesLookUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the user clicks the In-Game Lookup button
     */
    public void currentGameLookUp() {
        Intent currentLookUp = new Intent(this, InGameLookup.class);
        startActivity(currentLookUp);
    }

    public void featuredGamesLookUp(){
        Intent featuredLookUp = new Intent(this, FeaturedGamesLookup.class);
        startActivity(featuredLookUp);
    }

    /*public void Aatrox(){
        Intent Aatrox = new Intent(this, Aatrox.class);
        startActivity(Aatrox);
    }*/
    public void Aatrox(View view) {
        Intent Aatrox = new Intent(this, Aatrox.class);
        startActivity(Aatrox);
    }


   public void Ahri(View view) {
        Intent Ahri = new Intent(this, Ahri.class);
        startActivity(Ahri);
    }

    public void Akali(View view) {
        Intent Akali = new Intent(this, Akali.class);
        startActivity(Akali);
    }

    public void Alistar(View view) {
        Intent Alistar = new Intent(this, Alistar.class);
        startActivity(Alistar);
    }

    public void Amumu(View view) {
        Intent Amumu = new Intent(this, Amumu.class);
        startActivity(Amumu);
    }

    public void Anivia(View view) {
        Intent Anivia = new Intent(this, Anivia.class);
        startActivity(Anivia);
    }
    public void Annie(View view){
        Intent Annie = new Intent(this, Annie.class);
        startActivity(Annie);
    }
    public void Ashe(View view){
        Intent Ashe = new Intent(this, Ashe.class);
        startActivity(Ashe);
    }
    public void Azir(View view){
        Intent Azir = new Intent(this, Azir.class);
        startActivity(Azir);
    }
    public void Bard(View view){
        Intent Bard = new Intent(this, Bard.class);
        startActivity(Bard);
    }
    public void Blitzcrank(View view){
        Intent Blitzcrank = new Intent(this, Blitzcrank.class);
        startActivity(Blitzcrank);
    }
    public void Brand(View view){
        Intent Brand = new Intent(this, Brand.class);
        startActivity(Brand);
    }
    public void Braum(View view){
        Intent Braum = new Intent(this, Braum.class);
        startActivity(Braum);
    }
    public void Caitlyn(View view){
        Intent Caitlyn = new Intent(this, Caitlyn.class);
        startActivity(Caitlyn);
    }
    public void Cassiopeia(View view){
        Intent Cassiopeia = new Intent(this, Cassiopeia.class);
        startActivity(Cassiopeia);
    }
    public void Chogath(View view){
        Intent Chogath = new Intent(this, Chogath.class);
        startActivity(Chogath);
    }
    public void Corki(View view){
        Intent Corki = new Intent(this, Corki.class);
        startActivity(Corki);
    }
    public void Darius(View view){
        Intent Darius = new Intent(this, Darius.class);
        startActivity(Darius);
    }
    public void Diana(View view){
        Intent Diana = new Intent(this, Diana.class);
        startActivity(Diana);
    }
    public void Dmundo(View view){
        Intent Dmundo = new Intent(this, DMundo.class);
        startActivity(Dmundo);
    }
    public void Draven(View view){
        Intent Draven = new Intent(this, Draven.class);
        startActivity(Draven);
    }
    public void Elise(View view){
        Intent Elise = new Intent(this, Elise.class);
        startActivity(Elise);
    }
    public void Evelynn(View view){
        Intent Evelynn = new Intent(this, Evelynn.class);
        startActivity(Evelynn);
    }
    public void Ezreal(View view){
        Intent Ezreal = new Intent(this, Ezreal.class);
        startActivity(Ezreal);
    }
    public void Fiddlesticks(View view){
        Intent Fiddlesticks = new Intent(this, Fiddlesticks.class);
        startActivity(Fiddlesticks);
    }
    public void Fiora(View view){
        Intent Fiora = new Intent(this, Fiora.class);
        startActivity(Fiora);
    }
    public void Fizz(View view){
        Intent Fizz = new Intent(this, Fizz.class);
        startActivity(Fizz);
    }
    public void Galio(View view){
        Intent Galio = new Intent(this, Galio.class);
        startActivity(Galio);
    }
    public void Gangplank(View view){
        Intent Gangplank = new Intent(this, Gangplank.class);
        startActivity(Gangplank);
    }
    public void Garen(View view){
        Intent Garen = new Intent(this, Garen.class);
        startActivity(Garen);
    }
    public void Gnar(View view){
        Intent Gnar = new Intent(this, Gnar.class);
        startActivity(Gnar);
    }
    public void Gragas(View view){
        Intent Gragas = new Intent(this, Gragas.class);
        startActivity(Gragas);
    }
    public void Graves(View view){
        Intent Graves = new Intent(this, Graves.class);
        startActivity(Graves);
    }
    public void Hecarim(View view){
        Intent Hecarim = new Intent(this, Hecarim.class);
        startActivity(Hecarim);
    }
}