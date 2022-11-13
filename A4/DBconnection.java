import java.sql.*;
import java.util.ArrayList;

public class DBconnection {  
    
  
    Connection con = null;  
    public DBconnection(){  
       
        String url = "jdbc:mysql://localhost/mtbs";
        String uid = "root";
        String pw = "310rootpw"; 
        try { 
            con = DriverManager.getConnection(url, uid, pw); 
        } catch (SQLException e) {
            System.out.println(e);
        }
    
    }    

    public boolean checkExistingCust(String email) {  
        try {   
            PreparedStatement pstmt = con.prepareStatement("SELECT email FROM customer WHERE email = ?");   
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();   
            if(rs.next()) 
                return true;   
            else 
                return false;
        } catch (SQLException e){ 
            System.out.println(e);     
            return false; 
        } 
    }  

    public void createCustomer(String name, String gender, String email, String bdate) { 
        try{ 
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO customer(name, gender, email, bdate) VALUES(?, ?, ?, ?)"); 
            pstmt.setString(1, name);  
            pstmt.setString(2, gender);  
            pstmt.setString(3, email);    
            pstmt.setString(4, bdate); 
            pstmt.execute(); 
        } catch (SQLException e) {
            System.out.println(e);
        }
    }  
    
    public String getCustGender(String email) { 
        try{ 
            PreparedStatement pstmt = con.prepareStatement("SELECT gender FROM customer WHERE email = ?"); 
            pstmt.setString(1, email); 
            ResultSet rs = rs.executeQuery(); rs.next(); 
            return rs.getString("gender");
        } catch (SQLException e) {
            System.out.println(e); 
            return "couldn't get custgender";
        }
    }
    
    public String getCustName(String email) {  
          try{ 
            PreparedStatement pstmt = con.prepareStatement("SELECT name FROM customer WHERE email = ?"); 
            pstmt.setString(1, email); 
            ResultSet rs = rs.executeQuery(); rs.next(); 
            return rs.getString("name");
        } catch (SQLException e) {
            System.out.println(e); 
            return "couldn't get custname";
        }
    }

