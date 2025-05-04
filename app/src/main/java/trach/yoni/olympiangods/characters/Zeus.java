package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.WindGust;

/**
 * This includes Aeolus, Boreas, Aether, Eurus, Notus, Uranus, Zephyrus, Zeus
 */
public class Zeus extends GameCharacter {

    // METHODS
    /**
     * Constructor for Zeus with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageResource background resource id of the character
     * @param basicDamage basic damage of the character
     */
        public Zeus(String theName, int imageResource, float basicDamage) {
            super(theName, imageResource, 10, 100, basicDamage);
            myAttacks.add(new WindGust((float) (basicDamage * 0.8)));
        }
    /**
     * if the user has already gotten another Zeus then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Zeus
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
