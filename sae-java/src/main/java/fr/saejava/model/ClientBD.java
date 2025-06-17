package fr.saejava.model;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class ClientBD {
    ConnexionMySQL connexion;

    public ClientBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void creeCompteClient(int num, String adresse, String ville, int codePostal, String nom, String prenom, String username, String motDePasse) throws SQLException{
        System.out.println(""+num);
        PreparedStatement pstmt = this.connexion.prepareStatement("insert into UTILISATEUR values ("+num+",'"+nom+"','"+prenom+"','"+username+"','"+motDePasse+"','client')");
        System.out.println("Préparation de l'insertion de l'utilisateur...");
        pstmt.executeUpdate();
        System.out.println("Utilisateur inséré avec succès !");
        pstmt.close();
        pstmt = this.connexion.prepareStatement("insert into CLIENT values ("+num+", '"+adresse+"', '"+codePostal+"', '"+ville+"')");
        pstmt.executeUpdate();
        System.out.print("Client inséré avec succès !");
        pstmt.close();
    }

    /**
     * pour que le client consulte le catalogue de livre
     */
    public void consulterCatalogue(){
        
    }

    // *------------------------ Méthode pour onVousRecommande() ------------------------* //


    /**
     * calcule la proximité entre deux clients
     * @param client1 le premier client
     * @param client2 le deuxième client
     * @return la proximité entre les deux clients
     * @throws SQLException si une erreur SQL se produit
     */

    public int proximiteClient(Client client1, Client client2) throws SQLException{
        int valProximite = 0;
        CommandeBD commandeBD = new CommandeBD(connexion);
        
        Commande listCommandesClient1 = commandeBD.getDerniereCommande(client1);
        List<Commande> listCommandesClient2 = commandeBD.getCommandes(client2);
        
        // Créer un set des livres du client1
        Set<Livre> livresClient1 = new HashSet<>();
            for(DetailCommande comDetail : listCommandesClient1.getContenue()){
                livresClient1.add(comDetail.getLivre());
            }
        
        // Comparer avec les livres du client2
        for(Commande com : listCommandesClient2){
            for(DetailCommande comDetail : com.getContenue()){
                Livre livre = comDetail.getLivre();
                if(livresClient1.contains(livre)){
                    valProximite += 1;
                }
            }
        }
        return valProximite;
    }


    /**
     * retourne les 2 clients les plus proches du client passé en paramètre
     * @param client le client pour lequel on cherche les clients proches
     * @return un dico contenant les clients proches et leur proximité
     * @throws SQLException si une erreur SQL se produit
     */

    public Map<Client, Integer> clientLesPlusProches(Client client) throws SQLException{
        Map<Client, Integer> clientProches = new HashMap<>();
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM CLIENT c JOIN UTILISATEUR u ON c.idcli=u.idutilisateur WHERE idcli != " + client.getNum());
            while(r.next()){
                Client clientBD = new Client(r.getInt("idcli"), r.getString("adressecli"), r.getString("villecli"), r.getInt("codepostal"), r.getString("nom"), r.getString("prenom"), r.getString("username"), r.getString("motDePasse"));
                int valProxi = this.proximiteClient(client, clientBD);
                if(valProxi > 0){
                    clientProches.put(clientBD, valProxi);
                }
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        // trie de la map
        Stream<Map.Entry<Client,Integer>> clientProchesSorted = clientProches.entrySet().stream().sorted(Map.Entry.comparingByValue());
        clientProches = new HashMap<>();
        // ajoute les 2 premiers clients plus proche
        Iterator<Map.Entry<Client,Integer>> clientProchesIter = clientProchesSorted.iterator();
        for(int i = 0; i<2 && clientProchesIter.hasNext(); i++){
            System.out.println(i);
            Entry<Client, Integer> entry = clientProchesIter.next();
            clientProches.put(entry.getKey(), entry.getValue());
        }
        return clientProches;
    }

    /**
     * retourne les livres recommandés pour le client passé en paramètre
     * @param client le client pour lequel on cherche les livres recommandés
     * @return un ensemble de livres recommandés
     * @throws SQLException si une erreur SQL se produit
     */
    
    public Map<Livre,Boolean> onVousRecommande(Client client) throws SQLException {
        VendeurBD connexionVendeur = new VendeurBD(connexion);
        Map<Livre,Boolean> livresRecommandes = new HashMap<>();
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
                        livresRecommandes.put(livre,connexionVendeur.verifierDispo(livre));
                    }
                }
            }
        }
        return livresRecommandes;  
    }

    public void modifierClient(Client clientTempo) throws SQLException {
        PreparedStatement pstmt = this.connexion.prepareStatement("UPDATE UTILISATEUR SET nom = ?, prenom = ?, username = ?, motdepasse = ? WHERE idutilisateur = ?");
        pstmt.setString(1, clientTempo.getNom());
        pstmt.setString(2, clientTempo.getPrenom());
        pstmt.setString(3, clientTempo.getPseudo());
        pstmt.setString(4, clientTempo.getMotDePasse());
        pstmt.setInt(5, clientTempo.getNum());
        pstmt.executeUpdate();
        pstmt.close();
        
        pstmt = this.connexion.prepareStatement("UPDATE CLIENT SET adressecli = ?, codepostal = ?, villecli = ? WHERE idcli = ?");
        pstmt.setString(1, clientTempo.getAdresse());
        pstmt.setInt(2, clientTempo.getCodePostal());
        pstmt.setString(3, clientTempo.getVille());
        pstmt.setInt(4, clientTempo.getNum());
        pstmt.executeUpdate();
        pstmt.close();
    }

    /**
     * Finalise la commande en insérant les données dans la base de données
     * @param modeLivraison le mode de livraison choisi par le client
     * @throws SQLException si une erreur SQL se produit
     */
    public void finaliseCommande(Client client, ModeLivraison modeLivraison, Commande panier, Magasin magasin) throws SQLException{
        MagasinBD magasinConnexion = new MagasinBD(connexion);
        CommandeBD commandeConnexion = new CommandeBD(connexion);
        panier.setModeLivraison(modeLivraison);
        if(modeLivraison.equals(ModeLivraison.MAGASIN)){
            panier.setMagasin(magasin);
        }
        else if(modeLivraison.equals(ModeLivraison.MAISON)){
            panier.setMagasin(magasinConnexion.trouverMeilleurMagasin(panier));
        }
        else{
            throw new SQLException("Mode de livraison inconnu");
        }
        panier.setNumcom(commandeConnexion.getDerniereIdCommande()+1);
        String sqlCommande = "INSERT INTO COMMANDE(numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstCommande = connexion.prepareStatement(sqlCommande);

        pstCommande.setInt(1, panier.getNumcom());
        pstCommande.setDate(2, panier.getDateCommande());
        pstCommande.setString(3, "O");
        pstCommande.setString(4, modeLivraison.equals(ModeLivraison.MAGASIN) ? "M" : "C");
        pstCommande.setInt(5, panier.getClient().getNum());
        pstCommande.setInt(6, panier.getMagasin().getId());

        pstCommande.executeUpdate();
        System.out.println("Commande insérée avec succès !");

        String sqlDetail = "INSERT INTO DETAILCOMMANDE(numcom, numlig, isbn, qte, prixvente) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstDetail = connexion.prepareStatement(sqlDetail);

        int numlig = 1;
        for(DetailCommande detail : panier.contenue){
            pstDetail.setInt(1, panier.getNumcom());
            pstDetail.setInt(2, numlig);
            pstDetail.setString(3, detail.getLivre().getIsbn());
            pstDetail.setInt(4, detail.getQte());
            pstDetail.setDouble(5, detail.getLivre().getPrix());
            pstDetail.executeUpdate();
            if(magasinConnexion.getQuantiteLivre(detail.getLivre(),panier.getMagasin())==0){
                for(Magasin mag:magasinConnexion.chargerMagasin()){
                    if(magasinConnexion.getQuantiteLivre(detail.getLivre(),magasin)>detail.getQte()){
                        magasinConnexion.ajoutStock(magasin,detail.getLivre(),detail.getQte());
                    }
                }
            }
            else if(magasinConnexion.getQuantiteLivre(detail.getLivre(),panier.getMagasin())<detail.getQte()){
                int qteRestante=detail.getQte()-magasinConnexion.getQuantiteLivre(detail.getLivre(),panier.getMagasin());
                magasinConnexion.ajoutStock(panier.getMagasin(),detail.getLivre(),detail.getQte()-qteRestante);
                for(Magasin mag:magasinConnexion.chargerMagasin()){
                    if(magasinConnexion.getQuantiteLivre(detail.getLivre(),magasin)>qteRestante){
                        magasinConnexion.ajoutStock(magasin,detail.getLivre(),qteRestante);
                    }
                }
            }
            else{
                magasinConnexion.ajoutStock(panier.getMagasin(),detail.getLivre(),detail.getQte()*-1);
            }
            numlig++;
        }
        System.out.println("Détails de la commande insérés avec succès !");
        pstCommande.close();
        pstDetail.close();
        // reset du panier
        Date date = new Date(System.currentTimeMillis());
        try{
            client.creeCommande(date);
        }
        catch(SQLException e){
            System.out.println("Erreur lors du reset du panier " + e.getMessage());
        }
    }
}