    public void createMovieTicket(String email, String movie, String seatpos, String movietime) {  
        try{   
            PreparedStatement getMoviecost = con.prepareStatement("SELECT cost FROM movie WHERE name = ?"); 
            getMoviecost.setString(1, movie); 
            ResultSet rs = getMoviecost.executeQuery();  rs.next();  
            double cost = rs.getDouble("cost"); 
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO movieticket(cemail, mname, seatpos, movtime, totalPrice) VALUES(?, ?, ?, ?, ?)"); 
            pstmt.setString(1, email);  
            pstmt.setString(2, movie);   
            pstmt.setString(3, seatpos);  
            pstmt.setString(4, movietime); 
            pstmt.setDouble(5, cost);  
            pstmt.execute(); 
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
   /*  public ArrayList<String> showMovieTicket(String email, String movie) { 
        ArrayList<String> output = new ArrayList<>();  
        try{   
            PreparedStatement pstmt = con.prepareStatement("SELECT"); 
            pstmt.setString(1, email);  
            pstmt.setString(2, movie);  
            pstmt.execute(); 
        } catch (SQLException e) {
            System.out.println(e);
        }

    }  */

    public void deleteMovieTicket(String email, String movie) {  
        try{   
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM movieticket WHERE cemail = ? and mname = ?"); 
            pstmt.setString(1, email);  
            pstmt.setString(2, movie);  
            pstmt.execute(); 
        } catch (SQLException e) {
            System.out.println(e);
        } 
    } 

    public int getMovieTicketID(String email, String movie) {   
        try{    
            PreparedStatement pstmt = con.prepareStatement("SELECT mtid FROM movieticket WHERE cemail = ? and mname = ?"); 
            pstmt.setString(1, email);  
            pstmt.setString(2, movie);  
            ResultSet rs = pstmt.executeQuery(); rs.next(); 
            return rs.getInt("mtid"); 
        } catch (SQLException e) {
            System.out.println(e); 
            return -1; 
        } 
    }

    public ArrayList<String> getAllMovies() {    
        ArrayList<String> output = new ArrayList<>(); 
        try {   
            Statement stmt = con.createStatement();   
            ResultSet rs = stmt.executeQuery("SELECT * FROM movie");  
            while(rs.next()) { 
                output.add(String.format("%s, Genre: %s, ReleaseDate: %s, Movie Duration: %s",rs.getString("name"),rs.getString("genre"),rs.getString("releasedate"), rs.getString("duration"))); 
            } 
            return output; 
        } catch (SQLException e){ 
            System.out.println(e);    
            output.add("Unable to generate movie data..."); 
            return output; 
        }

    }  
    
    public ArrayList<String> getMovieTimes(String movie) {  
        ArrayList<String> output = new ArrayList<>(); 
        try {   
            PreparedStatement pstmt = con.prepareStatement("SELECT movietime FROM movietimes WHERE mname = ?");  
            pstmt.setString(1, movie);
            ResultSet rs = pstmt.executeQuery();  
            while(rs.next()) { 
                output.add(rs.getString("movietime"));
            } 
            return output; 
        } catch (SQLException e){ 
            System.out.println(e);    
            output.add("Unable to generate movie data..."); 
            return output; 
        }
    }

    public ArrayList<String> showAvailableSeats(String movie) { 
        ArrayList<String> output = new ArrayList<>();  
        try {   
            PreparedStatement pstmt = con.prepareStatement("SELECT srowcol FROM seat WHERE cemail IS NULL and mname = ?");   
            pstmt.setString(1, movie);
            ResultSet rs = pstmt.executeQuery(); 
            while(rs.next()) { 
                output.add(String.format("%s",rs.getString("srowcol"))); 
            } 
            return output; 
        } catch (SQLException e){ 
            System.out.println(e);    
            output.add("Unable to generate seat data..."); 
            return output; 
        }
    } 

    public void chooseSeat(String email, String movie, String seatpos) { 
        try { 
            PreparedStatement pstmt = con.prepareStatement("UPDATE seat SET cemail = ? WHERE mname = ? and srowcol = ?"); 
            pstmt.setString(1, email); 
            pstmt.setString(2, movie); 
            pstmt.setString(3, seatpos); 
            pstmt.execute(); 
        } catch (SQLException e) { 
            System.out.println(e);
        }
    } 
    
    public void unselectSeat(String email, String movie) { 
        try { 
            PreparedStatement pstmt = con.prepareStatement("UPDATE seat SET cemail = NULL WHERE mname = ? and cemail = ?"); 
            pstmt.setString(1, movie); 
            pstmt.setString(2, email); 
            pstmt.execute(); 
        } catch (SQLException e) { 
            System.out.println(e);
        }
    }   
    
    public String getSeatPos(String email, String movie) {  
        try { 
            PreparedStatement pstmt = con.prepareStatement("SELECT srowcol FROM seat WHERE cemail = ? and mname = ?"); 
            pstmt.setString(1, email); 
            pstmt.setString(2, movie); 
            ResultSet rs = pstmt.executeQuery(); rs.next();
            return rs.getString("srowcol");
        } catch (SQLException e) { 
            System.out.println(e);
            return "no seat found";
        }
    
    }

   /*public ArrayList<String> showAllAddons(){ 
        ArrayList<String> output = new ArrayList<>();  
        try {   
            PreparedStatement pstmt = con.prepareStatement("SELECT srowcol FROM seat WHERE cemail IS NULL and mname = ?");   
            pstmt.setString(1,);
            ResultSet rs = pstmt.executeQuery(); 
            while(rs.next()) { 
                output.add(String.format("%s",rs.getString("srowcol"))); 
            } 
            return output; 
        } catch (SQLException e){ 
            System.out.println(e);    
            output.add("Unable to generate seat data..."); 
            return output; 
        }
    } 

    public void showCustomerAddons(){ 

    }

    public void addToCart() { 


    } 

    public void deleteFromCart() { 


    } 


    public void printTicketSummary(){

    }
    */
    
}
