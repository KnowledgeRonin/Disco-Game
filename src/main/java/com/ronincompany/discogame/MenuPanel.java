package com.ronincompany.discogame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class MenuPanel extends JPanel {
    
    // === Variables de estado ===
    private boolean transitionFinished = false;  
    private float transitionAlpha = 0.0f;  
    private int currentImageIndex = 0;
    private boolean buttonsAdded = false;
    
    // === Componentes gráficos ===
    private Timer timer;
    private java.util.List<Image> images = new ArrayList<>();  
    private JButton startButton, optionsButton, extrasButton, quitButton;  
    private Image titleImage;
    
    // === Música ===
    private Clip backgroundMusic;
    private List<String> playlist = new ArrayList<>();
    private List<String> shuffledPlaylist = new ArrayList<>();

    // === Constructor ===
    public MenuPanel() {
        
        // Cargar imágenes
        images.add(new ImageIcon("./Game Images/Suite-3-scaled-e1685907493415.jpeg").getImage());
        
        titleImage = new ImageIcon("./Game Images/DISCO GAME.png").getImage();
        
        // Inicializar lista de música
        initializePlaylist();
        
        // Iniciar animación de transición
        setupTransition();
        
        // Inicializar botones
        setupButtons();
        
        // Atajo de teclado: N para cambiar canción
        setupKeyboardShortcut();      
    }
    
    // === Animación de transición de imagen ===
    private void setupTransition() {
        timer = new Timer(30, e -> {
            if (transitionAlpha >= 1f) {
                transitionAlpha = 1f;
                transitionFinished = true;
                timer.stop();

                setupMenuButtons();

                // Reproducir música tras pequeña espera
                new Timer(3400, evt -> {
                    ((Timer) evt.getSource()).stop();
                    repaint();
                    playNextSong();
                }).start();

            } else {
                transitionAlpha = Math.min(transitionAlpha + 0.01f, 1f);
                repaint();
            }
        });

        timer.start();
    }
    
    // === Estilizar botones ===
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE); // Color base
        button.setFont(new Font("Arial", Font.BOLD, 28)); // Cambia según tu estilo

        button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.getForeground().equals(Color.CYAN)) {
                button.setForeground(Color.CYAN);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.getForeground().equals(Color.WHITE)) {
                button.setForeground(Color.WHITE);
            }
        }
    });
    }
    
    // === Inicializar y configurar botones ===
    private void setupButtons() {
        startButton = new JButton("New Game");
        optionsButton = new JButton("Options");
        extrasButton = new JButton("Extras");
        quitButton = new JButton("Quit");
        
        // En estas 4 líneas de código llamo al método "styleButton" para darles estilo e interacción a los botones
        styleButton(startButton);   
        styleButton(optionsButton);
        styleButton(extrasButton);
        styleButton(quitButton);
        
        startButton.addActionListener(e -> startNewGame());
        quitButton.addActionListener(e -> System.exit(0)); 
    }

    private void setupMenuButtons() {
        if (buttonsAdded) return;
        buttonsAdded = true;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(200, 50);
        JButton[] buttons = { startButton, optionsButton, extrasButton, quitButton };
        for (JButton button : buttons) {
            button.setMaximumSize(buttonSize);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        buttonPanel.add(Box.createVerticalStrut(150));
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(optionsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(extrasButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(quitButton);

        add(Box.createVerticalGlue());
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
    
    // === Shortcut de teclado (Letra N) ===
    private void setupKeyboardShortcut() { 
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("N"), "nextTrack");
        getActionMap().put("nextTrack", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNextSong();
            }
        });
    }
    
    // === Dibujar fondo e imagen de título ===
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Explicar qué hace esto
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (!images.isEmpty()) {
            Image image = images.get(currentImageIndex);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transitionFinished ? 1f : transitionAlpha));
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        if (transitionFinished && titleImage != null) {
            int imgWidth = titleImage.getWidth(this);
            int x = (getWidth() - imgWidth) / 2;
            g2d.drawImage(titleImage, x, 50, this);  // Y fijo en 50 px
        }
    }

    // === Lógica del juego ===
    private void startNewGame() {
        System.out.println("Nuevo Juego Iniciado");
     
    }
    
    // === Inicializar playlist de música ===
    private void initializePlaylist() {
    playlist.add("./Game Music/dakiti.wav");
    playlist.add("./Game Music/dejamepensar.wav");
    playlist.add("./Game Music/despuesdela1.wav");
    playlist.add("./Game Music/gataonly.wav");
    playlist.add("./Game Music/mylove.wav");
    playlist.add("./Game Music/partymj.wav");
    playlist.add("./Game Music/peligrosa.wav");
    playlist.add("./Game Music/sinoescontigo.wav");

    shuffledPlaylist = new ArrayList<>(playlist);
    Collections.shuffle(shuffledPlaylist);
    }
   
    // === Reproducir siguiente canción ===
    private void playNextSong() {
        
        try {
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }

       
        if (shuffledPlaylist.isEmpty()) {
            shuffledPlaylist = new ArrayList<>(playlist);
            Collections.shuffle(shuffledPlaylist);
        }

        String nextTrack = shuffledPlaylist.remove(0);  
        File audioFile = new File(nextTrack);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        
        backgroundMusic = AudioSystem.getClip();
        backgroundMusic.open(audioStream);
        backgroundMusic.start();

        backgroundMusic.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                backgroundMusic.close();
                playNextSong();
            }
        });

        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
