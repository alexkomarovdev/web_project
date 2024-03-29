package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import filter.DbFilter;
import model.Division;
import model.Fdoc;
import model.Ifo;
import model.Law;
import model.Notification;
import model.Tru;
import model.User;
import utils.DocDb;
import utils.IfoDb;
import utils.LawDb;
import utils.NotiificationDb;
import utils.TruDb;
import utils.Type_docsDb;
import utils.UrgencyDb;
import utils.UserDb;
import utils.Calendar;
import utils.ContractorDb;
import utils.DivisionDb;

/**
 * Servlet implementation class DocEditServlet
 */
@WebServlet("/DocEditServlet")
public class DocEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	       try {
	            int id = Integer.parseInt(request.getParameter("id"));	            
	            Fdoc doc = DocDb.selectone2(id); 
	            ArrayList<String> type_docs =  Type_docsDb.select();
	            ArrayList<String> contractors =  ContractorDb.select();
	            ArrayList<String> urgencies =  UrgencyDb.select();
	            List<Tru> trues =  TruDb.selectModel();
				List<Law> laws = LawDb.selectModel();
				List<Division> divisions = DivisionDb.selectModel();	
				List<Ifo> ifoes = IfoDb.selectModel();
				List<Fdoc> allDocs = DocDb.selectAllFull();
				
