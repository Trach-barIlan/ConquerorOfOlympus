package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.SkelatonCrawl;

public class Hades extends GameCharacter
{
    /**
     * Constructor for Hades with the background resource id, total health, basic damage, name
     * and then another attack added, SkelatonCrawl
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Hades(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);
        myAttacks.add(new SkelatonCrawl((int) (basicDamage)));

    }

    /**
     * if the user has already gotten another Hades then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Hades
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
