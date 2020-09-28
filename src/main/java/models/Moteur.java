package models;

import models.commandes.moteur.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Moteur de l'ascenseur
 */
public class Moteur {
    /**
     * Mode de débugage
     */
    private boolean debugMode = false;

    /**
     * Nombre de niveaux (constant)
     */
    private final int levels;

    /**
     * Niveau actuel
     */
    private AtomicInteger actualLevel;

    /**
     * Prochain arrêt de l'ascenseur
     */
    private AtomicInteger nextStop;

    /**
     * Liste des niveaux à se déplacer pendant la montée
     */
    private volatile PriorityBlockingQueue<Integer> nextUpLevels;

    /**
     * Liste des niveaux à se déplacer pendant la descente
     */
    private volatile PriorityBlockingQueue<Integer> nextDownLevels;

    /**
     * Direction actuelle de l'ascenseur
     */
    private AtomicReference<String> actualDirection;

    /**
     * Arrêt d'urgence
     */
    private AtomicBoolean isEmergencyStopped;

    /**
     * État de l'ouverture
     */
    private AtomicBoolean isOpen;

    /**
     * Listener sur le moteur
     */
    private List<PropertyChangeListener> listener;

    /**
     * Liste des commandes associées
     */
    private ArrayList<CommandeMoteur> commands;

    /**
     * Constructeur par défaut
     */
    public Moteur(int levels, int defaultLevel) {
        this.levels = levels;
        this.actualLevel = new AtomicInteger(defaultLevel);
        this.nextUpLevels = new PriorityBlockingQueue<>(4);
        this.nextDownLevels = new PriorityBlockingQueue<>(4, Comparator.reverseOrder());
        this.actualDirection = new AtomicReference<>("");
        this.isEmergencyStopped = new AtomicBoolean(false);
        this.isOpen = new AtomicBoolean(false);
        this.nextStop = new AtomicInteger(-1);
        this.listener = new ArrayList<>();
        this.commands = new ArrayList<>();
        initCommands();
    }

    /**
     * Relie les commandes système au moteur
     */
    private void initCommands() {
        commands.add(new ArretProchainNiveau(this));
        commands.add(new OuverturePortes(this));
        commands.add(new ArretUrgence(this));
        commands.add(new Descendre(this));
        commands.add(new Monter(this));
    }

    /**
     * Exécuter la commande arrêt prochain niveau
     * @param args paramètres
     */
    public void executeArretProchainNiveau(String... args) {
        getArretProchainNiveau().setArgs(args);
        new Thread(getArretProchainNiveau()).start();
    }

    /**
     * Exécuter la commande ouverture des portes
     */
    public void executeOuverturePortes() { new Thread(getOuverturesPortes()).start(); }

    /**
     * Exécuter la commande arrêt urgence
     * @param args paramètres
     */
    public void executeArretUrgence(String... args) {
        getArretUrgence().setArgs(args);
        new Thread(getArretUrgence()).start();
    }

    /**
     * Exécuter la commande descendre
     */
    public void executeDescendre() {
        new Thread(getDescendre()).start();
    }

    /**
     * Exécuter la commande monter
     */
    public void executeMonter() {
        new Thread(getMonter()).start();
    }

    /**
     * Récupérer le prochain niveau à s'arrêter
     * @return prochain niveau
     */
    public int getNextLevel() {
        final String finalDirection = actualDirection.get();
        int nextLevel = -1;
        if (finalDirection.equals("UP")) {
            final Integer upHead = getNextUpLevels().peek();
            if (upHead != null) nextLevel = upHead;
        } else if (finalDirection.equals("DOWN")) {
            final Integer downHead = getNextDownLevels().peek();
            if (downHead != null) nextLevel = downHead;
        }
        return nextLevel;
    }

    /**
     * Récupérer la prochaine direction de l'ascenseur
     * @return prochaine direction
     */
    public String getNextDirection() {
        String nextDirection = "";
        if (nextUpLevels.isEmpty() && nextDownLevels.isEmpty())
            nextDirection = "";
        else if (nextUpLevels.isEmpty() || actualLevel.get() >= levels)
            nextDirection = "DOWN";
        else if (nextDownLevels.isEmpty() || actualLevel.get() <= 0)
            nextDirection = "UP";
        else System.out.println("ERROR NEXT DIRECTION");
        return nextDirection;
    }

