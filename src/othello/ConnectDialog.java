package othello;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConnectDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final JPanel _contentPanel = new JPanel();
    private JTextField _hostText;
    private JTextField _portText;
    private JTextField _textField_2;

    public ConnectDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        _contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(_contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
        gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        _contentPanel.setLayout(gbl_contentPanel);
        {
            JLabel lblServer = new JLabel("Server");
            lblServer.setHorizontalAlignment(SwingConstants.RIGHT);
            GridBagConstraints gbc_lblServer = new GridBagConstraints();
            gbc_lblServer.anchor = GridBagConstraints.WEST;
            gbc_lblServer.gridwidth = 2;
            gbc_lblServer.insets = new Insets(0, 0, 5, 5);
            gbc_lblServer.gridx = 0;
            gbc_lblServer.gridy = 0;
            _contentPanel.add(lblServer, gbc_lblServer);
        }
        {
            JLabel lblHost = new JLabel("Host");
            lblHost.setHorizontalAlignment(SwingConstants.RIGHT);
            GridBagConstraints gbc_lblHost = new GridBagConstraints();
            gbc_lblHost.anchor = GridBagConstraints.EAST;
            gbc_lblHost.insets = new Insets(0, 0, 5, 5);
            gbc_lblHost.gridx = 1;
            gbc_lblHost.gridy = 1;
            _contentPanel.add(lblHost, gbc_lblHost);
        }
        {
            _hostText = new JTextField();
            GridBagConstraints gbc_hostText = new GridBagConstraints();
            gbc_hostText.insets = new Insets(0, 0, 5, 0);
            gbc_hostText.fill = GridBagConstraints.HORIZONTAL;
            gbc_hostText.gridx = 2;
            gbc_hostText.gridy = 1;
            _contentPanel.add(_hostText, gbc_hostText);
            _hostText.setColumns(10);
        }
        {
            JLabel lblPort = new JLabel("Port");
            lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
            GridBagConstraints gbc_lblPort = new GridBagConstraints();
            gbc_lblPort.anchor = GridBagConstraints.EAST;
            gbc_lblPort.insets = new Insets(0, 0, 5, 5);
            gbc_lblPort.gridx = 1;
            gbc_lblPort.gridy = 2;
            _contentPanel.add(lblPort, gbc_lblPort);
        }
        {
            _portText = new JTextField();
            GridBagConstraints gbc_portText = new GridBagConstraints();
            gbc_portText.insets = new Insets(0, 0, 5, 0);
            gbc_portText.fill = GridBagConstraints.HORIZONTAL;
            gbc_portText.gridx = 2;
            gbc_portText.gridy = 2;
            _contentPanel.add(_portText, gbc_portText);
            _portText.setColumns(10);
        }
        {
            JLabel lblNickname = new JLabel("Nickname");
            lblNickname.setHorizontalAlignment(SwingConstants.RIGHT);
            GridBagConstraints gbc_lblNickname = new GridBagConstraints();
            gbc_lblNickname.anchor = GridBagConstraints.WEST;
            gbc_lblNickname.gridwidth = 2;
            gbc_lblNickname.insets = new Insets(0, 0, 0, 5);
            gbc_lblNickname.gridx = 0;
            gbc_lblNickname.gridy = 4;
            _contentPanel.add(lblNickname, gbc_lblNickname);
        }
        {
            _textField_2 = new JTextField();
            GridBagConstraints gbc_textField_2 = new GridBagConstraints();
            gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
            gbc_textField_2.gridx = 2;
            gbc_textField_2.gridy = 4;
            _contentPanel.add(_textField_2, gbc_textField_2);
            _textField_2.setColumns(10);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
