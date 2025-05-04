package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.Hit;
import trach.yoni.olympiangods.attacks.RecuringAttack;
import trach.yoni.olympiangods.fragments.FightFragment;

/**
 * This includes Ares, Artemis, Athena,
 */
public class Ares extends GameCharacter {
    /**
     * Constructor for Ares with the background resource id, total health, basic damage, name
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Ares(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);
    }

    /**
     * if the user has already gotten another Ares then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Ares
     */
    @Override
    public void unlockSuperAbility() {
        if (unlockedSuperAbility){
            superAbilityLevel++;

        }
        else {
            unlockedSuperAbility = true;
            superAbilityLevel = 1;
        }
    }
}
