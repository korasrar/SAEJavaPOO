package fr.saejava;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;
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

    // *------------------------ Méthode pour onVousRecommande() ------------------------* //

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
        r = st.executeQuery("SELECT * FROM CLIENT NATURAL JOIN UTILISATEUR WHERE numcli != " + client.getNum());
        while(r.next()){
            Client clientBD = new Client(r.getInt("numcli"), r.getString("adressecli"), r.getString("villecli"), r.getInt("codepostal"), r.getString("nom"), r.getString("prenom"), r.getString("username"), r.getString("motDePasse"));
            int valProxi = this.proximiteClient(client, clientBD);
            if(valProxi > 0){
                clientProches.put(clientBD, valProxi);
            }
        }
        // trie de la map
        Stream<Map.Entry<Client,Integer>> clientProchesSorted = clientProches.entrySet().stream().sorted(Map.Entry.comparingByValue());
        clientProches = new HashMap<>();
        // ajoute les 10 premiers clients plus proche
        // utilisation de iterator pour parcours
        for(int i = 0; i<10 && clientProchesSorted.iterator().hasNext(); i++){
            Entry<Client, Integer> entry = clientProchesSorted.iterator().next();
            clientProches.put(entry.getKey(), entry.getValue());
            
        }
        return clientProches;
    }

    public Set<Livre> onVousRecommande(Client client) throws SQLException {
        Set<Livre> livresRecommandes = new HashSet<>();
        Map<Client, Integer> clientsProches = clientLesPlusProches(client);

        // livre deja commandé par le client
        CommandeBD commandeBD = new CommandeBD(connexion);
        List<Commande> commandesClient = commandeBD.getCommandes(client);
        Set<Livre> livresClient = new HashSet<>();
        for (Commande c : commandesClient) {
            for (DetailCommande comDet : c.getContenue()) {
                livresClient.add(comDet.getLivre());
            }
        }

        // recup livre pas deja commandé
        for (Client clientProche : clientsProches.keySet()) {
            List<Commande> commandesProche = commandeBD.getCommandes(clientProche);
            for (Commande com : commandesProche) {
                for (DetailCommande comDet : com.getContenue()) {
                    Livre livre = comDet.getLivre();
                    if (!livresClient.contains(livre)) {
                        livresRecommandes.add(livre);
                    }
                }
            }
        }
        return livresRecommandes;  
    }
}

