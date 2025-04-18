import java.sql.*;

public class AccountService {
    private Connection conn;

    public AccountService(Connection conn) {
        this.conn = conn;
    }

    public boolean createAccount(int userId) throws SQLException {
        String sql = "INSERT INTO accounts (user_id, balance) VALUES (?, 0)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public int getAccountId(int userId) throws SQLException {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("account_id");
        }
        return -1;
    }

    public double getBalance(int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        }
        return 0.0;
    }

    public void updateBalance(int accountId, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        }
    }
}
