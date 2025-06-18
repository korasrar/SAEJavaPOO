package fr.saejava.model;

import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class Client extends Utilisateur{
    
    private int num;
    private String adresse;
    private String ville;
    private int codePostal;
    private Commande panier;

    public Client(int num, String adresse, String ville, int codePostal, String nom, String prenom, String username, String motDePasse){
        super(num, nom, prenom, username, motDePasse, Role.client);
        this.num = num;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        try{
        creeCommande(new Date(System.currentTimeMillis()));
        }
        catch (SQLException e){
            System.out.println("Erreur lors de la création de la commande: " + e.getMessage());
        }
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

    /**
     * édite la facture d'une commande et peut etre genérer un PDF
     * @param commande la commande à éditer
     */
    public void editerFacture(Commande commande){
        Document document = new Document();
        try{
            PdfWriter.getInstance(document, new FileOutputStream("./facture/facture_client_"+this.num+"_commande_"+commande.getNumcom()+".pdf"));
            document.open();
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font paraFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            // header
            document.add(new Paragraph("FACTURE", titleFont));
            document.add(new Paragraph(" "));
            // info client
            document.add(new Paragraph(this.getNom()+ " " + this.getPrenom(), headerFont));
            document.add(new Paragraph(this.adresse, paraFont));
            document.add(new Paragraph(this.ville + " " +this.codePostal, paraFont));
            document.add(new Paragraph("Commande n° " + commande.getNumcom(), paraFont));
            document.add(new Paragraph(" "));
            
            // info du livre
            document.add(new Paragraph("ISBN | Titre | Auteur | Quantité | Prix | Prix total", headerFont));
            document.add(new Paragraph("--------------------------------------------------------------", paraFont));
            
            double total = 0;
            int totalLivres = 0;
            
            // parcours de la commande
            for(DetailCommande detail : commande.getContenue()) {
                Livre livre = detail.getLivre();
                int quantite = detail.getQte();
                double prixUnitaire = livre.getPrix();
                double prixTotal = detail.getPrixVente();
                
                total += prixTotal;
                totalLivres += quantite;
                
                // je prend le premier auteur du livre si yen a un
                String auteur = "";
                if(!livre.getAuteurs().isEmpty()) {
                    auteur = livre.getAuteurs().get(0).getNomAuteur();
                }
                
                String lignePdf = livre.getIsbn() + " | " +livre.getTitre() + " | " + auteur + " | " + quantite + " | " + prixUnitaire + " | " + prixTotal;
                document.add(new Paragraph(lignePdf, paraFont));
            }
            document.add(new Paragraph("--------------------------------------------------------------", paraFont));
            document.add(new Paragraph("Total: " + total + "€", headerFont));
            document.add(new Paragraph("Nombre de livres: " + totalLivres, paraFont));
            
        }
        catch(Exception e){
            System.out.println("Erreur lors de la création du fichier PDF: " + e.getMessage());
        }
        finally {
            // ferme document dans tout les cas
            if(document.isOpen()) {
                document.close();
            }
        }
    }

    void creeCommande(Date date) throws SQLException {
        panier = new Commande(0,date,this,null, null);
    }

    public void ajouterLivre(Livre livre, int quantite) {
        panier.ajouterDetailCommande(new DetailCommande(quantite, livre, panier));
    }
}