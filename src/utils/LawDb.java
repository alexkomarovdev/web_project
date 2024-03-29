package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import filter.DbFilter;
import model.Law;
import model.Tru;


public class LawDb {

    public static int insert(Law law) {
    	Connection conn = DbFilter.getConn(); 
    	
        try{  
            PreparedStatement ps=conn.prepareStatement(  
		            "insert into law (id, name)"+
		            "values (nextval('seq_pk_id_law'),?)");  
	        
            ps.setString(1, law.getName());
           
            ps.executeUpdate();  
    		        //System.out.println("запрос выполнен успешно!!!");
    		 
        }catch(Exception ex){
        	ex.printStackTrace();
        	System.out.println(ex);}  
        finally 
        {try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}                                  

        
        return 0;
    } 	
//*****************************************************************************************************************
    public static List<String> select() {
    	Connection conn = DbFilter.getConn(); 
    	List<String> laws = new ArrayList<String>();

	        Statement statement = null;
			try {
				statement  = ((Connection) conn).createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                   

            try {
          	
            	ResultSet resultSet = statement.executeQuery("Select name FROM law");
    			while (resultSet.next()) {
    		        
    		        String name =  resultSet.getString("name");
    		        
    		        laws.add(name);
    		        
    		        //System.out.println("тип документа " + name);
    		        
    			}
    		        //System.out.println("запрос выполнен успешно!!!");
    		        
        }catch(SQLException ex){
        	ex.printStackTrace();
        	System.out.println(ex);}
        
        finally {

		}                                   

        
        return laws;
    }
  //*****************************************************************************************************************
    public static List<Law> selectModel() {
    	Connection conn = DbFilter.getConn(); 
    	List<Law> laws = new ArrayList<Law>();

	        Statement statement = null;
			try {
				statement  = ((Connection) conn).createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
                   

            try {
          	
            	ResultSet resultSet = statement.executeQuery("Select id, name FROM law");
    			while (resultSet.next()) {
    				int id =  resultSet.getInt("id");
    		        String name =  resultSet.getString("name");
    		        Law law = new Law (id,name);
    		        laws.add(law);
    		        
    		        //System.out.println("тип документа " + name);
    		        
    			}
    		        //System.out.println("запрос выполнен успешно!!!");
    		        
        }catch(SQLException ex){
        	ex.printStackTrace();
        	System.out.println(ex);}
        
        finally {

		}                                   

        
        return laws;
    }    
}
//*****************************************************************************************************************