// ===========================================================
// PASSWORD GENERATOR - PROFESSIONAL POLISHED EDITION (FINAL)
// ===========================================================

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

import net.miginfocom.swing.MigLayout;

public class PasswordGeneratorGUI extends JFrame {

    // UI Components
    private JSlider lengthSlider;
    private JLabel lengthValueLabel;

    private JPasswordField resultField;
    private JCheckBox upperCheck, lowerCheck, numberCheck, specialCheck;

    private JCheckBox excludeAmbiguousCheck;
    private JCheckBox noRepeatCheck;
    private JCheckBox noSequenceCheck;

    private JTextField customCharField;
    private JCheckBox useCustomCheck;

    private DefaultListModel<String> historyModel;
    private JList<String> historyList;

    private JProgressBar strengthBar;
    private JLabel strengthLabel;

    private JButton copyButton;
    private JCheckBox showPasswordCheck;

    public PasswordGeneratorGUI() {

        // =====================================================
        // WINDOW SETTINGS
        // =====================================================
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 1000);
        setLocationRelativeTo(null);

        // Smooth fonts
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // FlatLaf styling
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.focusWidth", 1);

        // =====================================================
        // OUTER SCROLL PANEL
        // =====================================================
        JPanel outer = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[grow]"));

        JScrollPane scrollPane = new JScrollPane(
                outer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        // =====================================================
        // CENTERED WRAPPER (FIXES FULLSCREEN CENTERING)
        // =====================================================
        JPanel centerWrapper = new JPanel(new MigLayout(
                "insets 20, fill",
                "[center]",      // <--- PERFECT CENTER FIX
                ""
        ));

        outer.add(centerWrapper, "grow, alignx center");  // <--- REQUIRED FIX

        // =====================================================
        // MAIN CONTAINER (max width = 600)
        // =====================================================
        JPanel container = new JPanel(new MigLayout(
                "fillx, insets 10, gapy 20",
                "[grow, fill, 500::600]",   // <--- stays centered with max width
                ""
        ));
        centerWrapper.add(container, "growx");

        // =====================================================
        // TITLE
        // =====================================================
        JLabel appTitle = new JLabel("Password Generator");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        container.add(appTitle, "span, alignx center, wrap");

        // =====================================================
        // PASSWORD LENGTH
        // =====================================================
        JPanel lengthCard = card();
        lengthCard.add(section("Password Length"), "wrap");

        lengthSlider = new JSlider(8, 64, 12);
        lengthSlider.setMajorTickSpacing(8);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        lengthCard.add(lengthSlider, "growx, wrap");

        lengthValueLabel = new JLabel("Length: 12");
        lengthCard.add(lengthValueLabel, "wrap");

        lengthSlider.addChangeListener(e ->
                lengthValueLabel.setText("Length: " + lengthSlider.getValue())
        );

        container.add(lengthCard, "growx, wrap");

        // =====================================================
        // CHARACTER OPTIONS
        // =====================================================
        JPanel charCard = card();
        charCard.add(section("Character Options"), "wrap");

        upperCheck = new JCheckBox("Include Uppercase");
        lowerCheck = new JCheckBox("Include Lowercase");
        numberCheck = new JCheckBox("Include Numbers");
        specialCheck = new JCheckBox("Include Special Characters");

        charCard.add(upperCheck, "wrap");
        charCard.add(lowerCheck, "wrap");
        charCard.add(numberCheck, "wrap");
        charCard.add(specialCheck, "wrap");

        container.add(charCard, "growx, wrap");

        // =====================================================
        // ADVANCED FILTERS
        // =====================================================
        JPanel advCard = card();
        advCard.add(section("Advanced Filters"), "wrap");

        excludeAmbiguousCheck = new JCheckBox("Exclude Ambiguous Characters");
        noRepeatCheck = new JCheckBox("No Repeating Characters");
        noSequenceCheck = new JCheckBox("No Sequential Characters (ABC / 123)");

        advCard.add(excludeAmbiguousCheck, "wrap");
        advCard.add(noRepeatCheck, "wrap");
        advCard.add(noSequenceCheck, "wrap");

        container.add(advCard, "growx, wrap");

        // =====================================================
        // CUSTOM CHARACTERS
        // =====================================================
        JPanel customCard = card();
        customCard.add(section("Custom Characters"), "wrap");

        customCharField = new JTextField();
        customCharField.putClientProperty("JTextField.placeholderText", "Optional custom character set");
        customCard.add(customCharField, "growx, wrap");

        useCustomCheck = new JCheckBox("Use Custom Character Set Only");
        customCard.add(useCustomCheck, "wrap");

        container.add(customCard, "growx, wrap");

        // =====================================================
        // ACTION BUTTONS
        // =====================================================
        JPanel actionCard = card();

        JButton generateBtn = new JButton("Generate Password");
        generateBtn.putClientProperty("JButton.buttonType", "roundRect");
        actionCard.add(generateBtn, "growx, wrap");

        JPanel btnRow = new JPanel(new MigLayout("insets 0", "[grow][][]", ""));
        copyButton = new JButton("Copy");
        copyButton.putClientProperty("JButton.buttonType", "roundRect");

        showPasswordCheck = new JCheckBox("Show Password");

        btnRow.add(new JLabel(), "growx");
        btnRow.add(copyButton, "gapright 10");
        btnRow.add(showPasswordCheck);

        actionCard.add(btnRow, "growx, wrap");

        container.add(actionCard, "growx, wrap");

        // =====================================================
        // GENERATED PASSWORD
        // =====================================================
        JPanel genCard = card();
        genCard.add(section("Generated Password"), "wrap");

        resultField = new JPasswordField();
        resultField.setEchoChar('•');
        resultField.setEditable(false);
        resultField.setFocusable(false);

        genCard.add(resultField, "growx, wrap");

        container.add(genCard, "growx, wrap");

        // =====================================================
        // STRENGTH
        // =====================================================
        JPanel strengthCard = card();
        strengthCard.add(section("Strength"), "wrap");

        strengthLabel = new JLabel("Strength: Unknown");
        strengthCard.add(strengthLabel, "wrap");

        strengthBar = new JProgressBar(0, 100);
        strengthCard.add(strengthBar, "growx, h 22!, wrap");

        container.add(strengthCard, "growx, wrap");

        // =====================================================
        // PASSWORD HISTORY
        // =====================================================
        JPanel historyCard = card();
        historyCard.add(section("Password History"), "wrap");

        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);

