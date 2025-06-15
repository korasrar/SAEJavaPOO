package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MagasinBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet rs;

    public MagasinBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    /**
     * permet de consulter le catalogue de livres d'un magasin
     * @param magasin le magasin dont on veut consulter le catalogue
     * @return un ensemble contenant les livres du catalogue
     * @throws SQLException si une erreur SQL se produit
     */

    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{ 
        st = connexion.createStatement();
        rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = '"+magasin.getId()+"'");
        // si le livre existe dans la magasin
        if(rs.next()){
            int qteExistante = rs.getInt("qte");
            int nouvelleQte = qteExistante+qte;
            rs.close();
            st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
            st.close();
        }
        // sinon, vérifier les informations du livre et ajouter ce qui n'est pas dans la bdd
        else{
            rs.close();
            // ajout du livre en premier
            st.executeUpdate("insert into LIVRE(isbn, titre, nbpages, datepubli, prix) values ('"+l.getIsbn()+"','"+l.getTitre()+"',"+l.getNbPages()+","+l.getDatePubli()+","+l.getPrix()+");");
            List<Editeur> editeursL = l.getEditeurs();
            List<Auteur> auteursL = l.getAuteurs();
            List<Classification> classificationsL = l.getClassifications();

            // Ajout de tout les nouveaux éditeurs
            for(Editeur editeur: editeursL){
                rs = st.executeQuery("SELECT idedit FROM EDITEUR WHERE idauteur"+editeur.getIdEdit());
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    // ajout de l'éditeur si il n'existe pas
                    st.executeUpdate("insert into EDITEUR(nomedit,idedit) values ('"+editeur.getNomEdit()+"'',"+editeur.getIdEdit()+");");
                    // ajout dans la table association
                    st.executeUpdate("insert into EDITER(isbn,idedit) values ('"+l.getIsbn()+"',"+editeur.getIdEdit()+");");
                    System.out.println("Ajout de l'éditeur "+editeur.getNomEdit());
                }
            }

            // Ajout de tout les nouveaux auteurs
            for(Auteur auteur: auteursL){
                rs = st.executeQuery("SELECT idauteur FROM AUTEUR WHERE idedit"+auteur.getIdAuteur());
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    // ajout de l'auteur si il n'existe pas
                    st.executeUpdate("insert into AUTEUR(idauteur, nomauteur,anneenais,anneedeces) values ('"+auteur.getIdAuteur()+"','"+auteur.getNomAuteur()+"',"+auteur.getAnneeNais()+","+auteur.getAnneeDeces()+");");
                    st.executeUpdate("insert into ECRIRE(isbn,idauteur) values ()");
                    System.out.println("Ajout de l'auteur "+auteur.getNomAuteur());
                }
            }

            // Ajout de toutes les nouvelles classfications
            for(Classification classification : classificationsL){
                rs = st.executeQuery("SELECT * FROM CLASSIFICAtiON WHERE iddewey"+classification.getIdDewey());
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    // ajout de la classification si elle n'existe pas
                    st.executeUpdate("insert into CLASSIFICATION(iddewey, nomclass) values ("+classification+",'"+classification.getNomClass()+"');");
                    st.executeUpdate("insert into THEMES(isbn,iddewey) values ('"+l.getIsbn()+"',"+classification.getIdDewey()+")");
                    System.out.println("Ajout de la classification "+ classification.getNomClass());
                }
            }
            st.close();
        }
    }

    /**
     * permet de charger tous les magasins de la base de données
     * @return la liste des magasins
     * @throws SQLException
     */
    public List<Magasin> chargerMagasin() throws SQLException {
        st = connexion.createStatement();
        rs = st.executeQuery("SELECT * FROM MAGASIN");
        List<Magasin> magasins = new ArrayList<>();
        while(rs.next()) {
            int idMagasin = rs.getInt("idmag");
            String nomMagasin = rs.getString("nommag");
            String villeMagasin = rs.getString("villemag");
            magasins.add(new Magasin(idMagasin, nomMagasin, villeMagasin));
        }
        rs.close();
        st.close();
        return magasins;
    }

    /**
     * permet de trouver le meilleur magasin pour une commande
     * @param commande
     * @return le meilleur magasin
     * @throws SQLException
     */
    public Magasin trouverMeilleurMagasin(Commande commande) throws SQLException {
        int nbLivresDispo = 0;
        int maxLivresDispo = 0;
        Magasin meilleurMagasin = null;
        st = connexion.createStatement();
        List<Magasin> magasins = chargerMagasin();
        for(DetailCommande detail : commande.getContenue()) {
            Livre livre = detail.getLivre();
            int qte = detail.getQte();
            rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' and qte>="+qte);
            if(!rs.next()) {
                rs.close();
                st.close();
                throw new SQLException("Le livre " + livre.getTitre() + " n'est pas disponible dans la base de données.");
            }
            rs.close();
        }
        for(Magasin magasin : magasins){
            nbLivresDispo = 0;
            for(DetailCommande detail : commande.getContenue()) {
                Livre livre = detail.getLivre();
                int qte = detail.getQte();
                rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' and qte>="+qte+" and idmag="+magasin.getId());
                if(rs.next()) {
                    nbLivresDispo++;
                    System.out.println("Livre disponible : " + livre.getTitre() + " en quantité " + qte + " dans le magasin " + magasin.getNom());
                }
                else {
                    System.out.println("Livre non disponible : " + livre.getTitre() + " en quantité " + qte + " dans le magasin " + magasin.getNom());
                }
                rs.close();
                if(nbLivresDispo> maxLivresDispo) {
                    maxLivresDispo = nbLivresDispo;
                    meilleurMagasin = magasin;
                }
            }
        }
        st.close();
        if(meilleurMagasin == null) {
            throw new SQLException("Aucun magasin ne peut satisfaire la commande.");
        }
        System.out.println("Le meilleur magasin pour la commande est : " + meilleurMagasin);
        return meilleurMagasin;
    }

    public List<Magasin> livreDansMagasin(Livre livre) throws SQLException{
        st = connexion.createStatement();
        List<Magasin> allMagasins = chargerMagasin();
        List<Magasin> magasinDispo = new ArrayList<>();
        for (Magasin unMagasin : allMagasins){
            rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' AND idmag ='"+unMagasin.getId());
            if (rs.getInt("qte") > 0){
                magasinDispo.add(unMagasin);
            }
        }
        return magasinDispo;
    }

    //public int getQuantiteLivre(Livre livre){
    //    st = connexion.createStatement();
    //    rs = st.executeQuery("SELECT qte FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' AND idmag ='"+unMagasin.getId());
    //    return rs.getInt("qte");
    //}
}
