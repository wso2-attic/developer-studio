package org.wso2.datamapper.engine.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.wso2.datamapper.engine.core.DataMapper;

public class MapperMain {

	public static void main(String[] args) throws FileNotFoundException, JSONException {
		
		DataMapper mapper = new DataMapper();
		InputStream inStream = new FileInputStream(new File("./resources/input6.xml"));
		//InputStream inStream = new FileInputStream(new File("./resources/input3.xml"));
		
		try {
		//	String doMap = mapper.doMap(new File("./resources/MappingConfig3.js"),inStream,new File("./resources/inputSchema3.avsc"),new File("./resources/outputSchema3.avsc"));			
			
			String doMap = mapper.doMap(new File("./resources/MappingConfig6.js"),inStream,new File("./resources/inputSchema6.avsc"),new File("./resources/outputSchema6.avsc"));			
			
			System.out.println(doMap);
			inStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
