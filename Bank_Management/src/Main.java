import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            Scanner sc = new Scanner(System.in);
            UserService userService = new UserService(conn);
            AccountService accountService = new AccountService(conn);
            TransactionService transactionService = new TransactionService(conn);

            int userId = -1;
            boolean authenticated = false;

            while (!authenticated) {
                System.out.println("Welcome to the Bank System");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.print("Enter your choice: ");  // 👈 added this line
                int choice = sc.nextInt();
                sc.nextLine();

                String username, password;

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        username = sc.nextLine();
                        System.out.print("Enter password: ");
                        password = sc.nextLine();

                        if (userService.register(username, password)) {
                            System.out.println("✅ Registration successful. Please login to continue.\n");
                        } else {
                            System.out.println("❌ Registration failed. Username may already exist.\n");
                        }
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        username = sc.nextLine();
                        System.out.print("Enter password: ");
                        password = sc.nextLine();

                        userId = userService.login(username, password);
                        if (userId != -1) {
                            authenticated = true;
                            System.out.println("✅ Login successful.\n");
                        } else {
                            System.out.println("❌ Invalid credentials. Try again.\n");
                        }
                        break;

                    default:
                        System.out.println("❌ Invalid option. Try again.\n");
                }
            }

            // Ensure account exists
            int accountId = accountService.getAccountId(userId);
            if (accountId == -1) {
                accountService.createAccount(userId);
                accountId = accountService.getAccountId(userId);
                System.out.println("✅ Account created.\n");
            }

            // Menu
            boolean exit = false;
            while (!exit) {
                System.out.println("--- Main Menu ---");
                System.out.println("1. Check Balance");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int option = sc.nextInt();

                switch (option) {
                    case 1:
                        double balance = accountService.getBalance(accountId);
                        System.out.printf(" Current Balance: Rs%.2f%n", balance);
                        break;
                    case 2:
                        System.out.print("Enter deposit amount: ");
                        double dep = sc.nextDouble();
                        transactionService.deposit(accountId, dep);
                        break;
                    case 3:
                        System.out.print("Enter withdraw amount: ");
                        double wd = sc.nextDouble();
                        try {
                            transactionService.withdraw(accountId, wd);
                        } catch (IllegalArgumentException e) {
                            System.out.println("❌ " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("👋 Goodbye!");
                        exit = true;
                        break;
                    default:
                        System.out.println("❌ Invalid option.");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
        }
    }
}
