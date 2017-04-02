package jmlv.org.Metryc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ReadConfig {
	InputStream inputStream;

	public String[] getConn(String index) throws IOException {

		Properties prop = new Properties();
		InputStream input = null;
		String[] result = new String[6];
		try {
			input = getClass().getClassLoader().getResourceAsStream("conn.properties");
			// load a properties file
			prop.load(input);
			// get the property value and print it out
			String[] bdname = prop.getProperty("bdName").split(",");
			String[] user = prop.getProperty("user").split(",");
			String[] password = prop.getProperty("password").split(",");
			String[] driver = prop.getProperty("driver").split(",");
			String[] url = prop.getProperty("url").split(",");
			String[] initC = prop.getProperty("initC").split(",");
			String[] maxC = prop.getProperty("maxC").split(",");
			String[] idcon = prop.getProperty("idcon").split(",");

			for (int i = 0; i < idcon.length; i++) {
				if (index.equals(idcon[i])) {

					result[0] = driver[i];
					result[1] = url[i] + bdname[i];
					result[2] = user[i];
					result[3] = password[i];
					result[4] = initC[i];
					result[5] = maxC[i];
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public String getQuery(String id) throws IOException {

		Properties prop = new Properties();
		InputStream input = null;
		String query = "";
		try {
			input = getClass().getClassLoader().getResourceAsStream("query.properties");
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			query = prop.getProperty(id);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return query;
	}
	public long getTimeout(){
		Properties prop = new Properties();
		InputStream input = null;
		long timeout = 0;
		try {
			input = getClass().getClassLoader().getResourceAsStream("metryc.properties");
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			timeout = Long.parseLong(prop.getProperty("timeout"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return timeout;
		
	}
	
	public Boolean getAutoregulate(){
		Properties prop = new Properties();
		InputStream input = null;
		String autoregulate = "";
		Boolean flag = false;
		try {
			input = getClass().getClassLoader().getResourceAsStream("metryc.properties");
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			autoregulate = prop.getProperty("auto_regulate").toLowerCase();
			if(autoregulate.equals("on")){
				flag=true;
			}	
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
		
	}
	
	

}
