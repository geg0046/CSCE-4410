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
    public void Heimerdinger(View view){
        Intent Heimerdinger = new Intent(this, Heimerdinger.class);
        startActivity(Heimerdinger);
    }
    public void Irelia(View view){
        Intent Irelia = new Intent(this, Irelia.class);
        startActivity(Irelia);
    }
    public void Janna(View view){
        Intent Janna = new Intent(this, Janna.class);
        startActivity(Janna);
    }
    public void JarvanIV(View view){
        Intent JarvanIV = new Intent(this, JarvanIV.class);
        startActivity(JarvanIV);
    }
    public void Jax(View view){
        Intent Jax = new Intent(this, Jax.class);
        startActivity(Jax);
    }
    public void Jayce(View view){
        Intent Jayce = new Intent(this, Jayce.class);
        startActivity(Jayce);
    }
    public void Jinx(View view){
        Intent Jinx = new Intent(this, Jinx.class);
        startActivity(Jinx);
    }
    public void Kalista(View view){
        Intent Kalista = new Intent(this, Kalista.class);
        startActivity(Kalista);
    }
    public void Karma(View view){
        Intent Karma = new Intent(this, Karma.class);
        startActivity(Karma);
    }
    public void Karthus(View view){
        Intent Karthus = new Intent(this, Karthus.class);
        startActivity(Karthus);
    }
    public void Kassadin(View view){
        Intent Kassadin = new Intent(this, Kassadin.class);
        startActivity(Kassadin);
    }
    public void Katarina(View view){
        Intent Katarina = new Intent(this, Katarina.class);
        startActivity(Katarina);
    }
    public void Kayle(View view){
        Intent Kayle = new Intent(this, Kayle.class);
        startActivity(Kayle);
    }
    public void Kennen(View view){
        Intent Kennen = new Intent(this, Kennen.class);
        startActivity(Kennen);
    }
    public void Khazix(View view){
        Intent Khazix = new Intent(this, KhaZix.class);
        startActivity(Khazix);
    }
    public void Kogmaw(View view){
        Intent Kogmaw = new Intent(this, KogMaw.class);
        startActivity(Kogmaw);
    }
    public void LeBlanc(View view){
        Intent LeBlanc = new Intent(this, LeBlanc.class);
        startActivity(LeBlanc);
    }
    public void LeeSin(View view){
        Intent LeeSin = new Intent(this, LeeSin.class);
        startActivity(LeeSin);
    }
    public void Leona(View view){
        Intent Leona = new Intent(this, Leona.class);
        startActivity(Leona);
    }
    public void Lissandra(View view){
        Intent Lissandra = new Intent(this, Lissandra.class);
        startActivity(Lissandra);
    }
    public void Lucian(View view){
        Intent Lucian = new Intent(this, Lucian.class);
        startActivity(Lucian);
    }
    public void Lulu(View view){
        Intent Lulu = new Intent(this, LuLu.class);
        startActivity(Lulu);
    }
    public void Lux(View view){
        Intent Lux = new Intent(this, Lux.class);
        startActivity(Lux);
    }
    public void Malphite(View view){
        Intent Malphite = new Intent(this, Malphite.class);
        startActivity(Malphite);
    }
    public void Malzahar(View view){
        Intent Malzahar = new Intent(this, Malzahar.class);
        startActivity(Malzahar);
    }
    public void Maokai(View view){
        Intent Maokai = new Intent(this, Maokai.class);
        startActivity(Maokai);
    }
    public void MasterYi(View view){
        Intent MasterYi = new Intent(this, MasterYi.class);
        startActivity(MasterYi);
    }
    public void MissFortune(View view){
        Intent MissFortune = new Intent(this, MissFortune.class);
        startActivity(MissFortune);
    }
    public void Mordekaiser(View view){
        Intent Mordekaiser = new Intent(this, Mordekaiser.class);
        startActivity(Mordekaiser);
    }
    public void Morgana(View view){
        Intent Morgana = new Intent(this, Morgana.class);
        startActivity(Morgana);
    }
    public void Nami(View view){
        Intent Nami = new Intent(this, Nami.class);
        startActivity(Nami);
    }
    public void Nasus(View view){
        Intent Nasus = new Intent(this, Nasus.class);
        startActivity(Nasus);
    }
    public void Nautilus(View view){
        Intent Nautilus = new Intent(this, Nautilus.class);
        startActivity(Nautilus);
    }
    public void Nidalee(View view){
        Intent Nidalee = new Intent(this, Nidalee.class);
        startActivity(Nidalee);
    }
    public void Nocturne(View view){
        Intent Nocturne = new Intent(this, Nocturne.class);
        startActivity(Nocturne);
    }
    public void Nunu(View view){
        Intent Nunu = new Intent(this, Nunu.class);
        startActivity(Nunu);
    }

    public void Olaf(View view){
        Intent Olaf = new Intent(this, Olaf.class);
        startActivity(Olaf);
    }
    public void Orianna(View view){
        Intent Orianna = new Intent(this, Orianna.class);
        startActivity(Orianna);
    }
    public void Pantheon(View view){
        Intent Pantheon = new Intent(this, Pantheon.class);
        startActivity(Pantheon);
    }
    public void Poppy(View view){
        Intent Poppy = new Intent(this, Poppy.class);
        startActivity(Poppy);
    }
    public void Quinn(View view){
        Intent Quinn = new Intent(this, Quinn.class);
        startActivity(Quinn);
    }
    public void Rammus(View view){
        Intent Rammus = new Intent(this, Rammus.class);
        startActivity(Rammus);
    }
    public void RekSai(View view){
        Intent RekSai = new Intent(this, RekSai.class);
        startActivity(RekSai);
    }
    public void Renekton(View view){
        Intent Renekton = new Intent(this, Renekton.class);
        startActivity(Renekton);
    }
    public void Rengar(View view){
        Intent Rengar = new Intent(this, Rengar.class);
        startActivity(Rengar);
    }
    public void Riven(View view){
        Intent Riven = new Intent(this, Riven.class);
        startActivity(Riven);
    }
    public void Rumble(View view){
        Intent Rumble = new Intent(this, Rumble.class);
        startActivity(Rumble);
    }
    public void Ryze(View view){
        Intent Ryze = new Intent(this, Ryze.class);
        startActivity(Ryze);
    }
    public void Sejuani(View view){
        Intent Sejuani = new Intent(this, Sejuani.class);
        startActivity(Sejuani);
    }
    public void Shaco(View view){
        Intent Shaco = new Intent(this, Shaco.class);
        startActivity(Shaco);
    }
    public void Shen(View view){
        Intent Shen = new Intent(this, Shen.class);
        startActivity(Shen);
    }
    public void Shyvana(View view){
        Intent Shyvana = new Intent(this, Shyvana.class);
        startActivity(Shyvana);
    }
    public void Singed(View view){
        Intent Singed = new Intent(this, Singed.class);
        startActivity(Singed);
    }
    public void Sion(View view){
        Intent Sion = new Intent(this, Sion.class);
        startActivity(Sion);
    }
    public void Sivir(View view){
        Intent Sivir = new Intent(this, Sivir.class);
        startActivity(Sivir);
    }
    public void Skarner(View view){
        Intent Skarner = new Intent(this, Skarner.class);
        startActivity(Skarner);
    }
    public void Sona(View view){
        Intent Sona = new Intent(this, Sona.class);
        startActivity(Sona);
    }
    public void Soraka(View view){
        Intent Soraka = new Intent(this, Soraka.class);
        startActivity(Soraka);
    }
    public void Swain(View view){
        Intent Swain = new Intent(this, Swain.class);
        startActivity(Swain);
    }
    public void Syndra(View view){
        Intent Syndra = new Intent(this, Syndra.class);
        startActivity(Syndra);
    }
    public void Talon(View view){
        Intent Talon = new Intent(this, Talon.class);
        startActivity(Talon);
    }
    public void Taric(View view){
        Intent Taric = new Intent(this, Taric.class);
        startActivity(Taric);
    }
    public void Teemo(View view){
        Intent Teemo = new Intent(this, Teemo.class);
        startActivity(Teemo);
    }
    public void Thresh(View view){
        Intent Thresh = new Intent(this, Thresh.class);
        startActivity(Thresh);
    }
    public void Tristana(View view){
        Intent Tristana = new Intent(this, Tristana.class);
        startActivity(Tristana);
    }
    public void Trundle(View view){
        Intent Trundle = new Intent(this, Trundle.class);
        startActivity(Trundle);
    }
    public void Tryndamere(View view){
        Intent Tryndamere = new Intent(this, Tryndamere.class);
        startActivity(Tryndamere);
    }
    public void TwistedFate(View view){
        Intent TwistedFate = new Intent(this, TwistedFate.class);
        startActivity(TwistedFate);
    }
    public void Twitch(View view){
        Intent Twitch = new Intent(this, Twitch.class);
        startActivity(Twitch);
    }
    public void Udyr(View view){
        Intent Udyr = new Intent(this, Udyr.class);
        startActivity(Udyr);
    }
    public void Urgot(View view){
        Intent Urgot = new Intent(this, Urgot.class);
        startActivity(Urgot);
    }
    public void Varus(View view){
        Intent Varus = new Intent(this, Varus.class);
        startActivity(Varus);
    }
    public void Vayne(View view){
        Intent Vayne = new Intent(this, Vayne.class);
        startActivity(Vayne);
    }
    public void Veigar(View view){
        Intent Veigar = new Intent(this, Veigar.class);
        startActivity(Veigar);
    }
    public void VelKoz(View view){
        Intent VelKoz = new Intent(this, VelKoz.class);
        startActivity(VelKoz);
    }
    public void Vi(View view){
        Intent Vi = new Intent(this, Vi.class);
        startActivity(Vi);
    }
    public void Viktor(View view){
        Intent Viktor = new Intent(this, Viktor.class);
        startActivity(Viktor);
    }
    public void Vladimir(View view){
        Intent Vladimir = new Intent(this, Vladimir.class);
        startActivity(Vladimir);
    }
    public void Volibear(View view){
        Intent Volibear = new Intent(this, Volibear.class);
        startActivity(Volibear);
    }
    public void Warwick(View view){
        Intent Warwick = new Intent(this, Warwick.class);
        startActivity(Warwick);
    }
    public void Wukong(View view){
        Intent Wukong = new Intent(this, Wukong.class);
        startActivity(Wukong);
    }
    public void Xerath(View view){
        Intent Xerath = new Intent(this, Xerath.class);
        startActivity(Xerath);
    }
    public void XinZhao(View view){
        Intent XinZhao = new Intent(this, XinZhao.class);
        startActivity(XinZhao);
    }
    public void Yasuo(View view){
        Intent Yasuo = new Intent(this, Yasuo.class);
        startActivity(Yasuo);
    }
    public void Yorick(View view){
        Intent Yorick = new Intent(this, Yorick.class);
        startActivity(Yorick);
    }
    public void Zac(View view){
        Intent Zac = new Intent(this, Zac.class);
        startActivity(Zac);
    }
    public void Zed(View view){
        Intent Zed = new Intent(this, Zed.class);
        startActivity(Zed);
    }
    public void Ziggs(View view){
        Intent Ziggs = new Intent(this, Ziggs.class);
        startActivity(Ziggs);
    }
    public void Zilean(View view){
        Intent Zilean = new Intent(this, Zilean.class);
        startActivity(Zilean);
    }
    public void Zyra(View view){
        Intent Zyra = new Intent(this, Zyra.class);
        startActivity(Zyra);
    }

}