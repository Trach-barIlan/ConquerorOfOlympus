package trach.yoni.olympiangods.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import trach.yoni.olympiangods.Items.GenericItem;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.aux.Methods;
import trach.yoni.olympiangods.aux.Player;
import trach.yoni.olympiangods.characters.GameCharacter;

public class ShopFragment extends Fragment {

    private final MainActivity myָActivity;

    public ShopFragment(MainActivity theActivity) {
        super(R.layout.fragment_fight);
        myָActivity = theActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.navigation_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoneyText();
        _showShopItems();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGods:
                myָActivity.navigateFragment(new UserGodsFragment(myָActivity), "User Gods");
                break;
            case R.id.menuAboutGame:
                Methods.makeAlert(getContext(), "About Game Coming Soon...", "About game coming soon...");
                break;
            case R.id.menuStats:
                Methods.makeAlert(getContext(), "Stats Coming Soon...", "stats coming soon...");
                break;
            case R.id.menuHome:
                myָActivity.navigateFragment(new HomeScreenFragment(myָActivity), "Home");
                break;
            case R.id.menuSettings:
                myָActivity.navigateFragment(new SettingsFragment(myָActivity), "Settings");
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void _showShopItems() {
        LinearLayout layout = getActivity().findViewById(R.id.itemsForSale);
        for(GenericItem item : MainActivity.allItems){
            _drawItemInShop(item, layout);
        }
    }

    public void updateMoneyText(){
        TextView moneyView = getActivity().findViewById(R.id.availableMoney);
        moneyView.setText("" + Player.playerMoney);
    }

    private void _drawItemInShop(GenericItem item, LinearLayout layout) {
        if(item.canBuy()){
            LinearLayout  itemLayout = new LinearLayout(getContext());
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            Button itemButton = new Button(getContext());
            itemButton.setBackgroundResource(item.getImageResouce());
            TextView costView = new TextView(getContext());
            costView.setText("Cost: " + item.getCost());
            TextView nameView = new TextView(getContext());
            nameView.setText(item.getName());
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Player.playerMoney >= item.getCost()) {
                        _showPurchaseDetails(item, item.isDuplicate);
//                        item.buy();
                        getContext();
                        Player.playerMoney -= item.getCost();
                        updateMoneyText();
                    }
                    else {
                        Methods.makeAlert(getContext(),
                                "Not Enough Money",
                                "You do not currently have enough money get "
                                        + Math.abs(Player.playerMoney - item.getCost())
                                        + " more money and then try again");
                    }
                }
            });
            itemLayout.addView(itemButton);
            itemLayout.addView(nameView);
            itemLayout.addView(costView);
            layout.addView(itemLayout);
        }
    }

    /**
     * Activates the buy ability of the given item in the shop, and then
     * displays the baught page either showing you got a duplicate or new god
     * @param item what item in the shop to activate the buy ability of
     */
    private void _showPurchaseDetails(GenericItem item, Boolean isDuplicate) {
        GameCharacter theCharacter = item.buy();
        if (Objects.nonNull(theCharacter)) {
            if (item.isDuplicate) {
                Methods.makeImageAlert(getContext(), "Duplicate God", "You got another "
                                + theCharacter.getPlayerName()
                                + ".\nYou just unlocked "
                                + theCharacter.getPlayerName()
                                + "'s super ability."
                                + "\nGo test it out!",
                        theCharacter.getCharImageRes());
                return;
            }
            Methods.makeImageAlert(
                    getContext(),
                    "Congradulations!",
                    "\n\n You bought a new "
                            + theCharacter.getPlayerName(), theCharacter.getCharImageRes());
        }
    }
}
