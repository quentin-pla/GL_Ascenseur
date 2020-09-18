package models.commandes.moteur;

public class Monter extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static Monter instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new Monter();
        return instance;
    }

    @Override
    public void execute() {
        System.out.println("Montée");
    }
}
