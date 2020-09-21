package models.commandes.moteur;

import java.util.concurrent.atomic.AtomicReference;

public class ArretUrgence extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<ArretUrgence> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new AtomicReference<>(new ArretUrgence());
        return instance.get();
    }

    @Override
    public void run() {
        System.out.println("Arret urgence");
    }
}
