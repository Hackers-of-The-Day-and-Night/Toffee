import java.util.Vector;

public class CustomersMgr {

  public boolean addCustomer() {
    String name = GeneralMethods.GetStringInput("Enter name:");
    String email = GeneralMethods.GetStringInput("Enter email:");
    String password = GeneralMethods.GetStringInput("Enter password:");
    String address = GeneralMethods.GetStringInput("Enter address:");
    DbExe db = new DbExe();
    boolean isEmailTaken = db.dmlExe("SELECT * FROM Customer WHERE email = '" + email + "';").size() != 0;
    if (isEmailTaken) {
      System.out.println("Email is already taken.");
      return false;
    }
    MailService ms = new MailService();
    String otp = ms.sendOTP(email, "Welcome to Toffee!");
    String enteredOtp = GeneralMethods.GetStringInput("Enter OTP:");
    int cnt = 2;
    while (cnt-- != 0 && !otp.equals(enteredOtp)) {
      enteredOtp = GeneralMethods.GetStringInput("invalid OTP. Enter again:");
    }
    if (otp.equals(enteredOtp)) {
      System.out.println("OTP verified successfully.");
      boolean isInserted = db.ddlExe("INSERT INTO Customer (name, email, password, address)" +
          "VALUES ('" + name + "', '" + email + "', '" + password + "', '" + address + "');");
      if (isInserted) {
        System.out.println("Customer added successfully.");
        ms.sendEmail(email, "Welcome to Toffee!", "Your registration completed!\nNow you can buy our products as you want!\n");
        return true;
      } else {
        System.out.println("Error: Failed to add customer. Please try again.");
        return false;
      }
    } else {
      System.out.println("Failed to verify OTP.");
      System.out.println("Customer not added.");
      return false;
    }
  }

  public Customer accessCustomer() {
    String email = GeneralMethods.GetStringInput("Enter email:");
    String password = GeneralMethods.GetStringInput("Enter password:");
    DbExe db = new DbExe();
    System.out.println("Getting customer info...");
    Vector<Vector<String>> vec = db.dmlExe("SELECT * FROM Customer " +
        "WHERE email = '" +  email + "' AND password = '" + password + "';");
    if (vec.size() == 0) {
      System.out.println("Invalid email or password.");
      return null;
    } else {
      Vector<String> cInfo = vec.get(0);
      System.out.println("Welcome, " + cInfo.get(1) + "!");
      return new Customer(Integer.parseInt(cInfo.get(0)), cInfo.get(1),
          cInfo.get(2), cInfo.get(3), cInfo.get(4));
    }
  }

}
