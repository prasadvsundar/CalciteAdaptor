package com.nanobi.calcite.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.monitorjbl.xlsx.StreamingReader;

public class EXcelReader {
	public static void main(String[] args) {
	    try {
	        FileInputStream file = new FileInputStream(new File("/home/nanobi/Drill/Excel/dm_source_10.xlsx"));

	       // FileInputStream fileIn = new FileInputStream(file);
	        
	       //  XSSFWorkbook workbook = new XSSFWorkbook(file);
	       //  XSSFSheet sheet = workbook.getSheetAt(0);
	        
	        
		    Workbook workbook =  StreamingReader.builder()
		    	        .rowCacheSize(10)   
		    	        .bufferSize(4096)     
		    	        .open(file); 
		    Sheet  sheet = workbook.getSheetAt(0);
	        
	        Iterator<Row> rowIterator = sheet.iterator();
	        int i=0;
	        while (rowIterator.hasNext())
	        {
	        	System.out.println(i++);
	            Row row = rowIterator.next();
	            //For each row, iterate through all the columns
	            Iterator<Cell> cellIterator = row.cellIterator();

	            while (cellIterator.hasNext()) 
	            {
	                Cell cell = cellIterator.next();
	               // System.out.println(Cell.CELL_TYPE_STRING);
	                System.out.println(cell.getCellType());
	                try{
	                	 System.out.println(cell.getDateCellValue());
	                }catch(Exception e){
	                	
	                }
	               // System.out.println(cell.getStringCellValue() +"  *****  "+ cell.getCellType());
	               /* switch (cell.getCellType()) 
	                {
	                    case Cell.CELL_TYPE_NUMERIC:
	                        System.out.print(cell.getNumericCellValue() + "\t");
	                        break;
	                    case Cell.CELL_TYPE_STRING:
	                        System.out.print(cell.getStringCellValue() + "\t");
	                        break;
	                }*/
	            }
	            System.out.println("");
	        }
	         
	        /* int index = workbook.getSheetIndex("Sheet1");
	         sheet = workbook.getSheetAt(index);
	         int rownumber=sheet.getLastRowNum()+1;  
	         
	         for (int i=1; i<rownumber; i++ )
	         {
	        	 XSSFRow row = sheet.getRow(i);
	             int colnumber = row.getLastCellNum();
	             for (int j=0; j<colnumber; j++ )
	             {
	            	 XSSFCell cell = row.getCell(j);
	            	 System.out.println(cell.getCellType());
	            	 //System.out.println(cell.getStringCellValue() +"  *****  "+ cell.getCellType());
	             }
	         }*/
	        
	        
	       /* int i = sheet.getLastRowNum();
	        for(int j=0; j<i; j++){
	        	XSSFRow excelrow = sheet.getRow(j);
	        	
	        	for(int col=0;col<excelrow.getLastCellNum(); col++){
	        		XSSFCell cell = (XSSFCell) excelrow.getCell(col);
					
					System.out.println(cell.getCellType());
					System.out.println(cell.getStringCellValue());
					
	        	}
	        	
	        						
	        }*/
	      
	        file.close();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
