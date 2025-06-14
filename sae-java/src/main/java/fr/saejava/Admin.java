package fr.saejava;

public class Admin extends Utilisateur{

    private int idAdmin;

    public Admin(int idAdmin, String nom, String prenom, String pseudo, String motDePasse){
        super(idAdmin, nom, prenom, pseudo, motDePasse, Role.admin);
        this.idAdmin = idAdmin;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){return true;}
        if(obj == null){return false;}
        if(!(obj instanceof Admin)){return false;}
        Admin admin = (Admin) obj;
        return idAdmin == admin.idAdmin;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idAdmin);
    }
    
}