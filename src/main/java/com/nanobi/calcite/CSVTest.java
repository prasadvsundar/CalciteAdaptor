package com.nanobi.calcite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import com.nanobi.calcite.csv.CsvSchema;
import com.nanobi.calcite.csv.CsvTable.Flavor;

public class CSVTest {
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
        rootSchema.add("CSV", new CsvSchema(csvDir, Flavor.TRANSLATABLE));
        
        Statement statement = connection.createStatement();
	     ResultSet resultSet =   statement.executeQuery("SELECT * from \"CSV\".\"City_Details_Stg\"");
	     //SELECT max(CHAR_LENGTH("assignee_nm")) as assignee_nm, max(CHAR_LENGTH("bug_id")) as bug_id, max(CHAR_LENGTH("client_id")) as client_id, max(CHAR_LENGTH("component_nm")) as component_nm, max(CHAR_LENGTH("created_dt")) as created_dt, max(CHAR_LENGTH("curr_version_flag")) as curr_version_flag, max(CHAR_LENGTH("customer_name")) as customer_name, max(CHAR_LENGTH("days_past_due")) as days_past_due, max(CHAR_LENGTH("deadline_dt")) as deadline_dt, max(CHAR_LENGTH("deploy_platform")) as deploy_platform, max(CHAR_LENGTH("dev_resolution_date")) as dev_resolution_date, max(CHAR_LENGTH("duration_days")) as duration_days, max(CHAR_LENGTH("mis_date")) as mis_date, max(CHAR_LENGTH("modified_dt")) as modified_dt, max(CHAR_LENGTH("priority")) as priority, max(CHAR_LENGTH("product_nm")) as product_nm, max(CHAR_LENGTH("product_version")) as product_version, max(CHAR_LENGTH("reopen_flag")) as reopen_flag, max(CHAR_LENGTH("reporter_nm")) as reporter_nm, max(CHAR_LENGTH("resolution")) as resolution, max(CHAR_LENGTH("resolution_dt")) as resolution_dt, max(CHAR_LENGTH("severity")) as severity, max(CHAR_LENGTH("status")) as status, max(CHAR_LENGTH("summary")) as summary,  FROM "CSV"."bugs"
		// ResultSet resultSet =   statement.executeQuery("select * from \"EXCEL\".\"bugsb\"");
	     StringBuilder buf = new StringBuilder();
	    /* try{
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
	    	 
	     }*/
	     
	     //max(CHAR_LENGTH(\"component_nm\")) as component_nm
	     
	    while (resultSet.next()) {
    	 int n = resultSet.getMetaData().getColumnCount();
    	 for (int i = 1; i <= n; i++) {
    		 System.out.println(resultSet.getMetaData().getColumnTypeName(i) +" :"+resultSet.getMetaData().getColumnName(i)+" :"+resultSet.getMetaData().getPrecision(i));
    	 }
           break;
            //System.out.println(resultSet.getObject("date"));
           // Date d =(Date) resultSet.getObject("date");
            //System.out.println(d);
        }
	     
	     
	    /* while (resultSet.next()) {
	    	 int n = resultSet.getMetaData().getColumnCount();
	    	 StringBuilder sizeString = new StringBuilder();
	    	 sizeString.append("SELECT ");
	    	 for (int i = 1; i <= n; i++) {
	    		 //System.out.println(resultSet.getMetaData().getColumnTypeName(i) +" :"+resultSet.getMetaData().getColumnName(i)+" :"+resultSet.getMetaData().getPrecision(i));
	    		 
	    		 if(resultSet.getMetaData().getColumnTypeName(i).equalsIgnoreCase("VARCHAR"))
	    		 sizeString.append("max(CHAR_LENGTH(\"").append(resultSet.getMetaData().getColumnName(i)).append("\")) as ").append(resultSet.getMetaData().getColumnName(i)).append(", ");
	    	 }
	    	 sizeString.replace(sizeString.lastIndexOf(","), sizeString.length(), "");
	    	 sizeString.append(" FROM ").append("\"CSV\".\"bugs\"");
	         System.out.println(sizeString.toString()); 
	         
	         ResultSet resultSet2 =   statement.executeQuery(sizeString.toString());
	         
	         
	         try{
		     while (resultSet2.next()) {
		            int n2 = resultSet2.getMetaData().getColumnCount();
		           // System.out.println(n);
		            for (int i = 1; i <= n2; i++) {
		            	//System.out.println(resultSet.getMetaData().getColumnName(i) +" --------  "+resultSet.getMetaData().getColumnTypeName(i));
		                buf.append(i > 1 ? "; " : "")
		                        .append(resultSet2.getMetaData().getColumnName(i))
		                        .append("=")
		                        .append(resultSet2.getObject(resultSet2.getMetaData().getColumnName(i)));
		                //System.out.println(resultSet.getMetaData().getColumnType(i));
		              
		            }
		            buf.append("\n");
		            System.out.println(buf.toString());
		        }
		     	//System.out.println(buf.toString());
		     }catch(Exception e){
		    	 e.printStackTrace();
		    	 
		     }
	         break;
	            //System.out.println(resultSet.getObject("date"));
	           // Date d =(Date) resultSet.getObject("date");
	            //System.out.println(d);
	        }*/
	}
}
