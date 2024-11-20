package backend;

import java.sql.*;

public class InventoryBackend {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private Connection connection;

    // Constructor to establish database connection
    public InventoryBackend() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Method to add a new item to the inventory
    public boolean addItem(String itemName, int quantity, double price) {
        String query = "INSERT INTO inventory (item_name, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        }
    }

    // Method to view all items in the inventory
    public void viewItems() {
        String query = "SELECT * FROM inventory";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("ID\tName\tQuantity\tPrice");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                                   rs.getString("item_name") + "\t" +
                                   rs.getInt("quantity") + "\t" +
                                   rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching items: " + e.getMessage());
        }
    }

    // Method to update an item's quantity or price
    public boolean updateItem(int itemId, int newQuantity, double newPrice) {
        String query = "UPDATE inventory SET quantity = ?, price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newQuantity);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, itemId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
            return false;
        }
    }

    // Method to delete an item from the inventory
    public boolean deleteItem(int itemId) {
        String query = "DELETE FROM inventory WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting item: " + e.getMessage());
            return false;
        }
    }

    // Close database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database: " + e.getMessage());
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        InventoryBackend backend = new InventoryBackend();

        // Add items
        backend.addItem("Laptop", 10, 50000.0);
        backend.addItem("Mouse", 50, 500.0);

        // View items
        backend.viewItems();

        // Update an item
        backend.updateItem(1, 15, 48000.0);

        // View items again
        backend.viewItems();

        // Delete an item
        backend.deleteItem(2);

        // View items again
        backend.viewItems();

        // Close connection
        backend.close();
    }
}
