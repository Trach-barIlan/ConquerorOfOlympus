package trach.yoni.olympiangods.Items;

import android.content.Context;

import trach.yoni.olympiangods.characters.GameCharacter;

public abstract class GenericItem {

    //FIELDS

    protected final String myName;
    protected final int myCost;
    protected final int myImageRes;
    public Boolean isDuplicate;

    /**
     * creates a generic item to buy in the shop
     * @param name the name of the item
     * @param cost how much this item costs
     * @param imageResource the resource id of the image to display for chosing this item
     */
    public GenericItem(String name, int cost, int imageResource) {
        myName = name;
        myCost = cost;
        myImageRes = imageResource;
    }

    /**
     * @return the id of the image resource of the item
     */
    public int getImageResouce(){
        return myImageRes;
    }

    public String getName(){
        return myName;
    }

    public int getCost(){
        return myCost;
    }

    /**
     * the way you buy an item
     */
    public abstract GameCharacter buy();

    /**
     * @return true if the player can buy the item
     *          false if not
     */
    public abstract boolean canBuy();
}
