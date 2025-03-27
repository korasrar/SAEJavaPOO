import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class LivreTest {
    @Test
    public void testGetISBN() {
        Livre livre = new Livre(1, null, 0, null, 0);
        assert livre.getISBN() == 1;
    }
}

