package jmlv.org.Metryc;

import com.google.gson.JsonObject;

public class MetrycBuilder {
	
private String body="";

public void add(String name,String value){
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": \"" + value +"\"";
	}
	else{
		this.body+=",\"" + name + "\": \"" + value +"\"";
	}
}

public void add(String name, JsonObject value) {
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": " + value.toString() +"";
	}
	else{
		this.body+=",\"" + name + "\": " + value +"";
	}
}

public void add(String name,int value){
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": " + value +"";
	}
	else{
		this.body+=",\"" + name + "\": " + value +"";
	}
}

public void add(String name, boolean value){
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": " + value +"";
	} 
	else{
		this.body+=",\"" + name + "\":" + value +"";
	}
}

public void add(String name,MetrycBuilder value){
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": " + value.getMetrycBuilder();
	}
	else{
		this.body+=",\"" + name + "\": " + value.getMetrycBuilder();
	}
}

public void add(String name, double value){
	if(this.body.equals("")){
		this.body+=" \"" + name + "\": " + value +"";
	}
	else{
		this.body+=",\"" + name + "\": " + value +"";
	}
}

public void add(String name, String value[]){
	String x= "[";
	for(int i=0;i<value.length;i++){
		if(i == value.length-1){
			x+="\""+value[i]+"\""+"]";
		}
		else{
			x+="\""+value[i]+"\""+",";
		}
	}
 
	if(this.body.equals("")){
		this.body+=" \"" + name + "\":"+x;
	}
	else{
		this.body+=",\"" + name + "\":"+x;
	}
}

public void add(String name, int value[]){
	String x= "[";
	for(int i=0;i<value.length;i++){
		if(i == value.length-1){
			x+=value[i]+"]";
		}
		else{
			x+=value[i]+",";
		}
	}
 
	if(this.body.equals("")){
		this.body+=" \"" + name + "\":"+x;
	}
	else{
		this.body+=",\"" + name + "\":"+x;
	}
}

public void add(String name, MetrycBuilder value[]){
	String x= "[";
	for(int i=0;i<value.length;i++){
		if(i == value.length-1){
			x+=value[i].getMetrycBuilder()+"]";
		}
		else{
			x+=value[i].getMetrycBuilder()+",";
		}
	}
 
	if(this.body.equals("")){
		this.body+=" \"" + name + "\":"+x;
	}
	else{
		this.body+=",\"" + name + "\":"+x;
	}
}

public void add(String name, double value[]){
	String x= "[";
	for(int i=0;i<value.length;i++){
		if(i == value.length-1){
			x+=value[i]+"]";
		}
		else{
			x+=value[i]+",";
		}
	}
 
	if(this.body.equals("")){
		this.body+=" \"" + name + "\":"+x;
	}
	else{
		this.body+=",\"" + name + "\":"+x;
	}
}

public void add(String name, String[][] value){
	String x= "[";
	if(this.body.equals("")){
 		this.body+=" \"" + name + "\":";
 	}
	for (int i = 1; i< value.length; i++) {
        for (int j = 0; j < value[1].length; j++) {
        	if(value[1].length==2){
        		if(j==0){
                	x+= "{\""+value[0][j]+"\":"+"\""+value[i][j]+"\"";
        		}
                	if(j==(value[1].length)-1){
                		x+=",\""+value[0][j]+"\":"+"\""+value[i][j]+"\"}";	
                	}
        	}
        	else{
        	if(j==0){
        	x+= "{\""+value[0][j]+"\":"+"\""+value[i][j]+"\"";
        			}
        	if(j<((value[1].length)) && j!=0){
        		x+=",\""+value[0][j]+"\":"+"\""+value[i][j]+"\"";	
        	}
        	if(j==(value[1].length)-1){
        		x+=",\""+value[0][j]+"\":"+"\""+value[i][j]+"\"}";	
        	}
          }
        }
        if(i<((value.length)-1)){
        this.body+=x+",      ";
        x="";}
        if(i==((value.length)-1)){
        	this.body+=x+"]";
        }
      
	}	
}

public String getMetrycBuilder(){
	return "{"+this.body +"}";
}


}
