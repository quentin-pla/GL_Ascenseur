package models.commandes.moteur;

public class ArretUrgence extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static ArretUrgence instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new ArretUrgence();
        return instance;
    }

    @Override
    public void execute() {
        System.out.println("Arret urgence");
    }
}
