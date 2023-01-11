package esempio;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import singleton.DatabaseManagerSingleton;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class InsertMessage
 */
public class InsertMessage extends HttpServlet {
	Logger logger = LoggerFactory.getLogger(InsertMessage.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InsertMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int row;
		String username = request.getParameter("username");
		String messageText = request.getParameter("message");
		String output = "";

		row = saveMessage(username, messageText);

		if (row >= 1)
			output = "<h1>Messaggio inserito</h1>";
		else
			output = "<h1>Non e' stato possibile inserire il messaggio</h1>";

		String messagesList = "";
		try {
			messagesList = DatabaseManagerSingleton.getInstance().getAllMessages();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.getWriter().append(output);
		response.getWriter().append(messagesList);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private int saveMessage(String username, String messageText) {

		int row = 0;
		Message message = new Message();
		message.setUserName(username);
		message.setText(messageText);
		message.setUserInsertedTime(LocalDateTime.now());
		message.setServerReceivedTime(LocalDateTime.now());

		try {
			row = DatabaseManagerSingleton.getInstance().insertMessage(message);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;

	}

}
