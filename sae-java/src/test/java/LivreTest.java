import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import fr.saejava.Client;
import fr.saejava.Commande;
import fr.saejava.DetailCommande;
import fr.saejava.Editeur;
import fr.saejava.Livre;
import fr.saejava.LivrePasDansStockMagasinException;
import fr.saejava.Magasin;

public class LivreTest {
    @Test
    public void testGetISBN() {
        Livre livre = new Livre("1", "titre", 100, "01/01/2020", 10.0);
        assertTrue(livre.getIsbn().equals("1"));
    }

    @Test
    public void testEditeurEquals() {
        Editeur editeur = new Editeur(1, "Gallimard");
        String autreObjet = "pas un editeur";
        assertFalse(editeur.equals(autreObjet));
    }

    @Test
    public void testEditeurEqualsID() {
        Editeur editeur1 = new Editeur(1, "Gallimard");
        Editeur editeur2 = new Editeur(1, "Flammarion"); 
        assertTrue(editeur1.equals(editeur2));
    }


    @Test
    public void testCommandeVide() {
        Date date = Date.valueOf("2025-01-01");
        Client client = new Client(2,"Praire", "Paris",91000, "Infinity", "Nikki", "infinitynikki", "mdp");
        Magasin magasin = new Magasin(1, "Librairie Jsp", "paris");
        Commande commande = new Commande(1, date, client, magasin);
        Livre livre = new Livre(1, "Test", 200, "01/01/2020", 15.0);
        assertFalse(commande.contientLivre(livre));
    }

    @Test
    public void testCommande() {
        Date date = Date.valueOf("2025-01-08");
        Client client = new Client(1,"Vallons", "Lumiere",99000, "Obscure", "Lune", "clairobscure", "mdp1");
        Magasin magasin = new Magasin(1, "Librairie Truc", "Lumiere");
        Commande commande = new Commande(1, date, client, magasin);
        Livre livre = new Livre(1, "Test", 200, "01/01/1899", 19.0);
        DetailCommande detail = new DetailCommande(2, livre, commande);
        commande.ajouterDetailCommande(detail);
        assertTrue(commande.contientLivre(livre));
    }


    @Test
    public void testCommandeLivres() {
        Date date = Date.valueOf("2024-05-01");
        Client client = new Client(3,"Indigo", "Lumiere",99000, "Obscure", "Gustave", "clairobscure3", "mdp3");
        Magasin magasin = new Magasin(1, "Librairie", "Indigo");
        Commande commande = new Commande(1, date, client, magasin);
        Livre livre1 = new Livre(1, "Livre1", 200, "01/01/2020", 5.0);
        Livre livre2 = new Livre(2, "Livre2", 300, "01/02/2020", 10.0);
        Livre livre3 = new Livre(3, "Livre3", 400, "01/03/2020", 25.0);
        commande.ajouterDetailCommande(new DetailCommande(1, livre1, commande));
        commande.ajouterDetailCommande(new DetailCommande(2, livre2, commande));
        assertTrue(commande.contientLivre(livre1));
        assertTrue(commande.contientLivre(livre2));
        assertFalse(commande.contientLivre(livre3));
    }

    @Test
    public void testException() {
        LivrePasDansStockMagasinException exception = new LivrePasDansStockMagasinException();
        assertEquals("Le livre n'est pas dans le stock du magasin, il viendra peut-être prochainement.", exception.getMessage());
    }

}
