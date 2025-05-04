package trach.yoni.olympiangods.attacks;

import android.util.Log;

import java.util.Objects;

import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

public class Hit extends GenericAttack {


    /**
     * gets the standard damage to deal
     * then sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     */
    public Hit() {
        super("Hit",
                25,
                UNUSED_RES,
                true);
    }

    /**
     * deals standardDamage from the attacker to the defender
     * @param attacker the attacking character
     * @param defender the defending character
     * @return true if the defender is not null
     *          false if the defender is null
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender) {
        if(Objects.nonNull(defender) && Objects.nonNull(attacker)) {
            defender.dealtAffectedDamage(attacker, defender, getDamage(attacker));
            defender.resetVulnerability();
            attacker.resetWeakness();
            Log.i("ATTACK", attacker + " attacks " + defender);
            return true;
        }
        else {
            Methods.makeAlert(attacker.getContext(),
                    "No Defender",
                    "This attack requires you to select a defender. Please try again");
            return false;

        }
    }

    /**
     * stores the description of a hit attack
     * @param attacker
     * @return
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "Hit for " + getDamage(attacker) + " damage";
    }

    /**
     * @param attacker the attacker
     * @return the raw damage the hit will deal before modyfiers
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage();
    }

    /**
     * animates the character using the standard animation
     * @param character attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore
     * @param doAfter things to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter character, CharDisplay defenderDisplay, long animDelayMs,
                             long animTimeMs, Runnable doBefore, Runnable doAfter) {
        myAttackPictRes = character.getCharDisplay().getButtonBackgroundRes();
        super.animateChars(character, defenderDisplay, animDelayMs, animTimeMs, doBefore, doAfter);
    }
}
