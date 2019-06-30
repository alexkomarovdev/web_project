package utils;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import filter.DbFilter;
import model.Doc;

public class DocDb {

    public DocDb() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static int insert(Doc doc) {
    	Connection conn = DbFilter.getConn(); 
    	
        try{  
            PreparedStatement ps=conn.prepareStatement(  
		            "insert into documents (id, id_type_docs, id_contractor, name, "
		            + "content, creator, id_urgency, date_cre, status_finished, rec_date, receiver_list, sender_list, current_dep)"+
		            "values (nextval('seq_pk_id_docs'),?,?,?,?,?,?, ?, ?, ?, ?, ?, ?)");  
	        
            ps.setInt(1, doc.getId_type());
            ps.setInt(2, doc.getId_contractor());
            ps.setString(3, doc.getName());
            ps.setString(4, doc.getContent());
            ps.setInt(5, doc.getCreator());  
            ps.setInt(6, doc.getId_urgency());
            ps.setString(7, doc.getDate_cre());  
            ps.setInt(8, doc.getStatus_finished());
            ps.setString(9, doc.getRec_date());
            
            ArrayList<String> list = new ArrayList<String>(doc.getReceiver_list());
            Array array = conn.createArrayOf("text", list.toArray()); //This is Postgre feature Особенность реализации, преобразуем массив понятный Постгре 
			ps.setArray(10, array);
			
            ArrayList<String> list2 = new ArrayList<String>(doc.getSender_list());
            Array array2 = conn.createArrayOf("text", list2.toArray()); //This is Postgre feature Особенность реализации, преобразуем массив понятный Постгре 
			ps.setArray(11, array2);
			
			ps.setInt(12, doc.getCurrent_dep());              
            ps.executeUpdate();  
    		        System.out.println("запрос выполнен успешно!!!");
    		 
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

	
    public static ArrayList<Doc> select() {
    	ArrayList<Doc> docs = new ArrayList<Doc>();

		Connection conn = DbFilter.getConn();

        Statement statement = null;
		try {
			statement  = ((Connection) conn).createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Выполним запрос
        ResultSet resultset = null;
		try {
			resultset = statement.executeQuery(
			        "SELECT * FROM documents ORDER BY id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //result это указатель на первую строку с выборки
        //чтобы вывести данные мы будем использовать 
        //метод next() , с помощью которого переходим к следующему элементу
        System.out.println("Выводим statement");
        try {
			while (resultset.next()) {
		        int id = resultset.getInt("id");
		        int id_type =  resultset.getInt("id_type_docs");
		        int id_contractor =  resultset.getInt("id_contractor");
		        byte[] blob =  resultset.getBytes("blob");
		        String name =  resultset.getString("name");
		        String content =  resultset.getString("content");
		        int creator =  resultset.getInt("creator");
		        int id_urgency =  resultset.getInt("id_urgency");
		        String date_cre =  resultset.getString("date_cre");
		        int status_finished =  resultset.getInt("status_finished");
		        String rec_date =  resultset.getString("rec_date");
		        
		        Array receiver = resultset.getArray("receiver_list");
                String[] receiver_arr = (String[])receiver.getArray();
                ArrayList<String> receiver_arraylist= new ArrayList<String>();
                Collections.addAll(receiver_arraylist, receiver_arr);
                System.out.println("отработала коллекция");
		        
		        
		        Array sender = resultset.getArray("sender_list");
                String[] sender_arr = (String[])sender.getArray();
                ArrayList<String> sender_arraylist= new ArrayList<String>();
                Collections.addAll(sender_arraylist, sender_arr);
                System.out.println("отработала коллекция");
        
		        int current_dep =  resultset.getInt("current_dep");

                
                Doc doc = new Doc (id, id_type, id_contractor, name, content, creator, 
    	    			id_urgency, date_cre, status_finished, rec_date, receiver_arraylist, sender_arraylist, current_dep);
                docs.add(doc);

					
			    /*System.out.println(//arrayList+
			    		"\t Номер в базе #" + 
			    resultset.getInt("id")
			            + "\t" + name
			            +"\t" + content);*/
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally 
	        {try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} 		    	
    	
    	return docs;
    	
    }	
//--------------------------------------------------------------------------------------------------------------------------    
    
}
