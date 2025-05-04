package trach.yoni.olympiangods.Items;

import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Player;
import trach.yoni.olympiangods.characters.GameCharacter;

public class HealthPotion extends GenericItem{

    public HealthPotion(int cost) {
        super("Health Potion", cost, R.mipmap.health_potion);
    }

    @Override
    public GameCharacter buy() {
        Player.userMagicItems.add(this);
        return null;
    }

    @Override
    public boolean canBuy() {
        return true;
    }
}
