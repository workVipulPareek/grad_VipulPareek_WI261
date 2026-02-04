import java.sql.*;
import java.util.Scanner;

public class SiteMaintenanceProgram {

    static Connection con;
    static Scanner sc = new Scanner(System.in);
    
    //can have constructor here itself 
    enum PropertyType {
        VILLA, APARTMENT, INDEPENDENTHOUSE, OPEN_SITE
    }

    abstract class Property {
        int length;
        int width;

        int area() {
            return length * width;
        }

        abstract double calculateMaintenance();
    }

    class OpenSite extends Property {
        double calculateMaintenance() {
            return area() * 6;
        }
    }

    class OccupiedSite extends Property {
        double calculateMaintenance() {
            return area() * 9;
        }
    }

    
    static void connectDB() {
        try {
            con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Sitemanagement",
                "postgres",
                "wissen@123"
            );
            System.out.println("Database connected.");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    boolean adminLogin(String u, String p) {
        return u.equals("admin") && p.equals("admin@123");
    }

    void adminMenu() {
        int ch;
        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Owner");
            System.out.println("2. Add Site");
            System.out.println("3. View All Sites");
            System.out.println("4. Collect Maintenance");
            System.out.println("5. View Pending Maintenance");
            System.out.println("6. Approve/Reject Site Update Requests");
            System.out.println("0. Logout");

            ch = sc.nextInt();

            switch (ch) {
                case 1 -> addOwner();
                case 2 -> addSite();
                case 3 -> viewSites();
                case 4 -> collectMaintenance();
                case 5 -> viewPending();
                case 6 -> approveRequests();
                case 0 -> System.out.println("Logged out.");
                default -> System.out.println("Invalid choice");
            }
        } while (ch != 0);
    }

