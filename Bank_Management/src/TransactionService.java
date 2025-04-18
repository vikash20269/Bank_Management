import java.sql.*;

public class TransactionService {
    private Connection conn;

    public TransactionService(Connection conn) {
        this.conn = conn;
    }

    public void deposit(int accountId, double amount) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        double balance = new AccountService(conn).getBalance(accountId);
        new AccountService(conn).updateBalance(accountId, balance + amount);
        logTransaction(accountId, "DEPOSIT", amount);
        System.out.println("Deposit successful!");
    }

    public void withdraw(int accountId, double amount) throws SQLException {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        double balance = new AccountService(conn).getBalance(accountId);
        if (balance < amount) throw new IllegalArgumentException("Insufficient funds.");
        new AccountService(conn).updateBalance(accountId, balance - amount);
        logTransaction(accountId, "WITHDRAW", amount);
        System.out.println("Withdrawal successful!");
    }

    public void logTransaction(int accountId, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }
}