        JScrollPane historyScroll = new JScrollPane(historyList);
        historyCard.add(historyScroll, "growx, h 220!");

        container.add(historyCard, "growx, wrap");

        // =====================================================
        // ACTION HANDLERS
        // =====================================================
        generateBtn.addActionListener(e -> generatePassword());

        copyButton.addActionListener(e -> {
            String password = new String(resultField.getPassword());
            if (!password.isEmpty()) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(password), null);
                JOptionPane.showMessageDialog(this, "Password copied!");
            }
        });

        showPasswordCheck.addActionListener(e ->
                resultField.setEchoChar(showPasswordCheck.isSelected() ? 0 : '•')
        );

        setVisible(true);
    }

    // =====================================================
    // CARD STYLE
    // =====================================================
    private JPanel card() {
        JPanel p = new JPanel(new MigLayout("fillx, insets 15, gapy 8"));

        p.putClientProperty("FlatLaf.style", ""
                + "background: lighten(@background,4%);"
                + "arc:12;"
                + "borderWidth: 1;"
        );

        p.setBorder(BorderFactory.createLineBorder(
                UIManager.getColor("Component.borderColor"), 1
        ));

        return p;
    }

    // =====================================================
    // SECTION LABEL STYLE
    // =====================================================
    private JLabel section(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return label;
    }

    // =====================================================
    // HISTORY HANDLING
    // =====================================================
    private void updateHistory(String password) {
        historyModel.add(0, password);
        if (historyModel.size() > 20)
            historyModel.remove(20);
    }

    // =====================================================
    // STRENGTH CALCULATION
    // =====================================================
    private int calculateStrength(String password) {
        int score = 0;
        if (password.length() >= 8) score += 20;
        if (password.length() >= 12) score += 20;
        if (password.matches(".*[A-Z].*")) score += 20;
        if (password.matches(".*[a-z].*")) score += 20;
        if (password.matches(".*[0-9].*")) score += 10;
        if (password.matches(".*[!@#$%^&*()_\\-+=<>?/{}~|].*")) score += 10;
        return score;
    }

    // =====================================================
    // GENERATE PASSWORD
    // =====================================================
    private void generatePassword() {
        try {
            String custom = customCharField.getText();

            String password = PasswordGenerator.generatePassword(
                    lengthSlider.getValue(),
                    upperCheck.isSelected(),
                    lowerCheck.isSelected(),
                    numberCheck.isSelected(),
                    specialCheck.isSelected(),
                    excludeAmbiguousCheck.isSelected(),
                    noRepeatCheck.isSelected(),
                    noSequenceCheck.isSelected(),
                    useCustomCheck.isSelected(),
                    custom
            );

            resultField.setText(password);
            updateHistory(password);

            int strength = calculateStrength(password);
            strengthBar.setValue(strength);

            if (strength < 20) {
                strengthLabel.setText("Strength: Very Weak");
                strengthBar.setForeground(new Color(180, 0, 0));
            } else if (strength < 40) {
                strengthLabel.setText("Strength: Weak");
                strengthBar.setForeground(Color.RED);
            } else if (strength < 70) {
                strengthLabel.setText("Strength: Medium");
                strengthBar.setForeground(Color.ORANGE);
            } else {
                strengthLabel.setText("Strength: Strong");
                strengthBar.setForeground(new Color(0, 160, 0));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
