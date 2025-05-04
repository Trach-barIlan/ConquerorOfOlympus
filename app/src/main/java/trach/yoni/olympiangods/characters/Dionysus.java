package trach.yoni.olympiangods.characters;

/**
 * This includes Asclepius,
 */

import trach.yoni.olympiangods.attacks.Regeneration;

public class Dionysus extends GameCharacter
{
    /**
     * Constructor for Dionysus with the background resource id, total health, basic damage, name
     * and then another attack added, Regeneration
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Dionysus(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);
        myAttacks.add(new Regeneration(basicDamage));

    }

    /**
     * if the user has already gotten another Dionysus then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Dionysus
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
