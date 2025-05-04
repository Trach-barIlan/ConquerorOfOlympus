package trach.yoni.olympiangods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Random;

import trach.yoni.olympiangods.Items.GenericItem;
import trach.yoni.olympiangods.Items.HealthPotion;
import trach.yoni.olympiangods.Items.LegendarySafe;
import trach.yoni.olympiangods.Items.MythicSafe;
import trach.yoni.olympiangods.Items.CommonSafe;
import trach.yoni.olympiangods.Items.RareSafe;
import trach.yoni.olympiangods.Items.UncommonSafe;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.Aphrodite;
import trach.yoni.olympiangods.characters.Dionysus;
import trach.yoni.olympiangods.characters.GameCharacter;
import trach.yoni.olympiangods.characters.Hades;
import trach.yoni.olympiangods.characters.Hephaestus;
import trach.yoni.olympiangods.characters.Hercules;
import trach.yoni.olympiangods.characters.Opollo;
import trach.yoni.olympiangods.characters.Zeus;
import trach.yoni.olympiangods.characters.Ares;
import trach.yoni.olympiangods.characters.Hermes;
import trach.yoni.olympiangods.characters.Poseidon;
import trach.yoni.olympiangods.characters.Cronos;
import trach.yoni.olympiangods.fragments.HomeScreenFragment;

public class MainActivity extends AppCompatActivity
{

    // CONSTANTS

    /**
     * the duration of any animtaion
     */
    public static final long ANIM_TIME_MS = 2500;


    // FIELDS

    // ...ITEMS
    public final static GenericItem[] allItems = {
            new CommonSafe(10),
            new UncommonSafe(20),
            new RareSafe(40),
            new MythicSafe(100),
            new LegendarySafe(200),
            new HealthPotion(50)
    };

    // ...PLAYERS

    /**
     * list of the user's current characters from which he can pick when entering a fight
     */
    public static final ArrayList<GameCharacter> allUserChars = new ArrayList<>();
    /**
     * The selected {@link #numCharsInFight} characters from {@link #allUserChars}
     */
    public static final ArrayList<GameCharacter> currentPlayerChars = new ArrayList<>();

    /**
     * List of all the possible players that you can obtain
     * they all take a name and a image recource as params
     * some have other params though too
     */
    final static GameCharacter[] allCharacters = {
            new Cronos("Cronos", R.mipmap.ic_launcher, 20),
            new Zeus("Zeus", R.mipmap.zeus, 15),
            new Ares("Ares", R.mipmap.ares, 30),
            new Poseidon("Poseidon", R.mipmap.poseidon, 15),
            new Hercules("Hercules", R.mipmap.hercules, 30),
            new Hermes("Hermes", R.mipmap.hermes, 17),
            new Hephaestus("Hephaestus", R.mipmap.hephaestus, 25),
            new Dionysus("Dionysus", R.mipmap.dionysus, 15),
            new Opollo("Opollo", R.mipmap.opollo, 15),
            new Aphrodite("Aphrodite", R.mipmap.aphrodite, 10),
            new Hades("Hades", R.mipmap.hades, 20)
    };
    /**
     * The number of charcters you can bring into a fight.
     */
    public static int numCharsInFight = 3;
    /**
     * this stores the current enemies changing based of the level you are in
     */
    public static ArrayList<GameCharacter> currentEnemies = new ArrayList<>();
    /**
     * the number of enemies in a given fight
     */
    public static int numEnemiesInFight = 3;



    /**
     * a persistent reference to the HomeScreenFragment
     */
    public HomeScreenFragment myHomeScreen;
    public static AudioManager audioManager;

    /**
     * @param numCharacters
     * @return copy of numCharacters number of random characters from
     *          {@link #allCharacters} without repetition
     */
    public static ArrayList<GameCharacter> getCharacters(int numCharacters)
    {
        ArrayList<GameCharacter> result = new ArrayList<>();
        ArrayList<GameCharacter> copyAllPlayers = new ArrayList<>();
        for(int ii = 0; ii< allCharacters.length; ii++)
        {
            copyAllPlayers.add((GameCharacter) allCharacters[ii].clone());
        }

        Random rand = new Random();
        for(int ii = 0; ii<numCharacters; ii++)
        {
            int choice = rand.nextInt(copyAllPlayers.size());
            result.add(copyAllPlayers.get(choice)); //.copy());
            copyAllPlayers.remove(choice);

        }
        return result;
    }

    /**
     * @param numCharacters
     * @return copy of numCharacters number of random characters from
     *          {@link #allCharacters} without repetition with the given rarity
     * @param rarity the integer value of the color for the new person corresponding to their rarity level
     */
    public static ArrayList<GameCharacter> getRankedCharacters(int numCharacters, int rarity)
    {
        ArrayList<GameCharacter> result = new ArrayList<>();
        ArrayList<GameCharacter> copyAllPlayers = new ArrayList<>();
        for(int ii = 0; ii< allCharacters.length; ii++)
        {
            copyAllPlayers.add((GameCharacter) allCharacters[ii].clone());
        }

        Random rand = new Random();
        for(int ii = 0; ii<numCharacters; ii++)
        {
            int choice = rand.nextInt(copyAllPlayers.size());
            copyAllPlayers.get(choice).setMyCharColor(rarity);
            result.add(copyAllPlayers.get(choice)); //.copy());
            copyAllPlayers.remove(choice);

        }
        return result;
    }


    // METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);

        //sets up main fragment
        myHomeScreen = new HomeScreenFragment(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainerView, myHomeScreen);
        ft.commit();


        //Sets the user characters initially
        allUserChars.addAll(getCharacters(HomeScreenFragment.initailNumOfPlayers));

        Methods.makeDebugToast(Methods.DebugOption.LIFECYCLE, getApplicationContext(),"onCreate finished");

        SharedPreferences systemP = PreferenceManager.getDefaultSharedPreferences(this);
        //Methods.makeAlert(this,"Hi", "Found: "+sp.getString("Shalom",""));
        audioManager =
                (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Methods.makeDebugToast( Methods.DebugOption.LIFECYCLE, getApplicationContext(),"onResume finished");
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("Shalom","Abba");
        spe.apply();

        Methods.makeDebugToast( Methods.DebugOption.LIFECYCLE, getApplicationContext(),"onStop finished");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Methods.makeDebugToast( Methods.DebugOption.LIFECYCLE, getApplicationContext(),"onRestart finished");
    }

    @Override
    protected void onStart()
    {
        super.onStart();


        Methods.makeDebugToast( Methods.DebugOption.LIFECYCLE, getApplicationContext(), "onStart finished");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Methods.makeDebugToast( Methods.DebugOption.LIFECYCLE, getApplicationContext(),"onDestroy finished");
    }

    // PUBLIC

    /**
     * goes into the given fragment from the current one
     * @param nextFragment the new fragment to enter
     * @param name the name of the fragment that you are entering
     */
    public void navigateFragment(Fragment nextFragment, String name) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, nextFragment, name)
                .addToBackStack("fight")
                .commit();
    }

    // PRIVATE METHODS


}