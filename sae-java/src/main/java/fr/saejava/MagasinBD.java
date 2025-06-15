package fr.saejava;

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
            rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = '"+magasin.getId()+"'");
            if(rs.next()){
                int qteExistante = rs.getInt("qte");
                int nouvelleQte = qteExistante+qte;
                rs.close();
                st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
            }
            else{
                rs.close();
                st.executeUpdate("insert into LIVRE(isbn, titre, nbpages, datepubli, prix) values ('"+l.getIsbn()+"','"+l.getTitre()+"',"+l.getNbPages()+","+l.getDatePubli()+","+l.getPrix()+");");
                List<Editeur> editeursL = l.getEditeurs();
                List<Auteur> auteursL = l.getAuteurs();
                List<Classification> classificationsL = l.getClassifications();

                for(Editeur editeur: editeursL){
                    rs = st.executeQuery("SELECT idedit FROM EDITEUR WHERE idauteur"+editeur.getIdEdit());
                    if(rs.next()){
                        rs.close();
                        continue;
                    }
                    else{
                        rs.close();
                        st.executeUpdate("insert into EDITEUR(nomedit,idedit) values ('"+editeur.getNomEdit()+"'',"+editeur.getIdEdit()+");");
                        st.executeUpdate("insert into EDITER(isbn,idedit) values ('"+l.getIsbn()+"',"+editeur.getIdEdit()+");");
                        System.out.println("Ajout de l'éditeur "+editeur.getNomEdit());
                    }
                }

                for(Auteur auteur: auteursL){
                    rs = st.executeQuery("SELECT idauteur FROM AUTEUR WHERE idedit"+auteur.getIdAuteur());
                    if(rs.next()){
                        rs.close();
                        continue;
                    }
                    else{
                        rs.close();
                        st.executeUpdate("insert into AUTEUR(idauteur, nomauteur,anneenais,anneedeces) values ('"+auteur.getIdAuteur()+"','"+auteur.getNomAuteur()+"',"+auteur.getAnneeNais()+","+auteur.getAnneeDeces()+");");
                        st.executeUpdate("insert into ECRIRE(isbn,idauteur) values ()");
                        System.out.println("Ajout de l'auteur "+auteur.getNomAuteur());
                    }
                }

                for(Classification classification : classificationsL){
                    rs = st.executeQuery("SELECT * FROM CLASSIFICAtiON WHERE iddewey"+classification.getIdDewey());
                    if(rs.next()){
                        rs.close();
                        continue;
                    }
                    else{
                        rs.close();
                        st.executeUpdate("insert into CLASSIFICATION(iddewey, nomclass) values ("+classification+",'"+classification.getNomClass()+"');");
                        st.executeUpdate("insert into THEMES(isbn,iddewey) values ('"+l.getIsbn()+"',"+classification.getIdDewey()+")");
                        System.out.println("Ajout de la classification "+ classification.getNomClass());
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
}

    //public int getQuantiteLivre(Livre livre){
    //    st = connexion.createStatement();
    //    rs = st.executeQuery("SELECT qte FROM POSSEDER WHERE isbn ='"+livre.getIsbn()+"' AND idmag ='"+unMagasin.getId());
    //    return rs.getInt("qte");
    //}

