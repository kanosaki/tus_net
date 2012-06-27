package othello;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel _contentPane;
    private JTextField tf;
    private JTextArea ta;
    private JLabel label;
    private BoardView canvas;
    private Controller _controller;

    public MainFrame(Controller controller) {
        _controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        _contentPane = new JPanel();
        _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        _contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(_contentPane);
        this.setSize(640, 360);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        tf = new JTextField(40);
        tf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _controller.say(tf.getText());
                tf.setText("");
            }
        });
        ta = new JTextArea(18, 40);
        ta.setLineWrap(true);
        ta.setEditable(false);

        JPanel mainp = (JPanel) getContentPane();
        JPanel ep = new JPanel();
        GridLayout gl = new GridLayout(1, 2);
        gl.setHgap(5);
        mainp.setLayout(gl);
        ep.setLayout(new BorderLayout());
        ep.add(new JScrollPane(ta), BorderLayout.CENTER);
        ep.add(tf, BorderLayout.SOUTH);
        label = new JLabel();
        canvas = new BoardView();
        JPanel wp = new JPanel();
        _contentPane.add(wp, BorderLayout.NORTH);
        wp.setLayout(new BorderLayout());
        wp.add(label, BorderLayout.SOUTH);
        wp.add(canvas, BorderLayout.CENTER);
        mainp.add(ep);
        this.setMessage("Ready.");
    }

    public void pushMessage(String msg) {
        SwingUtilities.invokeLater(new RunnableContainer<String>(msg) {
            @Override
            void work(String arg) {
                ta.append(arg + "\n");
            }
        });
    }

    public void setMessage(String msg) {
        SwingUtilities.invokeLater(new RunnableContainer<String>(msg) {
            @Override
            void work(String arg) {
                label.setText(arg);                
            }
        });
    }

    public void setChatMessage(String user, String msg) {
        this.pushMessage(String.format("<%s> : %s", user, msg));
    }

    public BoardView getBoardView() {
        return canvas;
    }
}
