package edu.bdu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.bdu.dao.SurveyDaoImpl;12312
import edu.bdu.dao.UserDaoImpl;
import edu.bdu.entity.User;
import edu.bdu.entity.Survey;
import java.util.List;
import java.util.Date;
import edu.bdu.entity.User;

public class AdminIndexAction extends HttpServlet {
    /**
     * ��ʼ��
     */
	private SurveyDaoImpl surveyOp;//�����ʾ������

	private UserDaoImpl userOp;//�û�������

	private int titleSize = 9;//������ʾ������ַ���

	private int linkSize = 9;//������ʾ������ַ���

	private int nameSize = 9;//�û�����ʾ������ַ���

	private int passSize = 9;//�û�������ʾ������ַ���

	public AdminIndexAction() {
		super();
	}


	public void destroy() {
		super.destroy();

	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        doPost(request,response);//����doPost�������д���
		/*response.setContentType("text/html");
		PrintWriter out = response.getWriter();//��ȡ�����
		out.println("hello");
		out.flush();
		out.close();*/
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");//���ñ����ʽ
		response.setContentType("text/html");
		HttpSession session = request.getSession();//�������session
		PrintWriter out = response.getWriter();//��ȡ�����

		int commType = Integer.parseInt(request.getParameter("comType"));//��ò���������

		switch(commType)
		{
		case 1:

	        out.println(getSurveyListTen());//��õ����ʾ��б�

			break;
		case 2:

			out.println(getUserListTen());//���������ӵ��û��б�

			break;
		case 3:

	        User user = (User)session.getAttribute("user");//��õ�ǰ�û���Ϣ

	        String userInfo = "{\"userID\":\"" + user.getUserID() + "\","   +
	        		"\"userName\":\"" + user.getUserName() + "\"," +
	        		"\"userPass\":\"" + user.getUserPassword() + "\"," +
	        	    "\"userType\":\"" + user.getUserType() + "\"}";

	        if(user != null)
	           out.print(userInfo);//����û�����
	        else
	           out.print("usererror");//�û���ݲ���ȷ

			break;

		case 4:

            session.invalidate();//�û�session����ʧЧ

            out.print("loginout");//�û��˳��ɹ�

			break;

		case 5:
	        User user1 = new User();//�����û�����

	        if(request.getParameter("userid")!= null)
	        {
	           int userid = Integer.parseInt(request.getParameter("userid"));//����û����

	           user1.setUserID(userid);//�û���Ÿ�ֵ
	        }

	        if(request.getParameter("username")!=null)
	        {
	            String username = request.getParameter("username");

	            user1.setUserName(username);//Ϊ�û����Ƹ�ֵ
	        }

	        if(request.getParameter("userpassword")!=null)
	        {
	            String userpassword = request.getParameter("userpassword");

	            user1.setUserPassword(userpassword);//Ϊ�û����븳ֵ
	        }

	        if(request.getParameter("usertype")!= null)
	        {
	           String usertype = request.getParameter("usertype");

	           int usertypeId = Integer.parseInt(usertype);

	           user1.setUserType(usertypeId);//Ϊ�û����͸�ֵ
	        }

	        this.userOp = new UserDaoImpl();//�û�������

	        if(userOp.updateUser(user1))
	        {
	          session.setAttribute("user",user1);//���µ��û���Ϣ����session
	          //response.sendRedirect("testuserdao.jsp");
	          out.print("success");//����ɹ���־
	        }
	        else
	        {
	          out.println("fail");//���ʧ�ܱ�־
	        }

			break;

		default:
			break;
		}

		out.flush();
		out.close();
	}


