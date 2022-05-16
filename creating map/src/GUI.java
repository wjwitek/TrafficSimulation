import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ActionListener, ChangeListener {
    private static final long serialVersionUID = 1L;
    private Timer timer;
    private Board board;
    private JButton start;
    private JButton clear;
    private JComboBox<Integer> drawType;
    private JSlider pred;
    private JFrame frame;
    private int iterNum = 0;
    private final int maxDelay = 500;
    private final int initDelay = 100;
    private boolean running = false;

    public GUI(JFrame jf) {
        frame = jf;
        timer = new Timer(initDelay, this);
        timer.stop();
    }

    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024, 768));

        JPanel buttonPanel = new JPanel();

        start = new JButton("Start");
        start.setActionCommand("Start");
        start.addActionListener(this);

        clear = new JButton("Calc Field");
        clear.setActionCommand("clear");
        clear.addActionListener(this);

        pred = new JSlider();
        pred.setMinimum(0);
        pred.setMaximum(maxDelay);
        pred.addChangeListener(this);
        pred.setValue(maxDelay - timer.getDelay());

        drawType = new JComboBox<Integer>(Point.types);
        drawType.addActionListener(this);
        drawType.setActionCommand("drawType");

        buttonPanel.add(start);
        buttonPanel.add(clear);
        buttonPanel.add(drawType);
        buttonPanel.add(pred);

        board = new Board(1024, 768 - buttonPanel.getHeight());
        container.add(board, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(timer)) {
            iterNum++;
            frame.setTitle("Sound simulation (" + Integer.toString(iterNum) + " iteration)");
            board.iteration();
        } else {
            String command = e.getActionCommand();
            if (command.equals("Start")) {
                try {
                    FileWriter myWriter = new FileWriter("table.txt");
//                    StringBuilder S = new StringBuilder() ;
                    String[][] S = new String[board.points.length][board.points[0].length];
                    for(int j=0;j<board.points.length;j++){
                        for(int i=0;i<board.points[0].length;i++){
                            S[j][i] = String.valueOf(board.points[j][i].type);
                        }
                    }
                    myWriter.write(Arrays.deepToString(S));
                    myWriter.close();
                    FileWriter myWriter2 = new FileWriter("table pow.txt");
                    StringBuilder S2 = new StringBuilder() ;
//                    String[][] S2 = new String[board.points.length][board.points[0].length];
                    for(int j=0;j<board.points[0].length;j++){
                        for(int i=0;i<board.points.length;i++){
                            S2.append(String.valueOf(board.points[i][j].type));
                        }
                        S2.append("\n");
                    }
                    myWriter2.write(String.valueOf(S2));
                    myWriter2.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

//                try {
//                    if (myObj.createNewFile()) {
//                        System.out.println("File created: " + myObj.getName());
//                    } else {
//                        System.out.println("File already exists.");
//                    }
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }

                if (!running) {
//                    timer.start();
                    start.setText("Pause");
                } else {
//                    timer.stop();
                    start.setText("Start");
                }
                running = !running;
                clear.setEnabled(true);
                System.exit(0);
            } else if (command.equals("clear")) {
                iterNum = 0;
                timer.stop();
                start.setEnabled(true);
                board.clear();
                frame.setTitle("Cellular Automata Toolbox");
            }
            else if (command.equals("drawType")){
                int newType = (Integer)drawType.getSelectedItem();
                board.editType = newType;
            }

        }
    }

    public void stateChanged(ChangeEvent e) {
        timer.setDelay(maxDelay - pred.getValue());
    }
}
