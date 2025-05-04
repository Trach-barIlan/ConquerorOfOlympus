package trach.yoni.olympiangods.attacks;

import java.util.Objects;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

public class WindGust extends GenericAttack {

    /**
     * multiplicative damage effect that wind does to balance it out
     */
    private static final float WIND_DAMAGE_REDUCTION = 0.75f;
    /**
     * the attack penalty that the wind gust applies onto the defender
     */
    final float myAttackPenalty = 0.5f;

    /**
     * gets the standard damage to deal
     * then sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     * @param standardDamage how much damage the attack will deal
     */
    public WindGust(float standardDamage) {
        super("Wind Gust",
                30,
                R.mipmap.wind_effect,
                true);
    }


    /**
     * attacks the given defender with a WindGust that leaves the defender shocked
     * so they do half the damage but this is at the cost of a smaller damage attack
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the defender exists and it continues
     *          false if there is no selected defender
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender) {
        if(Objects.nonNull(defender) && Objects.nonNull(attacker)) {
            defender.dealtAffectedDamage(attacker, defender, attacker.getBasicDamage()*WIND_DAMAGE_REDUCTION);
            defender.resetVulnerability();
            attacker.resetWeakness();
            defender.setWeakness(myAttackPenalty);
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
     * @return the description of a WindGust attack
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "This attack blows down the enemy leaving them partially stunned afterward: "
                + "\n Damage: " + getDamage(attacker)
                + "\n Special ability: the defender deals half the normal damage on their next hit";
    }

    /**
     * @param attacker the attacker
     * @return the damage that the attack will do after the windgust attack reduction
     * but before other reduction
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage()*WIND_DAMAGE_REDUCTION;
    }

    /**
     * animates the WindGust attack with the correct picture but generic animation
     * @param character attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore
     * @param doAfter things to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter character, CharDisplay defenderDisplay,
                             long animDelayMs, long animTimeMs, Runnable doBefore,
                             Runnable doAfter) {
        super.animateChars(character, defenderDisplay, animDelayMs, animTimeMs, null, doAfter);
    }
}
