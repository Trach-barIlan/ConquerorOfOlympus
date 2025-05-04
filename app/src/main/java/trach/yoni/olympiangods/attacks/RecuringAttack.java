package trach.yoni.olympiangods.attacks;

import java.util.Objects;

import trach.yoni.olympiangods.characters.GameCharacter;
import trach.yoni.olympiangods.fragments.FightFragment;

public class RecuringAttack extends GenericAttack{
    /**
     * integer amount of times to repeat the damage over time
     */
    private final int myNumTimes;

    /**
     * the attack that is going to recur
     */
    private final GenericAttack myAttack;
    /**
     * if start is true then this is the turn that the damage over time was applied
     * false to show that the damage should be dealt
     */
    private boolean start = true;

    /**
     * deals damage over time to the this character
     * sets the name, description, and the upgrade cost for the attack
     */
    public RecuringAttack(GenericAttack theAttack, int numTimes){
        super("Recuring attack",
                40,
                theAttack.myAttackPictRes,
                true);
        myNumTimes = numTimes;
        myAttack = theAttack;
    }

    private RecuringAttack(GenericAttack theAttack, int numTimes, boolean startState){
        this(theAttack, numTimes);
        start = startState;
    }

    /**
     * attacks the defender {@link #myNumTimes} times
     * @param attacker the character that is attacking
     * @param defender the character that is defending
     * @return true if the defender was not null
     *          false otherwise
     */
    @Override
    public boolean attack(GameCharacter attacker, GameCharacter defender) {
        if (Objects.isNull(defender)) {
            return false;
        }
        if (myNumTimes != -1) {
            GenericAttack Bleed = new RecuringAttack(myAttack, myNumTimes-1, false);
            FightFragment.addFutureAttack(Bleed, attacker, defender);
            if(start) {
                start = false;
                return true;
            }
            else {
                return myAttack.attack(attacker, defender);
            }
        } else {
            return true;
        }

    }

    /**
     * @param attacker the attacker
     * @return the description of a a recurring attack
     */
    @Override
    public String getDescription(GameCharacter attacker) {
        return  "Recuring Attack: " + myNumTimes + "times of " +
                myAttack + " [" + myAttack.getDescription(attacker) + "] ";
    }

    /**
     * not used, this attack does no damage by itself
     */
    @Override
    protected float getDamage(GameCharacter attacker) {
        return 0;
    }

}
