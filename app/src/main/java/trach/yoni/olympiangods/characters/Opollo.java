package trach.yoni.olympiangods.characters;

/**
 * This includes Aristaeus, Attis, Cronus, Demeter, Dionysus, Pricus,
 */

public class Opollo extends GameCharacter
{
    /**
     * Constructor for Opollo with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param imageRecourceId background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Opollo(String theName, int imageRecourceId, float basicDamage) {
        super(theName, imageRecourceId, 10, 100, basicDamage);

    }

    /**
     * if the user has already gotten another Opollo then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Opollo
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
