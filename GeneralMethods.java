import java.util.Scanner;

public class GeneralMethods {
  public static String GetStringInput(String prompt) {
    Scanner in = new Scanner(System.in);
    System.out.println(prompt);
    return in.nextLine();
  }
}