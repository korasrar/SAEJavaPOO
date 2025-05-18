package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class CommandeBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public CommandeBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }    
    
    public Commande getDerniereCommande(Client client) throws SQLException{
        Commande derniereCommande = null;
        try {
            st = connexion.createStatement();
            // Tester la requête sur machine IUT
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag FROM MAGASIN NATURAL JOIN COMMANDE NATURAL JOIN CLIENT WHERE idcli =" + client.getNum() + " ORDER BY datecom DESC;");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                derniereCommande = new Commande(client, magasin);
                // detail de la dernière commande
                int numcom = r.getInt("numcom");
                Statement stDetailCommande = connexion.createStatement();                
                ResultSet rDetailCommande = stDetailCommande.executeQuery("SELECT numlig, qte, isbn, titre, nbpages, datepubli, prix from DETAILCOMMANDE natural join LIVRE where numcom = "+numcom+" order by numlig");
                while (rDetailCommande.next()) {
                    Livre livre = new Livre(rDetailCommande.getInt("isbn"), rDetailCommande.getString("titre"), rDetailCommande.getInt("nbPages"), rDetailCommande.getString("datePubli"), rDetailCommande.getDouble("prix"));
                    DetailCommande detail = new DetailCommande(rDetailCommande.getInt("qte"), livre, derniereCommande);
                    derniereCommande.ajouterDetailCommande(detail);
                }
                rDetailCommande.close();
                stDetailCommande.close();
            }
            r.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la dernière commande pour le client " + 
                              client.getNom() + " " + client.getPrenom() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return derniereCommande;
    }
}
