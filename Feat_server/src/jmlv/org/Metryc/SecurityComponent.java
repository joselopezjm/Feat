package jmlv.org.Metryc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SecurityComponent extends Exception {

	private static HashMap data;
	public static Map<String, Map<String, ArrayList>> finaldata = new HashMap<>();
	private static Boolean activated;

	public void initialize(String con) throws SQLException, IOException {
		activated = true;
		JDBCPool connectionPool;
		connectionPool = new JDBCPool(con);
		Connection connection = connectionPool.getConnection();
		connectionPool.executeQuery(connection, "security");
		ResultSet rs = connectionPool.getResultSet();
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		connectionPool.close();
		ArrayList list = new ArrayList();
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
			// System.out.println("Row= "+row);
		}

		for (int i = 0; i < list.size(); i++) {
			data = (HashMap) list.get(i);
			// System.out.println("2Row= "+data);
			Object value = finaldata.get("" + data.get("id_usuarios"));
			// System.out.println("init_iduser= "+value);
			if (value != null) {
				value = ((ArrayList) ((HashMap) finaldata.get("" + data.get("id_usuarios")))
						.get("" + data.get("descripcion_objetos")));
				if (value != null) {

					finaldata.get("" + data.get("id_usuarios")).get("" + data.get("descripcion_objetos"))
							.add(data.get("nombre_metodos"));
				} else {
					Map<String, ArrayList> objetos2 = new HashMap<>();
					ArrayList metodos = new ArrayList();
					metodos.add(data.get("nombre_metodos"));
					// HashMap x = (HashMap)(data.get("descripcion_objetos"));
					// objetos2.put("" + data.get("descripcion_objetos"),
					// metodos);
					finaldata.get("" + data.get("id_usuarios")).put("" + data.get("descripcion_objetos"), metodos);
					// finaldata.put("" + data.get("id_usuarios"), objetos2);
				}
			} else {
				Map<String, ArrayList> objetos = new HashMap<>();
				ArrayList metodos = new ArrayList();
				metodos.add(data.get("nombre_metodos"));
				// System.out.println("first_obj="+data.get("descripcion_objetos"));
				objetos.put("" + data.get("descripcion_objetos"), metodos);
				finaldata.put("" + data.get("id_usuarios"), objetos);
				// System.out.println(data.get("id_usuarios"));
				// System.out.println("data
				// ="+finaldata.get(""+data.get("id_usuarios")).getClass());
			}

		}
	}

	public void Deactivate() {
		activated = false;
	}

	public SecurityComponent(String message) {
		super(message);
	}

	public SecurityComponent() {
		// TODO Auto-generated constructor stub
	}

	public Boolean CheckStatus() {
		return activated;
	}

	public Object[] CheckSecurityData(Object UserId, String object, String method) {

		if (!activated && UserId.equals("1")) {
			Object[] response = new Object[2];
			response[0] = "Guaranteed";
			response[1] = true;
			return response;
		} else {
			String Id = UserId + "";
			boolean status = false;
			Object[] response = new Object[2];
			Map<String, ArrayList> userdata = finaldata.get("" + Id);
			// System.out.println("userdata="+userdata+" reqobject="+object);
			if (userdata != null) {
				ArrayList value = finaldata.get("" + Id).get(object);
				// System.out.println("yo"+value);
				if (value != null) {
					for (int i = 0; i < value.size(); i++) {
						String current = (String) value.get(i);
						// System.out.println("hola" + current);
						if (current.equals(method)) {
							response[0] = "Guaranteed";
							response[1] = true;
							status = true;
						}
					}
					if (!status) {
						response[0] = "Error, restricted method";
						response[1] = false;
					}
				} else {
					response[0] = "Error, restricted object";
					response[1] = false;
				}
			} else {
				response[0] = "Error, restricted user";
				response[1] = false;
			}
			return response;
		}
	}

}
