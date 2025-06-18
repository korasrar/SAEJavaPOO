# SAEJavaPOO  

Système de gestion de librairie développé en Java avec MySQL.

## Fonctionnalité à implémenter  
### Client 👤
- [X] Passer une commande (en magasin ou en ligne)  
- [X] Choisir le mode de réception (retrait en magasin ou livraison à domicile)  
- [X] Consulter le catalogue des livres disponibles  
### Vendeur 🏪  
- [X] Ajouter un livre au stock de sa librairie  
- [X] Mettre à jour la quantité disponible d’un livre  
- [X] Vérifier la disponibilité d’un livre dans une librairie  
- [ ] Passer une commande pour un client en magasin  
- [X] Transférer un livre d’une autre librairie pour satisfaire une commande client.  
### Administrateur ou administratrice 🛠️  
- [X] Créer un compte vendeur et l’affecter à une librairie  
- [X] Ajouter une nouvelle librairie au réseau  
- [ ] Gérer les stocks globaux de toutes les librairies  
- [ ] Consulter les statistiques de vente  

**L’application devra permettre d’accéder aux diverses informations demandées dans le
tableau de bord de votre SAE « Exploitation d’une base de données », de gérer les palmarès (livres, vendeurs, boutiques) et d’éditer des factures au format PDF**

## 🚀 Lancement Rapide avec Docker

### Prérequis
- Docker
- Docker Compose
- Vérifier que le port 3306 est disponible !

### Commandes de lancement

```bash
# Cloner le projet
git clone https://github.com/korasrar/SAEJavaPOO.git
cd SAEJavaPOO

# Compiler le projet Java en .jar
cd sae-java
mvn clean package
cd ..

# Build l'application avec Docker Compose
docker-compose build

# Lancer l'application
docker-compose run --rm app
```
