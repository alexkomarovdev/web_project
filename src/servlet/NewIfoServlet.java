package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Ifo;
import utils.IfoDb;

/**
 * Servlet implementation class NewIfoServlet
 */
@WebServlet("/NewIfoServlet")
public class NewIfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewIfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<String> ifoes = IfoDb.select();
		request.setAttribute("ifoes", ifoes);	
		RequestDispatcher dispatcher //
        = this.getServletContext()//
              .getRequestDispatcher("/WEB-INF/view/newifo.jsp");

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
    	    	Ifo ifo = new Ifo (id, name);
    	    	IfoDb.insert(ifo);
    	    	response.sendRedirect(
    					request.getContextPath() + "/InfoPageServlet?infomessage=" + 
    	    					"Success!!");
    	    }catch(Exception ex){ex.printStackTrace();}         
        		}

        PrintWriter writer = response.getWriter();
        writer.println("<p>Новый вид источника финансирования с этими данными успешно зарегистрирован в системе!"+"</p>");
        writer.println("<p>Имя: " + name + "</p>");
        writer.println("<a href=/web_app>Главная страница</a>");
        writer.println("<br>");        
		/*
		 * writer.println("<form method=[GET] "+ "accept-charset=[UTF-8] "+
		 * "action=EmployeeTaskServlet>"+ "<input type=\"submit\" value=\"Назад\">"+
		 * "</form>");
		 */
        writer.println("<input type=\"submit\" class=\"btn-sm btn-dark\" value=\"Назад\" onCLick=\"history.back()\"> ");
	}

}
