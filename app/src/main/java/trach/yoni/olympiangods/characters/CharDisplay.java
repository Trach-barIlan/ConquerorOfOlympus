package trach.yoni.olympiangods.characters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.EffectIcon.GenericEffect;

public class CharDisplay extends LinearLayout
{
    /**
     * int value for a neutral brightness character
     */
    final float BUTTON_UNSET_ALPHA = 1;
    /**
     * int value for a highlighted character
     */
    final float BUTTON_SET_ALPHA = 0.5F;
    /**
     * character image button
     */
    private Button myButton;
    /**
     * layout for all the effect to go in
     */
    private LinearLayout myEffectsLayout;
    /**
     * textView for the mid-fight information
     */
    private TextView myNameTextView;
    /**
     * textView showing the xp of the character
     */
    private final TextView myXpTextView;
    /**
     * Progress bar representing how much health the character has left
     */
    private ProgressBar myHealth;

    /**
     * remainig percent health on the charatcer from 0 to 100.
     * 100 is full health
     * 0 is dead
     */
    private float percentHealth = 100;

    /**
     * the tint color of the current background
     * Ex. Color.RED
     */
    private int myColor;
    /**
     * the background resource id of the current character
     */
    private int myCharImageRes;
    /**
     * the base layout of the character
     */
    private ViewGroup myBaseLayout;

    /**
     * creates the who layout of the character with the character, effects and the image and stats
     * @param context the context
     * @param theBaseLayout the base layout to put the character in
     */
    public CharDisplay(Context context, ViewGroup theBaseLayout)
    {
        super(context);

        myBaseLayout = theBaseLayout;

        //the overall layout for the display of a character
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        setOrientation(HORIZONTAL);
        setGravity(TEXT_ALIGNMENT_CENTER);

        myEffectsLayout = new LinearLayout(context);
        myEffectsLayout.setOrientation(VERTICAL);
        myEffectsLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // the space for all the character stats
        myXpTextView = new TextView(context);
        addView(_layoutImageAndStats(context));

        //puts all the views together into the layout
        addView(myEffectsLayout);
    }

    /**
     * @param context the context
     * @return the layout that includes the character button, text, and the xp
     */
    private LinearLayout _layoutImageAndStats(Context context) {
        LinearLayout fullImageLayout = new LinearLayout(context);
        fullImageLayout.setOrientation(VERTICAL);
        LinearLayout textLayout = new LinearLayout(context);
        textLayout.setOrientation(HORIZONTAL);
        myButton = new Button(context); // character image
        myButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        myNameTextView = new TextView(context);
        myHealth = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        textLayout.addView(myNameTextView);
        textLayout.addView(myXpTextView);

        fullImageLayout.addView(myButton);
        fullImageLayout.addView(textLayout);
        fullImageLayout.addView(myHealth);

        return fullImageLayout;
    }

    public Button getButton(){
        return myButton;
    }

    public ProgressBar getHealth(){
        return myHealth;
    }

    public ViewGroup getMyBaseLayout(){
        return myBaseLayout;
    }

    /**
     * sets the brightness of the current button to the neutral alpha
     */
    public void unHighlight()
    {
        myButton.setAlpha(BUTTON_UNSET_ALPHA);
    }

    /**
     * sets the brightness to {@link #BUTTON_SET_ALPHA}, the standard brightness corresponding to
     * a selected character
     */
    public void highlight(){
        myButton.setAlpha(BUTTON_SET_ALPHA);
    }

    public void setName(String name) {
        myNameTextView.setText(name+" ");
    }

    /**
     * sets the percent health to the given parameter thePercentHealth
     * @param thePercentHealth float percent health
     */
    public void setPercentHealth(float thePercentHealth) {
        percentHealth = thePercentHealth;
        myHealth.setProgress((int) percentHealth);
    }

    /**
     * draws all the effects in the given arrayList of Generic Effects
     * @param theEffects arrayList of all the effects to draw
     */
    public void setEffects(ArrayList<GenericEffect> theEffects) {
        for (GenericEffect theEffect : theEffects) {
            Button effectButton = new Button(getContext());
            effectButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            effectButton.setBackgroundResource(theEffect.resourceId);
            effectButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.makeAlert(getContext(),
                            "Effect: " + theEffect.name,
                            theEffect.description);
                }
            });
            myEffectsLayout.addView(effectButton);
        }
    }

    /**
     * erases all drawn effect
     */
    public void removeEffects(){
        myEffectsLayout.removeAllViews();
    }

    public void setXp(int theXp) {
        myXpTextView.setText("Xp: " + theXp);
    }

    public void resetHealthProgressBar(){
        myHealth.setProgress(100);
    }

    public void setColor(int theColor){
        myColor = theColor;
    }

    public void setButtonBackgroundRes(int charImageRes) {
        myCharImageRes = charImageRes;
        myButton.setBackgroundResource(myCharImageRes);
        myButton.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
        myButton.setBackgroundTintList(ColorStateList.valueOf(myColor));
    }

    public int getButtonBackgroundRes(){
        return myCharImageRes;
    }

    public void setButtonOnClickListener(OnClickListener onClickListener) {
        myButton.setOnClickListener(onClickListener);
    }
}
