package fr.saejava;

import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Class qui permet de récupérer les nom et prénom des clients pour créer des username et mdp
 */
public class ChangementBD {
    public static void main(String[] args) {
        try{
        ConnexionMySQL connexion = new ConnexionMySQL();
        connexion.connecter("servinfo-maria", "Librairie", "maubert", "maubert");
        Statement st = connexion.createStatement();
        ResultSet r = st.executeQuery("SELECT * FROM CLIENT");
        // Création du fichier
        File myObj = new File("insertUtilisateur.txt");
        if (myObj.createNewFile()) {
            System.out.println("Fichier créer: " + myObj.getName());
        } else {
            System.out.println("File already exists.");
        }
        // Ecriture dans le fichier
        FileWriter myWriter = new FileWriter("insertUtilisateur.txt");
        myWriter.write("insert into UTILISATEUR(idutilisateur, nom, prenom, username, motdepasse) values"+"\n");
        while(r.next()){
            String username = r.getString("prenomcli")+r.getInt("idcli");
            String password = Math.abs(r.getString("adressecli").hashCode())+r.getString("nomcli");
            myWriter.write("("+r.getInt("idcli")+",'"+r.getString("nomcli")+"','"+r.getString("prenomcli")+"','"+username+"','"+password+"'"+"),"+"\n");
        }
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
    }
}
