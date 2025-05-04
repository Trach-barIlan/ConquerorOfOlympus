package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.GenericAttack;
import trach.yoni.olympiangods.attacks.Hit;
import trach.yoni.olympiangods.attacks.RecuringAttack;
import trach.yoni.olympiangods.attacks.Regeneration;

public class Hermes extends GameCharacter
{
    /**
     * Constructor for Hermes with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Hermes(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);
        myAttacks.add(new Regeneration(basicDamage));

    }

    /**
     * if the user has already gotten another Hermes then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Cronos
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
