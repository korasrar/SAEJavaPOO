package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommandeBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public CommandeBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }    

    /**
     * retourne la dernière commande d'un client
     * @param client le client dont on veut récupérer la dernière commande
     * @return la dernière commande du client
     * @throws SQLException si une erreur SQL se produit
     */
    
    public Commande getDerniereCommande(Client client) throws SQLException{
        Commande derniereCommande = null;
        try {
            st = connexion.createStatement();
            // Tester la requête sur machine IUT
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag FROM MAGASIN NATURAL JOIN COMMANDE NATURAL JOIN CLIENT WHERE idcli =" + client.getNum() + " ORDER BY datecom DESC;");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                derniereCommande = new Commande(r.getInt("numcom"), client, magasin);
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
            System.out.println("Erreur lors de la récupération de la dernière commande pour le client ");
        }
        return derniereCommande;
    }

    public Commande getCommande(Client client, int numCommande) throws NumberFormatException, SQLException{
        Commande commande = null;
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag FROM MAGASIN natural join COMMANDE natural join CLIENT WHERE idcli =" + client.getNum() + " and numcom = " + numCommande + ";");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                commande = new Commande(r.getInt("numcom"), client, magasin);
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
            System.out.println("Erreur lors de la récupération de la commande pour le client");
        }
        return commande;
    }

    /**
     * retourne la liste des commandes d'un client
     * @param client le client 
     * @return la liste des commandes du client
     */

    public List<Commande> getCommandes(Client client){
        List<Commande> listCommandes = new ArrayList<>();
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag FROM MAGASIN natural join COMMANDE natural join CLIENT WHERE idcli =" + client.getNum() + ";");
            while (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                Commande commande = new Commande(r.getInt("numcom"), client, magasin);
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
                listCommandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes pour le client");
        }
        return listCommandes;
    }
}
