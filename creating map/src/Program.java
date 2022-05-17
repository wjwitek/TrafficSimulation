import javax.swing.JFrame;
import java.io.Serial;

public class Program extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new Program();
    }

    public Program() {
        setTitle("Drawing field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GUI gof = new GUI(this, 24);
        gof.initialize(this.getContentPane());
        this.setSize(1024, 768);
        this.setVisible(true);
    }
}
