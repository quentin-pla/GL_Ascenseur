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
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!args.isEmpty() && args.element().length == 1) {
                final String[] actualArgs = args.remove();
                System.out.println(actualArgs[0].equals("true") ? "Interruption du moteur." : "Reprise du moteur.");
                getEngine().setEmergencyStopped(Boolean.parseBoolean(actualArgs[0]));
            }
        }
    }
}
