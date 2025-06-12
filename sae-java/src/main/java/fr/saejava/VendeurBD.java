package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class VendeurBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;
    MagasinBD magasinBD;

    public VendeurBD(ConnexionMySQL connexion){
        this.connexion = connexion;
        this.magasinBD = new MagasinBD(connexion);
    }
    
    public void ajouteLivre(int idVendeur,Livre l, int qte) throws VendeurSansMagasinException, SQLException{ //execption a corriger
        magasinBD.ajoutStock(getMagasin(idVendeur), l, qte);
    }
    
    public void ajouteLivre(int idVendeur,Livre l) throws VendeurSansMagasinException, SQLException{
        ajouteLivre(idVendeur, l, 1);
    }

    /**
     * pour passer une commande de livres
     *
     * @param livres la liste des livres à commander
     * @param qte la quantité de chaque livre 
     * @param client le client qui passe la commande
     */

    public void commander(List<Livre> livres, int qte, Client client){
        //Commande commande = new Commande(client, this.magasin);
        //for (Livre l : livres){
        //    try{
        //    if (this.verifierDispo(l)){ // verifie la dispo dans le magasin du vendeur
        //        commande.ajouterLivre(new DetailCommande(qte, l, commande));
        //        client.getHistoriquLivres().add(l);
        //    }
        //    }
        //    catch(LivrePasDansStockMagasinExecption e){
        //        System.out.println("Le livre n'est pas disponible dans le magasin");
        //    }
        //}

        // Ajoute une commande dans la table commande avec l'id du client mis paramètre
    }

    /**
     * met à jour la quantité d'un livre dans le stock
     *
     * @param l le livre à mettre à jour
     * @param qte la nouvelle quantité
     * @throws LivrePasDansStockMagasinExecption si le livre n'est pas dans le stock
     */

    public void mettreAJour(Livre l, int qte, Magasin mag) throws SQLException, LivrePasDansStockMagasinException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+mag.getId());
        if (r.getInt("qte")<= qte){
        throw new LivrePasDansStockMagasinException();}
        if (verifierDispo(mag, l)){
            r = st.executeQuery("SELECT qte FROM POSSEDER where isbn = '"+l.getIsbn()+"' AND idmag = "+mag.getId());
            Integer quantiteInit = r.getInt("qte");
            Integer nouvelleQte = quantiteInit + qte;
            st.executeUpdate("UPDATE * FROM POSSEDER SET qte =" + nouvelleQte);
        }
        else {
            //ok

        }    }

    public void transferer(Livre l, Magasin magasinRecoit, Magasin magasinEnvoit, Integer qte) throws SQLException, LivrePasDansStockMagasinException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+magasinEnvoit.getId());
        if (r.getInt("qte")<= qte){
        throw new LivrePasDansStockMagasinException();}
        // DONE verifier la dispo du livre, si true faire update pour recup la valeur et incremente avec qte et renvoie dans bd
        // sinon , on fait un insert avec le livre et la qte
        if (verifierDispo(magasinRecoit, l)){
            r = st.executeQuery("SELECT qte FROM POSSEDER where isbn = '"+l.getIsbn()+"' AND idmag = "+magasinRecoit.getId());
            Integer quantiteInit = r.getInt("qte");
            Integer nouvelleQte = quantiteInit + qte;
            st.executeUpdate("UPDATE * FROM POSSEDER SET qte =" + nouvelleQte);
        }
        else {
            st.executeUpdate("UPDATE * FROM POSSEDER SET qte =" + 1 +"AND idmag = " + magasinRecoit.getId() +"AND isbn ='" + l.getIsbn() + "'");

        }
        }

    /**
     * vérifie si un livre est disponible dans le stock du magasin
     *
     * @param magasin le magasin à vérifier
     * @param l le livre à vérifier
     * @return true si le livre est disponible, false sinon
     * @throws SQLException si une erreur SQL se produit
     */
    public boolean verifierDispo(Magasin magasin, Livre l) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+magasin.getId());
        if (r.next()){
            int qte = r.getInt("qte");
            if (qte > 0){return true;}
            else {return false;}
            }
        else {return false;}
    }

    /**
     * vérifie si un livre est disponible dans le stock du réseau
     *
     * @param magasin le magasin à vérifier
     * @param l le livre à vérifier
     * @return true si le livre est disponible, false sinon
     * @throws SQLException si une erreur SQL se produit
     */
    public boolean verifierDispo(Livre l) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"'");
        if (r.next()){
            int qte = r.getInt("qte");
            if (qte > 0){return true;}
            else {return false;}
            }
        else {return false;}
    }


    /**
     * récupère le magasin du vendeur
     *
     * @param idVendeur l'identifiant du vendeur
     * @return le magasin du vendeur
     * @throws SQLException si une erreur SQL se produit
     * @throws VendeurSansMagasinException si le vendeur n'a pas de magasin associé
     */
    public Magasin getMagasin(int idVendeur) throws SQLException, VendeurSansMagasinException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM VENDEUR NATURAL JOIN MAGASIN WHERE idVendeur =" +idVendeur);
        if(r.next()){
            return new Magasin(r.getInt("idmag"), r.getString("nommag"), r.getString("villemag"));
        }
        else{
            throw new VendeurSansMagasinException();
        }
    }


}