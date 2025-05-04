package trach.yoni.olympiangods.characters;

/**
 * This includes Alcyoneus, Clytius, Damasen, Polybotes,
 * And the king of the giants Porphyrion
 */

public class Hercules extends GameCharacter
{
    /**
     * Constructor for Hercules with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Hercules(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);

    }

    /**
     * if the user has already gotten another Hercules then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Hercules
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
