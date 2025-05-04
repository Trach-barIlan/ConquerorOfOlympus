package trach.yoni.olympiangods.attacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Log;

import java.util.Objects;

import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

public class Regeneration extends GenericAttack {

    /**
     * the amount of health the character will regenerate
     */
    final float healthToRegen;

    /**
     * gets the amount to heal
     * then sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     *
     * @param standardRegen how much health the attack will heal
     */
    public Regeneration(float standardRegen) {
        super("Regeneration",
                25,
                UNUSED_RES,
                false);
        healthToRegen = (int) standardRegen;
    }


    /**
     * Regenerates the attacker {@link #healthToRegen} health
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     *                 this attack doesnt need a defender but can have one
     * @return true if attacker is not null
     * false if the attacker is null
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender) {
        if (Objects.nonNull(attacker)) {
            attacker.regenBasicHealth(attacker.getBasicDamage());
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param attacker
     * @return the description of the regeneration attack
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "Regenerate " + attacker.getBasicDamage() + " health";
    }

    /**
     * @param attacker
     * @return the amount of health that the attacker will heal before any changes
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage();
    }

    /**
     * animates the attacker by shaking them side to side as they regenerate the amount of health
     * @param attacker the attacker
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore what runnable to do before the attack
     * @param doAfter things to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter attacker, CharDisplay defenderDisplay,
                             long animDelayMs, long animTimeMs, Runnable doBefore,
                             Runnable doAfter) {
        final int SHAKE_RADIUS = 15;
        final int SHAKE_COUNT = 20;

        ObjectAnimator animX = ObjectAnimator.ofFloat(attacker.getCharDisplay(),
                "translationX",
                -SHAKE_RADIUS, 0, SHAKE_RADIUS,0);
        animX.setRepeatCount(SHAKE_COUNT);
        animX.setDuration(animTimeMs/SHAKE_COUNT);
        animX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                attacker.getCharDisplay().setTranslationX(0);
                if (Objects.nonNull(doAfter)) {
                    doAfter.run();
                } else {
                    Log.i("null", "do after is null");
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animX.setStartDelay(animDelayMs);
        animX.start();
    }

}
