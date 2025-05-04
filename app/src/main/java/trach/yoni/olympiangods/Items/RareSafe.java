package trach.yoni.olympiangods.Items;

import android.graphics.Color;
import android.util.Log;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.characters.GameCharacter;

public class RareSafe extends CommonSafe {
    /**
     * Gives the user a random person and then makes his stats equal to a common person
     * @param theCost
     */

    public RareSafe(int theCost) {
        super("Rare Safe", theCost, R.mipmap.rare_safe);
    }

    @Override
    public GameCharacter buy() {
        GameCharacter newCharacter = MainActivity.getRankedCharacters(
                1,
                Color.argb(100, 255, 255, 0)).get(0);
        newCharacter.setTotalHealth(newCharacter.getTotalHealth() * 9);
        newCharacter.setBasicDamage((float) (newCharacter.getBasicDamage()*3));
        newCharacter.setPlayerName(newCharacter.getPlayerName() + " - Rare");
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

    @Override
    public boolean canBuy() {
        return true;
    }
}

