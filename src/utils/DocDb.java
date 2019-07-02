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
import model.Fdoc;
import model.User;

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

//***********************************************************************************************************************************
    public static Fdoc selectone(int id) {
		Connection conn = DbFilter.getConn();
    	Fdoc fdoc = null;
		//Выполним запрос
		String sqlquery =					
				"SELECT \r\n" + 
				"(SELECT documents.id FROM documents WHERE documents.id=?) as \"id\",\r\n" + 
				"type_docs.name as \"type\",\r\n" + 
				"contractor.name as \"contractor\",\r\n" + 
				"documents.name as \"name\",\r\n" + 
				"documents.content as \"content\",\r\n" + 
				"users.name as \"creator_name\",\r\n" + 
				"users.second as \"creator_second\",\r\n" + 
				"urgency.name as \"urgency\",\r\n" + 
				"documents.date_cre,\r\n" + 
				"documents.status_finished,\r\n" + 
				"documents.rec_date,\r\n" + 
				"documents.receiver_list,\r\n" + 
				"documents.sender_list,\r\n" + 
				"departments.name as \"dep\"\r\n" + 
				"FROM documents\r\n" +
				"LEFT JOIN contractor ON documents.id_contractor = contractor.id \r\n" + 
				"LEFT JOIN type_docs ON documents.id_type_docs = type_docs.id\r\n" + 
				"LEFT JOIN users ON documents.creator = users.id\r\n" + 
				"LEFT JOIN urgency ON documents.id_urgency = urgency.id\r\n" + 
				"LEFT JOIN departments ON departments.id = documents.current_dep\r\n";
		
        try (PreparedStatement preparedStatement = conn.prepareStatement(sqlquery)){
            preparedStatement.setInt(1, id);
            ResultSet resultset = preparedStatement.executeQuery();		
			if (resultset.next()) {
		        int id_doc = resultset.getInt("id");
		        String type =  resultset.getString("type");
		        String contractor =  resultset.getString("contractor");
		        //byte[] blob =  resultset.getBytes("blob");
		        String name =  resultset.getString("name");
		        String content =  resultset.getString("content");
		        String creator_name =  resultset.getString("creator_name");
		        String creator_second =  resultset.getString("creator_second");
		        String urgency =  resultset.getString("urgency");
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
        
                String dep =  resultset.getString("dep");

                fdoc = new Fdoc (id_doc, type, contractor, name, content, creator_name,creator_second, 
    	    			urgency, date_cre, status_finished, rec_date, receiver_arraylist, sender_arraylist, dep);
                
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
    	
    	return fdoc;
    	
    }	
//********************************************************************************************************************************
	
    public static ArrayList<Fdoc> select() {
    	//ArrayList<Doc> docs = new ArrayList<Doc>();
    	ArrayList<Fdoc> fdocs = new ArrayList<Fdoc>();

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
			        //"SELECT * FROM documents ORDER BY id"
					"SELECT \r\n" + 
					"documents.id as \"id\",\r\n" + 
					"type_docs.name as \"type\",\r\n" + 
					"contractor.name as \"contractor\",\r\n" + 
					"documents.name as \"name\",\r\n" + 
					"documents.content as \"content\",\r\n" + 
					"users.name as \"creator_name\",\r\n" + 
					"users.second as \"creator_second\",\r\n" + 
					"urgency.name as \"urgency\",\r\n" + 
					"documents.date_cre,\r\n" + 
					"documents.status_finished,\r\n" + 
					"documents.rec_date,\r\n" + 
					"documents.receiver_list,\r\n" + 
					"documents.sender_list,\r\n" + 
					"departments.name as \"dep\"\r\n" + 
					"FROM documents\r\n" + 
					"LEFT JOIN contractor ON documents.id_contractor = contractor.id \r\n" + 
					"LEFT JOIN type_docs ON documents.id_type_docs = type_docs.id\r\n" + 
					"LEFT JOIN users ON documents.creator = users.id\r\n" + 
					"LEFT JOIN urgency ON documents.id_urgency = urgency.id\r\n" + 
					"LEFT JOIN departments ON departments.id = documents.current_dep;"
					);
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
		        String type =  resultset.getString("type");
		        String contractor =  resultset.getString("contractor");
		        //byte[] blob =  resultset.getBytes("blob");
		        String name =  resultset.getString("name");
		        String content =  resultset.getString("content");
		        String creator_name =  resultset.getString("creator_name");
		        String creator_second =  resultset.getString("creator_second");
		        String urgency =  resultset.getString("urgency");
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
        
                String dep =  resultset.getString("dep");

                
                //0107 Doc doc = new Doc (id, id_type, id_contractor, name, content, creator, 
    	    			//id_urgency, date_cre, status_finished, rec_date, receiver_arraylist, sender_arraylist, current_dep);
                //0107 docs.add(doc);
                Fdoc fdoc = new Fdoc (id, type, contractor, name, content, creator_name,creator_second, 
    	    			urgency, date_cre, status_finished, rec_date, receiver_arraylist, sender_arraylist, dep);
                fdocs.add(fdoc);
					
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
    	
    	return fdocs;
    	
    }	
//--------------------------------------------------------------------------------------------------------------------------    
    
}