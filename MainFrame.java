package com.example.ciphertool.ui;

import com.example.ciphertool.cipher.Cipher;
import com.example.ciphertool.cipher.CipherRegistry;
import com.example.ciphertool.cipher.KeyType;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private final JComboBox<Cipher> cipherSelector;
    private final JLabel keyLabel;
    private final JTextField keyField;
    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JLabel statusLabel;

    public MainFrame() {
        super("Cipher Tool");

        cipherSelector = new JComboBox<>(CipherRegistry.allCiphers().toArray(new Cipher[0]));
        keyLabel = new JLabel();
        keyField = new JTextField();
        inputArea = new JTextArea(8, 40);
        outputArea = new JTextArea(8, 40);
        statusLabel = new JLabel(" ");

        outputArea.setEditable(false);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        cipherSelector.addActionListener(e -> updateKeyFieldForSelectedCipher());
        updateKeyFieldForSelectedCipher(); // initialize for the first item

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(560, 460));
        setLocationRelativeTo(null);
    }

    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cipher:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        panel.add(cipherSelector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(keyLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        panel.add(keyField, gbc);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel inputPanel = new JPanel(new BorderLayout(4, 4));
        inputPanel.add(new JLabel("Input text"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout(4, 4));
        outputPanel.add(new JLabel("Result"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        panel.add(inputPanel);
        panel.add(outputPanel);
        return panel;
    }

    private JPanel buildBottomPanel() {
        JPanel container = new JPanel(new BorderLayout(4, 4));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");
        JButton swapButton = new JButton("Use result as input");
        JButton clearButton = new JButton("Clear");
        JButton copyButton = new JButton("Copy result");

        encryptButton.addActionListener(this::onEncrypt);
        decryptButton.addActionListener(this::onDecrypt);
        swapButton.addActionListener(e -> {
            inputArea.setText(outputArea.getText());
            outputArea.setText("");
        });
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            setStatus(" ", false);
        });
        copyButton.addActionListener(e -> copyToClipboard());

        buttons.add(encryptButton);
        buttons.add(decryptButton);
        buttons.add(swapButton);
        buttons.add(copyButton);
        buttons.add(clearButton);

        statusLabel.setForeground(new Color(160, 0, 0));

        container.add(buttons, BorderLayout.NORTH);
        container.add(statusLabel, BorderLayout.SOUTH);
        return container;
    }

    private void updateKeyFieldForSelectedCipher() {
        Cipher cipher = (Cipher) cipherSelector.getSelectedItem();
        if (cipher == null) return;

        keyLabel.setText("Key:");
        keyField.setToolTipText(cipher.getKeyHint());

        boolean needsKey = cipher.getKeyType() != KeyType.NONE;
        keyField.setEnabled(needsKey);
        if (!needsKey) {
            keyField.setText("");
        }
        keyLabel.setText(needsKey ? "Key (" + cipher.getKeyHint() + "):" : "Key: not needed for this cipher");
        setStatus(" ", false);
    }

    private void onEncrypt(ActionEvent e) {
        runCipherAction(true);
    }

    private void onDecrypt(ActionEvent e) {
        runCipherAction(false);
    }

    private void runCipherAction(boolean encrypting) {
        Cipher cipher = (Cipher) cipherSelector.getSelectedItem();
        if (cipher == null) return;

        String input = inputArea.getText();
        if (input.isEmpty()) {
            setStatus("Enter some text first.", true);
            return;
        }

        try {
            String result = encrypting
                    ? cipher.encrypt(input, keyField.getText())
                    : cipher.decrypt(input, keyField.getText());
            outputArea.setText(result);
            setStatus((encrypting ? "Encrypted" : "Decrypted") + " with " + cipher.getName() + ".", false);
        } catch (IllegalArgumentException ex) {
            setStatus(ex.getMessage(), true);
        }
    }

    private void copyToClipboard() {
        String text = outputArea.getText();
        if (text.isEmpty()) {
            setStatus("Nothing to copy yet.", true);
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        setStatus("Result copied to clipboard.", false);
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? new Color(180, 0, 0) : new Color(0, 110, 0));
    }
}
