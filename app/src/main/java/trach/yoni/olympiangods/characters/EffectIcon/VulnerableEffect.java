package trach.yoni.olympiangods.characters.EffectIcon;

import trach.yoni.olympiangods.R;

/**
 * constructor for the vulnerable effect including the background resource
 */
public class VulnerableEffect extends GenericEffect {
    public VulnerableEffect(){
        super(
                "Vulnerable",
                "This character is vulnerable to the next attack.",
                R.mipmap.vulnerable_icon);
    }
}
