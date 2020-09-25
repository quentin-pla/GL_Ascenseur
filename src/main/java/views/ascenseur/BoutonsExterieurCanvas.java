package views.ascenseur;

import views.VueAscenseur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoutonsExterieurCanvas extends JPanel implements MouseListener {
    /**
     * Vue
     */
    private VueAscenseur view;

    /**
     * Boutons
     */
    private Map<String,Map<String,Shape>> buttons;

    /**
     * Boutons actifs
     */
    private ArrayList<String> activeButtons;

    /**
     * Niveaux
     */
    private int levels;

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
     * @param view vue
     * @param levels niveaux
     */
    public BoutonsExterieurCanvas(VueAscenseur view, int levels) {
        this.view = view;
        this.levels = levels;
        this.buttons = new HashMap<>();
        this.activeButtons = new ArrayList<>();
        this.hStart = 0;
        this.vStart = 30;
        addMouseListener(this);
        initButtons();
    }

    /**
     * Initialisation des boutons
     */
    private void initButtons() {
        for (int i = levels; i >= 0; i--) {
            int gap = (100*levels)-(i*100);
            Map<String,Shape> map = new HashMap<>();
            map.put("UP",new Ellipse2D.Double(hStart+20,vStart+20+gap, 40, 40));
            map.put("DOWN",new Ellipse2D.Double(hStart+70,vStart+20+gap, 40, 40));
            buttons.put(i+"",map);
        }
    }

    /**
     * Dimensions
     * @return dimensions
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(185, 620);
    }

    /**
     * Dessiner les composants
     * @param g graphique
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, 185, 600);
        g2d.setColor(Color.white);
        paintButtons(g2d);
    }

    /**
     * Dessiner les boutons
     * @param g graphique
     */
    private void paintButtons(Graphics2D g) {
        for (Map.Entry<String, Map<String,Shape>> level : buttons.entrySet()) {
            for (Map.Entry<String,Shape> button : level.getValue().entrySet()) {
                final Rectangle bounds = button.getValue().getBounds();
                if (activeButtons.contains(level.getKey() + button.getKey())) {
                    g.setColor(Color.ORANGE);
                    g.fillOval(bounds.x, bounds.y, 40, 40);
                    g.setColor(Color.WHITE);
                }
                g.draw(button.getValue());
                g.drawString(button.getKey().equals("UP") ? "↑" : "↓",bounds.x+16,bounds.y+24);
            }
        }
    }

    /**
     * Supprimer un bouton actif
     * @param button bouton
     */
    public void clearActiveButton(String button) {
        if (button.length() == 1) {
            activeButtons.remove(button+"UP");
            activeButtons.remove(button+"DOWN");
        } else {
            activeButtons.remove(button);
        }
        repaint();
    }

    /**
     * Nouvelle saisie utilisateur
     * @param value valeur
     */
    private void newInput(String value) {
        if (!activeButtons.contains(value)) activeButtons.add(value);
        view.notifyListeners("buttonInput", value);
        repaint();
    }

    /**
     * Lorsqu'un click a été effectué
     * @param e évènement
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        for (Map.Entry<String,Map<String,Shape>> level : buttons.entrySet())
            for (Map.Entry<String, Shape> button : level.getValue().entrySet())
                if (button.getValue().contains(p))
                    newInput(level.getKey() + button.getKey());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}