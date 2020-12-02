package com.company;

import javax.xml.transform.Result;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connect {
    /**
     * Connect to a sample database
     */

    public Connection conn = null;
    private final String filename;

    public Connect() {
        this.filename = "db.db";
    }

    public Connect(String filename) {
        this.filename = filename;
    }

    // Utility function to make array usable
    public List<Object[]> ResultSetToArray(ResultSet result) {
        List<Object[]> table = new ArrayList<>();
        try {
            int nCol = result.getMetaData().getColumnCount();

            while (result.next()) {
                String[] row = new String[nCol];
                for (int iCol = 1; iCol <= nCol; iCol++) {
                    Object obj = result.getObject(iCol);
                    row[iCol - 1] = (obj == null) ? null : obj.toString();
                }
                table.add(row);
            }

        } catch(Exception e) {

        }

        return table;
    }

    // Connect to database
    public void connect() {
        try {
            // db parameters
            URL url = getClass().getResource(filename);
            String url_path = "jdbc:sqlite:" + url.getPath();
            // create a connection to the database
            conn = DriverManager.getConnection(url_path);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ResultSet GetCart(String username) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Cart INNER JOIN CartItems ON Cart.id=CartItems.cartID\n" +
                    "INNER JOIN Card ON Card.id=CartItems.cardID WHERE Cart.username= \'" + username +"\'";
            Statement stmt = conn.createStatement();
            System.out.println("EXEC SQL: " + sql);
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Java error: " + e.getMessage());
        }
        return rs;
    }

    public void Add2Cart(String cartID, String cardID, int quantity) {
        try {
            String sql = "INSERT INTO CartItems (cartID, cardID, quantity)"
                    + "VALUES ('" + cartID + "', '" + cardID + "', '" + String.valueOf(quantity) + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Java error: " + e.getMessage());
        }
    }

    public List<Object[]> GetCard(String cardID) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Card INNER JOIN 'Set' ON Set.cardID=Card.id WHERE Card.id='" + cardID + "' LIMIT 1;";
            Statement stmt  = conn.createStatement();
            System.out.println("EXEC SQL: " + sql);
            rs    = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Java error: " + e.getMessage());
        }
        return ResultSetToArray(rs);
    }

    public List<Card> GetCards(String search) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Card INNER JOIN 'Set' ON 'Set'.cardID=Card.id WHERE Card.name LIKE '%" + search + "%' OR Card.id='" + search + "' OR 'Set'.'set'='" + search + "' LIMIT 100;";
            Statement stmt  = conn.createStatement();
            System.out.println("EXEC SQL: " + sql);
            rs    = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Java error: " + e.getMessage());
        }

        List<Card> table = new ArrayList<>();
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String cardID = rs.getString("id");
                String cmc = rs.getString("cmc");
                String type = rs.getString("type");
                String subtype = rs.getString("subtype");
                String text = rs.getString("text");
                int power = rs.getInt("power");
                int toughness = rs.getInt("toughness");
                String set = rs.getString("set");
                int number = rs.getInt("number");
                Card row = new Card(name, cardID, cmc, type, subtype, text, power, toughness, set, number);
                table.add(row);
            }

        } catch(Exception e) {

        }

        return table;
    }

    public List<Listing> GetListings(String cardID) {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Listing INNER JOIN Card ON Card.id=Listing.cardID WHERE Listing.cardID='" + cardID + "';";
            Statement stmt  = conn.createStatement();
            System.out.println("EXEC SQL: " + sql);
            rs    = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Java error: " + e.getMessage());
        }
        List<Listing> table = new ArrayList<>();
        try {
            while (rs.next()) {
                int CardID = rs.getInt("cardID");

                String username = rs.getString("username");

                String id = rs.getString("id");
                System.out.println(id);
                String condition = rs.getString("condition");
                String price = rs.getString("price");
                Listing row = new Listing(CardID, username, id, condition, price);
                table.add(row);

            }

        } catch(Exception e) {System.out.println("Java error: " + e.getMessage());}
        System.out.println(table.size());
        return table;
    }
}