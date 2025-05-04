package trach.yoni.olympiangods.characters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.aux.Player;
import trach.yoni.olympiangods.characters.EffectIcon.GenericEffect;
import trach.yoni.olympiangods.characters.EffectIcon.ShieldEffect;
import trach.yoni.olympiangods.characters.EffectIcon.VulnerableEffect;
import trach.yoni.olympiangods.characters.EffectIcon.WeakenedEffect;
import trach.yoni.olympiangods.fragments.FightFragment;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.attacks.GenericAttack;
import trach.yoni.olympiangods.attacks.Hit;
import trach.yoni.olympiangods.aux.Methods;

/**
 * One of the characters in the game.
 */
public abstract class GameCharacter implements Cloneable, Serializable {
    // CONSTANTS
    /**
     * the default xp given for killing a character
     */
    private static final int DEFAULT_XP_FOR_KILL = 10;

    /**
     * the value for {@link #vulnerability} that indicates that the character is not vulnerable
     */
    private static final float NO_VULNERABILITY = 1;

    // FIELDS

    private final int myID;

    private static int idCount=0;

    private float basicDamage;

    public int superAbilityLevel = 0;

    /**
     * Button associated with this character
     */
    protected CharDisplay myCharDisplay = null;

    /**
     * boolean value representing whether or not the character's super ability is unlocked
     */
    public boolean unlockedSuperAbility = false;

    /**
     * the name of the character
     */
    protected String name;

    /**
     * How much health the character has total between 0 to 1000 inclusive.
     * Higher number is healthier and 0 is dead.
     */
    protected int totalHealth;

    /**
     * Current health in battle.
     * between 0 and {@link #totalHealth}
     * 0 means dead
     */
    protected int currentHealth;

    /**
     * This shield can take some damage for the character that has the shield
     * 0 is that the character's shield cannot take any damage for them
     * X means that that the shield will take the next X damage dealt to the character
     */
    protected int shield;

    /**
     * The image of the character
     */
    protected int myImageRes;

    /**
     * the amount of money the user will get for killing this character
     */
    protected int myKillMoney;

    /**
     * a number from 0 to 1 that the next attack that the character does will be multiplied by
     * 1 is no penalty meaning the next attack will do full damage
     * 0 is no damage meaning the next attack deals no damage
     */
    protected float attackWeakness;

    /**
     * a number from 1 to 20 that the next attack the character does will be multiplied by
     * 1 is no bonus to the characters attack
     * 20 means the attack damage is multiplied by 20
     */
    protected float attackBonus;

    /**
     * how vulnerable a character is
     * default value is 1 and anything more than that will make someone
     * attacking a vulnerable player do that much multiplied damage
     * opposite for a vulnerability less than 1
     */
    private float vulnerability = 1;

    /**
     * list of all the attacks this player can do
     */
    public ArrayList<GenericAttack> myAttacks = new ArrayList<>();

    /**
     * the specific xp of this character which increases b killing enemies and can be used
     * to upgrade attacks
     */
    private int myXp = 0;

    /**
     * ArrayList of all the effects that this character has and they will be drawn later
     */
    public ArrayList<GenericEffect> myEffects = new ArrayList<>();

    /**
     * the background drawable used to draw the characters image
     */
    private ShapeDrawable myBackgroundDrawable;

    /**
     * the tint color of the background for the character
     * Ex. Color.RED
     */
    private int myCharColor = Color.TRANSPARENT;

