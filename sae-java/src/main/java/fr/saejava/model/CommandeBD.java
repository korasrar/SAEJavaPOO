package fr.saejava.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommandeBD {
    ConnexionMySQL connexion;

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
        Statement st = null;
        ResultSet r = null;
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag, livraison FROM MAGASIN NATURAL JOIN COMMANDE NATURAL JOIN CLIENT WHERE idcli ="+client.getNum()+" ORDER BY datecom DESC LIMIT 1");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                String modeLivraison = r.getString("livraison");
                if (modeLivraison.equals("C")) {
                    derniereCommande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAISON);
                }
                else{
                    derniereCommande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAGASIN);
                }
                int numcom = r.getInt("numcom");
                Statement stDetailCommande = connexion.createStatement();                
                ResultSet rDetailCommande = stDetailCommande.executeQuery("SELECT numlig, qte, isbn, titre, nbpages, datepubli, prix from DETAILCOMMANDE natural join LIVRE where numcom = "+numcom+" order by numlig");
                while (rDetailCommande.next()) {
                    Livre livre = new Livre(rDetailCommande.getString("isbn"), rDetailCommande.getString("titre"), rDetailCommande.getInt("nbpages"), rDetailCommande.getString("datepubli"), rDetailCommande.getDouble("prix"));
                    DetailCommande detail = new DetailCommande(rDetailCommande.getInt("qte"), livre, derniereCommande);
                    derniereCommande.ajouterDetailCommande(detail);
                }
                rDetailCommande.close();
                stDetailCommande.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la dernière commande pour le client ");
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return derniereCommande;
    }

    public Commande getCommande(Client client, int numCommande) throws NumberFormatException, SQLException{
        Commande commande = null;
        Statement st = null;
        ResultSet r = null;
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag, livraison FROM MAGASIN natural join COMMANDE natural join CLIENT WHERE idcli =" + client.getNum() + " and numcom = " + numCommande + ";");
            if (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                String modeLivraison = r.getString("livraison");
                if (modeLivraison.equals("C")) {
                    commande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAISON);
                }
                else{
                    commande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAGASIN);
                }
                int numcom = r.getInt("numcom");
                Statement stDetailCommande = connexion.createStatement();                
                ResultSet rDetailCommande = stDetailCommande.executeQuery("SELECT numlig, qte, isbn, titre, nbpages, datepubli, prix from DETAILCOMMANDE natural join LIVRE where numcom = "+numcom+" order by numlig");
                while (rDetailCommande.next()) {
                    Livre livre = new Livre(rDetailCommande.getString("isbn"), rDetailCommande.getString("titre"), rDetailCommande.getInt("nbpages"), rDetailCommande.getString("datepubli"), rDetailCommande.getDouble("prix"));
                    DetailCommande detail = new DetailCommande(rDetailCommande.getInt("qte"), livre, commande);
                    commande.ajouterDetailCommande(detail);
                }
                rDetailCommande.close();
                stDetailCommande.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la commande pour le client");
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return commande;
    }

    /**
     * retourne la liste des commandes d'un client
     * @param client le client 
     * @return la liste des commandes du client
     */

    public List<Commande> getCommandes(Client client) throws SQLException{
        List<Commande> listCommandes = new ArrayList<>();
        Statement st = null;
        ResultSet r = null;
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT numcom, datecom, idmag, nommag, villemag, livraison FROM MAGASIN natural join COMMANDE natural join CLIENT WHERE idcli =" + client.getNum() + ";");
            while (r.next()) {
                Magasin magasin = new Magasin(r.getInt("idmag"),r.getString("nommag"),r.getString("villemag"));
                String modeLivraison = r.getString("livraison");
                Commande commande = null;
                if (modeLivraison.equals("C")) {
                    commande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAISON);
                }
                else{
                    commande = new Commande(r.getInt("numcom"),r.getDate("datecom"), client, magasin, ModeLivraison.MAGASIN);
                }
                int numcom = r.getInt("numcom");
                Statement stDetailCommande = connexion.createStatement();                
                ResultSet rDetailCommande = stDetailCommande.executeQuery("SELECT numlig, qte, isbn, titre, nbpages, datepubli, prix from DETAILCOMMANDE natural join LIVRE where numcom = "+numcom+" order by numlig");
                while (rDetailCommande.next()) {
                    Livre livre = new Livre(rDetailCommande.getString("isbn"), rDetailCommande.getString("titre"), rDetailCommande.getInt("nbpages"), rDetailCommande.getString("datepubli"), rDetailCommande.getDouble("prix"));
                    DetailCommande detail = new DetailCommande(rDetailCommande.getInt("qte"), livre, commande);
                    commande.ajouterDetailCommande(detail);
                }
                rDetailCommande.close();
                stDetailCommande.close();
                listCommandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes pour le client");
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return listCommandes;
    }

    public Integer getDerniereIdCommande() throws SQLException{
        Statement st = null;
        ResultSet r = null;
        Integer lastNomCom = 0;
        try {
            st = connexion.createStatement();
            r = st.executeQuery("SELECT * FROM COMMANDE ORDER BY numcom DESC LIMIT 0, 1");
            if(r.next()){
                lastNomCom = r.getInt("numcom");
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return lastNomCom;
    }

    
}
