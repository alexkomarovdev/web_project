package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Fdoc;
import model.User;
import utils.CreateReport;
import utils.DocDb;
import utils.IfoDb;
import utils.UserDb;

/**
 * Servlet implementation class JurTaskServlet
 */
@WebServlet("/JurTaskServlet")
public class JurTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JurTaskServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		String login = null;
		String loginedUser = null;
		int id_department = 0;
        String filter_docs = "0";
		try {
			// получаем сессию
			HttpSession session = request.getSession();
			//// получаем объект login
			login = (String) session.getAttribute("login");
			//// получаем объект logineduser
			loginedUser = (String) session.getAttribute("loginedUser");				
			System.out.println("jurTask полученный логин из сессии " + login);
			System.out.println("jurTask полученный логинUser из сессии " + loginedUser);
			
			
			if (login == null ) {System.out.println("Зайдите пользователем!!"); 
				String path = request.getContextPath() + "/LoginPageServlet";
				response.sendRedirect(path);
				return; 
			
			}else{
				//// получаем объект id_department
				id_department = (int) session.getAttribute("id_department");
				System.out.println("jurTask полученный id_department из сессии " + id_department);
				System.out.println("Здравствуйте   " + loginedUser + "!! Вы зашли в кабинет юриста");
				User user = UserDb.selectone(login);
				System.out.println("Доступные роли пользователя" + user.getRoles());
				ArrayList<String> roles = user.getRoles();
				//перебор элементов массива
				for(String role : roles){			
				System.out.println(role);
				}
			// проверяем наличие элемента
			if(roles.contains("ROLE_JUR")){
				List<Integer> users = UserDb.selectUserFromDep(id_department);
				System.out.println("успешно взят список юзеров " + users.get(0));
				
				int i = 0;
				ArrayList<Fdoc> docs = null;
				for (int user2: users) {					
					i++;
					System.out.println("id user " + user2);
					ArrayList<Fdoc> docs2 = DocDb.selectForCurUser_Full(user2);
					if (i==1) {
						docs = docs2;
					} 
					if (i>1)  {
						docs = (ArrayList<Fdoc>) joinlist(docs, docs2);
					}			        			        			        
				}
				
				System.out.println("i: " + i);
				System.out.println("размер массива: " + docs.size());
				//ArrayList<String> ifo_arraylist_str = new ArrayList<String>();
				
/*
				docs = DocDb.selectForCurUser_Full(users);
	            request.setAttribute("docs", docs);
	            session.setAttribute("docs", docs);
*/ 
	            
		        try {
		        	filter_docs = request.getParameter("filter_docs");
		        	if (filter_docs==null) {
		                filter_docs = "*";
		        	}
		        	System.out.println(filter_docs);
		        }catch (NullPointerException e) {
		        	System.out.println("Параметр не найден!" + e);
		        }	            
	            
				
		        if (filter_docs.equals("*")) {
		        	docs = DocDb.selectForCurUser_Full(users);         
		            request.setAttribute("docs", docs);
		            session.setAttribute("docs", docs);
		        } else {
		        	docs = DocDb.selectForCurUser_Full(users, Integer.parseInt(filter_docs));
		            request.setAttribute("docs", docs);
		            session.setAttribute("docs", docs);
		        }	
				

				System.out.println("размер массива из селекта : " + docs.size());
				request.setAttribute("docs_size", docs.size());
				//request.setAttribute("docs", docs);
				request.setAttribute("user", user);
				//session.setAttribute("docs", docs); // присваиваем сессии для выгрузки отчёта
				session.setAttribute("user", user); // присваиваем сессии для использовании при редактировании документа
				//RequestDispatcher dispatcher = null;
				RequestDispatcher dispatcher //
				= this.getServletContext()//
				.getRequestDispatcher("/WEB-INF/view/jurTaskView.jsp");
			
				dispatcher.forward(request, response);			
			}else{		
				RequestDispatcher dispatcher = null;
				dispatcher //
				= this.getServletContext()//
				.getRequestDispatcher("/WEB-INF/view/accessDenied.jsp");
				
				dispatcher.forward(request, response);		
				}
			}
		}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();    }

}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String filepath = null;
		try {			
			// получаем сессию если есть
	        HttpSession session = request.getSession(true);
	        ArrayList<Fdoc> fdocs = (ArrayList<Fdoc>) session.getAttribute("docs");
	        System.out.println("полученный docs из сессии " + fdocs.get(0));
	        Fdoc name = fdocs.get(0);
	        String name2 = name.getName();
	        System.out.println("полученный name2 docs из сессии " + name2);
	        System.out.println("закончен ли " + fdocs.get(0).getStatus_finished());
	        filepath = request.getParameter("filepath");
	        System.out.println(filepath);
	        CreateReport.createReport(fdocs, filepath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		doGet(request, response);
	}

	
	
	public static <T> List <? super T> joinlist(
			final List <? extends T> listA,
			final List <? extends T> listB){
			if (listA ==null){
				throw new NullPointerException("listA is null");
			}
			if (listB ==null){
				throw new NullPointerException("listB is null");
			}
			if (listA.isEmpty()) {
				return new ArrayList<T>(listB);
			}else if (listB.isEmpty()) {
				return new ArrayList<T>(listA);
			} else {
				ArrayList<T> result= new ArrayList<T>(
						listA.size() + listB.size());
				result.addAll(listA);
				result.addAll(listB);
				return result;
			}				
		}	
	
	
}
