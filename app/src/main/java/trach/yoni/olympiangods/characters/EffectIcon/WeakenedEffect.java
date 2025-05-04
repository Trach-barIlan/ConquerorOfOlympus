package trach.yoni.olympiangods.characters.EffectIcon;

import trach.yoni.olympiangods.R;

/**
 * constructor for the vulnerable effect including the background resource
 */
public class WeakenedEffect extends GenericEffect {
    public WeakenedEffect(){
        super(
                "Weakness",
                "This character's attack is weakened. Their next attack will deal less damage.",
                R.mipmap.weakened_icon);
    }
}