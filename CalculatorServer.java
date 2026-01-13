
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // start RMI registry
            Calculator calc = new CalculatorImpl();
            Naming.rebind("CalculatorService", calc);
            System.out.println("Calculator RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
