package com.nanobi.calcite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.calcite.adapter.mongodb.MongoSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

public class MongoDBTest {

		public static void main(String[] args) throws SQLException, ClassNotFoundException {
			  
			
			
			
	        Class.forName("org.apache.calcite.jdbc.Driver");
	        Connection connection =
	                DriverManager.getConnection("jdbc:calcite:");
	        CalciteConnection calciteConnection =
	                connection.unwrap(CalciteConnection.class);
	        SchemaPlus rootSchema = calciteConnection.getRootSchema(); 
	        
	             
	        /*CSV Schema*/
	        File csvDir = new File("/home/nanobi/Drill/CSV/");
	       // SchemaPlus schema = rootSchema.add("s", new CsvSchema(csvDir,null)); 
	       // rootSchema.add("CSV", new CsvSchema(csvDir, Flavor.SCANNABLE));
	        rootSchema.add("MONGO", new MongoSchema("172.16.0.201:27017","NanobiDb"));
	        
	        Statement statement = connection.createStatement();
	       // select cast(_MAP[\\'product_id\\'] AS double) AS \"product_id\" from \"_foodmart\".\"sales_fact_1998
		     ResultSet resultSet =   statement.executeQuery("select _MAP['event'] event, count(*) gcount from \"MONGO\".\"nbmdcd_calendar_events\" GROUP BY _MAP['event']");
			// ResultSet resultSet =   statement.executeQuery("select * from \"EXCEL\".\"bugsb\"");
		     StringBuilder buf = new StringBuilder();
		     try{
		     while (resultSet.next()) {
		            int n = resultSet.getMetaData().getColumnCount();
		           // System.out.println(n);
		            for (int i = 1; i <= n; i++) {
		            	//System.out.println(resultSet.getMetaData().getColumnName(i) +" --------  "+resultSet.getMetaData().getColumnTypeName(i));
		                buf.append(i > 1 ? "; " : "")
		                        .append(resultSet.getMetaData().getColumnName(i))
		                        .append("=")
		                        .append(resultSet.getObject(resultSet.getMetaData().getColumnName(i)));
		                //System.out.println(resultSet.getMetaData().getColumnType(i));
		              
		            }
		            buf.append("\n");
		            System.out.println(buf.toString());
		        }
		     	//System.out.println(buf.toString());
		     }catch(Exception e){
		    	 e.printStackTrace();
		    	 
		     }
		     
		    /*while (resultSet.next()) {
	    	 int n = resultSet.getMetaData().getColumnCount();
	    	 for (int i = 1; i <= n; i++) {
	    		 System.out.println(resultSet.getMetaData().getColumnTypeName(i) +" :"+resultSet.getMetaData().getColumnName(i));
	    	 }
	           
	            //System.out.println(resultSet.getObject("date"));
	           // Date d =(Date) resultSet.getObject("date");
	            //System.out.println(d);
	        }*/
		}
	

}
