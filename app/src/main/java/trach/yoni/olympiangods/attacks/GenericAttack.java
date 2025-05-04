package trach.yoni.olympiangods.attacks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Objects;

import trach.yoni.olympiangods.aux.Classes;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

/**
 * generic attack for a character. Each attack has a:
 * name, description, and an initial attack upgrade cost
 */
public abstract class GenericAttack implements Serializable
{

    static protected final int UNUSED_RES = -1;
    /**
     * the name of the attack
     */
    public final String name;
    /**
     * how much the attack costs to upgrade
     */
    public int attackUpgradeCost;
    /**
     * the background resource of the object being animated
     */
    public int myAttackPictRes;
    /**
     * boolean value expressing whether or not this attack requires a defender
     */
    public boolean requiresDefender;

    /**
     * A generic attack
     * @param theName the name of the attack
     * @param theAttackUpgradeCost the cost to do the first upgrade of this attack
     * @param theAttackPictRes the background resource of the object being animated
     * @param requireDefender boolean value expressing whether or not this attack requires a defender
     */
    public GenericAttack(String theName, int theAttackUpgradeCost, int theAttackPictRes, boolean requireDefender){
        name = theName;
        attackUpgradeCost = theAttackUpgradeCost;
        myAttackPictRes = theAttackPictRes;
        requiresDefender = requireDefender;
    }

    /**
     * attacks the defender by the attacker (implemented in each individual attack)
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the attack completed properly
     *          false otherwise
     */
    public abstract boolean attack(GameCharacter attacker, GameCharacter defender);

    /**
     * converts the attack int number to a string value
     */
    @Override
    public String toString() {
        return "Attack: "+name;
    }

    /**
     * @return the description of this attack when made by the attacker
     */
    public abstract String getDescription(GameCharacter attacker);

    /**
     * @return the damage that this attack will deal before any modifications
     */
    protected abstract float getDamage(GameCharacter attacker);

    /**
     * animates the characters depending on the specified attack done
     * default: the {@link #myAttackPictRes} will go to the defender and then back
     * @param character attacker to animate
     * @param defenderDisplay defender display to animate
     * @param animDelayMs the time to delay before the animation starts, in milliseconds
     * @param animTimeMs time to animate for, in milliseconds
     * @param doAfter things to do after the animation finishes
     */
    public void animateChars(GameCharacter character, CharDisplay defenderDisplay, long animDelayMs,
                             long animTimeMs, Runnable doBefore, Runnable doAfter) {
        if(Objects.nonNull(defenderDisplay)) {
            Button theCdButton = character.getCharDisplay().getButton();
            Button otherCdButton = defenderDisplay.getButton();

            // ... start and end point
            Classes.ScreenPoint startPoint = _getLocationOnScreen(theCdButton);
            Classes.ScreenPoint endPoint = _getLocationOnScreen(otherCdButton);

            // ... set up image
            ImageView shadowCharImage = new ImageView(character.getCharDisplay().getContext());
            shadowCharImage.setBackgroundResource(myAttackPictRes);
            shadowCharImage.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
            shadowCharImage.setBackgroundTintList(ColorStateList.valueOf(character.getMyCharColor()));
            shadowCharImage.setScaleX(theCdButton.getScaleX());
            shadowCharImage.setScaleY(theCdButton.getScaleY());

            shadowCharImage.setTranslationX(startPoint.myX);
            shadowCharImage.setTranslationY(startPoint.myY);
            _moveImage(
                    character.getCharDisplay(),
                    shadowCharImage,
                    startPoint,
                    endPoint,
                    animDelayMs,
                    animTimeMs,
                    Objects.nonNull(doBefore) ? doBefore : null,
                    doAfter);
        }
    }

    /**
     * @return the screenPoint of the location of the given view on the screen
     */
    protected Classes.ScreenPoint _getLocationOnScreen(View theView) {
        int[] loc = new int[2];
        theView.getLocationOnScreen(loc);
        return new Classes.ScreenPoint(loc[0], loc[1]);
    }

    /**
     * move an image from the start point to the end point in the base layout
     * @param attackerDisplay the character display of the attacker
     * @param shadowCharImage the image to be moved
     * @param startPoint the point to start
     * @param endPoint the point to end
     * @param delayMs how long to delay the animation, in milliseconds
     * @param animTimeMs the time for the complete animation
     * @param afterMove what to do after moving the image
     */
    protected void  _moveImage(
            CharDisplay attackerDisplay,
            View shadowCharImage,
            Classes.ScreenPoint startPoint,
            Classes.ScreenPoint endPoint,
            long delayMs,
            long animTimeMs,
            Runnable startMove,
            Runnable afterMove) {

        //Animates the attack
        Animator moveForwardX = ObjectAnimator.ofFloat(shadowCharImage, "translationX", endPoint.myX);
        moveForwardX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (Objects.nonNull(startMove)) {
                    startMove.run();
                }
                attackerDisplay.getMyBaseLayout().addView(shadowCharImage);
                shadowCharImage.setTranslationX(startPoint.myX);
                shadowCharImage.setTranslationY(startPoint.myY);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(Objects.nonNull(afterMove)){
                    afterMove.run();
                }
            }
        });
        Animator moveForwardY = ObjectAnimator.ofFloat(shadowCharImage, "translationY", endPoint.myY);
        Animator reverseForwardX = ObjectAnimator.ofFloat(shadowCharImage, "translationX", startPoint.myX);
        Animator reverseForwardY = ObjectAnimator.ofFloat(shadowCharImage, "translationY", startPoint.myY);

        reverseForwardY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                attackerDisplay.getMyBaseLayout().removeView(shadowCharImage);
                shadowCharImage.setVisibility(View.INVISIBLE);
                attackerDisplay.getButton().setVisibility(View.VISIBLE);
            }
        });

        AnimatorSet setAnimForward = new AnimatorSet();
        AnimatorSet setAnimBack = new AnimatorSet();
        AnimatorSet setAnim = new AnimatorSet();
        setAnimForward.playTogether(moveForwardX, moveForwardY);
        setAnimBack.playTogether(reverseForwardX, reverseForwardY);
        setAnim.playSequentially(
                setAnimForward.setDuration(animTimeMs / 2),
                setAnimBack.setDuration(animTimeMs / 2));
        setAnim.setStartDelay(delayMs);
        setAnim.start();
    }

}
