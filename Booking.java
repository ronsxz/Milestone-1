
package m1.pkg1;

public class Booking {
    private int bookingId;
    private String visitorName;
    private String scheduleDate;
    private double amountPaid;
    private String status; 

    public Booking(int id, String name, String date, double amount) {
        this.bookingId = id;
        this.visitorName = name;
        this.scheduleDate = date;
        this.amountPaid = amount;
        this.status = "Confirmed";
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public String getVisitorName() { return visitorName; }
    public String getScheduleDate() { return scheduleDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getAmountPaid() { return amountPaid; }
}
