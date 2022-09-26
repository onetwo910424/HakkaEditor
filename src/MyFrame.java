import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;

public class MyFrame extends JFrame {
    File lastFile = new File("未命名.txt");
    File lastDir = null;
    JPanel top = new JPanel();
    JPanel under = new JPanel();
    //JTextArea area = new JTextArea();
    JTextArea area = new JTextArea();
    JMenuItem save = new JMenuItem("儲存檔案");
    int textCount = 0;
    int lineCount = 0;
    JLabel label1 = new JLabel("字數:" + textCount + " | 行數:" + lineCount);
    JPanel lineView = new JPanel();
    boolean typed = false;

    public MyFrame(String title) {
        super(title);
        JPanel root = new JPanel(new BorderLayout());
        setResizable(false);
        setContentPane(root);
        root.setLayout(new SimpleLayout());
        under.setBackground(Color.WHITE);
        Border border = new MatteBorder(0, 0, 3, 0, Color.GRAY);
        top.setBorder(border);
        top.setBackground(Color.LIGHT_GRAY);
        under.setLayout(new BorderLayout());

        MenuActionLister menuActionLister = new MenuActionLister();
        area.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println(e);
//                if (e.getKeyCode()== ?? & e.isControlDown()) {

//                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setTitle("*" + lastFile.getName());
                label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
                typed = true;
            }
        });
        root.add(lineView);
        root.add(under);
        root.add(top);


        JMenu menu = new JMenu("檔案");
        JMenuItem create = new JMenuItem("新增檔案");
        create.addActionListener(menuActionLister);
        create.setActionCommand("create");

        save.setEnabled(false);
        save.addActionListener(menuActionLister);
        save.setActionCommand("save");
        JMenuItem saveAs = new JMenuItem("另存新檔");
        saveAs.addActionListener(menuActionLister);
        saveAs.setActionCommand("saveAs");
        JMenuItem load = new JMenuItem("開啟舊檔");
        load.addActionListener(menuActionLister);
        load.setActionCommand("load");

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        menu.add(create);
        menu.add(save);
        menu.add(saveAs);
        menu.add(load);
        area.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(area);

        this.setJMenuBar(menuBar);
        lineView.setBorder(new MatteBorder(1, 0, 0, 0, Color.WHITE));
        lineView.setBackground(new Color(237, 237, 237));
        under.add(scrollPane);
        lineView.setLayout(null);
        label1.setBounds(580, 0, 100, 22);
        lineView.add(label1);

    }
    private class MenuActionLister implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "create":
                    System.out.println("** create");
                    create();
                    break;
                case "saveAs":
                    System.out.println("** saveAs");
                    saveFileAs();
                    break;
                case "load":
                    System.out.println("** load");
                    try {
                        readFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "save":
                    System.out.println("** saveFile");
                    System.out.println(lastFile);
                    saveFile();
                    break;
            }
        }
    }

    private void create() {
        int cho = 1;
        if (typed)
            cho = JOptionPane.showConfirmDialog(this, "是否儲存此次變更?", "HakkaEditor", JOptionPane.YES_NO_CANCEL_OPTION);
        switch (cho) {
            case 0:
                saveFileAs();
                lastFile = new File("未命名.txt");
                lastDir = null;
                save.setEnabled(false);
                setTitle(lastFile.getName());
                typed = false;
                label1.setText("字數:" + area.getText().length()+ " | 行數:" + area.getLineCount());
                break;
            case 1:
                lastFile = new File("未命名.txt");
                lastDir = null;
                save.setEnabled(false);
                area.setText("");
                setTitle(lastFile.getName());
                typed = false;
                label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
                break;
            case 2:
                break;
        }

    }

    private void saveFile() {
        System.out.println("** saveFile");
        setTitle(lastFile.getName());
        typed = false;
        try {
            String text = area.getText();
            FileWriter fw = new FileWriter(lastFile);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFileAs() {
        System.out.println("** saveFileAs");

        String text = area.getText();
        JFileChooser fileChooser = new JFileChooser();
        File cf = new File("*.txt");
        fileChooser.setSelectedFile(cf);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("文字文件(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int cho = fileChooser.showSaveDialog(this);
        if (cho == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            save.setEnabled(true);
            lastFile = file;
            this.setTitle(lastFile.getName());
            typed = false;
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(text);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFile() throws Exception {
        System.out.println("** readFile");

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("文字文件(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int cho = fileChooser.showSaveDialog(this);
        if (cho == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            save.setEnabled(true);

            this.setTitle(file.getName());
            typed = false;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String text = "";
            String i;
            while ((i = br.readLine()) != null) {
                text += i;
                text += "\n";
            }
            area.setText(text);
            lastFile = file;
            label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
        }
    }

    public class SimpleLayout extends LayoutAdapter {
        @Override
        public void addLayoutComponent(Component comp, Object constraints) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }

        @Override
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            int topHeight = 10;
            int underHeight = (int) (height * 0.95)-50;
            top.setBounds(0, 0, width, topHeight);
            under.setBounds(0, topHeight, width, underHeight+40);
            lineView.setBounds(0, underHeight+50, width,height - underHeight);
        }


    }
    public int getTextCount(){
        int realCount;
        label1.setText("字數:" + area.getText().length() + " | 行數:" + area.getLineCount());
        System.out.println("字數" + area.getText().length() + "行數" + area.getLineCount());
        realCount = area.getText().length() - (area.getLineCount() - 1);
        return realCount;
    }
}
