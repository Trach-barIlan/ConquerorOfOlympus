package trach.yoni.olympiangods.Items;

import android.graphics.Color;
import android.util.Log;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.characters.GameCharacter;

public class UncommonSafe extends CommonSafe {
    /**
     * Gives the user a random person and then makes his stats equal to a common person
     * @param theCost
     */

    public UncommonSafe(int theCost) {
        super("Uncommon Safe", theCost, R.mipmap.uncommon_safe);
    }

    @Override
    public GameCharacter buy() {
        GameCharacter newCharacter = MainActivity.getRankedCharacters(
                1,
                Color.GRAY).get(0);
        newCharacter.setTotalHealth(newCharacter.getTotalHealth() * 6);
        newCharacter.setBasicDamage((float) (newCharacter.getBasicDamage()*2));
        newCharacter.setPlayerName(newCharacter.getPlayerName() + " - Uncommon");
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
