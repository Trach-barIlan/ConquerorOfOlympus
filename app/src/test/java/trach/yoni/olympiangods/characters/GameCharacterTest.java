package trach.yoni.olympiangods.characters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameCharacterTest {

    private Cronos testChar; // a test character

    @Before
    public void setUp() {
        testChar = new Cronos("Shalom Abba", -1, 30);
    }

    /**
     * Tests {@link GameCharacter#clone()}
     */
    @Test
    public void testClone() {
        GameCharacter clonedChar = (GameCharacter) testChar.clone();

        assertEquals(testChar.getPlayerName(), clonedChar.getPlayerName());
        assertEquals(testChar.getAttackBonus(), clonedChar.getAttackBonus(),1.0f);
        assert(testChar.sameID_Q(clonedChar));
    }

    /**
     * Tests the character's serializability
     */
    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        // serialize
        FileOutputStream fos = new FileOutputStream("Character");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(testChar);
        oos.close();
        fos.close();

        // deserialize
        FileInputStream fis = new FileInputStream("Character");
        ObjectInputStream ois = new ObjectInputStream(fis);
        GameCharacter serializedChar = (GameCharacter) ois.readObject();

        assert(testChar.sameID_Q(serializedChar));
    }
}