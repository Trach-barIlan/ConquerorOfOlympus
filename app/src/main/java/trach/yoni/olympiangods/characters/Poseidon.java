package trach.yoni.olympiangods.characters;

import trach.yoni.olympiangods.attacks.GenericAttack;
import trach.yoni.olympiangods.attacks.Hit;
import trach.yoni.olympiangods.attacks.RecuringAttack;
import trach.yoni.olympiangods.fragments.FightFragment;

public class Poseidon extends GameCharacter
{
    /**
     * Constructor for Poseidon with the background resource id, total health, basic damage, name
     * and then another attack added, LoveSpell
     * @param theName the name of the character
     * @param ImageRes background resource id of the character
     * @param basicDamage basic damage of the character
     */
    public Poseidon(String theName, int ImageRes, float basicDamage)
    {
        super(theName, ImageRes, 10, 100, basicDamage);
    }


    /**
     * unlocks the super ability of the character
     * this one deals a bleed over time on the first hit of each fight over three attacks
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
