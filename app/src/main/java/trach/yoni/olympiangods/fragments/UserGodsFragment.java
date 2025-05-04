package trach.yoni.olympiangods.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.attacks.GenericAttack;
import trach.yoni.olympiangods.aux.Constants;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.characters.CharDisplay;
import trach.yoni.olympiangods.characters.GameCharacter;

public class UserGodsFragment extends Fragment {
    // CONSTANTS
    /**
     * each upgrade multiplies the characters basic damage by this number
     */
    private static final float UPGRADE_MULTIPLIER = 1.3f;

    // FIELDS
    private final MainActivity myActivity;


    // METHODS
    public UserGodsFragment(MainActivity theActivity){

        myActivity = theActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_gods, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.navigation_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        showUserGods();
        setHasOptionsMenu(true);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuAboutGame:
                Methods.makeAlert(getContext(), "About Game Coming Soon...", "About game coming soon...");
                break;
            case R.id.menuStats:
                Methods.makeAlert(getContext(), "Stats Coming Soon...", "Stats coming Soon...");
                break;
            case R.id.menuShop:
                myActivity.navigateFragment(new ShopFragment(myActivity), "Shop");
                break;
            case R.id.menuHome:
                myActivity.navigateFragment(new HomeScreenFragment(myActivity), "Home");
                break;
            case R.id.menuSettings:
                myActivity.navigateFragment(new SettingsFragment(myActivity), "Settings");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showUserGods(){
        LinearLayout layout = getActivity().findViewById(R.id.allUserGods);
        for(int ii = 0; ii < MainActivity.allUserChars.size(); ii++){
            _drawChar(MainActivity.allUserChars.get(ii), layout);
        }
    }

    private void _drawChar(GameCharacter character, LinearLayout layout) {
            LinearLayout  charLayout = new LinearLayout(getContext());
            charLayout.setOrientation(LinearLayout.VERTICAL);

            character.setCharDisplay(new CharDisplay(getContext(), layout));
        character.getCharDisplay().getHealth().setVisibility(View.GONE);

            Button charButton = character.getCharDisplay().getButton();
            charButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Methods.makeAlert(getContext(), "no", "bad");
                    LinearLayout imageLayout = getActivity().findViewById(R.id.bigCharImage);
                    LinearLayout descriptionLayout = getActivity().findViewById(R.id.charDescription);

                    imageLayout.removeAllViews();
                    descriptionLayout.removeAllViews();

                    _showCharDetails(character, imageLayout, descriptionLayout);
                }
            });
            charLayout.addView(character.getCharDisplay());
            layout.addView(charLayout);
    }

    /**
     * shows the details of the selected character including image, health, all attacks
     * also gives the user the option to upgrade their attacks
     * @param character
     * @param imagelayout
     * @param descriptionLayout
     */
    private void _showCharDetails(GameCharacter character, LinearLayout imagelayout, LinearLayout descriptionLayout) {
        LinearLayout charDetailsLayout = new LinearLayout(getContext());
        charDetailsLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView bigImageView = new ImageView(getContext());
        bigImageView.setBackgroundResource(character.getCharImageRes());
        bigImageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        TextView nameView = new TextView(getContext());
        nameView.setTextSize(20.0f);
        nameView.setText(character.getPlayerName() + ":");
        TextView superAbiltiy = new TextView(getContext());
        superAbiltiy.setText("Super Ability: " + (character.unlockedSuperAbility?
                "Level " + character.superAbilityLevel + "\n" :
                "Locked! Open more safes to try and unlock this super ability" + "\n"));
        TextView totalHealthView = new TextView(getContext());
        totalHealthView.setText("Total Health: " + character.getTotalHealth() + "\n");
        TextView myXpView = new TextView(getContext());
        myXpView.setText("Xp: " + character.getMyXp() + "\n");
        imagelayout.addView(bigImageView);
        charDetailsLayout.addView(nameView);
        charDetailsLayout.addView(superAbiltiy);
        charDetailsLayout.addView(totalHealthView);
        charDetailsLayout.addView(myXpView);
        for(int ii = 0; ii < character.myAttacks.size(); ii++) {
            TextView attackView = new TextView(getContext());
            Button upgradeAttackButton = new Button(getContext());
            upgradeAttackButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            upgradeAttackButton.setText("Upgrade " + character.myAttacks.get(ii).name
                    + " for " + character.myAttacks.get(ii).attackUpgradeCost + "xp");
            attackView.setText(Constants.numbers[ii] + " attack: " +
                    character.myAttacks.get(ii).name + "\n" + character.myAttacks.get(ii).getDescription(character) +
                    "\n");
            int finalIi = ii;
            upgradeAttackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(character.getMyXp() >= character.myAttacks.get(finalIi).attackUpgradeCost){
                        int newXp = character.getMyXp();
                        newXp -= character.myAttacks.get(finalIi).attackUpgradeCost;
                        character.setMyXp(newXp);
                        _upgradeAttack(character, imagelayout, descriptionLayout, character.myAttacks.get(finalIi));
                    }
                    else {
                        Methods.makeAlert(getContext(),
                                "Insufficient Funds",
                                "You do not currently have enought xp to buy this upgrade. " +
                                        "Get " +
                                        (character.myAttacks.get(finalIi).attackUpgradeCost-
                                        character.getMyXp()) + " more xp to buy this upgrade");
                    }
                }
            });
            charDetailsLayout.addView(attackView);
            charDetailsLayout.addView(upgradeAttackButton);
        }
            descriptionLayout.addView(charDetailsLayout);
    }

    /**
     * upgrades the attack given
     * @param theAttack the attack to upgrade
     */
    private void _upgradeAttack(GameCharacter character, LinearLayout imagelayout, LinearLayout descriptionLayout, GenericAttack theAttack) {
        character.setBasicDamage(character.getBasicDamage()*UPGRADE_MULTIPLIER);
        imagelayout.removeAllViews();
        descriptionLayout.removeAllViews();
        _showCharDetails(character, imagelayout, descriptionLayout);
    }

}
