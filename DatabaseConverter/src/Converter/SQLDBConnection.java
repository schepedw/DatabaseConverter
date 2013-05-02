package Converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Apr 30, 2013.
 */
public class SQLDBConnection {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	private Connection conn;
	private Statement stmt;

	private String user;
	private String password;
	private String db_url;

	public SQLDBConnection(String user, String password, String DBname)
			throws SQLException, ClassNotFoundException {
		this.user = user;
		this.password = password;
		this.db_url = "jdbc:mysql://localhost/" + DBname;

		try {
			this.conn = null;
			this.stmt = null;
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(db_url, user, password);
			stmt = conn.createStatement();
		} catch (Exception e) {
			throw new SQLException("Could not connect to database");
		}

	}
	//probably not needed
	public int executeUpdate(String sql) throws ClassNotFoundException,
			SQLException {
		if (!hasOpenStatementAndConnection())
			reopenConnectionAndStatement();
		return this.stmt.executeUpdate(sql);

	}

	public ResultSet executeQuery(String sql)  {
		try {
			if (!hasOpenStatementAndConnection())
				reopenConnectionAndStatement();
				return this.stmt.executeQuery(sql);
		} catch (ClassNotFoundException | SQLException exception) {
			exception.printStackTrace();
			return null;
		}
		
	}

	private boolean hasOpenStatementAndConnection() throws SQLException {
		return !this.conn.isClosed() && !this.stmt.isClosed();
	}

	private void reopenConnectionAndStatement() throws SQLException,
			ClassNotFoundException {
		if (this.conn == null || this.conn.isClosed())
			this.conn = DriverManager.getConnection(db_url, user, password);
		if (this.stmt == null || this.stmt.isClosed())
			this.stmt = this.conn.createStatement();
	}

}
