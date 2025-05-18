package fr.saejava;

import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public ClientBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void consulterCatalogue(){
        // List de livre, requete dans la table livre de tout les livres, les afficher peut etre 5 par 5
    }

    public void passerCommande(Commande commande) throws SQLException{
        //ajoute la commande a la BD
        st = connexion.createStatement();
        // Demander les informations au client
        st.executeUpdate("");
    }

    public Set<Livre> onVousRecommande(Client client){
        // requete SQL pour récuperer les livres des clients qui ont au moins un livre en commun avec le client passé en parametre
        // donc ne pas hésiter a faires des requetes SQL pour ce genre de méthodes
        // liste de tt les clients du magasin?, faire une copi de la liste et enlever le client en paramettre

        // recuperer la liste de commandes de chaques client de la liste et du client passe en paramettre

        // comparer les listes

        // faire une liste avec les livres qui ne sont pas en commun pour chaque client qui a au moins un 
        // livre en commun avec la liste du client passe en paramettre   
        return null;
    }

    public Commande getCommande(Client client, int numCom) throws NumberFormatException, SQLException{
        Commande commande = null;
        try {
            st = connexion.createStatement();
            // Tester la requête sur machine IUT
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag FROM MAGASIN NATURAL JOIN COMMANDE NATURAL JOIN CLIENT WHERE idcli =" + client.getNum() + " AND numcom = " + numCom + ";");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                commande = new Commande(client, magasin);
                // detail de la dernière commande
                int numcom = r.getInt("numcom");
                Statement stDetailCommande = connexion.createStatement();                
                ResultSet rDetailCommande = stDetailCommande.executeQuery("SELECT numlig, qte, isbn, titre, nbpages, datepubli, prix from DETAILCOMMANDE natural join LIVRE where numcom = "+numcom+" order by numlig");
                while (rDetailCommande.next()) {
                    Livre livre = new Livre(rDetailCommande.getInt("isbn"), rDetailCommande.getString("titre"), rDetailCommande.getInt("nbPages"), rDetailCommande.getString("datePubli"), rDetailCommande.getDouble("prix"));
                    DetailCommande detail = new DetailCommande(rDetailCommande.getInt("qte"), livre, commande);
                    commande.ajouterDetailCommande(detail);
                }
                rDetailCommande.close();
                stDetailCommande.close();
            }
            r.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la dernière commande pour le client");
        }
        return commande;
    }
}
