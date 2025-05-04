package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.EarthSmash;

/**
 * This includes Cronos, Crios, Nereus, Oceanus, Pallas, Prometheus, Hyperion, Iapetus
 * Also: Koios, Krios,
 * And the father of them all Ouranos
 */
public class Cronos extends GameCharacter {
    /**
     * Constructor for Cronos with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageRecourseId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Cronos(String theName, int imageRecourseId, float basicDamage) {
        super(theName, imageRecourseId, 10, 150, basicDamage);
        myAttacks.add(new EarthSmash((float) (basicDamage*.75)));
    }
    /**
     * if the user has already gotten another Cronos then this will just level up the super ability
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