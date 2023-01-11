package esempio;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import singleton.DatabaseManagerSingleton;

/**
 * Servlet implementation class TestServlet
 */
public class MessagesList extends HttpServlet {
	Logger logger = LoggerFactory.getLogger(MessagesList.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MessagesList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		logger.debug("test Git");
		logger.debug("test Git 2");
		logger.debug("start - doGet");
		String messages = getMessagesString();
		response.getWriter().append(messages);
		logger.debug("end - doGet");
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

	private String getMessagesString() {
		ArrayList<Message> messages = new ArrayList<Message>();
		String result = "";
		try {

			messages = DatabaseManagerSingleton.getInstance().getMessages(LocalDateTime.MIN);

		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Message message : messages) {
			result += message.toString() + "\n";
		}
		return result;

	}

}
