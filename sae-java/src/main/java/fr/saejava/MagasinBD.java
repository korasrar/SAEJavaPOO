package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
        }
        // sinon, vérifier les informations du livre et ajouter ce qui n'est pas dans la bdd
        else{
            // ajout du livre en premier
            st.executeUpdate("insert into LIVRE(isbn, titre, nbpages, fatepubli, prix) values ('"+l.getIsbn()+"','"+l.getTitre()+"',"+l.getNbPages()+","+l.getDatePubli()+","+l.getPrix()+");");
            List<Editeur> editeursL = l.getEditeurs();
            List<Auteur> auteursL = l.getAuteurs();
            List<Classification> classificationsL = l.getClassifications();

            // Ajout de tout les nouveaux éditeurs
            for(Editeur editeur: editeursL){
                rs = st.executeQuery("SELECT idedit FROM EDITEUR WHERE idauteur"+editeur.getIdEdit());
                if(rs.next()){continue;}
                else{
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
                if(rs.next()){continue;}
                else{
                    // ajout de l'auteur si il n'existe pas
                    st.executeUpdate("insert into AUTEUR(idauteur, nomauteur,anneenais,anneedeces) values ('"+auteur.getIdAuteur()+"','"+auteur.getNomAuteur()+"',"+auteur.getAnneeNais()+","+auteur.getAnneeDeces()+");");
                    st.executeUpdate("insert into ECRIRE(isbn,idauteur) values ()");
                    System.out.println("Ajout de l'auteur "+auteur.getNomAuteur());
                }
            }

            // Ajout de toutes les nouvelles classfications
            for(Classification classification : classificationsL){
                rs = st.executeQuery("SELECT * FROM CLASSIFICAtiON WHERE iddewey"+classification.getIdDewey());
                if(rs.next()){continue;}
                else{
                    // ajout de la classification si elle n'existe pas
                    st.executeUpdate("insert into CLASSIFICATION(iddewey, nomclass) values ("+classification+",'"+classification.getNomClass()+"');");
                    st.executeUpdate("insert into THEMES(isbn,iddewey) values ('"+l.getIsbn()+"',"+classification.getIdDewey()+")");
                    System.out.println("Ajout de la classification "+ classification.getNomClass());
                }
            }
        }
    }
}