    /**
     * Ajouter un niveau à satisfaire
     * @param direction direction
     * @param nextLevel prochain niveau
     */
    public void addNextLevel(int nextLevel, String direction) {
        if (!nextUpLevels.contains(nextLevel) && !nextDownLevels.contains(nextLevel)) {
            if (actualDirection.get().equals("")) {
                if (nextLevel > actualLevel.get()) {
                    nextUpLevels.add(nextLevel);
                    setActualDirection("UP");
                    setNextStop(nextLevel);
                } else if (nextLevel < actualLevel.get()) {
                    nextDownLevels.add(nextLevel);
                    setActualDirection("DOWN");
                    setNextStop(nextLevel);
                } else {
                    setNextStop(nextLevel);
                }
            } else {
                if (direction.equals("UP")) {
                    if (actualDirection.get().equals("DOWN")) nextUpLevels.add(nextLevel);
                    else if (actualDirection.get().equals("UP")) {
                        if (actualLevel.get() < nextLevel) nextUpLevels.add(nextLevel);
                        else nextDownLevels.add(nextLevel);
                    }
                }
                else if (direction.equals("DOWN")) {
                    if (actualDirection.get().equals("UP")) nextDownLevels.add(nextLevel);
                    else if (actualDirection.get().equals("DOWN")) {
                        if (actualLevel.get() > nextLevel) nextDownLevels.add(nextLevel);
                        else nextUpLevels.add(nextLevel);
                    }
                }
                else {
                    if (nextLevel < actualLevel.get()) nextDownLevels.add(nextLevel);
                    else if (nextLevel > actualLevel.get()) nextUpLevels.add(nextLevel);
                    else executeOuverturePortes();
                }
            }
            if (debugMode) System.out.println("up: " + nextUpLevels + " down: " + nextDownLevels);
        }
    }

    /**
     * Lorsque que le moteur s'est arrêté à un niveau demandé
     */
    public void levelPerformed() {
        if (actualDirection.get().equals("UP")) nextUpLevels.remove();
        else if (actualDirection.get().equals("DOWN")) nextDownLevels.remove();
        setActualDirection(getNextDirection());
        if (debugMode) System.out.println("Next direction: " + getActualDirection());
        setNextStop(getNextLevel());
        if (debugMode) System.out.println("Next Stop: " + getNextStop());
    }

    /**
     * Notifier les listeners
     * @param property propriété
     * @param newValue valeur
     */
    private void notifyListeners(String property, String newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, null, newValue));
        }
    }

    /**
     * Ajouter un listener sur le moteur
     * @param newListener listener
     */
    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    /**
     * Incrémenter le niveau actuel
     */
    public void incrementActualLevel() {
        getActualLevel().getAndIncrement();
        notifyListeners("actualLevel",this.actualLevel.get()+"");
    }

    /**
     * Décrémenter le niveau actuel
     */
    public void decrementActualLevel() {
        getActualLevel().getAndDecrement();
        notifyListeners("actualLevel",this.actualLevel.get()+"");
    }

    /*** GETTERS & SETTERS ***/

    public int getLevels() { return levels; }

    public AtomicInteger getActualLevel() { return actualLevel; }

    public AtomicInteger getNextStop() { return nextStop; }

    public void setNextStop(int nextStop) {
        this.nextStop.getAndSet(nextStop);
        notifyListeners("nextStop",this.nextStop.get()+"");
    }

    public PriorityBlockingQueue<Integer> getNextUpLevels() { return nextUpLevels; }

    public PriorityBlockingQueue<Integer> getNextDownLevels() { return nextDownLevels; }

    public AtomicReference<String> getActualDirection() { return actualDirection; }

    public void setActualDirection(String actualDirection) { this.actualDirection.getAndSet(actualDirection); }

    public AtomicBoolean isEmergencyStopped() { return isEmergencyStopped; }

    public void setEmergencyStopped(boolean emergencyStopped) {
        this.isEmergencyStopped.getAndSet(emergencyStopped);
        notifyListeners("isEmergencyStopped",this.isEmergencyStopped.get()+"");
    }

    public AtomicBoolean getIsOpen() { return isOpen; }

    public void setIsOpen(boolean isOpen) {
        this.isOpen.getAndSet(isOpen);
        notifyListeners("isOpen",this.isOpen.get()+"");
    }

    public CommandeMoteur getArretProchainNiveau() { return commands.get(0); }

    public CommandeMoteur getOuverturesPortes() { return commands.get(1); }

    public CommandeMoteur getArretUrgence() { return commands.get(2); }

    public CommandeMoteur getDescendre() { return commands.get(3); }

    public CommandeMoteur getMonter() { return commands.get(4); }
}
