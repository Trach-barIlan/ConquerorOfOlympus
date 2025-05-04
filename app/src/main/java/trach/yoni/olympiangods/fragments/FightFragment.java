package trach.yoni.olympiangods.fragments;

import static trach.yoni.olympiangods.MainActivity.ANIM_TIME_MS;
import static trach.yoni.olympiangods.MainActivity.currentEnemies;
import static trach.yoni.olympiangods.MainActivity.currentPlayerChars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;

import trach.yoni.olympiangods.Items.GenericItem;
import trach.yoni.olympiangods.Items.HealthPotion;
import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.attacks.GenericAttack;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.aux.Player;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FightFragment extends Fragment {

    // FIELDS
    CharDisplay[] userCharDisplays = new CharDisplay[MainActivity.numCharsInFight];
    CharDisplay[] enemyCharDisplays = new CharDisplay[MainActivity.numCharsInFight];

    private static Deque<FutureAttack> futureAttacks = new ArrayDeque<>();
    /**
     * the character the player is attacking
     */
    private GameCharacter defender = null;

    private static GameState myGameState = GameState.GAME_SETUP;

    private final MainActivity myָActivity;

    private static float myBiggestHit = 0;

    private static int myNumAttacks = 0;

    private static float myTotalDamage = 0;

    private static float damageRecieved = 0;

    // SUBCLASSES

    public final static class FutureAttack
    {
        public final GenericAttack myAttack;
        public final GameCharacter myAttacker;
        public final GameCharacter myDefender;

        public FutureAttack(GenericAttack theAttack, GameCharacter theAttacker, GameCharacter theDefender){
            myAttack = theAttack;
            myAttacker = theAttacker;
            myDefender = theDefender;
        }

    }

    /**
     * denotes the current state of the game
     */
    public static enum GameState {

        /**
         * game hasn't started yet
         */
        GAME_SETUP,

        /**
         * fight is currently taking place
         */
        GAME_ONGOING,

        /**
         * the fight is over and the player won
         */
        PLAYER_WON,

        /**
         * the fight is over and the enemy won
         */
        ENEMY_WON,

        /**
         * the user attempted an attack incorrectly
         */
        BAD_ATTACK
    }

    // METHODS

    // ... CONSTRUCTORS

    /**
     * sets up a {@link FightFragment}
     * @param theActivity the activity of the app
     */
    public FightFragment(MainActivity theActivity) {
        super(R.layout.fragment_fight);
        myָActivity = theActivity;
    }

    // ... LIFECYCLE
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (myָActivity.getSupportActionBar() != null) {
            myָActivity.getSupportActionBar().hide();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fight, container, false);
    }

    private void _linkSurrenderButton() {
        Button surrenderFightButton = myָActivity.findViewById(R.id.surrenderButton);
        surrenderFightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder Surrender = new AlertDialog.Builder(getContext());
                Surrender.setTitle("Surrender?");
                Surrender.setMessage("Are you sure you want to surrender the fight\n" +
                        "If you enter the fight again then your progress will be reset\n");
                Surrender.setPositiveButton("Surrender", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myָActivity.navigateFragment(new HomeScreenFragment(myָActivity), "Home Screen");
                    }
                });
                Surrender.show();
            }
        });
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fight_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        _linkSurrenderButton();
    }

    /**
     * on the creation of this fragment this will draw
     * {@link MainActivity#numCharsInFight} characters
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setGameState(GameState.GAME_ONGOING);
        futureAttacks.clear();

        //draws user's characters
        for(int ii = 0; ii < MainActivity.numCharsInFight; ii++) {
            _drawCharacter(ii, MainActivity.currentPlayerChars, R.id.CharacterImagesLayout, false);
        }
        //draws enemy characters
        for(int ii = 0; ii < MainActivity.numEnemiesInFight; ii++) {
            _drawCharacter(ii, currentEnemies, R.id.EnemyCharImages, true);
        }
    }

    // ... PUBLIC

    public static void setGameState (GameState newGameState) {
        myGameState = newGameState;
    }

    // ... PRIVATE
    /**
     * Creates a new function for drawing the character.
     * it sets the weight equal so you get equally spaced character images.
     * @param index the index in the {@link MainActivity#numCharsInFight} list
     *              of the character to draw
     */
    private void _drawCharacter(int index, ArrayList<GameCharacter> charList, int layoutRecourceId, boolean enemy) {
        GameCharacter theCharacter = charList.get(index);
        ViewGroup theBaseLayout = getView().findViewById(R.id.baseLayout);
        LinearLayout charImagesLayout = getView().findViewById(layoutRecourceId);
        CharDisplay myCharDisplay;
        if(enemy)
        {
            myCharDisplay = enemyCharDisplays[index] = new CharDisplay(getContext(), theBaseLayout);

        }
        else
        {
            myCharDisplay = userCharDisplays[index] = new CharDisplay(getContext(), theBaseLayout);
        }
        myCharDisplay.setEffects(theCharacter.myEffects);
        theCharacter.setCharDisplay(myCharDisplay);
        theCharacter.resetHealthBar();
        theCharacter.resetCurrentHealth();

        // Displays the characters attacks onClick on them
        myCharDisplay.setButtonOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    _clearAllButtons(enemy);
                    myCharDisplay.highlight();

                    if(enemy)
                    {
                       defender = theCharacter;
                    }
                    else
                    {
                        _setAttacks(theCharacter);

                    }

                    Methods.makeToast(getContext(), "it works");
                }
            });

        charImagesLayout.addView(myCharDisplay);

    }

    /**
     * makes all the buttons have the same alpha (brightness) so that later they will make one brighter
     */
    private void _clearAllButtons (boolean enemy) {

        CharDisplay[] buttonSet;
        if (enemy)
        {
            buttonSet = enemyCharDisplays;
        }
        else
        {
            buttonSet = userCharDisplays;
        }
        for(int ii = 0; ii < buttonSet.length; ii++)
        {
            buttonSet[ii].unHighlight();
        }
    }

    public static void addFutureAttack(GenericAttack myAttack, GameCharacter attacker, GameCharacter defender){
        futureAttacks.add(new FutureAttack(myAttack, attacker, defender));
    }

    /**
     * this removes all other attack buttons and then draws all of the characters attacks
     * then it will draw potions if the user has any magic items
     * @param theCharacter Attack button will be specific to the character selected
     */
    private void _setAttacks(GameCharacter theCharacter)
    {
        LinearLayout attackButtonsLayout = getView().findViewById(R.id.AttackButtonsLayout);
        attackButtonsLayout.removeAllViews();
        for(int ii = 0; ii < theCharacter.myAttacks.size(); ii++) {
            _drawAttackButton(theCharacter, attackButtonsLayout, ii);
        }
        _drawMagicItems(attackButtonsLayout, theCharacter);
    }

    private void _drawMagicItems(LinearLayout layout, GameCharacter myCharacter) {
        for (int i = 0; i < Player.userMagicItems.size(); i++) {
            if (Player.userMagicItems.get(i).getName() == "Health Potion"){
                ImageButton healthPotion = new ImageButton(getContext());
                healthPotion.setBackgroundResource(R.mipmap.health_potion);
                healthPotion.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                int finalI = i;
                healthPotion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myCharacter.regenBasicHealth(myCharacter.getTotalHealth()/4);
                        myCharacter.updateButtonPercentHealth();
                        healthPotion.setVisibility(View.GONE);
                        Player.userMagicItems.remove(finalI);
                    }
                });
                layout.addView(healthPotion);
            }
        }
    }

    public static void setMyBiggestHit(float newBiggestHit){
        myBiggestHit = newBiggestHit;
    }

    /**
     * Adds the given number to the myNumAttacks counter that will be displayed at the end of the fight
     * @param attacksToAdd
     */
    public static void addAttacks(int attacksToAdd){
        myNumAttacks += attacksToAdd;
    }

    /**
     * Adds the given number to the myTotalDamage counter that will be displayed at the end of the fight
     */
    public static void addDamage(float damageToAdd){
        myTotalDamage += damageToAdd;
    }

    /**
     * Adds the given number to the damageRecieved counter that will be displayed at the end of the fight
     */
    public static void addDamageRecieved(float damageToAdd){
        damageRecieved += damageToAdd;
    }

    public static float getMyBiggestHit(){
        return myBiggestHit;
    }

    /**
     * Draws the attack button of the given character
     * @param theCharacter whatever character that the user selected to use in the fight
     * @param ButtonsLayout where to put the characters specified attacks
     * @param attackIndex the index of the attack in theCharacter's{@link GameCharacter#myAttacks}
     */
    private void _drawAttackButton (GameCharacter theCharacter, LinearLayout ButtonsLayout, int attackIndex)
    {
        Button attackButton = new Button(getContext());
        GenericAttack attack = theCharacter.myAttacks.get(attackIndex);
        attackButton.setText(attack.name);
        attackButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Methods.makeAlert(getContext(), attack.name, attack.getDescription(theCharacter));
                return true;
            }
        });
        attackButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(Objects.nonNull(defender) || (!attack.requiresDefender)) {
                                                    Methods.makeToast(getContext(), "character " +
                                                            theCharacter.getPlayerName() +
                                                            "attacks " +
                                                            (Objects.nonNull(defender) ? defender.getPlayerName() : " no one ") +
                                                            "with " + attack.name);

                                                    // attack cycle:
                                                    // ... Player attacks
                                                    GameState attackResult = theCharacter.attack(defender, attackIndex, true);
                                                    if (attackResult == GameState.BAD_ATTACK) {
                                                        return;
                                                    }

                                                    if (
                                                            !_checkGameStatus(attackResult) || // status after player moves
                                                                    // ... enemy attacks
                                                                    !_checkGameStatus(_enemyTurn()) || // status after enemy moves
                                                                    // ... future (player) attacks
                                                                    !_checkGameStatus(_doFutureAttacks(theCharacter, defender)) // status after future moves
                                                    ) {
                                                        return;
                                                    }

                                                    // ... clear for next round
                                                    defender = null;
                                                    ButtonsLayout.removeAllViews();
                                                    _clearAllButtons(false);
                                                    _clearAllButtons(true);
                                                }
                                                else {
                                                    Methods.makeAlert(
                                                            getContext(),
                                                            "No Defender Selected",
                                                            "This attack requires a defender. Please select a defender and try again");
                                                }
                                            }
                                        });
        ButtonsLayout.addView(attackButton);
    }


    /**
     * goes through all the future attacks and does them one at a time
     * and then deletes them from the furture attacks ArrayList
     * @param attacker
     * @param defender
     * @return
     */
    private GameState _doFutureAttacks(GameCharacter attacker, GameCharacter defender) {
        int numFutureAttacks = futureAttacks.size();
        GameState theGS = GameState.GAME_ONGOING;
        for(int ii = 0; ii < numFutureAttacks; ii++){
            FutureAttack theFutureAttack = futureAttacks.pop();
            theGS = (theFutureAttack.myAttacker).attack(
                    theFutureAttack.myDefender,
                    theFutureAttack.myAttack,
                    _isUserCharacter(theFutureAttack.myAttacker)
            );
            if(!_checkGameStatus(theGS)){
                return theGS;
            }
        }
        return theGS;
    }

    /**
     * @param theAttacker
     * @return true if the attacker is in {@link MainActivity#currentPlayerChars}
     * false otherwise
     */
    private boolean _isUserCharacter(GameCharacter theAttacker) {
        for(GameCharacter theCharacter : currentPlayerChars){
            if(theCharacter.sameID_Q(theAttacker)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param theAttacker
     * @return true if the attacker is in {@link MainActivity#currentPlayerChars}
     * false otherwise
     */
    public static boolean isUserCharacter(GameCharacter theAttacker) {
        for(GameCharacter theCharacter : currentPlayerChars){
            if(theCharacter.sameID_Q(theAttacker)) {
                return true;
            }
        }
        return false;
    }


    /**
     * checks if the user has won or lost
     * @param theGameStatus the current status of the game
     * @return true if game is still going
     *          false if game is over
     */
    private boolean _checkGameStatus(GameState theGameStatus) {
        if (theGameStatus == GameState.PLAYER_WON) {
            // delay alert buntil animation is done - use a handler
            // with the looper because alerts cannot happen in a Timer context
            // see https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _playerWon();
                }
            }, ANIM_TIME_MS);
            return false;
        } else if (theGameStatus == GameState.ENEMY_WON) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    _enemyWon();
                }
            }, ANIM_TIME_MS);
            return false;
        }
        return true;
    }

    /**
     * since the computer won this will not increase the level but will still show the fight summary
     */
    private void _enemyWon() {
        _showFightSummary(false);
    }

    /**
     * Increases the player level and then shows the fight summary
     */
    private void _playerWon() {
        Methods.makeDebugToast(Methods.DebugOption.FIGHT_FRAGMENT, getContext(), "Game Over");
        Player.fightLevel++;
        _showFightSummary(true);
        myTotalDamage = 0;
        myBiggestHit = 0;
        myNumAttacks = 0;
        damageRecieved = 0;
    }

    /**
     * Displays the summary of the fight with the user stats on it
     * @param userWon if the user one this is true
     *                false otherwise
     */
    private void _showFightSummary(boolean userWon) {
        AlertDialog.Builder fightSummary = new AlertDialog.Builder(getContext());
        fightSummary.setTitle(userWon? "Congradulations You Won": "Better Luck Next Time");
        fightSummary.setMessage(
                        "Biggest Hit: " + myBiggestHit + "\n\n" +
                        "Number of Attacks: " + myNumAttacks + "\n\n" +
                        "Total Damage Dealt: " + myTotalDamage + "\n\n" +
                        "Damage Recieved: " + damageRecieved + "\n\n");
        fightSummary.setPositiveButton("Return Home", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myָActivity.navigateFragment(new HomeScreenFragment(myָActivity), "Home Screen");
            }
        });
        setMyBiggestHit(0);
        fightSummary.show();
    }


    /**
     * one Attack of the enemy on the user
     * it choses a random character and a random attack and then attacks a random one of the users characters
     * @return the current GameState of the game
     * i.e. good attack bad attack or game continuing
     */
    private GameState _enemyTurn() {
        Log.i("DEBUG","Current enemies: "+currentEnemies);
        Random rand = new Random();
        GameCharacter enemyAttacker = _getRandomElement(currentEnemies);
        int randAttackIndex = rand.nextInt(enemyAttacker.myAttacks.size());
        GameCharacter userDefender = _getRandomElement(currentPlayerChars);

        Methods.makeToast(getContext(),
                "enemy responds: " +
                        enemyAttacker.getPlayerName() + " attacks player " +
                        userDefender.getPlayerName() + " with attack " +
                        enemyAttacker.myAttacks.get(randAttackIndex).name
        );

        return enemyAttacker.attack(userDefender, randAttackIndex, false);
    }

    /**
     * @param theArrayList the list of all options to chose from
     * @param <BaseType> the base type of the arrayList
     * @return a random element from theArraylist
     */

    private <BaseType> BaseType _getRandomElement(ArrayList<BaseType> theArrayList)
    {
        Random rand = new Random();
        return theArrayList.get(
                rand.nextInt(theArrayList.size())
        );

    }
}
