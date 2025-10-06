package com.ronincompany.discogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class IntroPanel extends JPanel {
    
    private final List<Image> images;
    private int currentImageIndex = 0;
    private float alpha = 0.0f;
    private boolean fadingIn = true;

    private Timer timer;
    
    private List<Runnable> transitionEndListeners = new ArrayList<>();

    public IntroPanel(List<Image> images) {
        this.images = images;
        setBackground(Color.BLACK);
    }

    public void startTransition() {
        timer = new Timer(40, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fadingIn) {
                    alpha += 0.05f;
                    if (alpha >= 1.0f) {
                        alpha = 1.0f;
                        fadingIn = false;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    alpha -= 0.05f;
                    if (alpha <= 0.0f) {
                        alpha = 0.0f;
                        fadingIn = true;
                        currentImageIndex = (currentImageIndex + 1) % images.size();
                    }
                }
                repaint();
                
                if (alpha == 0.0f && currentImageIndex == 0) {
                  
                    for (Runnable listener : transitionEndListeners) {
                        listener.run();
                    }
                }
            }
        });
        timer.start();
    }
    
    public void addTransitionEndListener(Runnable listener) {
        transitionEndListeners.add(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

      
        if (!images.isEmpty()) {
            Image image = images.get(currentImageIndex);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }  
}
