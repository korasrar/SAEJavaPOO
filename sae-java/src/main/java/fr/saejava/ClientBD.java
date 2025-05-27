package fr.saejava;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
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

    public int proximiteClient(Client client1, Client client2) throws SQLException{
        int valProximite = 0;
        CommandeBD commandeBD = new CommandeBD(connexion);
        Commande derniereCommandeClient1 = commandeBD.getDerniereCommande(client1);
        List<Commande> listCommandesClient2 = commandeBD.getCommandes(client2);
        Set<Commande> setCommandesClient1 = new HashSet<>(listCommandesClient2);
        for(Commande com : setCommandesClient1){
            for(DetailCommande comDetail : com.getContenue()){
                Livre livre = comDetail.getLivre();
                if(derniereCommandeClient1.contientLivre(livre)){
                    valProximite += 1;
                }
            }
        }
        return valProximite;
    }

    public Map<Client, Integer> clientLesPlusProches(Client client) throws SQLException{
        Map<Client, Integer> clientProches = new HashMap<>();
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM CLIENT");
        while(r.next()){
            Client clientBD = new Client(r.getString("adressecli"), r.getString("villecli"), r.getInt("codepostal"), r.getString("nomcli"), r.getString("prenomcli"));
            int valProxi = this.proximiteClient(client, clientBD);
            if(clientProches.keySet().size()<10){
                clientProches.put(clientBD, valProxi);
            }
            else{
                for(Map.Entry<Client,Integer> coupleVal : clientProches.entrySet())
                if(coupleVal.getValue()<valProxi){
                    clientProches.replace(clientBD, null);
                }
            }
        }
        return clientProches;
    }

    // Calculer proxi avec notre client
    // faire méthode pour ca
    // parcours des client, calcul proxi, les 10 plus proches, recup livre

    public Set<Livre> onVousRecommande(Client client){
        Set<Livre> livresRecommandes = new HashSet<>();
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM CLIENT NATURAL JOIN COMMANDE NATURAL JOIN DETAILCOMMANDE NATURAL JOIN LIVRE");

        // requete SQL pour récuperer les livres des clients qui ont au moins un livre en commun avec le client passé en parametre
        // donc ne pas hésiter a faires des requetes SQL pour ce genre de méthodes
        // liste de tt les clients du magasin?, faire une copi de la liste et enlever le client en paramettre

        // recuperer la liste de commandes de chaques client de la liste et du client passe en paramettre

        // comparer les listes

        // faire une liste avec les livres qui ne sont pas en commun pour chaque client qui a au moins un 
        // livre en commun avec la liste du client passe en paramettre   
        return null;
    }
}
