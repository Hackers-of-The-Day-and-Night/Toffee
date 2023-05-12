import java.sql.*;
import java.util.Vector;

/**
 * Class to execute SQL queries
 */
public class DbExe {
  /** Connection to the database
   */
  private Connection conn;

  /** Constructor
   */
  public DbExe() {
    this.conn = null;
  }

  /** Connects to the database
   */
  private void connect() throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite:src/data.db");
  }

  /** Disconnects from the database
   */
  private void disconnect() throws SQLException {
    conn.close();
  }

  /** Executes a DDL query
   * @param query SQL query to execute
   */
  public Boolean ddlExe(String query) {
    try {
      connect();
      Statement stmt = conn.createStatement();
      stmt.execute(query);
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    } finally {
      try { disconnect(); }
      catch (SQLException e) { System.out.println(e.getMessage()); }
    }
  }

  /** Executes a DML query and returns the results as a table
   * @param query SQL query to execute
   * @return Vector<Vector<String>> table of results
   */
  public Vector<Vector<String>> dmlExe(String query) {
    Vector<Vector<String>> table = new Vector<>();
    try {
      connect();
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      ResultSetMetaData rsmd = rs.getMetaData();
      while (rs.next()) {
        Vector<String> row = new Vector<>();
        for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
          row.add(rs.getString(i));
        }
        table.add(row);
      }
    } catch (SQLException e) {
      table.clear();
      System.out.println(e.getMessage());
    } finally {
      try { disconnect(); }
      catch (SQLException e) { System.out.println(e.getMessage()); }
    }
    return table;
  }

}
