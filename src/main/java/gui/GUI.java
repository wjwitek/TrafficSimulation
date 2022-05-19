package main.java.gui;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class GUI extends JPanel implements ActionListener, ChangeListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private Board board;
    private JButton startStopSimulation;
    private JSlider simulationSpeed;
    int squareSize;
    private final int maxDelay = 20; //TODO set higher limit after creating simulation, for now, with nothing to do, higher values crash app
    private final Timer timer;
    private int iterNum = 0;
    private final JFrame frame;

    public GUI(JFrame jf, int squareSize) {
        jf.setResizable(false);
        frame = jf;
        timer = new Timer(maxDelay / 5, this);
        timer.stop();
        this.squareSize = squareSize;
    }

    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024, 768));

        JPanel buttonPanel = new JPanel();

        startStopSimulation = new JButton("Start");
        startStopSimulation.setActionCommand("Start");
        startStopSimulation.addActionListener(this);

        simulationSpeed = new JSlider();
        simulationSpeed.setMinimum(0);
        simulationSpeed.setMaximum(maxDelay);
        simulationSpeed.addChangeListener(this);
        simulationSpeed.setValue(maxDelay - timer.getDelay());

        JButton restart = new JButton("Restart");
        restart.setActionCommand("Restart");
        restart.addActionListener(this);

        buttonPanel.add(startStopSimulation);
        buttonPanel.add(simulationSpeed);
        buttonPanel.add(restart);

        board = new Board(1024, 768 - buttonPanel.getHeight(), squareSize);
        container.add(board, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
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
            }
        }
    }

    public void stateChanged(ChangeEvent a){
        timer.setDelay(maxDelay - simulationSpeed.getValue());
    }
}
