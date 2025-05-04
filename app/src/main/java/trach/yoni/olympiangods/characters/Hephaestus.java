package trach.yoni.olympiangods.characters;

/**
 * Three fates that preexist the gods:
 * 1. Clotho, Who spins the thread of life.
 * 2. Lachesis, Measurer who determines how long life is.
 * 3. Atropos, Who cuts the thread of life with her shears.
 */


import trach.yoni.olympiangods.attacks.Hit;

public class Hephaestus extends GameCharacter
{
    /**
     * Constructor for Hephaestus with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param ImageRes background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Hephaestus(String theName, int ImageRes, float basicDamage)
    {
        super(theName, ImageRes, 10, 100, basicDamage);
    }
    /**
     * if the user has already gotten another Hephaestus then this will just level up the super ability
     * otherwise this will unlock a new ability specific to Hephaestus
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
