package models;

import models.partie_operative.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Moteur de traction de l'ascenseur
 */
public class MoteurTraction {
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
     * Position actuelle de l'ascenseur
     */
    private AtomicInteger actualPosition;

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
    private ArrayList<CommandeSysteme> commands;

    /**
     * Exécuteur
     */
    private Executor executor;

    /**
     * Constructeur par défaut
     */
    public MoteurTraction(int levels, int defaultLevel) {
        this.levels = levels;
        this.actualLevel = new AtomicInteger(defaultLevel);
        this.actualPosition = new AtomicInteger(10*defaultLevel);
        this.nextUpLevels = new PriorityBlockingQueue<>(4);
        this.nextDownLevels = new PriorityBlockingQueue<>(4, Comparator.reverseOrder());
        this.actualDirection = new AtomicReference<>("");
        this.isEmergencyStopped = new AtomicBoolean(false);
        this.isOpen = new AtomicBoolean(false);
        this.listener = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.executor = Executors.newWorkStealingPool();
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
        executor.execute(getArretProchainNiveau());
    }

    /**
     * Exécuter la commande ouverture des portes
     */
    public void executeOuverturePortes() { executor.execute(getOuverturesPortes()); }

    /**
     * Exécuter la commande arrêt urgence
     * @param args paramètres
     */
    public void executeArretUrgence(String... args) {
        getArretUrgence().setArgs(args);
        executor.execute(getArretUrgence());
    }

    /**
     * Exécuter la commande descendre
     */
    public void executeDescendre() {
        executor.execute(getDescendre());
    }

    /**
     * Exécuter la commande monter
     */
    public void executeMonter() {
        executor.execute(getMonter());
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
        if (nextUpLevels.isEmpty() && nextDownLevels.isEmpty())
            return "";
        else if (nextUpLevels.isEmpty())
            return "DOWN";
        else return "UP";
    }

    /**
     * Se déplacer au prochain niveau
     */
    public void moveToNextLevel() {
        if (debugMode) System.out.println("up: " + nextUpLevels + " down: " + nextDownLevels);
        setActualDirection(getNextDirection());
        if (debugMode) System.out.println("Next direction: " + getActualDirection());
        if (debugMode) System.out.println("Next level: " + getNextLevel());
        if (getNextLevel() != -1 && !debugMode) System.out.println("Arrêt prochain niveau :" + getNextLevel());
        switch (actualDirection.get()) {
            case "UP":   executeMonter();break;
            case "DOWN": executeDescendre();break;
            default: if (!debugMode) System.out.println("Attente du prochain arrêt.");break;
        }
    }

    /**
     * Lorsque que le moteur s'est arrêté à un niveau demandé
     */
    public void levelPerformed() {
        final String direction = actualDirection.get();
        if (direction.equals("UP")) nextUpLevels.remove();
        else if (direction.equals("DOWN")) nextDownLevels.remove();
        moveToNextLevel();
    }

    /**
     * Incrémenter la position actuelle
     */
    public void incrementActualPosition() {
        getActualPosition().getAndIncrement();
        notifyListeners("actualPosition",this.actualPosition.get()+"");
        checkActualPosition();
    }

    /**
     * Décrémenter la position actuelle
     */
    public void decrementActualPosition() {
        getActualPosition().getAndDecrement();
        notifyListeners("actualPosition",this.actualPosition.get()+"");
        checkActualPosition();
    }

    /**
     * Vérifier la position actuelle
     */
    public void checkActualPosition() {
        if (!isMoving()) {
            setActualLevel(actualPosition.get() / 100);
            if (!debugMode) System.out.println("Niveau " + actualLevel);
            final String direction = actualDirection.get();
            final int level = actualLevel.get();
            if (direction.equals("UP") && level == getNextLevel() ||
                    direction.equals("DOWN") && level == getNextLevel()) {
                executeOuverturePortes();
            }
        }
    }

    /**
     * Vérifier si l'ascenseur est en mouvement
     * @return booléen
     */
    public boolean isMoving() {
        final double position = (double) actualPosition.get() / 100;
        return (position % 1) != 0;
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

    /*** GETTERS & SETTERS ***/

    public int getLevels() { return levels; }

    public AtomicInteger getActualLevel() { return actualLevel; }

    public AtomicInteger getActualPosition() { return actualPosition; }

    public void setActualLevel(int actualLevel) {
        this.actualLevel.getAndSet(actualLevel);
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

    public boolean isDebugMode() { return debugMode; }

    public CommandeSysteme getArretProchainNiveau() { return commands.get(0); }

    public CommandeSysteme getOuverturesPortes() { return commands.get(1); }

    public CommandeSysteme getArretUrgence() { return commands.get(2); }

    public CommandeSysteme getDescendre() { return commands.get(3); }

    public CommandeSysteme getMonter() { return commands.get(4); }
}
