package trach.yoni.olympiangods.aux;

import java.util.ArrayList;

import trach.yoni.olympiangods.Items.GenericItem;

/**
 * all the player's information and stats
 */
public class Player {

    /**
     * fightLevel starts at 1 and every fight that the user wins this level will go up one
     * if the player loses then this just stays at the same level
     */
    public static int fightLevel = 1;
    /**
     * the level of the player will go up 1 level if the playerExperience is enough to level up
     */
    public static int playerLevel = 1;
    /**
     * the overall experience of the player
     * as this experience gets higher the user levels up but this does not reset
     * playerExperience keeps going up until the max level is reached
     * this is earned by defeating enemies
     */
    public static int playerExperience = 0;
    /**
     * after every time that an enemy character dies the player gets money
     */
    public static int playerMoney = 1000;

    /**
     * ArrayList of all of the user's Magic Items
     */
    public static ArrayList<GenericItem> userMagicItems = new ArrayList<>();

    /**
     * @return the amount of money the user has
     */
    public int getPlayerMoney(){
        return playerMoney;
    }
}