	//���������ӵ��û�
	private String getUserListTen()
	{
		 String results = "";//���ͷʮ�������ʾ�

	     UserDaoImpl userop = new UserDaoImpl();//�û����ݿ������

	     List userlist = userop.getUsersNotAdmin();//����û��б�

	     int userListSize = 10;//��ʾ�����ʾ�����


	     results += "<table border='0' cellpadding='0' cellspacing='0' class='tableClass'>";
	     results += "<tr align='center' class='tdHeader'>";
	     results += "<td align='center' class='tdHeader'>�û����</td>";
	     results += "<td align='center' class='tdHeader'>�û�����</td>";
	     results += "<td align='center' class='tdHeader'>�û�����</td>";
	     results += "<td align='center' class='tdHeader'>����</td>";
	     results += "<td align='center' class='tdHeader'>����</td>";
	     results += "<td align='center' class='tdHeader'>����</td>";
	     results += "</tr>";

	     if(userListSize > userlist.size())
	     {
	    	 userListSize = userlist.size();//����û��б�
	     }

	     for(int i = 0;i < userListSize;i++)
	     {
	         User user = (User)userlist.get(i);//����û���Ϣ

	         results += "<tr class='trContent'>";

	         results += "<td class='tdContent' align='center'>" + user.getUserID() + "</td>";

		     String userName = user.getUserName().length()<= nameSize?user.getUserName():user.getUserName().substring(0,nameSize);

	         results += "<td class='tdContent' align='center'>" + userName + "</td>";

		     String userPass = user.getUserPassword().length()<= passSize?user.getUserPassword():user.getUserPassword().substring(0,passSize);

	         results += "<td class='tdContent' align='center'>" + userPass + "</td>";

	         results += "<td class='tdContent' width='30' align='center'>" + "<a href='javascript:getUserDataById1(" + user.getUserID() + ")'>�鿴</a>" + "</td>";
	         results += "<td class='tdContent' width='30' align='center'>" + "<a href='javascript:getUserDataById(" + user.getUserID() + ")'>�޸�</a>" + "</td>";
	         results += "<td class='tdContent' width='30' align='center'>" + "<a href='javascript:deleteUserDataById(" + user.getUserID() + ")'>ɾ��</a>" + "</td>";

	         results += "</tr>";

	     }
	     results +="</table>";

		return results;
	}
	//�������ʮ�������ʾ�
	private String getSurveyListTen()
	{
		String results = "";//���ͷʮ�������ʾ�

	     SurveyDaoImpl surveyOp = new SurveyDaoImpl();//�����ʾ������

	     List surveylist = surveyOp.getServeyList();//��õ����ʾ��б�

	     int surveyListSize = 10;//��ʾ�����ʾ�����

	     if(surveyListSize > surveylist.size())
	     {
	    	 surveyListSize = surveylist.size();//��õ����б��ʵ�ʴ�С
	     }

	     results +="<table border='0' cellpadding='0' cellspacing='0' class='tableClass'>";
	     results +="<tr>";
	     results +="<td align='center' class='tdHeader'>�ʾ����</td>";
	     results +="<td align='center' class='tdHeader'>�ʾ�˵��</td>";
	     results +="<td align='center' class='tdHeader'>����ʱ��</td>";
	     results +="<td align='center' class='tdHeader'>����</td>";
	     results +="<td align='center' class='tdHeader'>����</td>";
	     results +="<td align='center' class='tdHeader'>����</td>";
	     results +="</tr>";
	     for(int i = 0;i < surveyListSize;i++)
	     {
	       Survey survey = (Survey)surveylist.get(i);//����û���Ϣ

	       results +="<tr class='trContent'>";

	       String surveyTitle = survey.getSurveyTitle().length()<= titleSize?survey.getSurveyTitle():survey.getSurveyTitle().substring(0,titleSize);

	       results +="<td class='tdContent' align='center'>" + surveyTitle + "</td>";

	       String surveyLink = survey.getSurveyLink().length()<=linkSize?survey.getSurveyLink():survey.getSurveyLink().substring(0,linkSize);
	       results +="<td class='tdContent' align='center'>" + surveyLink + "</td>";

	       Date date = survey.getSurveyCreateDate();//�����ʾ���ʱ��

	       if(date != null)
	         results +="<td class='tdContent' align='center'>" + date.toString() + "</td>";
	       else
	         results +="<td class='tdContent' align='center'>" + "����ʱ���쳣" + "</td>";



	       results +="<td class='tdContent' width='30' align='center'>" + "<a href='javascript:updateSurveyDataById(" + survey.getSurveyID() + ")'>�޸�</a>" + "</td>";
	       results +="<td class='tdContent' width='30' align='center'>" + "<a href='javascript:deleteSurveyDataById(" + survey.getSurveyID() + ")'>ɾ��</a>" + "</td>";
	       results +="<td class='tdContent' width='30' align='center'>" + "<a href='../showsurvey.jsp?surveyId=" + survey.getSurveyID() + "'>Ԥ��</a>" + "</td>";
	       results +="</tr>";

	     }
	     results +="</table>";

		return results;
	}


    //��ʼ��
	public void init() throws ServletException {

	}

}
