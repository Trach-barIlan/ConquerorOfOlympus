package trach.yoni.olympiangods.attacks;

import java.util.Objects;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;
import trach.yoni.olympiangods.fragments.FightFragment;

public class EarthSmash extends GenericAttack {

    /**
     * multiplier of the earthSmash attack damage to ballance out the ability
     */
    private static final float SMASH_MULTIPLIER = 0.75f;

    /**
     * sets the bonus multiplier that this attack applies to the next hit on that defender
     */
    final float myAttackBonus = 2.0f;

    /**
     * Sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     * then the super will take the background resourse of the characters attack animation
     * @param standardDamage how much damage the attack will deal
     */
    public EarthSmash(float standardDamage) {
        super("EarthSmash",
                35,
                R.mipmap.earth_effect,
                true);
    }


    /**
     * attacks the given defender with a EarthSmash that leaves the defender vulnerable
     * so the next attack done on them does {@link #myAttackBonus} times the damage
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the defender exists and it continues
     *          false if there is no selected defender
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender){
        if(Objects.nonNull(defender) && Objects.nonNull(attacker)) {
            defender.dealtAffectedDamage(attacker, defender, getDamage(attacker));
            defender.resetVulnerability();
            attacker.resetWeakness();
            defender.setVulnerability(myAttackBonus);
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
     * gets the description of an Earth Smash attack to be displayed when the user
     * long clicks the attack button
     * @param attacker
     * @return
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "Creates an earthquake dealing " + getDamage(attacker) +
                " damage and doubling the damage of the next attack against them";
    }

    /**
     * @param attacker
     * @return the float damage that the attack will deal before any modification to their attack
     * i.e. weakness effect
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage()*SMASH_MULTIPLIER;
    }

    /**
     * animates the character using the GenericAttack default animation
     * (move to the defender and then back)
     * @param character attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore what runnable to do before the animation starts
     * @param doAfter runnable to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter character, CharDisplay defenderDisplay, long animDelayMs, long animTimeMs, Runnable doBefore, Runnable doAfter) {
        super.animateChars(character, defenderDisplay, animDelayMs, animTimeMs, null, doAfter);
    }
}