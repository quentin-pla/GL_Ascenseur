package models.commandes.moteur;

public class ArretProchainNiveau extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static ArretProchainNiveau instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new ArretProchainNiveau();
        return instance;
    }

    @Override
    public void execute() {
        System.out.println("Arret prochain niveau");
    }
}