    void addOwner() {
        try {
            System.out.print("Owner Name: ");
            String name = sc.next();
            System.out.print("Phone: ");
            String phone = sc.next();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO owner(name, phone) VALUES (?,?)");
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.executeUpdate();

            System.out.println("Owner added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addSite() {
        try {
            System.out.print("Length: ");
            int l = sc.nextInt();
            System.out.print("Width: ");
            int w = sc.nextInt();

            System.out.println("Property Type:");
            System.out.println("1.VILLA 2.APARTMENT 3.INDEPENDENTHOUSE 4.OPEN_SITE");
            int t = sc.nextInt();

            PropertyType type = switch (t) {
                case 1 -> PropertyType.VILLA;
                case 2 -> PropertyType.APARTMENT;
                case 3 -> PropertyType.INDEPENDENTHOUSE;
                case 4 -> PropertyType.OPEN_SITE;
                default -> throw new IllegalArgumentException("Invalid type");
            };

            System.out.print("Owner ID: ");
            int ownerId = sc.nextInt();

            Property p = (type == PropertyType.OPEN_SITE)
                    ? new OpenSite()
                    : new OccupiedSite();

            p.length = l;
            p.width = w;
            double amount = p.calculateMaintenance();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO site(length,width,property_type,maintenance_amount,owner_id) VALUES (?,?,?,?,?)");
            ps.setInt(1, l);
            ps.setInt(2, w);
            ps.setString(3, type.name());
            ps.setDouble(4, amount);
            ps.setInt(5, ownerId);
            ps.executeUpdate();

            System.out.println("Site added. Maintenance = " + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewSites() {
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM site");
            System.out.println("\nID | Size | Type | Amount | Paid | Owner");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("site_id") + " | " +
                    rs.getInt("length") + "x" + rs.getInt("width") + " | " +
                    rs.getString("property_type") + " | Rs" +
                    rs.getDouble("maintenance_amount") + " | " +
                    rs.getBoolean("maintenance_paid") + " | " +
                    rs.getInt("owner_id")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void collectMaintenance() {
        try {
            System.out.print("Site ID: ");
            int id = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE site SET maintenance_paid=true WHERE site_id=?");
            ps.setInt(1, id);

            System.out.println(ps.executeUpdate() > 0
                    ? "Maintenance collected."
                    : "Invalid site ID.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void viewPending() {
        try {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT * FROM site WHERE maintenance_paid=false");

            System.out.println("\nPending Sites:");
            while (rs.next()) {
                System.out.println("Site " + rs.getInt("site_id") +
                        "  Rs" + rs.getDouble("maintenance_amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ownerMenu(int ownerId) {
        int ch;
        do {
            System.out.println("\n--- OWNER MENU ---");
            System.out.println("1. View My Sites");
            System.out.println("2. Request Site Update");
            System.out.println("0. Logout");

            ch = sc.nextInt();

            switch (ch) {
                case 1 -> viewOwnerSites(ownerId);
                case 2 -> requestUpdate(ownerId);
                case 0 -> System.out.println("Logged out.");
                default -> System.out.println("Invalid choice");
            }
        } while (ch != 0);
    }

    void viewOwnerSites(int ownerId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM site WHERE owner_id=?");
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    "Site ID: " + rs.getInt("site_id") +
                    " Size: " + rs.getInt("length") + "x" + rs.getInt("width") +
                    " Paid: " + rs.getBoolean("maintenance_paid")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void requestUpdate(int ownerId) {
        try {
            System.out.print("Site ID: ");
            int siteId = sc.nextInt();
            System.out.print("New Length: ");
            int l = sc.nextInt();
            System.out.print("New Width: ");
            int w = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO site_update_request(site_id,owner_id,new_length,new_width) VALUES (?,?,?,?)");
            ps.setInt(1, siteId);
            ps.setInt(2, ownerId);
            ps.setInt(3, l);
            ps.setInt(4, w);
            ps.executeUpdate();

            System.out.println("Update request submitted.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void approveRequests() {
        try {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT * FROM site_update_request WHERE status='PENDING'");

            while (rs.next()) {
                int reqId = rs.getInt("request_id");
                int siteId = rs.getInt("site_id");
                int l = rs.getInt("new_length");
                int w = rs.getInt("new_width");
                
                System.out.println("\nRequest ID: " + reqId +
                        " Site: " + siteId +
                        " New Size: " + l + "x" + w);
                System.out.print("Approve? (yes/no): ");
                String c = sc.next();

                if (c.equalsIgnoreCase("yes")) {
                    con.prepareStatement(
                        "UPDATE site SET length=?, width=? WHERE site_id=?")
                        .executeUpdate();

                    PreparedStatement ps1 = con.prepareStatement(
                        "UPDATE site SET length=?, width=? WHERE site_id=?");
                    ps1.setInt(1, l);
                    ps1.setInt(2, w);
                    ps1.setInt(3, siteId);
                    ps1.executeUpdate();

                    PreparedStatement ps2 = con.prepareStatement(
                        "UPDATE site_update_request SET status='APPROVED' WHERE request_id=?");
                    ps2.setInt(1, reqId);
                    ps2.executeUpdate();

                    System.out.println("Approved.");
                } else {
                    con.prepareStatement(
                        "UPDATE site_update_request SET status='REJECTED' WHERE request_id=" + reqId)
                        .executeUpdate();
                    System.out.println("Rejected.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        connectDB();
        SiteMaintenanceProgram app = new SiteMaintenanceProgram();
        while (true) {
            System.out.println("\n1. Admin Login");
            System.out.println("2. Owner Login");
            System.out.println("0. Exit");

            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Username: ");
                String u = sc.next();
                System.out.print("Password: ");
                String p = sc.next();
                if (app.adminLogin(u, p)) app.adminMenu();
                else System.out.println("Invalid credentials");
            }
            else if (choice == 2) {
                System.out.print("Owner Name: ");
                String name = sc.next();

                try {
                    PreparedStatement ps = con.prepareStatement(
                        "SELECT owner_id FROM owner WHERE name=?");
                    ps.setString(1, name);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) app.ownerMenu(rs.getInt("owner_id"));
                    else System.out.println("Owner not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (choice == 0) {
                System.out.println("Bye");
                break;
            }
        }
    }
}
