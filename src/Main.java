import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new MyFrame("HakkaEditor");
        frame.setSize(700, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        System.out.println("我耖");
    }
}
