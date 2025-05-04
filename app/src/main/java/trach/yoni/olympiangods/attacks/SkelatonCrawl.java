package trach.yoni.olympiangods.attacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Log;

import java.util.Objects;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

public class SkelatonCrawl extends GenericAttack {

    private int protectedHealth;

    /**
     * gets the standard damage to deal
     * then sets the name of the attack and the amount of xp the first upgrade
     * for that attack will cost
     * @param myProtectedHealth how much health the skelaton shield can protect
     */
    public SkelatonCrawl(int myProtectedHealth) {
        super("Skelaton Crawl",
                30,
                R.mipmap.hades,
                false);
        protectedHealth = myProtectedHealth;
    }


    /**
     * attacks with a Skelaton Crawl that will give the attacker {@link #protectedHealth} shield
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the defender exists and it continues
     *          false if there is no selected defender
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender) {
        if(Objects.nonNull(attacker)) {
            attacker.addShield(protectedHealth);
            return true;
        }
        else {
            return false;

        }
    }

    /**
     * @param attacker the attacker
     * @return the description of the SkelatonCrawl attack
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return "This attack spawns in an army of skelotons to block out " +
                protectedHealth + "health";
    }

    /**
     * @param attacker
     * @return the damage that the Skelotons can shield
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return attacker.getBasicDamage();
    }

    /**
     * animates the Skelaton Crawl with the standard animation
     * @param attacker attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doBefore
     * @param doAfter things to do after the animation finishes
     */
    @Override
    public void animateChars(GameCharacter attacker, CharDisplay defenderDisplay,
                             long animDelayMs, long animTimeMs, Runnable doBefore,
                             Runnable doAfter) {
        final int SHAKE_RADIUS = 15;
        final int SHAKE_COUNT = 20;

        ObjectAnimator animX = ObjectAnimator.ofFloat(attacker.getCharDisplay(),
                "translationY",
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
