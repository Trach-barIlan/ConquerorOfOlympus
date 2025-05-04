package trach.yoni.olympiangods.attacks;

import java.util.Objects;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;
import trach.yoni.olympiangods.fragments.FightFragment;

public class LoveSpell extends GenericAttack {

    /**
     * multiplier of the earthSmash attack damage to balance it out
     */
    private static final float SMASH_MULTIPLIER = 0.75f;

    /**
     * sets the bonus multiplier that this attack applies onto the next hit onto the defender
     */
    final float myAttackBonus = 2.0f;

    /**
     * gets the standard damage to deal
     * then sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     * @param standardDamage how much damage the attack will deal
     */
    public LoveSpell(float standardDamage) {
        super("Love Spell",
                35,
                R.mipmap.health_potion,
                true);
    }


    /**
     * attacks the given defender with a Love Potion that will make their next attack
     * deal some damage back to themself
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the defender exists and it continues
     *          false if there is no selected defender
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender){
        if(Objects.nonNull(defender) && Objects.nonNull(attacker)) {
            defender.dealtAffectedDamage(attacker, defender, attacker.getBasicDamage());
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
     * @param attacker the attacker
     * @return the description on a Love Spell attack
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "Puts the defender under love spell that makes the defenders next attack attack themself ";
    }

    /**
     * @param attacker the attacker
     * @return the raw damage this attack will deal before any changes
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage()*SMASH_MULTIPLIER;
    }

    /**
     * Animates the given attacker with the standard animation
     * @param character attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore
     * @param doAfter things to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter character, CharDisplay defenderDisplay, long animDelayMs, long animTimeMs, Runnable doBefore, Runnable doAfter) {
        super.animateChars(character, defenderDisplay, animDelayMs, animTimeMs, null, doAfter);
    }
}