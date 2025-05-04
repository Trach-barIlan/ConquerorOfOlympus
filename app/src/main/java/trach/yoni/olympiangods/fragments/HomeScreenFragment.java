package trach.yoni.olympiangods.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.aux.Player;
import trach.yoni.olympiangods.characters.GameCharacter;

public class HomeScreenFragment extends Fragment {

    // CONSTANTS


    /**
     * The number of {@link GameCharacter} options the user starts with
     */
    public static final int initailNumOfPlayers = 4;

    /**
     * constant number of xp to add when the xp button is pressed
     * *used for testing the game only not going to be in the real game
     */
    private static final int DEV_XP_TO_ADD = 51;

    /**
     * progress bar showing how close the user is to level up
     */
    private ProgressBar levelProgressBar;

    public int playerXp = 0;

    public static int[] neededXpForLevelUp = {
            100, 1000, 10000, 100000, 1000000, 1000000, 100000000
    };

    //FIELDS

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final MainActivity myActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MediaPlayer buttonPressSound;

    public HomeScreenFragment(MainActivity theActivity) {
        myActivity = theActivity;

    }


    // LIFECYCLE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    public void onStart() {
        super.onStart();
        _linkFightButton();
        MainActivity.currentPlayerChars.clear();
        if (myActivity.getSupportActionBar() != null) {
            myActivity.getSupportActionBar().show();
        }
        MediaPlayer homeScreenMusic = MediaPlayer.create(getContext(), R.raw.music);
        buttonPressSound = MediaPlayer.create(getContext(), R.raw.button_press_sound_effect);
        homeScreenMusic.start();
        levelProgressBar = myActivity.findViewById(R.id.levelProgress);
        //_addDeveloperXpButton();
    }

    /**
     * used only when trying to test the game and when you need to increase your xp fast
     */
    private void _addDeveloperXpButton() {
        LinearLayout Layout = myActivity.findViewById(R.id.layout);
        Button playerXp = new Button(getContext());
        playerXp.setText("Add " + DEV_XP_TO_ADD + " xp");
        playerXp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.playerExperience += DEV_XP_TO_ADD;
                updateStatsText();
            }
        });
        Layout.addView(playerXp);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatsText();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.navigation_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGods:
                myActivity.navigateFragment(new UserGodsFragment(myActivity), "User Gods");
                break;
            case R.id.menuAboutGame:
                Methods.makeAlert(getContext(), "About Game Coming Soon...", "About game coming soon...");
                break;
            case R.id.menuStats:
                Methods.makeAlert(getContext(), "Stats Coming Soon...", "Stats coming soon...");
                break;
            case R.id.menuShop:
                myActivity.navigateFragment(new ShopFragment(myActivity), "Shop");
                break;
            case R.id.menuSettings:
                myActivity.navigateFragment(new SettingsFragment(myActivity), "Settings");
        }
        return super.onOptionsItemSelected(item);
    }

    // Other methods
    /**
     * chooses {@link MainActivity#numEnemiesInFight} random characters and outs it in the
     * {@link MainActivity#currentEnemies} arrayList
     */
    private void _pickEnemies()
    {
        MainActivity.currentEnemies.clear();
        MainActivity.currentEnemies.addAll(MainActivity.getCharacters(MainActivity.numEnemiesInFight));
    }


    /**
     * Make an Alert Dialog that allows the user to pick three characters from 4 randomly
     * given character at the start of the gmae.
     */
    private void _showPickAPlayer()
    {
        // Set up an alert to ask the user to pick a player
        AlertDialog.Builder pickPlayer = new AlertDialog.Builder(getContext());
        pickPlayer.setTitle("Pick " + MainActivity.numCharsInFight + " players:");
        String[] names = new String[MainActivity.allUserChars.size()];

        // Build up the array of names
        int index = 0;
        for (GameCharacter player : MainActivity.allUserChars)
        {
            names[index] = player.getPlayerName();
            index++;
        }

        final boolean[] checkedItems = new boolean[MainActivity.allUserChars.size()];
        pickPlayer.setMultiChoiceItems(
                names,
                checkedItems,
                new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int whichIndex, boolean isChecked)
                    {
                        buttonPressSound.start();
                        checkedItems[whichIndex] = isChecked;
                        int count = 0;
                        // Counts the number of checked boxes
                        count = 0;
                        for (int ii = 0; ii < checkedItems.length; ii++)
                        {
                            if (checkedItems[ii])
                            {
                                count++;
                            }
                        }

                        if (count > MainActivity.numCharsInFight)
                        {
                            Methods.makeAlert(getContext(), "Note", "You have selected " +
                                    "too many characters only the first " + MainActivity.numCharsInFight +
                                    " will be selected");
                        }

                    }
                });
        pickPlayer.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which)
                    {
                        buttonPressSound.start();
                            int count = 0;
                            int otherCount = 0;
                        for (int ii = 0;
                             ii < checkedItems.length && count < MainActivity.numCharsInFight;
                             ii++
                        ) {
                            if (checkedItems[ii]) {
                                otherCount++;
                            }
                        }
                        if(otherCount >= MainActivity.numCharsInFight) {
                            /**
                             * picks the first {@link numCharsInFight} characters from what
                             * the user selected
                             */
                            for (int ii = 0;
                                 ii < checkedItems.length && count < MainActivity.numCharsInFight;
                                 ii++
                            ) {
                                if (checkedItems[ii]) {
                                    MainActivity.currentPlayerChars.add(MainActivity.allUserChars.get(ii));
                                    Methods.makeDebugToast(Methods.DebugOption.HOME_SCREEN, getContext(), MainActivity.allUserChars.get(ii).getPlayerName());
                                    count++;
                                }
                            }

                            /**
                             * Starts the fight fragment
                             */
                            myActivity.navigateFragment(new FightFragment(myActivity), "Fight");
                        }
                        else {
                            Methods.makeAlert(getContext(),
                                    "Select More Gods",
                                    "You have only selected " + otherCount + " gods, please select" +
                                            (MainActivity.numCharsInFight-otherCount) + "more gods");
                        }
                    }
                }
        );
        pickPlayer.show();
    }

    /**
     * updates the pregress bar of the player level wheich shows how close the user is to leveling up
     */
    public void updateStatsText(){
        TextView statsView = getActivity().findViewById(R.id.StatsView);
        String statsText = "\n " + Player.playerLevel + "\n";
        if(Player.playerExperience >= neededXpForLevelUp[Player.playerLevel - 1]) {
            Player.playerExperience -= neededXpForLevelUp[Player.playerLevel - 1];
            levelProgressBar.setMax(neededXpForLevelUp[Player.playerLevel]);
            Player.playerLevel++;
            updateStatsText();
        }
        levelProgressBar.setProgress(Player.playerExperience);
        statsView.setText(statsText);
    }
    /**
     * makes a button that when you click it starts the alert dialog prompting
     * the user to select {@link MainActivity#numCharsInFight} characters out of
     * the {@link MainActivity#allUserChars} array
     */
    private void _linkFightButton()
    {
        Button startFightButton = getActivity().findViewById(R.id.startFightButton);
        startFightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressSound.start();
                _showPickAPlayer();
                _pickEnemies();
            }
        });
    }
}