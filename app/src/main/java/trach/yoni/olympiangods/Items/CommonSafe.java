package trach.yoni.olympiangods.Items;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.characters.GameCharacter;

public class CommonSafe extends GenericItem {

    /**
     * Gives the user a random person and then makes his stats equal to a common person
     * @param theCost
     */

    public CommonSafe(int theCost) {
        super("Common Safe", theCost, R.mipmap.common_safe);
    }

    public CommonSafe(String theName, int theCost, int theImageResouce){
        super(theName, theCost, theImageResouce);
    }

    /**
     * the specific thing that will happen when you buy any generic item
     */
    @Override
    public GameCharacter buy() {
        GameCharacter newCharacter = MainActivity.getRankedCharacters(
                1,
                Color.argb(0,255,255,255)).get(0);
        newCharacter.setTotalHealth(newCharacter.getTotalHealth());
        newCharacter.setBasicDamage((float) (newCharacter.getBasicDamage()));
        newCharacter.setPlayerName(newCharacter.getPlayerName() + " - Common");
        for(GameCharacter myCharacter : MainActivity.allUserChars) {
            if (newCharacter.sameID_Q(myCharacter)) {
                isDuplicate = true;
                myCharacter.unlockSuperAbility();
                Log.i("BOUGHT", "You unlocked super ability of " + myCharacter.getPlayerName());
                return myCharacter;
            }
            else {
                isDuplicate = false;
            }
        }
        MainActivity.allUserChars.add(newCharacter);
        Log.i("BOUGHT","You bought a rare " + newCharacter.getPlayerName());
        return newCharacter;
    }

    /**
     * @return gets a random character using {@link MainActivity#getCharacters(int)};
     *      null if the user already had the character and it unlocks the super ability instead
     */
    protected GameCharacter getNewRandomChar(){
        GameCharacter newCharacter = MainActivity.getCharacters(1).get(0);
        for(GameCharacter myCharacter : MainActivity.allUserChars) {
            if (newCharacter.sameID_Q(myCharacter)) {
                myCharacter.unlockSuperAbility();
                Log.i("BOUGHT", "You unlocked super ability of " + myCharacter.getPlayerName());
                return null;
            }
        }
        return newCharacter;
    }

    /**
     * @return true if the user can buy this item
     *      false otherwise
     */
    @Override
    public boolean canBuy() {
        return true;
    }
}
