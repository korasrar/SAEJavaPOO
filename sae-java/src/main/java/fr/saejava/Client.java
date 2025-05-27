package fr.saejava;

import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

public class Client extends Utilisateur{
    
    private int num;
    private String adresse;
    private String ville;
    private int codePostal;

    public Client(int num, String adresse, String ville, int codePostal, String nom, String prenom, String username, String motDePasse){
        super(nom, prenom, username, motDePasse, Role.CLIENT);
        this.num = num;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
    }
    public int getNum(){ 
        return this.num;
    }
    public String getAdresse(){
        return this.adresse;
    }
    public String getVille(){
        return this.ville;
    }
    public int getCodePostal(){
        return this.codePostal;
    }
    public void setNum(int num){ 
        this.num = num;
    }
    public void setAdresse(String adresse){
        this.adresse = adresse;
    }
    public void setVille(String ville){
        this.ville = ville;
    }
    public void setCodePostal(int codePostal){
        this.codePostal = codePostal;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){return false;}
        if(obj == this){return true;}
        if(!(obj instanceof Client)){return false;}
        Client client = (Client) obj;
        return this.num == client.getNum();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(num);
    }


    // *--------------------------------* //
    // pas sur de cette méthode
    public void modeDeReception(){
    }

    public void editerFacture(Commande commande){
        Document document = new Document();
        try{
            PdfWriter.getInstance(document, new FileOutputStream("./facture/facture_client_"+this.num+"_commande_"+commande.getNumcom()+".pdf"));
            document.open();
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            // Flemme de continuer

        }
        catch(Exception e){
            System.out.println("Erreur lors de la création du fichier PDF");
        }
    }
}