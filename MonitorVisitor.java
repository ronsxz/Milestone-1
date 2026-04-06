package m1.pkg1;

import java.sql.*;
import java.util.Scanner;

public class MonitorVisitor {
    private Scanner sc = new Scanner(System.in);

       
           public String registerVisitorEntry() {
                   System.out.println("\n============================================");
                           System.out.println("      MUSEUM VISITOR ENTRY REGISTRATION     ");
                                   System.out.println("============================================");
                                           System.out.print("Please Enter Your Full Name: ");
                                                   String name = sc.nextLine();
                                                           
                                                                   
                                                                           String query = "INSERT INTO visitor_entries (visitor_name, entry_time) VALUES (?, GETDATE())";

                                                                                   try (Connection conn = Database.getConnection();
                                                                                                PreparedStatement pstmt = conn.prepareStatement(query)) {
                                                                                                            
                                                                                                                        pstmt.setString(1, name);
                                                                                                                                    pstmt.executeUpdate();

                                                                                                                                                System.out.println(">>> Entry Recorded. Welcome to the National Museum, " + name + "!");
                                                                                                                                                            return name; // Ibabalik natin ang pangalan para ma-acknowledge sa menu
                                                                                                                                                                        
                                                                                                                                                                                } catch (SQLException e) {
                                                                                                                                                                                            System.out.println(">>> Error recording entry: " + e.getMessage());
                                                                                                                                                                                                        return null;
                                                                                                                                                                                                                }
                                                                                                                                                                                                                    }

                                                                                                                                                                                                                        
                                                                                                                                                                                                                            public void displayAllEntries() {
                                                                                                                                                                                                                                    System.out.println("\n--- [ VISITOR MONITORING LOGS ] ---");
                                                                                                                                                                                                                                            String query = "SELECT * FROM visitor_entries ORDER BY entry_time DESC";

                                                                                                                                                                                                                                                    try (Connection conn = Database.getConnection();
                                                                                                                                                                                                                                                                 Statement stmt = conn.createStatement();
                                                                                                                                                                                                                                                                              ResultSet rs = stmt.executeQuery(query)) {
                                                                                                                                                                                                                                                                                          
                                                                                                                                                                                                                                                                                                      System.out.println("ID | VISITOR NAME         | TIME RECORDED");
                                                                                                                                                                                                                                                                                                                  System.out.println("------------------------------------------");
                                                                                                                                                                                                                                                                                                                              while (rs.next()) {
                                                                                                                                                                                                                                                                                                                                              System.out.printf("%-2d | %-20s | %s\n",
                                                                                                                                                                                                                                                                                                                                                                  rs.getInt("entry_id"), 
                                                                                                                                                                                                                                                                                                                                                                                      rs.getString("visitor_name"), 
                                                                                                                                                                                                                                                                                                                                                                                                          rs.getString("entry_time"));
                                                                                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                                                                                              } catch (SQLException e) {
                                                                                                                                                                                                                                                                                                                                                                                                                                          System.out.println("Database Error.");
                                                                                                                                                                                                                                                                                                                                                                                                                                                  }
                                                                                                                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                                                                                                                      }