    // NESTED CLASSES
    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat -1f);
        }
    }

    // METHODS


    @NonNull
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets a default for all the characteristics
     * @param characterName the name of the character
     * @param charImageRes  Resource ID of this character's image
     * @param theKillMoney the amount of money you get for killing this character
     */
    public GameCharacter(String characterName, int charImageRes, int theKillMoney,
                         int theTotalHealth, float theBasicDamage) {
        myID = idCount++;
        myImageRes = charImageRes;
        name = characterName;
        basicDamage = theBasicDamage;
        totalHealth = theTotalHealth;
        currentHealth = totalHealth;
        resetAttackPenalty();
        resetAttackBonus();
        shield = 0;
        myKillMoney = theKillMoney;
        myXp = 0;
        myAttacks.add(new Hit());
    }

    /**
     * same as {@link GameCharacter#GameCharacter(String, int, int, float)} but
     * uses the default image to draw the character
     *
     */
    public GameCharacter(String characterName,
                         int theKillMoney,
                         int theTotalHealth,
                         float theBasicDamage) {
        this(characterName, R.mipmap.ic_launcher, theKillMoney, theTotalHealth, theBasicDamage);
    }

    /**
     * deals damage to the character's current health
     *
     * @param damage how much raw damage to deal before other effects
     */
    public void dealtBasicDamage(GameCharacter attacker, float damage) {
        float multiplier = _computeDamageMultiplier(attacker, this);
        //if multiplier is equal to 1 then there is no advantage and so it tries to see
        //   if there is a dissadvantage of the defender to the attacker
        if(multiplier == 1){
            multiplier = 1/_computeDamageMultiplier(this, attacker);
        }
        if(damage <= shield){
            shield -= damage;
            damage = 0;
        }
        else {
            shield = 0;
            damage -= shield;
            int ii;
            for(ii = 0; ii < myEffects.size(); ii++){
                if(myEffects.get(ii) instanceof ShieldEffect){
                    break;
                }
            }
            if(ii<myEffects.size()){
                myEffects.remove(ii);
            }
            _redrawDisplayEffects();
        }
        currentHealth -= damage;
        if(!(FightFragment.isUserCharacter(attacker))){
            FightFragment.addDamageRecieved(damage);
        }
        else {
            FightFragment.addDamage(damage);
            FightFragment.addAttacks(1);
        }
        if(damage > FightFragment.getMyBiggestHit()){
            FightFragment.setMyBiggestHit(damage);
        }
        resetAttackPenalty();
        resetAttackBonus();
    }

    public void  dealtAffectedDamage(GameCharacter attacker, GameCharacter defender, float damage) {
        if(Objects.nonNull(defender)) {
            dealtBasicDamage(
                    attacker,
                    damage * attacker.getAttackWeakness() * attacker.getAttackBonus() * defender.getVulnerability()
            );
        }
        else {
            dealtBasicDamage(
                    attacker,
                    damage * attacker.getAttackWeakness() * attacker.getAttackBonus()
            );
        }
    }

    /**
     * gives a bonus to some gods if they are more powerful against the other one
     * @param attacker
     * @param defender
     * @return
     */
    private static final float _computeDamageMultiplier(GameCharacter attacker, GameCharacter defender) {
        if ((defender instanceof Cronos) && (attacker instanceof Hephaestus)) {
            return 1.5f;
        }
        if ((defender instanceof Hephaestus) && (attacker instanceof Poseidon)) {
            return 1.5f;
        }
        if ((defender instanceof Poseidon) && (attacker instanceof Hermes)) {
            return 1.5f;
        }
        if ((defender instanceof Hermes) && (attacker instanceof Dionysus)) {
            return 1.5f;
        }
        if ((defender instanceof Dionysus) && (attacker instanceof Opollo)) {
            return 1.5f;
        }
        if ((defender instanceof Opollo) && (attacker instanceof Ares)) {
            return 1.5f;
        }
        if ((defender instanceof Ares) && (attacker instanceof Aphrodite)) {
            return 1.5f;
        }
        if ((defender instanceof Aphrodite) && (attacker instanceof Zeus)) {
            return 1.5f;
        }
        if ((defender instanceof Zeus) && (attacker instanceof Hercules)) {
            return 1.5f;
        }
        if ((defender instanceof Hercules) && (attacker instanceof Cronos)) {
            return 1.5f;
        }
        return 1;
    }


    public void regenBasicHealth(float regen) {
        currentHealth += regen;
        if(currentHealth > totalHealth) {
            currentHealth = totalHealth;
        }
    }

    public void resetAttackPenalty(){
        attackWeakness = 1.0f;
    }

    public void resetAttackBonus(){
        attackBonus = 1.0f;
    }

    public void resetHealthBar(){
        myCharDisplay.resetHealthProgressBar();
    }

    public void resetCurrentHealth(){
        currentHealth = totalHealth;
    }





    public int getTotalHealth(){
        return totalHealth;
    }
    public float getAttackBonus() {
        return attackBonus;
    }
    public float getAttackWeakness(){
        return attackWeakness;
    }
    /**
     * @return the layout associated with the character
     */
    public CharDisplay getCharDisplay() {
        return myCharDisplay;
    }
    public String getPlayerName() {
        return name;
    }
    /**
     * @return the resource ID of this character's image
     */
    public int getCharImageRes() {
        return myImageRes;
    }
    public int getMyXp(){
        return myXp;
    }
    public float getBasicDamage(){
        return basicDamage;
    }

    public void setPlayerName(String newName){
        name = newName;
    }
    public void setBasicDamage(float newDamage){
        basicDamage = newDamage;
    }

    public void setMyXp(int newXp){myXp = newXp;}

    public void setWeakness(float newWeakness) {
        attackWeakness = newWeakness;
        if(newWeakness < 1) {
            myEffects.add(new WeakenedEffect());
            _redrawDisplayEffects();
        }
    }

    public void setAttackBonus(float newBonus) {
        attackBonus = newBonus;
    }
    public void setTotalHealth(int newTotalHealth){
        totalHealth = newTotalHealth;
    }
    public void setShield(int newShield){
        shield = newShield;
    }
    public void setMyCharColor(int newCharColor){
        myCharColor = newCharColor;
    }
    public int getMyCharColor(){
        return myCharColor;
    }
    public int getShield(){
        return shield;
    }
    public void addShield(int shieldToAdd){
        shield += shieldToAdd;
        if(shield > 0) {
            myEffects.add(new ShieldEffect(this));
        }
        _redrawDisplayEffects();
    }


    /**
     * sets the button associated with this character including setting the
     * background image and layout
     *
     * @param theCharDisplay the characterButton
     */
    public void setCharDisplay(CharDisplay theCharDisplay) {
        myCharDisplay = theCharDisplay;
        myCharDisplay.setColor(myCharColor);
        myCharDisplay.setButtonBackgroundRes(getCharImageRes());

        myCharDisplay.setName(getPlayerName());
        myCharDisplay.setXp(myXp);
        updateButtonPercentHealth();
    }

    /**
     * deals attack on another player
     * @param otherPlayer   the player that is being attacked
     * @param myAttackIndex index of the attack in the {@link #myAttacks} list
     * @return the current state of the game after the character was killed
     *         i.e., did someone win
     */
    public FightFragment.GameState attack(GameCharacter otherPlayer, int myAttackIndex, boolean userAttacks) {
        return attack(otherPlayer, myAttacks.get(myAttackIndex), userAttacks);
    }

    /**
     * deals attack on another player
     * @param otherPlayer   the player that is being attacked
     * @param myAttack the attack in the {@link #myAttacks} list
     * @return the current state of the game after the character was killed
     *         i.e., did someone win
     */
    public FightFragment.GameState attack(GameCharacter otherPlayer, GenericAttack myAttack, boolean userAttacks) {
        GameCharacter attacker = this;
        FightFragment.GameState result = FightFragment.GameState.GAME_ONGOING;

        myAttack.animateChars(
                attacker,
                Objects.isNull(otherPlayer) ? null : otherPlayer.getCharDisplay(),
                userAttacks ? 0 : MainActivity.ANIM_TIME_MS,
                MainActivity.ANIM_TIME_MS,
                null,
                null
        );

        if (Objects.nonNull(otherPlayer) || (!myAttack.requiresDefender)) {
            if (myAttack.attack(attacker, otherPlayer)) {

                Log.i("DEBUG", getPlayerName() + " attacks " + otherPlayer + " with " + myAttack.name + " ; userAttacks=" + userAttacks);

                //Update attacker
                if (currentHealth <= 0) {
                    result = _killCharacter(attacker, !userAttacks);
                }

                //update otherPlayer
                if (Objects.nonNull(otherPlayer)) {
                    if (otherPlayer.currentHealth <= 0) {
                        result= _killCharacter(otherPlayer, userAttacks);
                    }
                }
            } else {
                result = FightFragment.GameState.BAD_ATTACK;
            }
        }

        // delays the updating of health so that the health will be updated only after the
        // animation shows the attacker hitting the defender
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateButtonPercentHealth();
                if (Objects.nonNull(otherPlayer)) {
                    otherPlayer.updateButtonPercentHealth();
                }
            }
        }, MainActivity.ANIM_TIME_MS/2);

        return result;

