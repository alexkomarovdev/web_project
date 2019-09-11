package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Tru;
import model.Type_notification;
import utils.TruDb;
import utils.Type_notificationDb;

/**
 * Servlet implementation class NewTruServlet
 */
@WebServlet("/NewTruServlet")
public class NewTruServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewTruServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<String> trues = TruDb.select();
		request.setAttribute("trues", trues);	
		RequestDispatcher dispatcher //
        = this.getServletContext()//
              .getRequestDispatcher("/WEB-INF/view/newtru.jsp");

  dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        int id = 0;
        String name =  null;
       
        try {
        name = request.getParameter("name");//read from html form
        
        }catch(Exception ex){ex.printStackTrace();} 
        
        if (name.isEmpty()==true) 
        {
        System.out.println("Введите обязательные поля!!!"); 		
              
		doGet(request, response);
        }
        else {
    	    try{
    	    	Tru tru = new Tru (id, name);
    	    	TruDb.insert(tru);
    	    	
    	    }catch(Exception ex){ex.printStackTrace();}         
        		}

        PrintWriter writer = response.getWriter();
        writer.println("<p>Новый ТРУ с этими данными успешно зарегистрирован в системе!"+"</p>");
        writer.println("<p>Имя: " + name + "</p>");
        writer.println("<a href=/web_app>Главная страница</a>");
        writer.println("<br>");        
        writer.println("<form method=[GET] "+
    		 "accept-charset=[UTF-8] "+
    				"action=EmployeeTaskServlet>"+
    						"<input type=\"submit\" value=\"Назад\">"+
    						 "</form>"); 
        
	}

}