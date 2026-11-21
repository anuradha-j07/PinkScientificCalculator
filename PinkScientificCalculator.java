import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class PinkScientificCalculator extends JFrame implements ActionListener, KeyListener {

    private JTextField display;
    private JTextArea historyArea;
    private double num1 = 0, num2 = 0, result = 0;
    private char operator = ' ';
    private boolean isNewCalculation = true;

    // Pink color cha box motha
    Color bgPink = new Color(255, 228, 235);         // baby Pink Background
    Color displayBg = new Color(255, 248, 250);      // Soft White-Pink Display
    Color btnLightPink = new Color(255, 192, 203);   // Number Buttons
    Color btnSciPink = new Color(255, 145, 175);     // Scientific Buttons
    Color btnHotPink = new Color(255, 105, 180);     // Operator Buttons
    Color btnDarkPink = new Color(219, 112, 147);    // Equals, Clear, Delete
    Color textDark = new Color(80, 20, 45);          // Dark Plum Text

    public PinkScientificCalculator() {
        // Window cha  Setup
        setTitle("Pink Scientific Calculator");
        setSize(580, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(bgPink);
        setLayout(null);
        addKeyListener(this);
        setFocusable(true);

        // Display Screen
        display = new JTextField("0");
        display.setBounds(20, 20, 340, 65);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(displayBg);
        display.setForeground(textDark);
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(btnHotPink, 2),
            new EmptyBorder(5, 10, 5, 10)
        ));
        add(display);

        // Button Grid (6 rows x 5 columns)
        String[] buttons = {
            "sin", "cos", "tan", "C", "DEL",
            "√",   "x²",  "log", "%", "/",
            "π",   "7",   "8",   "9", "*",
            "e",   "4",   "5",   "6", "-",
            "1/x", "1",   "2",   "3", "+",
            "+/-", "00",  "0",   ".", "="
        };

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(20, 100, 340, 365);
        btnPanel.setLayout(new GridLayout(6, 5, 8, 8));
        btnPanel.setBackground(bgPink);

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setFocusPainted(false);
            btn.setFocusable(false); // Keyboard focus frame var rahnysathi
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            btn.addActionListener(this);

            // Color Coding
            if (text.equals("C") || text.equals("DEL") || text.equals("=")) {
                btn.setBackground(btnDarkPink);
                btn.setForeground(Color.WHITE);
            } else if (text.equals("/") || text.equals("*") || text.equals("-") || text.equals("+") || text.equals("%")) {
                btn.setBackground(btnHotPink);
                btn.setForeground(Color.WHITE);
            } else if (text.matches("sin|cos|tan|√|x²|log|π|e|1/x|\\+/-")) {
                btn.setBackground(btnSciPink);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(btnLightPink);
                btn.setForeground(textDark);
            }
            btnPanel.add(btn);
        }
        add(btnPanel);

        // History Panel (Right Side)
        JLabel histLabel = new JLabel("Calculation History");
        histLabel.setBounds(380, 20, 170, 25);
        histLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        histLabel.setForeground(textDark);
        add(histLabel);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFocusable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyArea.setBackground(displayBg);
        historyArea.setForeground(textDark);
        historyArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(380, 50, 170, 370);
        scrollPane.setBorder(BorderFactory.createLineBorder(btnHotPink, 2));
        add(scrollPane);

        JButton clearHistBtn = new JButton("Clear History");
        clearHistBtn.setBounds(380, 430, 170, 35);
        clearHistBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearHistBtn.setBackground(btnDarkPink);
        clearHistBtn.setForeground(Color.WHITE);
        clearHistBtn.setFocusPainted(false);
        clearHistBtn.setFocusable(false);
        clearHistBtn.addActionListener(e -> historyArea.setText(""));
        add(clearHistBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleInput(e.getActionCommand());
    }

    private void handleInput(String cmd) {
        try {
            // Numbers
            if (cmd.matches("[0-9]|00")) {
                if (isNewCalculation || display.getText().equals("0") || display.getText().equals("Error")) {
                    display.setText(cmd.equals("00") ? "0" : cmd);
                    isNewCalculation = false;
                } else {
                    display.setText(display.getText() + cmd);
                }
            } 
            // Decimal
            else if (cmd.equals(".")) {
                if (isNewCalculation) {
                    display.setText("0.");
                    isNewCalculation = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            } 
            // Clear
            else if (cmd.equals("C")) {
                display.setText("0");
                num1 = num2 = result = 0;
                operator = ' ';
                isNewCalculation = true;
            } 
            // Delete
            else if (cmd.equals("DEL")) {
                String current = display.getText();
                if (!isNewCalculation && current.length() > 1) {
                    display.setText(current.substring(0, current.length() - 1));
                } else {
                    display.setText("0");
                    isNewCalculation = true;
                }
            } 
            // Constants (Pi & e)
            else if (cmd.equals("π")) {
                display.setText(formatNumber(Math.PI));
                isNewCalculation = true;
            } else if (cmd.equals("e")) {
                display.setText(formatNumber(Math.E));
                isNewCalculation = true;
            } 
            // Scientific Unary Operations
            else if (cmd.matches("sin|cos|tan|√|x²|log|1/x|\\+/-")) {
                double val = Double.parseDouble(display.getText());
                double res = 0;
                String expr = "";

                switch (cmd) {
                    case "sin": res = Math.sin(Math.toRadians(val)); expr = "sin(" + formatNumber(val) + ")"; break;
                    case "cos": res = Math.cos(Math.toRadians(val)); expr = "cos(" + formatNumber(val) + ")"; break;
                    case "tan": res = Math.tan(Math.toRadians(val)); expr = "tan(" + formatNumber(val) + ")"; break;
                    case "√":   
                        if (val < 0) { display.setText("Error"); return; }
                        res = Math.sqrt(val); expr = "√(" + formatNumber(val) + ")"; break;
                    case "x²":  res = Math.pow(val, 2); expr = "(" + formatNumber(val) + ")²"; break;
                    case "log": 
                        if (val <= 0) { display.setText("Error"); return; }
                        res = Math.log10(val); expr = "log(" + formatNumber(val) + ")"; break;
                    case "1/x": 
                        if (val == 0) { display.setText("Error"); return; }
                        res = 1 / val; expr = "1/(" + formatNumber(val) + ")"; break;
                    case "+/-": res = -val; expr = "neg(" + formatNumber(val) + ")"; break;
                }
                String formattedRes = formatNumber(res);
                display.setText(formattedRes);
                historyArea.append(expr + " = " + formattedRes + "\n");
                isNewCalculation = true;
            } 
            // Binary Operators (+, -, *, /, %)
            else if (cmd.matches("[+\\-*/%]")) {
                num1 = Double.parseDouble(display.getText());
                operator = cmd.charAt(0);
                isNewCalculation = true;
            } 
            // Equals (=)
            else if (cmd.equals("=")) {
                if (operator == ' ') return;
                num2 = Double.parseDouble(display.getText());
                switch (operator) {
                    case '+': result = num1 + num2; break;
                    case '-': result = num1 - num2; break;
                    case '*': result = num1 * num2; break;
                    case '/': 
                        if (num2 == 0) { display.setText("Error"); isNewCalculation = true; return; }
                        result = num1 / num2; break;
                    case '%': result = num1 % num2; break;
                }
                String formattedRes = formatNumber(result);
                historyArea.append(formatNumber(num1) + " " + operator + " " + formatNumber(num2) + " = " + formattedRes + "\n");
                display.setText(formattedRes);
                num1 = result;
                operator = ' ';
                isNewCalculation = true;
            }
        } catch (Exception ex) {
            display.setText("Error");
            isNewCalculation = true;
        }
    }

    private String formatNumber(double num) 
 {
        if (Double.isNaN(num) || Double.isInfinite(num)) return "Error";
        if (num == (long) num) return String.format("%d", (long) num);
        return String.format("%.6f", num).replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isDigit(c)) handleInput(String.valueOf(c));
        else if (c == '.') handleInput(".");
        else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') handleInput(String.valueOf(c));
        else if (c == '\n' || c == '=') handleInput("=");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) handleInput("DEL");
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) handleInput("C");
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PinkScientificCalculator());
    }
}