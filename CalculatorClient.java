import java.rmi.Naming;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculatorClient extends JFrame {
    private JTextField num1Field, num2Field;
    private JButton addBtn, subBtn, mulBtn, divBtn;
    private JLabel resultLabel;
    private Calculator calc;

    public CalculatorClient() {
        try {
            calc = (Calculator) Naming.lookup("rmi://localhost/CalculatorService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server: " + e.getMessage());
            System.exit(0);
        }

        setTitle("RMI Calculator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 5, 5));

        num1Field = new JTextField();
        num2Field = new JTextField();
        addBtn = new JButton("Add");
        subBtn = new JButton("Subtract");
        mulBtn = new JButton("Multiply");
        divBtn = new JButton("Divide");
        resultLabel = new JLabel("Result: ", SwingConstants.CENTER);

        add(new JLabel("Number 1: "));
        add(num1Field);
        add(new JLabel("Number 2: "));
        add(num2Field);
        add(addBtn);
        add(subBtn);
        add(mulBtn);
        add(divBtn);
        add(resultLabel);

        addBtn.addActionListener(e -> calculate("add"));
        subBtn.addActionListener(e -> calculate("sub"));
        mulBtn.addActionListener(e -> calculate("mul"));
        divBtn.addActionListener(e -> calculate("div"));

        setVisible(true);
    }

    private void calculate(String op) {
        try {
            double a = Double.parseDouble(num1Field.getText());
            double b = Double.parseDouble(num2Field.getText());
            double res = switch(op) {
                case "add" -> calc.add(a, b);
                case "sub" -> calc.subtract(a, b);
                case "mul" -> calc.multiply(a, b);
                case "div" -> calc.divide(a, b);
                default -> 0;
            };
            resultLabel.setText("Result: " + res);
        } catch (ArithmeticException ae) {
            JOptionPane.showMessageDialog(this, "Error: " + ae.getMessage());
            resultLabel.setText("Result: Error");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            resultLabel.setText("Result: Error");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorClient::new);
    }
}
