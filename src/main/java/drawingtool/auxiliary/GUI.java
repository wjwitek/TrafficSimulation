package main.java.drawingtool.auxiliary;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;

public class GUI extends JPanel implements ActionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    private Board board;
    private JComboBox<Subsoil> drawType;
    JButton rectangle;
    int squareSize;

    public GUI(JFrame jf, int squareSize) {
        jf.setResizable(false);
        this.squareSize = squareSize;
    }

    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024, 768));

        JPanel buttonPanel = new JPanel();

        JButton save = new JButton("Save");
        save.setActionCommand("Save");
        save.addActionListener(this);

        rectangle = new JButton("Rectangle Mode On");
        rectangle.setActionCommand("Rectangle");
        rectangle.addActionListener(this);

        JButton clear = new JButton("Clear");
        clear.setActionCommand("Clear");
        clear.addActionListener(this);

        drawType = new JComboBox<>(Subsoil.values());
        drawType.addActionListener(this);
        drawType.setActionCommand("drawType");

        buttonPanel.add(save);
        buttonPanel.add(rectangle);
        buttonPanel.add(drawType);
        buttonPanel.add(clear);

        board = new Board(1024, 768 - buttonPanel.getHeight(), squareSize);
        container.add(board, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
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
        }
    }
}
