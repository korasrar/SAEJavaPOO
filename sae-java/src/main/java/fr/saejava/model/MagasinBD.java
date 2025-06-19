package fr.saejava.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MagasinBD {
    ConnexionMySQL connexion;

    public MagasinBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{ 
        Statement st = connexion.createStatement();
        ResultSet rs = null;
        try {
            // livre
            PreparedStatement pstCheck = connexion.prepareStatement("SELECT qte FROM POSSEDER WHERE isbn = ? AND idmag = ?");
            pstCheck.setString(1, l.getIsbn());
            pstCheck.setInt(2, magasin.getId());
            rs = pstCheck.executeQuery();
            if(rs.next()){
                int qteExistante = rs.getInt("qte");
                int nouvelleQte = qteExistante+qte;
                rs.close();
                PreparedStatement pstUpdate = connexion.prepareStatement("UPDATE POSSEDER SET qte = ? WHERE isbn = ? AND idmag = ?");
                pstUpdate.setInt(1, nouvelleQte);
                pstUpdate.setString(2, l.getIsbn());
                pstUpdate.setInt(3, magasin.getId());
                pstUpdate.executeUpdate();
            }
            else{
                PreparedStatement pstLivreExist = connexion.prepareStatement("SELECT isbn FROM LIVRE WHERE isbn = ?");
                pstLivreExist.setString(1, l.getIsbn());
                rs = pstLivreExist.executeQuery();

                if(!rs.next()){
                    rs.close();
                    PreparedStatement pstInsertLivre = connexion.prepareStatement("INSERT INTO LIVRE(isbn, titre, nbpages, datepubli, prix) VALUES (?, ?, ?, ?, ?)");
                    pstInsertLivre.setString(1, l.getIsbn());
                    pstInsertLivre.setString(2, l.getTitre());
                    pstInsertLivre.setInt(3, l.getNbPages());
                    pstInsertLivre.setString(4, l.getDatePubli());
                    pstInsertLivre.setDouble(5, l.getPrix());
                    pstInsertLivre.executeUpdate();
                }
                else{
                    rs.close();
                }
            
            rs.close();
            PreparedStatement pstInsertPosseder = connexion.prepareStatement("INSERT INTO POSSEDER(isbn, idmag, qte) VALUES (?, ?, ?)");
            pstInsertPosseder.setString(1, l.getIsbn());
            pstInsertPosseder.setInt(2, magasin.getId());
            pstInsertPosseder.setInt(3, qte);
            pstInsertPosseder.executeUpdate();
            List<Editeur> editeursL = l.getEditeurs();
            List<Auteur> auteursL = l.getAuteurs();
            List<Classification> classificationsL = l.getClassifications();
            // editeur
            for(Editeur editeur: editeursL){
                PreparedStatement pstCheckEditeur = connexion.prepareStatement("SELECT idedit FROM EDITEUR WHERE idedit = ?");
                pstCheckEditeur.setInt(1, editeur.getIdEdit());
                rs = pstCheckEditeur.executeQuery();
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    PreparedStatement pstInsertEditeur = connexion.prepareStatement("INSERT INTO EDITEUR(nomedit, idedit) VALUES (?, ?)");
                    pstInsertEditeur.setString(1, editeur.getNomEdit());
                    pstInsertEditeur.setInt(2, editeur.getIdEdit());
                    pstInsertEditeur.executeUpdate();
                    System.out.println("Ajout de l'éditeur "+editeur.getNomEdit());
                    PreparedStatement pstInsertEditer = connexion.prepareStatement("INSERT INTO EDITER(isbn, idedit) VALUES (?, ?)");
                    pstInsertEditer.setString(1, l.getIsbn());
                    pstInsertEditer.setInt(2, editeur.getIdEdit());
                    pstInsertEditer.executeUpdate();
                }
            }
            // auteur
            for(Auteur auteur: auteursL){
                PreparedStatement pstCheckAuteur = connexion.prepareStatement("SELECT idauteur FROM AUTEUR WHERE idauteur = ?");
                pstCheckAuteur.setInt(1, auteur.getIdAuteur());
                rs = pstCheckAuteur.executeQuery();
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    PreparedStatement pstInsertAuteur = connexion.prepareStatement("INSERT INTO AUTEUR(idauteur, nomauteur, anneenais, anneedeces) VALUES (?, ?, ?, ?)");
                    pstInsertAuteur.setInt(1, auteur.getIdAuteur());
                    pstInsertAuteur.setString(2, auteur.getNomAuteur());
                    pstInsertAuteur.setInt(3, auteur.getAnneeNais());
                    pstInsertAuteur.setInt(4, auteur.getAnneeDeces());
                    pstInsertAuteur.executeUpdate();
                    PreparedStatement pstInsertEcrire = connexion.prepareStatement("INSERT INTO ECRIRE(isbn, idauteur) VALUES (?, ?)");
                    pstInsertEcrire.setString(1, l.getIsbn());
                    pstInsertEcrire.setInt(2, auteur.getIdAuteur());
                    pstInsertEcrire.executeUpdate();
                }
            }
            // classification
            for(Classification classification : classificationsL){
                PreparedStatement pstCheckClassification = connexion.prepareStatement("SELECT iddewey FROM CLASSIFICATION WHERE iddewey = ?");
                pstCheckClassification.setInt(1, classification.getIdDewey());
                rs = pstCheckClassification.executeQuery();
                if(rs.next()){
                    rs.close();
                    continue;
                }
                else{
                    rs.close();
                    PreparedStatement pstInsertClassification = connexion.prepareStatement("INSERT INTO CLASSIFICATION(iddewey, nomclass) VALUES (?, ?)");
                    pstInsertClassification.setInt(1, classification.getIdDewey());
                    pstInsertClassification.setString(2, classification.getNomClass());
                    pstInsertClassification.executeUpdate();
                }
            }
        }
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }
    }



    public List<Magasin> chargerMagasin() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet rs = null;
        List<Magasin> magasins = new ArrayList<>();
        try {
            rs = st.executeQuery("SELECT * FROM MAGASIN");
            while(rs.next()) {
                int idMagasin = rs.getInt("idmag");
                String nomMagasin = rs.getString("nommag");
                String villeMagasin = rs.getString("villemag");
                magasins.add(new Magasin(idMagasin, nomMagasin, villeMagasin));
            }
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }
        return magasins;
    }

    public Magasin trouverMeilleurMagasin(Commande commande) throws SQLException {
        int nbLivresDispo = 0;
        int maxLivresDispo = 0;
        Magasin meilleurMagasin = null;
        Statement st = connexion.createStatement();
        ResultSet rs = null;
        try {
            List<Magasin> magasins = chargerMagasin();
            for(DetailCommande detail : commande.getContenue()) {
                Livre livre = detail.getLivre();
                int qte = detail.getQte();
                rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' and qte>="+qte);
                if(!rs.next()) {
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
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }
        if(meilleurMagasin == null) {
            throw new SQLException("Aucun magasin ne peut satisfaire la commande.");
        }
        System.out.println("Le meilleur magasin pour la commande est : " + meilleurMagasin);
        return meilleurMagasin;
    }

    public List<Magasin> livreDansMagasin(Livre livre) throws SQLException{
        Statement st = connexion.createStatement();
        ResultSet rs = null;
        List<Magasin> allMagasins = chargerMagasin();
        List<Magasin> magasinDispo = new ArrayList<>();
        try {
            for (Magasin unMagasin : allMagasins){
                rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' AND idmag ="+unMagasin.getId());
                if(rs.next()){
                    if (rs.getInt("qte") > 0){
                        magasinDispo.add(unMagasin);
                    }
                }
                if (rs != null) rs.close();
            }
        } finally {
            if (st != null) st.close();
        }
        return magasinDispo;
    }

    public int getQuantiteLivre(Livre livre, Magasin unMagasin) throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet rs = st.executeQuery("SELECT qte FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' AND idmag ="+unMagasin.getId());
        if(rs.next()){
            return rs.getInt("qte");
        }
        else {
            return 0; // Livre non trouvé dans le magasin
        }
    }
}

