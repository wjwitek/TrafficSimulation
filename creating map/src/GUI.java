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
    private JComboBox<Integer> drawType;

    public GUI(JFrame jf) {
        jf.setResizable(false);
    }

    public void initialize(Container container) {
        container.setLayout(new BorderLayout());
        container.setSize(new Dimension(1024, 768));

        JPanel buttonPanel = new JPanel();

        JButton save = new JButton("Save");
        save.setActionCommand("Save");
        save.addActionListener(this);

        JButton clear = new JButton("Clear");
        clear.setActionCommand("Clear");
        clear.addActionListener(this);

        drawType = new JComboBox<Integer>(Point.types);
        drawType.addActionListener(this);
        drawType.setActionCommand("drawType");

        buttonPanel.add(save);
        buttonPanel.add(drawType);
        buttonPanel.add(clear);


        board = new Board(1024, 768 - buttonPanel.getHeight(), 24);
        container.add(board, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Save" -> {
                try {
                    FileWriter myWriter = new FileWriter("table.txt");
//                    StringBuilder S = new StringBuilder() ;
                    String[][] S = new String[board.points.length][board.points[0].length];
                    for (int j = 0; j < board.points.length; j++) {
                        for (int i = 0; i < board.points[0].length; i++) {
                            S[j][i] = String.valueOf(board.points[j][i].type);
                        }
                    }
                    myWriter.write(Arrays.deepToString(S));
                    myWriter.close();
                    FileWriter myWriter2 = new FileWriter("table pow.txt");
                    StringBuilder S2 = new StringBuilder();
//                    String[][] S2 = new String[board.points.length][board.points[0].length];
                    for (int j = 0; j < board.points[0].length; j++) {
                        for (int i = 0; i < board.points.length; i++) {
                            S2.append(String.valueOf(board.points[i][j].type));
                        }
                        S2.append("\n");
                    }
                    myWriter2.write(String.valueOf(S2));
                    myWriter2.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
            case "drawType" -> {
                board.editType = (int) (Integer) drawType.getSelectedItem();
            }
            case "Clear" -> board.clear();
        }
    }
}