	            if(doc!=null) {
	                request.setAttribute("doc", doc);
	                request.setAttribute("allDocs", allDocs);
	                request.setAttribute("image", doc.getBlob()); 
	                request.setAttribute("type_docs", type_docs);
	                request.setAttribute("contractors", contractors);
	                request.setAttribute("urgencies", urgencies);
	                
	    			if (!trues.isEmpty()) {
	    				System.out.println("Взят trues!!");
	    			request.setAttribute("trues", trues);
	    				}
	    			if (!laws.isEmpty()) {
	    				System.out.println("Взят laws!!");
	    			request.setAttribute("laws", laws);
	    				}
	    			if (!divisions.isEmpty()) {
	    				System.out.println("Взят divisions!!");
	    			request.setAttribute("divisions", divisions);
	    				}
	    			if (!ifoes.isEmpty()) {
	    				System.out.println("Взят ifoes!!");
	    			request.setAttribute("ifoes", ifoes);
	    				}

//*********************************************************************************************************************************
	                try {
	            		// получаем сессию
	                    HttpSession session = request.getSession();
	                    //// получаем объект login
	                    String login = (String) session.getAttribute("login");
	                    //// получаем объект logineduser
	                    String loginedUser = (String) session.getAttribute("loginedUser");
	                    System.out.println("полученный логин из сессии " + login);
	                    System.out.println("полученный логинUser из сессии " + loginedUser);
               	                	
	                	id = Integer.parseInt(request.getParameter("id"));
	                	//String id_user = (request.getParameter("id_user"));
	                	//System.out.println ("id документа= " + id + "  " + "Пользователь id_user= "+ id_user);
	                	System.out.println ("id документа= " + id);
	                	
	                	User userModel = UserDb.selectone(login);
	                	//если в поле документа "ожидающий принятия" тот же пользователь то :
	                	if (doc.getReceiver_list().get(0).equals(String.valueOf(userModel.getId())) )  {
		                	//создаем уведомление, что документ принят (тип уведомления №1 - получение )
		                	Notification notification = new Notification(userModel.getId() , 1, Calendar.Date(), id, userModel.getId() );
		                	int id_notification = NotiificationDb.insert(notification);
		                	System.out.println (String.valueOf(id_notification));
		                	
		                	Connection conn2 = DbFilter.getConn(); 
		                    List<String> receiver_arraylist = new ArrayList<String>();	                    
		                    receiver_arraylist.add(String.valueOf (userModel.getId())); 
		                    receiver_arraylist.add(String.valueOf(id_notification));
		                    Array receiver_list = conn2.createArrayOf("text", receiver_arraylist.toArray()); //This is Postgre feature Особенность реализации, преобразуем массив понятный Постгре        
		                	
		                	DocDb.updateSender_listDoc(id, receiver_list);	
	                	}  else {
	                		//создаем уведомление, что документ открыт (тип уведомления №4 - открытие )
		                	Notification notification = new Notification(userModel.getId() , 4, Calendar.Date(), id, userModel.getId() );
		                	int id_notification = NotiificationDb.insert(notification);
		                	System.out.println (String.valueOf(id_notification));
	                	}
	                	

	                	System.out.println("Чтение завершено!! ");
	                    //doGet(request, response);
	                }catch (Exception ex)
	                {
	                	
	                }	                
//*********************************************************************************************************************************	                
	                getServletContext().getRequestDispatcher("/WEB-INF/view/docedit.jsp").forward(request, response);
	            }
	            else {
	                getServletContext().getRequestDispatcher("/WEB-INF/view/notfound.jsp").forward(request, response);
	            }
	        }
	        catch(Exception ex) {
	            getServletContext().getRequestDispatcher("/WEB-INF/view/notfound.jsp").forward(request, response);
	        }	
	}

	  @Override 
	  public void init() throws ServletException{ 

		  System.out.println("*************SERVLET IS INIT DocEditServlet**************");
		  
	  }	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		
        try {
        	int id = Integer.parseInt(request.getParameter("id"));
        	String num = request.getParameter("num");            
        	String num2 = request.getParameter("num2");
            String doc_urgency = request.getParameter("doc_urgency");
            String urgency = request.getParameter("urgency");
            String doc_id_type = request.getParameter("doc_id_type");
            String id_type = request.getParameter("id_type");
            String content = request.getParameter("content");
            String content2 = request.getParameter("content2");
            String name = request.getParameter("name");
            String name2 = request.getParameter("name2");
            String rec_date = request.getParameter("rec_date");
            String rec_date2 = request.getParameter("rec_date2");
            
            String doc_tru = request.getParameter("doc_tru");
            String tru = request.getParameter("tru");
            String doc_law = request.getParameter("doc_law");
            String law = request.getParameter("law");
            String doc_division = request.getParameter("doc_division");
            String division = request.getParameter("division");
            String price = request.getParameter("price");        
            String price2 = request.getParameter("price2");
            String paid = request.getParameter("paid");
            String add_agr = request.getParameter("add_agr"); 
            String add_agr2 = request.getParameter("add_agr2"); 
            String price_add_agr = request.getParameter("price_add_agr");
            String price_add_agr2 = request.getParameter("price_add_agr2");
            
            String[] ifo = {""};
            ifo = request.getParameterValues("ifo");//for check of change
            ArrayList<String> ifo_arr_check = new ArrayList<String>(Arrays.asList(ifo));
            
            
            String[] ifo_m = {""};
            ArrayList<String> ifo_arr = null;
            try {
            ifo_m = request.getParameterValues("ifo_m");//read ifo_m from html form input (name ifo_m)
            ifo_arr = new ArrayList<String>(Arrays.asList(ifo_m));
            System.out.println("ifo_m = " + ifo_m);
            System.out.println("ifo_arr = " + ifo_arr);
            } catch (Exception e) {
            	System.out.println("ИФО равно нулю!!!");
            }
            
            
            System.out.println("ifo = " + ifo);
            System.out.println("ifo_arr_check = " + ifo_arr_check);

            String date_concluded = request.getParameter("date_concluded");            
            String date_concluded2 = request.getParameter("date_concluded2");
            LocalDate localDate_concluded = LocalDate.parse(date_concluded);
            
            if (!doc_urgency.equals(urgency) )
            {
            	UrgencyDb.update(id, urgency);
            	System.out.println("Изменился статус срочности!");
            }
            if (!doc_id_type.equals(id_type))
            {
            	Type_docsDb.update(id, id_type);
            	System.out.println("Изменился тип!");
            }
            
	        if (!name.equals(name2))
	        {
	        	DocDb.updateName(id, name);
	        	System.out.println("Изменилось имя!");
	        }
	        
            if (!content.equals(content2))
            {	
            	DocDb.updateContent(id, content);
            	System.out.println("Изменилось содержание!");
            	
            }
            if (!rec_date.equals(rec_date2))
            {	
            	DocDb.updateRecDate(id, rec_date);
            	System.out.println("Изменилась рек. дата!");            	
            }          
            
            if (!doc_tru.equals(tru))
            {	
            	DocDb.updateTru(id, tru);
            	System.out.println("Изменилось ТРУ!");            	
            }
            if (!doc_law.equals(law))
            {	
            	DocDb.updateLaw(id, law);
            	System.out.println("Изменилcя вид закона!");            	
            }
            if (!doc_division.equals(division))
            {	
            	DocDb.updateDivision(id, division);
            	System.out.println("Изменились данные подразделения!");            	
            }
            
            
            if (!price.equals(price2))
            {	
            	DocDb.updatePrice(id, new BigDecimal(price));
            	System.out.println("Изменилась сумма!");
    			//производим полный пересчёт сумм
    			DocDb.updatePriceTotal();
            }
            
            if (!add_agr.equals(add_agr2))
            {	
            	DocDb.updateAdd_agr(id, add_agr);
            	System.out.println("Изменилcя доп. соглашение!");            	
            }
      
            if (!price_add_agr.equals(price_add_agr2))
            {	
            	DocDb.updatePrice_add_agr(id, new BigDecimal(price_add_agr));
            	System.out.println("Изменилась сумма по доп. соглашению!");            	
            }

            //if (paid.equals("on"))
            if (paid != null)
            {	            	
            	DocDb.updatePaid(id, true);
            	System.out.println("Изменился факт проплаты true!");            	
            } else {
            	DocDb.updatePaid(id, false);
            	System.out.println("Изменился факт проплаты false!"); 
            }

			if (ifo_arr!=null) {
				  if (!ifo_arr.equals(ifo_arr_check)) {
					  System.out.println("Изменились ИФО!");
				        int[] id_ifo_int = Arrays.asList(ifo_m).stream().mapToInt(Integer::parseInt).toArray();        
				        Integer[] id_ifo_integer = Arrays.stream( id_ifo_int ).boxed().toArray( Integer[]::new );	        
				        List<Integer> ifo_list = new ArrayList<Integer>(Arrays.asList(id_ifo_integer));
				        DocDb.updateIfo(id, ifo_list);
					  }
			}

			
			if (!date_concluded.equals(date_concluded2))
            {	
            	DocDb.updateDate_concluded(id, localDate_concluded);
            	System.out.println("Изменилась дата заключения документа!");            	
            }
			
			if (!num.equals(num2))
            {	
            	DocDb.updateNum(id, num);
            	System.out.println("Изменился номер документа!");            	
            }
            

			 

            doGet(request, response);
            //getServletContext().getRequestDispatcher("/WEB-INF/view/docedit.jsp").forward(request, response);
        }catch (Exception ex)
        {
        	System.out.println(ex);
        }
		
		
	}

}
