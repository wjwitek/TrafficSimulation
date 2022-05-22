package main.java.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;

public class GUI extends JPanel implements ActionListener, ChangeListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private Board board;
    int squareSize;
    private final int maxDelay = 20; //TODO set higher limit after creating simulation, for now, with nothing to do, higher values crash app
    private final Timer timer;
    private int iterNum = 0;
    private final JFrame frame;
    private Container container;
    private boolean mode = false;
    private JPanel buttonPanel;

    // normal mode buttons
    private JButton startStopSimulation;
    private JSlider simulationSpeed;
    private JButton restart;
    private JButton switchMode;
    private JButton newSource;
    private JFileChooser mapSource;

    // drawing tool buttons
    private JButton save;
    JButton rectangle;
    private JButton clear;
    private JComboBox<Subsoil> drawType;


    public GUI(JFrame jf, int squareSize) {
        jf.setResizable(false);
        frame = jf;
        timer = new Timer(maxDelay / 5, this);
        timer.stop();
        this.squareSize = squareSize;
    }

    public void initialize(Container container) {
        this.container = container;
        this.container.setLayout(new BorderLayout());
        this.container.setSize(new Dimension(1024, 768));

        // initialize normal mode buttons
        buttonPanel = new JPanel();

        startStopSimulation = new JButton("Start");
        startStopSimulation.setActionCommand("Start");
        startStopSimulation.addActionListener(this);

        simulationSpeed = new JSlider();
        simulationSpeed.setMinimum(0);
        simulationSpeed.setMaximum(maxDelay);
        simulationSpeed.addChangeListener(this);
        simulationSpeed.setValue(maxDelay - timer.getDelay());

        switchMode= new JButton("Drawing Tool");
        switchMode.setActionCommand("Switch mode");
        switchMode.addActionListener(this);

        restart = new JButton("Restart");
        restart.setActionCommand("Restart");
        restart.addActionListener(this);

        newSource = new JButton("Open map");
        newSource.setActionCommand("Open");
        newSource.addActionListener(this);
        mapSource = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        buttonPanel.add(newSource);
        buttonPanel.add(startStopSimulation);
        buttonPanel.add(simulationSpeed);
        buttonPanel.add(restart);
        buttonPanel.add(switchMode);

        board = new Board(1024, 768 - buttonPanel.getHeight(), squareSize, "src/main/resources/table.txt");
        this.container.add(board, BorderLayout.CENTER);
        this.container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void changeButtonsToDrawingTool(){
        // remove old buttons
        buttonPanel.remove(startStopSimulation);
        buttonPanel.remove(simulationSpeed);
        buttonPanel.remove(restart);
        buttonPanel.remove(switchMode);

        save = new JButton("Save");
        save.setActionCommand("Save");
        save.addActionListener(this);

        rectangle = new JButton("Rectangle Mode On");
        rectangle.setActionCommand("Rectangle");
        rectangle.addActionListener(this);

        clear = new JButton("Clear");
        clear.setActionCommand("Clear");
        clear.addActionListener(this);

        drawType = new JComboBox<>(Subsoil.values());
        drawType.addActionListener(this);
        drawType.setActionCommand("drawType");

        buttonPanel.add(save);
        buttonPanel.add(rectangle);
        buttonPanel.add(drawType);
        buttonPanel.add(clear);
        buttonPanel.add(switchMode);
    }

    public void changeToNormalMode(){
        // remove drawing tool buttons
        buttonPanel.remove(save);
        buttonPanel.remove(rectangle);
        buttonPanel.remove(drawType);
        buttonPanel.remove(clear);
        buttonPanel.remove(switchMode);

        // add normal mode buttons
        buttonPanel.add(startStopSimulation);
        buttonPanel.add(simulationSpeed);
        buttonPanel.add(restart);
        buttonPanel.add(switchMode);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)){
            iterNum++;
            frame.setTitle("Traffic simulation, iteration: " + iterNum);
            board.iteration();
        }
        else {
            String command = e.getActionCommand();
            switch (command) {
                case "Start" -> {
                    timer.start();
                    startStopSimulation.setText("Stop");
                    startStopSimulation.setActionCommand("Stop");
                }
                case "Stop" -> {
                    timer.stop();
                    startStopSimulation.setText("Start");
                    startStopSimulation.setActionCommand("Start");
                }
                case "Restart" -> {
                    timer.stop();
                    startStopSimulation.setText("Start");
                    startStopSimulation.setActionCommand("Start");
                    System.out.println("Restarting simulation");
                    // TODO actually restart simulation
                    iterNum = 0;
                    frame.setTitle("Traffic simulation, iteration: " + iterNum);
                }
                case "Switch mode" -> {
                    mode = !mode;
                    board.mode = mode;
                    System.out.println(mode);
                    if (mode){
                        timer.stop();
                        switchMode.setText("Normal mode");
                        changeButtonsToDrawingTool();
                    }
                    else{
                        switchMode.setText("Drawing tool");
                        changeToNormalMode();
                    }
                    container.revalidate();
                    container.repaint();
                    buttonPanel.repaint();

                }
                case "Save" -> {
                    try {
                        FileWriter table = new FileWriter("src/main/resources/table.txt");
                        String[][] S = new String[board.points.length][board.points[0].length];
                        for (int j = 0; j < board.points.length; j++) {
                            for (int i = 0; i < board.points[0].length; i++) {
                                S[j][i] = String.valueOf(board.points[j][i].type.toInt());
                            }
                        }
                        table.write(Arrays.deepToString(S));
                        table.close();

                        FileWriter table_pow = new FileWriter("src/main/resources/table pow.txt");
                        StringBuilder SB = new StringBuilder();
                        for (int j = 0; j < board.points[0].length; j++) {
                            for (int i = 0; i < board.points.length; i++) {
                                SB.append(board.points[i][j].type.toInt());
                            }
                            SB.append("\n");
                        }
                        table_pow.write(String.valueOf(SB));
                        table_pow.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
                case "drawType" -> board.editType = (Subsoil) drawType.getSelectedItem();
                case "Clear" -> board.clear();
                case "Rectangle" -> {
                    if(board.rectangleMode==0) {
                        board.rectangleMode = 2;
                        rectangle.setText("Rectangle Mode Off");
                    }
                    else if(board.rectangleMode==2) {
                        board.rectangleMode = 0;
                        rectangle.setText("Rectangle Mode On");
                    }
                }
                case "Open" -> {
                    int returnValue = mapSource.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        try {
                            board.initialize(1024, 768 - buttonPanel.getHeight(), String.valueOf(mapSource.getSelectedFile()));
                            board.repaint();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    board.repaint();
                }
            }
        }
    }

    public void stateChanged(ChangeEvent a){
        timer.setDelay(maxDelay - simulationSpeed.getValue());
    }
}
