package models.commandes.moteur;

import models.Moteur;

import java.util.concurrent.atomic.AtomicReference;

public class ArretProchainNiveau extends CommandeMoteur {
    /**
     * Instance unique
     */
    private static AtomicReference<ArretProchainNiveau> instance = null;

    /**
     * Récupérer l'instance unique
     * @return instance
     */
    public static CommandeMoteur getInstance() {
        if (instance == null) instance = new AtomicReference<>(new ArretProchainNiveau());
        return instance.get();
    }

    @Override
    public void run() {
        while (true) {
            //Deux arguments passés contenant la direction et le niveau
            if (!args.isEmpty() && args.element().length == 2) {
                final String[] actualArgs = args.remove();
                getEngine().addNextLevel(checkArgLevel(actualArgs[0]), Moteur.Direction.valueOf(actualArgs[1]));
            }
            if (getEngine().getNextStop().get() == getEngine().getActualLevel().get()) {
                getEngine().levelPerformed();
            }
        }
    }
}
