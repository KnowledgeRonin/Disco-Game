package com.ronincompany.discogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class DiscoGame {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
        JFrame GUI = new JFrame();
        GUI.setTitle("Disco Game");
        GUI.setSize(1366, 768);
        GUI.setResizable(false);
        GUI.setLocationRelativeTo(null);
        GUI.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        GUI.setIconImage(Toolkit.getDefaultToolkit().getImage("./Game Images/[004583].jpg"));

        List<Image> images = new ArrayList<>();
        images.add(new ImageIcon("./Game Images/Triggered Studios Cover.jpg").getImage());
        images.add(new ImageIcon("./Game Images/Coringa Games Cover.jpg").getImage());
        images.add(new ImageIcon("./Game Images/Cuico Entertainment Cover.jpg").getImage());

        IntroPanel fadePanel = new IntroPanel(images);
        GUI.add(fadePanel);
        GUI.setVisible(true);

        fadePanel.startTransition();
        
        MenuPanel menuPanel = new MenuPanel();
        
        fadePanel.addTransitionEndListener(() -> {
                
                GUI.setContentPane(menuPanel);
                GUI.revalidate();  
                GUI.repaint();     
            });
        });
    }
}
