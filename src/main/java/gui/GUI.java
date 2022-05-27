package main.java.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JPanel implements ActionListener, ChangeListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private Board board;
    int squareSize, squaresVertically, squaresHorizontally;
    private final int maxDelay = 500; //TODO set higher limit after creating simulation, for now, with nothing to do, higher values crash app
    private final Timer timer;
    private int iterNum = 0;
    private final JFrame frame;
    private Container container;
    private boolean mode = false;
    private JPanel buttonPanel;
    private JSlider simulationSpeed;
    private JFileChooser mapSource;
    private TextField xCords;
    private TextField yCords;

    private JComboBox<Subsoil> drawType;
    Map<ButtonNames, JButton> buttons = new HashMap<>();


    public GUI(JFrame jf, int squareSize) {
        jf.setResizable(false);
        frame = jf;
        timer = new Timer(100, this);
        timer.stop();
        this.squareSize = squareSize;
    }

    public void initialize(Container container, int squaresHorizontally, int squaresVertically) {
        this.squaresVertically = squaresVertically;
        this.squaresHorizontally = squaresHorizontally;

        this.container = container;
        this.container.setLayout(new BorderLayout());
        this.container.setSize(new Dimension(frame.getWidth(), frame.getWidth()));

        buttonPanel = new JPanel();

        // initialize normal mode buttons
        JButton startStopSimulation = initButton("Start", "Start", ButtonNames.StartStopSimulation);
        JButton switchMode= initButton("Drawing tool", "Switch mode", ButtonNames.SwitchMode);
        JButton restart = initButton("Restart", "Restart", ButtonNames.Restart);
        JButton newSource = initButton("Open map", "Open", ButtonNames.NewSource);
        JButton staticField = initButton("Show static field", "Static field", ButtonNames.StaticField);

        // initialize drawing tool buttons
        JButton save = initButton("Save", "Save", ButtonNames.Save);
        JButton rectangle = initButton("Rectangle Mode On", "Rectangle", ButtonNames.Rectangle);
        JButton clear = initButton("Clear", "Clear", ButtonNames.Clear);

        drawType = new JComboBox<>(Subsoil.values());
        drawType.addActionListener(this);
        drawType.setActionCommand("drawType");

        xCords = new TextField();
        xCords.setText("1");
        xCords.setPreferredSize(new Dimension(30,20));
        yCords = new TextField();
        yCords.setText("32");
        yCords.setPreferredSize(new Dimension(30,20));

        simulationSpeed = new JSlider();
        simulationSpeed.setMinimum(0);
        simulationSpeed.setMaximum(maxDelay);
        simulationSpeed.addChangeListener(this);
        simulationSpeed.setValue(maxDelay - timer.getDelay());

        mapSource = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        buttonPanel.add(newSource);
        buttonPanel.add(startStopSimulation);
        buttonPanel.add(staticField);
        buttonPanel.add(xCords);
        buttonPanel.add(yCords);
        buttonPanel.add(simulationSpeed);
        buttonPanel.add(restart);
        buttonPanel.add(switchMode);

        board = new Board(squaresHorizontally+2, squaresVertically+2, squareSize, "src/main/resources/table.txt");
        this.container.add(board, BorderLayout.CENTER);
        this.container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void changeButtonsToDrawingTool(){
        // remove old buttons
        buttonPanel.remove(buttons.get(ButtonNames.StartStopSimulation));
        buttonPanel.remove(buttons.get(ButtonNames.StaticField));
        buttonPanel.remove(xCords);
        buttonPanel.remove(yCords);
        buttonPanel.remove(simulationSpeed);
        buttonPanel.remove(buttons.get(ButtonNames.Restart));
        buttonPanel.remove(buttons.get(ButtonNames.SwitchMode));

        // add new buttons
        buttonPanel.add(buttons.get(ButtonNames.Save));
        buttonPanel.add(buttons.get(ButtonNames.Rectangle));
        buttonPanel.add(drawType);
        buttonPanel.add(buttons.get(ButtonNames.Clear));
        buttonPanel.add(buttons.get(ButtonNames.SwitchMode));
    }

    public void changeToNormalMode(){
        // remove drawing tool buttons
        buttonPanel.remove(buttons.get(ButtonNames.Save));
        buttonPanel.remove(buttons.get(ButtonNames.Rectangle));
        buttonPanel.remove(drawType);
        buttonPanel.remove(buttons.get(ButtonNames.Clear));
        buttonPanel.remove(buttons.get(ButtonNames.SwitchMode));

        // add normal mode buttons
        buttonPanel.add(buttons.get(ButtonNames.StartStopSimulation));
        buttonPanel.add(buttons.get(ButtonNames.StaticField));
        buttonPanel.add(xCords);
        buttonPanel.add(yCords);
        buttonPanel.add(simulationSpeed);
        buttonPanel.add(buttons.get(ButtonNames.Restart));
        buttonPanel.add(buttons.get(ButtonNames.SwitchMode));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)){
            iterNum++;
            frame.setTitle("Traffic simulation, iteration: " + iterNum);
            board.iteration(iterNum);
        }
        else {
            String command = e.getActionCommand();
            switch (command) {
                case "Start" -> {
                    timer.start();
                    buttons.get(ButtonNames.StartStopSimulation).setText("Stop");
                    buttons.get(ButtonNames.StartStopSimulation).setActionCommand("Stop");
                }
                case "Stop" -> {
                    timer.stop();
                    buttons.get(ButtonNames.StartStopSimulation).setText("Start");
                    buttons.get(ButtonNames.StartStopSimulation).setActionCommand("Start");
                }
                case "Restart" -> restartSimulation();
                case "Static field" -> {
                    board.startingPointSF.change(Integer.parseInt(xCords.getText()), Integer.parseInt(yCords.getText()));
                    board.showStaticField = !board.showStaticField;
                    board.repaint();
                }
                case "Switch mode" -> {
                    mode = !mode;
                    board.mode = mode;
                    System.out.println(mode);
                    if (mode){
                        timer.stop();
                        buttons.get(ButtonNames.SwitchMode).setText("Normal mode");
                        changeButtonsToDrawingTool();
                    }
                    else{
                        buttons.get(ButtonNames.SwitchMode).setText("Drawing tool");
                        changeToNormalMode();
                    }
                    container.revalidate();
                    container.repaint();
                    buttonPanel.repaint();

                }
                case "Save" -> saveMap();
                case "drawType" -> board.editType = (Subsoil) drawType.getSelectedItem();
                case "Clear" -> board.clear();
                case "Rectangle" -> {
                    if(board.rectangleMode==0) {
                        board.rectangleMode = 2;
                        buttons.get(ButtonNames.Rectangle).setText("Rectangle Mode Off");
                    }
                    else {
                        board.rectangleMode = 0;
                        buttons.get(ButtonNames.Rectangle).setText("Rectangle Mode On");
                    }
                }
                case "Open" -> {
                    int returnValue = mapSource.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        try {
                            board.initialize(String.valueOf(mapSource.getSelectedFile()));
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

    private JButton initButton(String text, String command, ButtonNames key){
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        buttons.put(key, button);
        return button;
    }

    private void restartSimulation(){
        timer.stop();
        buttons.get(ButtonNames.StartStopSimulation).setText("Start");
        buttons.get(ButtonNames.StartStopSimulation).setActionCommand("Start");
        System.out.println("Restarting simulation");
        // TODO actually restart simulation
        iterNum = 0;
        frame.setTitle("Traffic simulation, iteration: " + iterNum);
    }

    private void saveMap(){
        // TODO save map to custom filename, not always the same
        try {
            FileWriter table = new FileWriter("src/main/resources/table.txt");
            String[][] S = new String[board.points.length][board.points[0].length];
            for (int j = 0; j < board.points[0].length; j++) {
                for (int i = 0; i < board.points.length; i++) {
                    S[i][j] = String.valueOf(board.points[i][j].type.toInt());
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
//        System.exit(0);
    }
}
