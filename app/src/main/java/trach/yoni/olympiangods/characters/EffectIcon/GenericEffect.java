package trach.yoni.olympiangods.characters.EffectIcon;

import trach.yoni.olympiangods.characters.GameCharacter;

public abstract class GenericEffect
{
    /**
     * the name of the effect
     */
    public final String name;

    /**
     * the resource id for the image assosiated with the effect
     */
    public final int resourceId;

    /**
     * the description of the effect
     */
    public final String description;

    /**
     * constructor for each effect
     * @param theName the name of the effect
     * @param theDescription the description of the effect
     * @param theResourceId the resource id for the image assosiated with the effect
     */
    public GenericEffect(String theName, String theDescription, int theResourceId){
        name = theName;
        description = theDescription;
        resourceId = theResourceId;
    }
}
