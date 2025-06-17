package fr.saejava.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class VendeurBD {
    ConnexionMySQL connexion;
    MagasinBD magasinBD;

    public VendeurBD(ConnexionMySQL connexion){
        this.connexion = connexion;
        this.magasinBD = new MagasinBD(connexion);
    }
    
    public void ajouteLivre(int idVendeur,Livre l, int qte) throws VendeurSansMagasinException, SQLException{ //execption a corriger
        magasinBD.ajoutStock(getMagasin(idVendeur), l, qte);
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
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+mag.getId());
        if(r.next()){
            if (r.getInt("qte")<= qte){
                if (r != null) r.close();
                st.close();
                throw new LivrePasDansStockMagasinException();
            }
            if (verifierDispo(mag, l)){
                r = st.executeQuery("SELECT qte FROM POSSEDER where isbn = '"+l.getIsbn()+"' AND idmag = "+mag.getId());
                Integer quantiteInit = null;
                if(r.next()){
                    quantiteInit = r.getInt("qte");
                }
                Integer nouvelleQte = quantiteInit + qte;
                if (r != null) r.close();
                r.close();
                st.executeUpdate("UPDATE POSSEDER SET qte = " + nouvelleQte + " WHERE isbn = '" + l.getIsbn() + "' AND idmag = " + mag.getId());
            }
            else {
                //ok
            }
        }
        st.close();
    }

    public void transferer(Livre l, Magasin magasinRecoit, Magasin magasinEnvoit, Integer qte) throws SQLException, LivrePasDansStockMagasinException{
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+magasinEnvoit.getId());
        if(r.next()){
            if (r.getInt("qte") < qte){
                if (r != null) r.close();
                st.close();
                throw new LivrePasDansStockMagasinException();
            }
            if (verifierDispo(magasinRecoit, l)){
                ResultSet r2 = st.executeQuery("SELECT qte FROM POSSEDER where isbn = '"+l.getIsbn()+"' AND idmag = "+magasinRecoit.getId());
                Integer quantiteInit = null;
                if(r2.next()){
                    quantiteInit = r2.getInt("qte");
                }
                Integer nouvelleQte = quantiteInit + qte;
                if (r2 != null) r2.close();
                st.executeUpdate("UPDATE POSSEDER SET qte = " + nouvelleQte + " WHERE isbn = '" + l.getIsbn() + "' AND idmag = " + magasinRecoit.getId());
            } else {
                st.executeUpdate("INSERT INTO POSSEDER (isbn, idmag, qte) VALUES ('" + l.getIsbn() + "', " + magasinRecoit.getId() + ", " + qte + ")");
            }
            st.executeUpdate("UPDATE POSSEDER SET qte = qte - " + qte + " WHERE isbn = '" + l.getIsbn() + "' AND idmag = " + magasinEnvoit.getId());
            if (r != null) r.close();
        } else {
            if (r != null) r.close();
            st.close();
            throw new LivrePasDansStockMagasinException();
        }
        st.close();
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
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = "+magasin.getId());
        if (r.next()){
            int qte = r.getInt("qte");
            if (r != null) r.close();
            st.close();
            if (qte > 0){return true;}
            else {return false;}
        }
        else {
            if (r != null) r.close();
            st.close();
            return false;
        }
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
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"'");
        if (r.next()){
            int qte = r.getInt("qte");
            if (r != null) r.close();
            st.close();
            if (qte > 0){return true;}
            else {return false;}
        }
        else {
            r.close();
            st.close();
            return false;
        }
    }

    public boolean verifierQteDispo(Livre l, int qte) throws SQLException, PasStockPourLivreException{
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT SUM(qte) qtett FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' group by isbn");
        if (r.next()){
            int qteDispo = r.getInt("qtett");
            if (r != null) r.close();
            st.close();
            if (qteDispo >= qte){return true;}
            else {throw new PasStockPourLivreException();}
        }
        else {
            r.close();
            st.close();
            throw new PasStockPourLivreException();
        }
    }

    public boolean verifierQteDispo(Livre l, int qte, Magasin magasin) throws SQLException, PasStockPourLivreException{
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT SUM(qte) qtett FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' group by isbn and idmag = "+magasin.getId());
        if (r.next()){
            int qteDispo = r.getInt("qtett");
            if (r != null) r.close();
            st.close();
            if (qteDispo >= qte){return true;}
            else {throw new PasStockPourLivreException();}
        }
        else {
            if (r != null) r.close();
            st.close();
            throw new PasStockPourLivreException();
        }
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
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM VENDEUR NATURAL JOIN MAGASIN WHERE idVendeur =" +idVendeur);
        if(r.next()){
            Magasin magasin = new Magasin(r.getInt("idmag"), r.getString("nommag"), r.getString("villemag"));
            if (r != null) r.close();
            st.close();
            return magasin;
        }
        else{
            if (r != null) r.close();
            st.close();
            throw new VendeurSansMagasinException();
        }
    }


}