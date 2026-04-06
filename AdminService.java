package m1.pkg1;

import java.sql.*;
import java.util.Scanner;

public class AdminService {
    private Scanner sc = new Scanner(System.in);

   
    public boolean login() {
        System.out.println("\n--- MUSEUM ADMIN LOGIN ---");
        System.out.print("Username: ");
        String user = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful! Welcome, " + rs.getString("full_name"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return false;
    }

   
    public void showAdminRecords() {
        System.out.println("\n--- CURRENT ADMIN ACCOUNTS ---");
        String query = "SELECT username, full_name FROM admins";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("+----------------------+---------------------------+");
            System.out.println("| USERNAME             | FULL NAME                 |");
            System.out.println("+----------------------+---------------------------+");
            while (rs.next()) {
                System.out.printf("| %-20s | %-25s |\n", 
                    rs.getString("username"), rs.getString("full_name"));
            }
            System.out.println("+----------------------+---------------------------+");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

   
    public void addAdmin() {
        System.out.println("\n[ ADD NEW ADMIN ]");
        System.out.print("Enter New Username: ");
        String user = sc.next();
        System.out.print("Enter Password: ");
        String pass = sc.next();
        sc.nextLine(); // clear buffer
        System.out.print("Enter Full Name: ");
        String name = sc.nextLine();

        String query = "INSERT INTO admins (username, password, full_name) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
            System.out.println(">>> Positive: Admin [" + user + "] added to SQL.");
            showAdminRecords(); 
        } catch (SQLException e) {
            System.out.println(">>> Negative: Admin record operation failed. " + e.getMessage());
        }
    }

    public void updateAdmin() {
        showAdminRecords();
        System.out.print("\nEnter Username to update: ");
        String user = sc.next();
        sc.nextLine(); 
        System.out.print("Enter New Full Name for " + user + ": ");
        String newName = sc.nextLine();

        String query = "UPDATE admins SET full_name = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, user);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(">>> Positive: Record updated in SQL.");
                showAdminRecords();
            } else {
                System.out.println(">>> Negative: Admin record operation failed. Username not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteAdmin() {
        showAdminRecords();
        System.out.print("\nEnter Username to DELETE: ");
        String user = sc.next();

        if (user.equalsIgnoreCase("root")) {
            System.out.println(">>> Negative: Cannot delete the Main Root Admin!");
            return;
        }

        String query = "DELETE FROM admins WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(">>> Positive: Admin [" + user + "] deleted from SQL.");
                showAdminRecords();
            } else {
                System.out.println(">>> Negative: Admin record operation failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

   
    public void monitorBookings() {
        System.out.println("\n--- MONITORING VISITOR BOOKINGS ---");
        String query = "SELECT * FROM bookings";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("+---------+----------------------+------------+----------+-----------+");
            System.out.println("| ID      | VISITOR NAME         | DATE       | PAID     | STATUS    |");
            System.out.println("+---------+----------------------+------------+----------+-----------+");
            while (rs.next()) {
                System.out.printf("| %-7d | %-20s | %-10s | %-8.2f | %-9s |\n",
                    rs.getInt("booking_id"), rs.getString("visitor_name"), 
                    rs.getString("schedule_date"), rs.getDouble("amount_paid"), rs.getString("status"));
            }
            System.out.println("+---------+----------------------+------------+----------+-----------+");
        } catch (SQLException e) {
            System.out.println(">>> Negative: Unable to retrieve booking information.");
        }
    }

    public void manageBookings() {
        monitorBookings();
        System.out.print("\nEnter Booking ID to Manage (Cancel/Refund): ");
        int bId = sc.nextInt();
        System.out.println("[1] Cancel Booking");
        System.out.println("[2] Process Refund");
        System.out.print("Select Action: ");
        int choice = sc.nextInt();

        String newStatus = (choice == 1) ? "Cancelled" : "Refunded";

        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(">>> Positive: Booking ID [" + bId + "] is now " + newStatus + ".");
                monitorBookings();
            } else {
                System.out.println(">>> Negative: Booking ID not found.");
            }
        } catch (SQLException e) {
            System.out.println(">>> Negative: Action failed. Check connection.");
        }
    }

    public boolean verifyTicket(int bookingId) {
        String query = "SELECT visitor_name FROM bookings WHERE booking_id = ? AND status = 'Confirmed'";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(">>> Positive: Ticket Verified! Welcome, " + rs.getString("visitor_name") + ".");
                return true;
            } else {
                System.out.println(">>> Negative: Invalid or Unconfirmed Ticket ID.");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}