//        /*if(myAttack.attack(this, otherPlayer)) {
//            FightFragment.GameState result = FightFragment.GameState.GAME_ONGOING;
//
//            Log.i("DEBUG",getPlayerName()+" attacks "+otherPlayer+" with "+ myAttack.name+" ; userAttacks="+userAttacks);
//
//            //Update attacker
//            if (currentHealth <= 0) {
//                result = _killCharacter(this, !userAttacks);
//            }
//
//            //update otherPlayer
//            if(Objects.nonNull(otherPlayer)) {
//                if (otherPlayer.currentHealth <= 0) {
//                    result = _killCharacter(otherPlayer, userAttacks);
//                }
//            }
//
//            return result;
//        }
//        else {
//            return FightFragment.GameState.BAD_ATTACK;
//        }*/

    // do the attack

    }

    /**
     * kills the given deadCharacter
     * @param deadCharacter the deadCharacter to kill
     * @param enemy true if the deadCharacter is an enemy
     *             false if it's the user
     * @return the current state of the game after the deadCharacter was killed
     *          i.e., did someone win
     */
    private FightFragment.GameState _killCharacter(GameCharacter deadCharacter, boolean enemy) {

        // waits until the animtion of the attack is over to make the character dissapear
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                deadCharacter.getCharDisplay().setVisibility(View.INVISIBLE);
            }
        }, MainActivity.ANIM_TIME_MS/2);

        if(enemy) {
            MainActivity.currentEnemies.remove(deadCharacter);
            Player.playerMoney += deadCharacter.myKillMoney;
            myXp += _computeKillXp(deadCharacter);
            Player.playerExperience += _computeKillXp(deadCharacter);
            myCharDisplay.setXp(myXp);
            Methods.makeDebugToast(Methods.DebugOption.GAME_CHARACTER, myCharDisplay.getContext(), "player experience is now " + Player.playerMoney);
        }
        else {
            MainActivity.currentPlayerChars.remove(deadCharacter);
        }

        if(MainActivity.currentEnemies.size() == 0) {
            deadCharacter.myEffects.clear();
            return FightFragment.GameState.PLAYER_WON;
        }
        else if (MainActivity.currentPlayerChars.size() == 0) {
            deadCharacter.myEffects.clear();
            return FightFragment.GameState.ENEMY_WON;
        }
        deadCharacter.myEffects.clear();
        return FightFragment.GameState.GAME_ONGOING;
    }

    private int _computeKillXp(GameCharacter deadCharacter) {
        int result = DEFAULT_XP_FOR_KILL;
        result += Player.fightLevel;
        return result;
    }

    public abstract void unlockSuperAbility();

    /**
     * updates the current percent health of the character as displayed in its button
     */
    public void updateButtonPercentHealth() {
        if (Objects.isNull(myCharDisplay)) {
            System.out.print("HI");
        }
        myCharDisplay.setPercentHealth( ((float) currentHealth / totalHealth) * 100);
    }

    /**
     *
     * @param myCharacter
     * @return true if myCharacter has the same ID and the same background color as this character
     * different rarities of characters have different background colors
     */
    public boolean sameID_Q(GameCharacter myCharacter) {
        return (myCharacter.myID == myID) && (myCharacter.myCharColor == myCharColor);
    }

    public Context getContext() {
        return myCharDisplay.getContext();
    }

    @Override
    public String toString() {
        return getPlayerName();
    }

    public void setVulnerability(float myAttackBonus) {
        vulnerability = myAttackBonus;
        if(myAttackBonus > NO_VULNERABILITY) {
            myEffects.add(new VulnerableEffect());
            _redrawDisplayEffects();
        }
        else{
            int ii;
            for(ii = 0; ii < myEffects.size(); ii++){
                if(myEffects.get(ii) instanceof VulnerableEffect){
                    break;
                }
            }
            if(ii<myEffects.size()){
                myEffects.remove(ii);
            }
            _redrawDisplayEffects();
        }
    }

    /**
     * redraws the display of the effects of this character
     */
    private void _redrawDisplayEffects() {
        myCharDisplay.removeEffects();
        myCharDisplay.setEffects(myEffects);
    }

    public float getVulnerability(){
        return vulnerability;
    }

    public void resetVulnerability(){
        setVulnerability(NO_VULNERABILITY);
    }
    public void resetWeakness(){
        attackWeakness = 1;
        int ii;
        for(ii = 0; ii < myEffects.size(); ii++){
            if(myEffects.get(ii) instanceof WeakenedEffect){
                break;
            }
        }
        if(ii<myEffects.size()){
            myEffects.remove(ii);
        }
        _redrawDisplayEffects();
    }
}