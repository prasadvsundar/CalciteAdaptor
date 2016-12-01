package com.nanobi.calcite.excel;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import com.nanobi.calcite.csv.CsvSchema;
import com.nanobi.calcite.excel.ExcelTable.Flavor;




public class ExcelTest {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		    Class.forName("org.apache.calcite.jdbc.Driver");
	        Connection connection =
	                DriverManager.getConnection("jdbc:calcite:");
	        CalciteConnection calciteConnection =
	                connection.unwrap(CalciteConnection.class);
	        SchemaPlus rootSchema = calciteConnection.getRootSchema(); 
	        
	        File csvDir = new File("/home/nanobi/Drill/CSV/");
	        // SchemaPlus schema = rootSchema.add("s", new CsvSchema(csvDir,null)); 
	        //rootSchema.add("CSV", new CsvSchema(csvDir, Flavor.SCANNABLE));
	         //rootSchema.add("EXCEL", new ExcelSchema(csvDir, Flavor.TRANSLATABLE));
	         rootSchema.add("EXCEL", new ExcelSchema(csvDir, Flavor.TRANSLATABLE, "Product_Dim"));
		
		 Statement statement = connection.createStatement();
	     ResultSet resultSet =   statement.executeQuery("select * from \"EXCEL\".\"Product_Dim1\"");
		// ResultSet resultSet =   statement.executeQuery("select * from \"EXCEL\".\"bugsb\"");
	     StringBuilder buf = new StringBuilder();
	     /*   try{
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
	    */
	     while (resultSet.next()) {
	    	 int n = resultSet.getMetaData().getColumnCount();
	    	 for (int i = 1; i <= n; i++) {
	    		 System.out.println(resultSet.getMetaData().getColumnTypeName(i));
	    	 }
	           break;
	            //System.out.println(resultSet.getObject("date"));
	           // Date d =(Date) resultSet.getObject("date");
	            //System.out.println(d);
	        }
	     
	       
	}
}
