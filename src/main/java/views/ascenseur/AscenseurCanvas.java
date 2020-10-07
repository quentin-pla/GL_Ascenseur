package views.ascenseur;

import javax.swing.*;
import java.awt.*;

/**
 * Graphique de l'ascenseur
 */
public class AscenseurCanvas extends JPanel {
    /**
     * Nombre de niveaux
     */
    private int levels;

    /**
     * Hauteur entre chaque niveau
     */
    private int levelGap;

    /**
     * Position verticale de l'ascenseur
     */
    private int ascenseurYPosition;

    /**
     * Ascenseur ouvert ?
     */
    private boolean isOpen;

    /**
     * Départ positionnement horizontal
     */
    private int hStart;

    /**
     * Départ positionnement vertical
     */
    private int vStart;

    /**
     * Constructeur
     * @param levels niveaux
     * @param defaultLevel niveau par défaut
     */
    public AscenseurCanvas(int levels, int defaultLevel) {
        this.levels = levels;
        this.levelGap = 100;
        this.ascenseurYPosition = getAscenseurYPosition(defaultLevel);
        this.isOpen = false;
        this.hStart = 90;
        this.vStart = 100;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, levels*100+150);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, levels*100+150);
        g.setColor(Color.BLACK);
        paintLevels(g);
        paintAscenseur(g);
    }

    /**
     * Obtenir la position en pixels de l'ascenseur en fonction de sa position réelle
     * @param position position réelle
     * @return position de l'ascenseur
     */
    private int getAscenseurYPosition(int position) {
        return (int) ((levels * levelGap + 50) - (levelGap * ((double) position / 100)));
    }

    /**
     * Peindre les niveaux
     * @param g graphique
     */
    private void paintLevels(Graphics g) {
        for (int i = 0; i <= levels; i++) {
            int yPosition = (levels*100+vStart) - (levelGap * i);
            g.drawString("Niveau " + i,hStart-70,yPosition-20);
            g.drawLine(0, yPosition, 300, yPosition);
        }
    }

    /**
     * Peindre l'ascenseur
     * @param g graphique
     */
    private void paintAscenseur(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(hStart+10,ascenseurYPosition,50,50);
        g.setColor(Color.BLACK);
        g.drawRect(hStart+10,ascenseurYPosition,50,50);
        if (!isOpen) g.drawLine(hStart+35, ascenseurYPosition, hStart+35, ascenseurYPosition + 50);
        else g.fillRect(hStart+20,ascenseurYPosition,30,50);
    }

    /*** GETTERS & SETTERS ***/

    public void setOpen(boolean open) {
        isOpen = open;
        repaint();
    }

    public void setAscenseurYPosition(int position) {
        ascenseurYPosition = getAscenseurYPosition(position);
        repaint();
    }
}