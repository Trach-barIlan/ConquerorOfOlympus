package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.LoveSpell;

public class Aphrodite extends GameCharacter
{
    /**
     * Constructor for Aphrodite with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Aphrodite(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);
        myAttacks.add(new LoveSpell(basicDamage));
    }

    /**
     * if the user has already gotton another Aphrodite then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Aphrodite
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
