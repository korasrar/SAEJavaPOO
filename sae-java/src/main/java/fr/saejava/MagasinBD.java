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
            List<Classification> classficationsL = l.getClassifications();

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
            for(Editeur editeur: editeursL){
                rs = st.executeQuery("SELECT * FROM AUTEUR WHERE idauteur"+editeur.getIdEdit());
                if(rs.next()){continue;}
                else{
                    // ajout de l'éditeur si il n'existe pas
                    st.executeUpdate("insert into EDITEUR(nomedit,idedit) values ("+editeur.getNomEdit()+","+editeur.getIdEdit()+");");
                }
            }
            rs = st.executeQuery("SELECT * FROM LIVRE NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR NATURAL JOIN THEMES NATURAL JOIN CLASSIFICATION NATURAL JOIN EDITER NATURAL JOIN EDITEUR WHERE isbn="l)
        }
        /*if(rs.next()){
            int qteExistante = rs.getInt("qte");
            int nouvelleQte = qteExistante+qte;
            st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
        } 
        else {
            st.executeUpdate("INSERT INTO POSSEDER (isbn, idmag, qte) VALUES ("+l.getIsbn()+", " + magasin.getId()+", "+qte+")");
        }*/
        // Requete d'insert de stock, vérifier si le livre est deja dans la bd, sinon le rajouter, si oui modifier la table stock magasin
    }
}





/* Regarder si les éditeurs du livres sont dans la base, idem pour les classification et les auteurs (boucle for)





*/
/* 
    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{
        st = connexion.createStatement();
        rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = '"+magasin.getId()+"'");

    }

    		PreparedStatement pstmt = laConnexion.prepareStatement("INSERT INTO JOUEUR VALUES ("+(maxNumJoueur()+1)+",'"+j.getPseudo()+"','"+j.getMotdepasse()+"','"+value+"','"+j.getMain()+"',"+j.getNiveau()+")");
		pstmt.executeUpdate();
*/
