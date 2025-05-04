package trach.yoni.olympiangods.characters.EffectIcon;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.characters.GameCharacter;

/**
 * constructor for the shield effect including the background resource
 */
public class ShieldEffect extends GenericEffect {
    public ShieldEffect(GameCharacter myCharacter){
        super(
                "Shielded",
                "This character has an active shield which shileds the next " +
                myCharacter.getShield() + " damage",
                R.mipmap.shield);
    }
}