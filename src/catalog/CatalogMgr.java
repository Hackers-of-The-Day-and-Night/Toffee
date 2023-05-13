package catalog;

import java.util.Vector;
import db.DbExe;
import gm.GeneralMethods;
import order.ShippedItem;
import shoppingcart.ShoppingCart;

import java.util.Vector;

public class CatalogMgr {


  private final int ShoppingCartId_;

  CatalogMgr(int ShoppingCartId) {
    ShoppingCartId_ = ShoppingCartId;
  }

  private void listItems(Vector<String> names, Vector<String> categoryIds, Vector<String> brandIds) {
    DbExe db = new DbExe();
    String query = "SELECT * FROM Product";
    if (names.size() == 0 && categoryIds.size() == 0 && brandIds.size() == 0) { return; }
    query += " WHERE ";
    if (names.size() != 0) {
      query += "name Like '%";
      for (int i = 0; i < names.size(); i++) {
        query += names.get(i);
        if (i != names.size() - 1) { query += "%' OR name Like '%"; }
      }
      query += "%'";
    }
    if (categoryIds.size() != 0) {
      if (names.size() != 0) { query += " AND "; }
      query += "categoryId IN (";
      for (int i = 0; i < categoryIds.size(); i++) {
        query += categoryIds.get(i);
        if (i != categoryIds.size() - 1) { query += ", "; }
      }
      query += ")";
    }
    if (brandIds.size() != 0) {
      if (categoryIds.size() != 0) { query += " AND "; }
      query += "brandId IN (";
      for (int i = 0; i < brandIds.size(); i++) {
        query += brandIds.get(i);
        if (i != brandIds.size() - 1) { query += ", "; }
      }
      query += ")";
    }
    query += ";";
    Vector<Vector<String>> vec = db.dmlExe(query);
    System.out.println("id\tcategoryId\tbrandId\tname\tprice\tisSealed");
    for (Vector<String> v : vec) {
      System.out.println(v.get(0) + "\t" + v.get(1) + "\t" + v.get(2) + "\t" + v.get(3) + "\t" + v.get(5) + "\t" + v.get(7));
    }
  }

  private Vector<String> getData(String msg) {
    Vector<String> data = new Vector<String>();
    do {
      data.add(GeneralMethods.GetStringInput("Enter " + msg + ":"));
    } while (GeneralMethods.GetCharInput("Add another " + msg + "? (y/n)") == 'y');
    return data;
  }

  public void listItems() {
    System.out.println("1- List all items");
    System.out.println("2- List items by name");
    System.out.println("3- List items by category");
    System.out.println("4- List items by brand");
    char c;
    do {
      c = GeneralMethods.GetCharInput("Enter your choice: (1-4)");
    } while (c < '1' || c > '4');
    Vector<String> names = new Vector<String>(), categoryIds = new Vector<String>(), brandIds = new Vector<String>();

    switch (c) {
      case '1':
        listItems(names, categoryIds, brandIds);
        break;
      case '2':
        names = getData("name");
        listItems(names, categoryIds, brandIds);
        break;
      case '3':
        categoryIds = getData("category id");
        listItems(names, categoryIds, brandIds);
        break;
      case '4':
        brandIds = getData("brand id");
        listItems(names, categoryIds, brandIds);
        break;
    }
  }

  public void listCategories() {
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("SELECT * FROM Category;");
    System.out.println("id\tname");
    for (Vector<String> v : vec) {
      System.out.println(v.get(0) + "\t" + v.get(1));
    }
  }

  public void listBrands() {
    DbExe db = new DbExe();
    Vector<Vector<String>> vec = db.dmlExe("SELECT * FROM Brand;");
    System.out.println("id\tname");
    for (Vector<String> v : vec) {
      System.out.println(v.get(0) + "\t" + v.get(1));
    }
  }

  public boolean selectItem() {
    DbExe db = new DbExe();
    int pid = GeneralMethods.GetIntInput("Enter product id:");
    Vector<Vector<String>> product = db.dmlExe("SELECT id, isSealed FROM Product WHERE id = " + pid + ";");
    if (product.size() == 0) { return false; }
    double qty = GeneralMethods.GetDoubleInput("Enter quantity:");
    do {
      System.out.println("Quantity must be between 0 and 50.");
      qty = GeneralMethods.GetDoubleInput("Enter quantity:");
      if (product.get(0).get(1) == "1") { qty = Math.floor(qty); }
    } while (qty <= 0 || qty > 50);
    return db.ddlExe("INSERT INTO ShoppingCartItems (ShoppingCartId, productId, quantity) " +
        "VALUES (" + ShoppingCartId_ + ", " + pid + ", " + qty + ");");
  }

  public static void main(String[] args) {
    DbExe db = new DbExe();
    CatalogMgr cm = new CatalogMgr(1);
    cm.listCategories();
    cm.listItems();
  }

}
