package models.commandes.moteur;

public class Descendre extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static Descendre instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new Descendre();
        return instance;
    }

    @Override
    public void execute() {
        System.out.println("Descente");
    }
}
