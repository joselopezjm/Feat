package jmlv.org.Metryc;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class JParser {
	String data;
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Map<Integer, String> properties2 = new HashMap<Integer, String>();

	public JsonObject toJson(String json) {
		JsonElement jelement = new JsonParser().parse(json);
		JsonObject jobject = jelement.getAsJsonObject();
		// jobject = jobject.getAsJsonObject("request");
		return jobject;
	}

	public String getNameObj() {
		return "" + properties.get("nameObj");
	}

	public void setNameObj(Object value) {
		properties.put("nameObj", value);
	}

	public String getNameMeth() {
		return "" + properties.get("nameMeth");
	}

	public void setNameMeth(Object value) {
		properties.put("nameMeth", value);
	}

	public void setparam(Integer index, String value) {
		properties2.put(index, value);
	}

	public String getparam(Integer id) {
		return "" + properties2.get(id);
	}

	public HashMap parseParams(JsonPrimitive je, String struct) {

		HashMap myMap = new HashMap();
		// System.out.println(struct);
		// JsonPrimitive je = params.get(0).getAsJsonPrimitive();
		if (!je.isJsonObject()) {
			switch (struct.toLowerCase()) {
			case "integer":
					int value1 = je.getAsInt();
					// System.out.println(value1);
					myMap.put("paramclass", Integer.class);
					myMap.put("paramvalue", value1);
				break;
			case "int":
					int value2 = je.getAsInt();
					myMap.put("paramclass", int.class);
					myMap.put("paramvalue", value2);
				break;
			case "short":
					short value3 = je.getAsShort();
					myMap.put("paramclass", short.class);
					myMap.put("paramvalue", value3);
				break;
			case "long":
					long value4 = je.getAsLong();
					myMap.put("paramclass", long.class);
					myMap.put("paramvalue", value4);
				break;
			case "float":
					float value5 = je.getAsFloat();
					myMap.put("paramclass", float.class);
					myMap.put("paramvalue", value5);
				break;
			case "double":
					double value6 = je.getAsDouble();
					myMap.put("paramclass", double.class);
					myMap.put("paramvalue", value6);
				break;
			case "string":
					String value7 = je.getAsString();
					myMap.put("paramclass", String.class);
					myMap.put("paramvalue", value7);
				break;
			case "char":
					char value8 = je.getAsCharacter();
					myMap.put("paramclass", char.class);
					myMap.put("paramvalue", value8);
				break;
			case "boolean":
					Boolean value9 = je.getAsBoolean();
					myMap.put("paramclass", Boolean.class);
					myMap.put("paramvalue", value9);
		
				break;
			}
		} else {
			
		} // Parse JAVA OBJECT

		return myMap;
	}

	public HashMap parseParams(JsonArray je, String struct) {

		HashMap myMap = new HashMap();
		Boolean flag = false;
		// JsonPrimitive je = params.get(0).getAsJsonPrimitive();
		switch (struct.toLowerCase()) {
		case "integer":
			if(!flag){
			Integer[] arr1 = new Integer[je.size()];
			Integer[][] biarr1 = new Integer[je.size()][];
			for(int w=0; w<je.size();w++){
				if(je.get(w).isJsonArray()){
					flag=true;
				}
			}
			for(int i=0; i<je.size();i++){
				if(!flag){
					arr1[i] = je.get(i).getAsInt();
				}else{
					if(je.get(i).isJsonArray()){
						JsonArray arr = je.get(i).getAsJsonArray();	
						for(int x =0; x<arr.size(); x++){
							biarr1[i] = new Integer[arr.size()];
							biarr1[i][x]=(arr.get(x).getAsInt());
						}
					}else{
						biarr1[i] = new Integer[1];
						biarr1[i][0] = je.get(i).getAsInt();	
				}
				
				}
			}
			if(!flag){
				myMap.put("paramclass", Integer[].class);
				myMap.put("paramvalue", arr1);
			}
			else{
				myMap.put("paramclass", Integer[][].class);
				myMap.put("paramvalue", biarr1);
			}}
			break;
		case "int":
			if(!flag){
				int[] arr1 = new int[je.size()];
				int[][] biarr1 = new int[je.size()][];
				for(int w=0; w<je.size();w++){
					if(je.get(w).isJsonArray()){
						flag=true;
					}
				}
				for(int i=0; i<je.size();i++){
					if(!flag){
						arr1[i] = je.get(i).getAsInt();
					}else{
						if(je.get(i).isJsonArray()){
							JsonArray arr = je.get(i).getAsJsonArray();	
							for(int x =0; x<arr.size(); x++){
								biarr1[i] = new int[arr.size()];
								biarr1[i][x]=(arr.get(x).getAsInt());
							}
						}else{
							biarr1[i] = new int[1];
							biarr1[i][0] = je.get(i).getAsInt();	
					}
					
					}
				}
				if(!flag){
					myMap.put("paramclass", int[].class);
					myMap.put("paramvalue", arr1);
				}
				else{
					myMap.put("paramclass", int[][].class);
					myMap.put("paramvalue", biarr1);
				}}
				break;
		case "float":
			if(!flag){
					Float[] arr1 = new Float[je.size()];
					Float[][] biarr1 = new Float[je.size()][];
				for(int w=0; w<je.size();w++){
					if(je.get(w).isJsonArray()){
						flag=true;
					}
				}
				for(int i=0; i<je.size();i++){
					if(!flag){
						arr1[i] = je.get(i).getAsFloat();
					}else{
						if(je.get(i).isJsonArray()){
							JsonArray arr = je.get(i).getAsJsonArray();	
							for(int x =0; x<arr.size(); x++){
								biarr1[i] = new Float[arr.size()];
								biarr1[i][x]=(arr.get(x).getAsFloat());
							}
						}else{
							biarr1[i] = new Float[1];
							biarr1[i][0] = je.get(i).getAsFloat();	
					}
					
					}
				}
				if(!flag){
					myMap.put("paramclass", Float[].class);
					myMap.put("paramvalue", arr1);
				}
				else{
					myMap.put("paramclass", Float[][].class);
					myMap.put("paramvalue", biarr1);
				}}
				break;
		case "double":
			if(!flag){
				Double[] arr1 = new Double[je.size()];
				Double[][] biarr1 = new Double[je.size()][];
			for(int w=0; w<je.size();w++){
				if(je.get(w).isJsonArray()){
					flag=true;
				}
			}
			for(int i=0; i<je.size();i++){
				if(!flag){
					arr1[i] = je.get(i).getAsDouble();
				}else{
					if(je.get(i).isJsonArray()){
						JsonArray arr = je.get(i).getAsJsonArray();	
						for(int x =0; x<arr.size(); x++){
							biarr1[i] = new Double[arr.size()];
							biarr1[i][x]=(arr.get(x).getAsDouble());
						}
					}else{
						biarr1[i] = new Double[1];
						biarr1[i][0] = je.get(i).getAsDouble();	
				}
				
				}
			}
			if(!flag){
				myMap.put("paramclass", Double[].class);
				myMap.put("paramvalue", arr1);
			}
			else{
				myMap.put("paramclass", Double[][].class);
				myMap.put("paramvalue", biarr1);
			}}
			break;
		case "string":
			if(!flag){
				String[] arr1 = new String[je.size()];
				String[][] biarr1 = new String[je.size()][];
			for(int w=0; w<je.size();w++){
				if(je.get(w).isJsonArray()){
					flag=true;
				}
			}
			for(int i=0; i<je.size();i++){
				if(!flag){
					arr1[i] = je.get(i).getAsString();
				}else{
					if(je.get(i).isJsonArray()){
						JsonArray arr = je.get(i).getAsJsonArray();	
						for(int x =0; x<arr.size(); x++){
							biarr1[i] = new String[arr.size()];
							biarr1[i][x]=(arr.get(x).getAsString());
						}
					}else{
						biarr1[i] = new String[1];
						biarr1[i][0] = je.get(i).getAsString();	
				}
				
				}
			}
			if(!flag){
				myMap.put("paramclass", String[].class);
				myMap.put("paramvalue", arr1);
			}
			else{
				myMap.put("paramclass", String[][].class);
				myMap.put("paramvalue", biarr1);
			}}
			break;
		case "char":
			if(!flag){
				char[] arr1 = new char[je.size()];
				char[][] biarr1 = new char[je.size()][];
			for(int w=0; w<je.size();w++){
				if(je.get(w).isJsonArray()){
					flag=true;
				}
			}
			for(int i=0; i<je.size();i++){
				if(!flag){
					arr1[i] = je.get(i).getAsCharacter();
				}else{
					if(je.get(i).isJsonArray()){
						JsonArray arr = je.get(i).getAsJsonArray();	
						for(int x =0; x<arr.size(); x++){
							biarr1[i] = new char[arr.size()];
							biarr1[i][x]=(arr.get(x).getAsCharacter());
						}
					}else{
						biarr1[i] = new char[1];
						biarr1[i][0] = je.get(i).getAsCharacter();	
				}
				
				}
			}
			if(!flag){
				myMap.put("paramclass", char[].class);
				myMap.put("paramvalue", arr1);
			}
			else{
				myMap.put("paramclass", char[][].class);
				myMap.put("paramvalue", biarr1);
			}}
			break;
		case "boolean":
			if(!flag){
				Boolean[] arr1 = new Boolean[je.size()];
				Boolean[][] biarr1 = new Boolean[je.size()][];
			for(int w=0; w<je.size();w++){
				if(je.get(w).isJsonArray()){
					flag=true;
				}
			}
			for(int i=0; i<je.size();i++){
				if(!flag){
					arr1[i] = je.get(i).getAsBoolean();
				}else{
					if(je.get(i).isJsonArray()){
						JsonArray arr = je.get(i).getAsJsonArray();	
						for(int x =0; x<arr.size(); x++){
							biarr1[i] = new Boolean[arr.size()];
							biarr1[i][x]=(arr.get(x).getAsBoolean());
						}
					}else{
						biarr1[i] = new Boolean[1];
						biarr1[i][0] = je.get(i).getAsBoolean();	
				}
				
				}
			}
			if(!flag){
				myMap.put("paramclass", Boolean[].class);
				myMap.put("paramvalue", arr1);
			}
			else{
				myMap.put("paramclass", Boolean[][].class);
				myMap.put("paramvalue", biarr1);
			}}
			break;
		}
		return myMap;
	}

}
