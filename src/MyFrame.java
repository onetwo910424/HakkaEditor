import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
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
    JTextPane pane = new JTextPane();
    JMenuItem save = new JMenuItem("儲存檔案");
    int textCount = 0;
    int lineCount = 0;
    JLabel label1 = new JLabel("字數:" + textCount + " | 行數:" + lineCount);
    JPanel lineView = new JPanel();
    boolean typed = false;

    public MyFrame(String title) {
        super(title);
        JPanel root = new JPanel(new BorderLayout());

        // 固定視窗大小
        setResizable(false);
        // root 設定為frame的版面
        setContentPane(root);
        root.setLayout(new SimpleLayout());
        under.setBackground(Color.WHITE);
        Border border = new MatteBorder(0, 0, 3, 0, Color.GRAY);
        top.setBorder(border);
        top.setBackground(Color.LIGHT_GRAY);
        under.setLayout(new BorderLayout());
        MenuActionLister menuActionLister = new MenuActionLister();
        pane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setTitle("*" + lastFile.getName());
                label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
                typed = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setTitle("*" + lastFile.getName());
                label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
                typed = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        pane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // 快速鍵
                // System.out.println(e); // 測試用

                // Ctrl + S
                if (e.getKeyCode() == 83 & e.isControlDown()) {
                    if (save.isEnabled())
                        saveFile();
                    else
                        saveFileAs();
                    //Ctrl + o
                } else if (e.getKeyCode() == 79 & e.isControlDown()) {
                    try {
                        readFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

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
        JScrollPane scrollPane = new JScrollPane(pane);

        this.setJMenuBar(menuBar);
        lineView.setBorder(new MatteBorder(1, 0, 0, 0, Color.WHITE));
        lineView.setBackground(new Color(237, 237, 237));
        under.add(scrollPane);
        lineView.setLayout(null);
        label1.setBounds(580, 0, 100, 22);
        lineView.add(label1);

        //字體顏色
        JMenu menu2 = new JMenu("字體");
        JMenuItem red = new JMenuItem("紅色");
        red.addActionListener(e ->{
            System.out.println("** Change red");
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.RED);
            pane.setCharacterAttributes(attr, false);
        });
        JMenuItem blue = new JMenuItem("藍色");
        blue.addActionListener(e -> {
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, Color.BLUE);
            pane.setCharacterAttributes(attr, false);
        });
        menu2.add(red);
        menu2.add(blue);
        menuBar.add(menu2);

        //排版
        JMenu menu3 = new JMenu("排版");
        JMenuItem right = new JMenuItem("右排版");
        right.addActionListener(e ->{
            System.out.println("** Change red");
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
            int start = pane.getSelectionStart();
            int end = pane.getSelectionEnd();
            pane.getStyledDocument().setParagraphAttributes(start,end-start,attr,false);
        });
        JMenuItem center = new JMenuItem("中排版");
        center.addActionListener(e ->{
            System.out.println("** Change red");
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
            int start = pane.getSelectionStart();
            int end = pane.getSelectionEnd();
            pane.getStyledDocument().setParagraphAttributes(start,end-start,attr,false);
        });
        JMenuItem left = new JMenuItem("左排版");
        left.addActionListener(e ->{
            System.out.println("** Change red");
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
            int start = pane.getSelectionStart();
            int end = pane.getSelectionEnd();
            pane.getStyledDocument().setParagraphAttributes(start,end-start,attr,false);
        });
        menu3.add(right);
        menu3.add(center);
        menu3.add(left);
        menuBar.add(menu3);

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

    // 建立新檔
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
                label1.setText("字數:" + pane.getText().length()+ " | 行數:" + area.getLineCount());
                break;
            case 1:
                lastFile = new File("未命名.txt");
                lastDir = null;
                save.setEnabled(false);
                pane.setText("");
                setTitle(lastFile.getName());
                typed = false;
                label1.setText("字數:" + getTextCount() + " | 行數:" + area.getLineCount());
                break;
            case 2:
                break;
        }
    }
    public int getLineCount() {
        Element map = pane.getDocument().getDefaultRootElement();
        return map.getElementCount();
    }

    // 儲存檔案
    private void saveFile() {
        System.out.println("** saveFile");
        setTitle(lastFile.getName());
        typed = false;
        try {
            String text = pane.getText();
            FileWriter fw = new FileWriter(lastFile);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 另存新檔
    private void saveFileAs() {
        System.out.println("** saveFileAs");

        String text = pane.getText();
        JFileChooser fileChooser = new JFileChooser();
        File cf = new File("*.txt");
        fileChooser.setSelectedFile(cf);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("文字文件(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int cho = fileChooser.showSaveDialog(this);
        if (cho == JFileChooser.APPROVE_OPTION) {
            File newFile;
            File file = fileChooser.getSelectedFile();
            String sf = file.getName();

            if (!sf.endsWith(".txt")) {
                String plus = file + ".txt";
                newFile = new File(plus);
                System.out.println(newFile);

                save.setEnabled(true);
                lastFile = newFile;
                setTitle(lastFile.getName());
                typed = false;
                try {
                    FileWriter fw = new FileWriter(newFile);
                    fw.write(text);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                save.setEnabled(true);
                lastFile = file;
                setTitle(lastFile.getName());
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
    }

    // 開啟舊檔
    private void readFile() throws Exception {
        System.out.println("** readFile");

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("文字文件(*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        // filechoose
        int cho = fileChooser.showSaveDialog(this);
        // 判斷是否選擇檔案
        if (cho == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            save.setEnabled(true);
            typed = false;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String text = "";
            String i;
            while ((i = br.readLine()) != null) {
                text += i;
                text += "\n";
            }
            pane.setText(text);
            lastFile = file;
            setTitle(lastFile.getName());
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
    // 字數、行數
    public int getTextCount(){
        int realCount;
        label1.setText("字數:" + pane.getText().length() + " | 行數:" + getLineCount());
//        System.out.println("字數" + area.getText().length() + "行數" + area.getLineCount());
        realCount = pane.getText().length() - (getLineCount() - 1);
        return realCount;
    }
}
