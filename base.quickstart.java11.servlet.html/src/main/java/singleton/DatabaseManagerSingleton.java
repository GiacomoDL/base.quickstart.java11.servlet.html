package singleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import esempio.Message;

public class DatabaseManagerSingleton {

	Logger logger = LoggerFactory.getLogger(DatabaseManagerSingleton.class);

	private DatabaseManagerSingleton() {
		logger.debug("Instanziato Costruttore Privato Database Manager Singleton");
	}

	private static DatabaseManagerSingleton instance;

	public static DatabaseManagerSingleton getInstance() {
		if (instance == null) {
			instance = new DatabaseManagerSingleton();
		}
		return instance;
	}

	public ArrayList<Message> getMessages(LocalDateTime clientCall)
			throws ClassNotFoundException, SQLException, IOException {

		ArrayList<Message> messagesListToSendToCLient = new ArrayList<Message>();

		PropertiesManagerSingleton pms = PropertiesManagerSingleton.getInstance();
		String driver = pms.getProperty("database.mysql.driver");
		Class.forName(driver);
		String host = pms.getProperty("database.mysql.host");
		String port = pms.getProperty("database.mysql.port");
		String dbName = pms.getProperty("database.mysql.db.name");
		String url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;

		String username = pms.getProperty("database.mysql.db.username");
		String password = pms.getProperty("database.mysql.db.password");
		Connection con = DriverManager.getConnection(url, username, password);

		PreparedStatement query = con.prepareStatement("SELECT * FROM messages WHERE userInsertedTime >= ?");
		query.setTimestamp(1, java.sql.Timestamp.valueOf(clientCall));
		logger.debug(query.toString());
		ResultSet rs = query.executeQuery();
		ResultSetMetaData rsd = rs.getMetaData();

		while (rs.next()) {
			Message mex = new Message();
			ArrayList<Object> array = new ArrayList<>();
			for (int i = 2; i <= rsd.getColumnCount(); i++) {
				if (rs.getObject(i) != null)
					array.add(rs.getObject(i));
			}

			mex.setUserName((String) array.get(0));
			mex.setText((String) array.get(1));

			Timestamp userTime = (Timestamp) array.get(2);
			mex.setUserInsertedTime(userTime.toLocalDateTime());

			if (array.size() == rsd.getColumnCount() - 1) {
				Timestamp serverTime = (Timestamp) array.get(3);
				mex.setUserInsertedTime(serverTime.toLocalDateTime());
			}
			System.out.println(mex.toString());
			messagesListToSendToCLient.add(mex);
		}
		return messagesListToSendToCLient;
	}

	public int insertMessage(Message message) throws ClassNotFoundException, SQLException, IOException {
		PropertiesManagerSingleton pms = PropertiesManagerSingleton.getInstance();
		String driver = pms.getProperty("database.mysql.driver");
		Class.forName(driver);
		String host = pms.getProperty("database.mysql.host");
		String port = pms.getProperty("database.mysql.port");
		String dbName = pms.getProperty("database.mysql.db.name");
		String url = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;

		String username = pms.getProperty("database.mysql.db.username");
		String password = pms.getProperty("database.mysql.db.password");
		Connection con = DriverManager.getConnection(url, username, password);

		PreparedStatement query = con.prepareStatement(
				"INSERT INTO messages (userName, textMessage, userInsertedTime, serverReceivedTime) VALUES (?,?,?,?)");

		query.setString(1, message.getUserName());
		query.setString(2, message.getText());
		query.setTimestamp(3, java.sql.Timestamp.valueOf(message.getUserInsertedTime()));
		query.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));

		int row = query.executeUpdate();
		return row;

	}

	// update
	public void updateMessage() throws ClassNotFoundException, SQLException, IOException {

	}

	// delete
	public void deleteMessage() throws ClassNotFoundException, SQLException, IOException {

	}

}
