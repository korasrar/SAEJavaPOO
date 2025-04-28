import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.saejava.Livre;

public class LivreTest {
    @Test
    public void testGetISBN() {
        Livre livre = new Livre(1, "titre", 100, "01/01/2020", 10.0);
        assertTrue(livre.getIsbn() == 1);
    }
}
