package trach.yoni.olympiangods.Items;

import android.graphics.Color;
import android.util.Log;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.characters.GameCharacter;

public class LegendarySafe extends GenericItem{
    /**
     * Gives the user a random person and then makes his stats equal to a common person
     * @param theCost
     */

    public LegendarySafe(int theCost) {
        super("Legendary Safe", theCost, R.mipmap.legendary_safe);
    }

    @Override
    public GameCharacter buy() {
        GameCharacter newCharacter = MainActivity.getRankedCharacters(
                1,
                Color.argb(100, 0, 255, 0)).get(0);
        newCharacter.setTotalHealth(newCharacter.getTotalHealth() * 15);
        newCharacter.setBasicDamage((float) (newCharacter.getBasicDamage()*5));
        newCharacter.setPlayerName(newCharacter.getPlayerName() + " - Legendary");
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
