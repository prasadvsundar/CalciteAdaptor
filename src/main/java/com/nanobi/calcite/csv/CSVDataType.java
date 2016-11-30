package com.nanobi.calcite.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import au.com.bytecode.opencsv.CSVReader;


/**
 * @author Prasad V S(EmpId:1036)
 * @version 1.0
 * @organization NanoBi Analytics
 * @Date Nov 17, 2014
 */
public class CSVDataType {
	final int MAX_CHECK_ROW_COUNT = 10;
	private  CSVReader reader;

	public CSVDataType( CSVReader reader) {
		this.reader = reader;
	}

	public Map<String, Object> getColumnNameAndType(JavaTypeFactory typeFactory) throws IOException {
		Map<String, Object> columnData = new HashMap<String, Object>();
		List<RelDataType> typesList = new ArrayList<>();
		List<String> names = new ArrayList<>();
		List<CsvFieldType> ExcelFieldTypes = new ArrayList<>();
		
		RelDataType[] types;
		int counter=0;
		String[] row;
		while((row = reader.readNext()) != null){
			for(String columnName : row){
				names.add(columnName);
			}
	    	break;
	      }
		types = new RelDataType[names.size()];
		int rowCount=1;
		while((row = reader.readNext()) != null&&checkArrayHasNullValue(types)&&rowCount<=MAX_CHECK_ROW_COUNT){
			
			for(int i=0; i<row.length; i++){
				/*final CsvFieldType fieldType;
		        final int colon = dataVal.indexOf(':');
		        if (colon >= 0) {
		          String typeString = dataVal.substring(colon + 1);
		          fieldType = CsvFieldType.of(typeString);
		        } else {
		          fieldType = null;
		        }*/
				CsvFieldType fieldType = CsvFieldType.of(row[i]);
				RelDataType type;
				if (fieldType == null) {
      	          type = typeFactory.createJavaType(String.class);
      	        } else {
      	          type = fieldType.toType(typeFactory);
      	        }
				if(types[i]==null){
                	types[i]=type;
                	//ExcelFieldTypes.add(fieldType);
                }
			}
	    	rowCount++;
	      }
		convertAllNullToStringType(types,typeFactory);
		ExcelFieldTypes=convertTypesToExcelFieldType(types);
		typesList = Arrays.asList(types);
		columnData.put("columnDataTyeps", typesList);
		columnData.put("columnNames", names);
		columnData.put("excelFieldTypes", ExcelFieldTypes);
		return columnData;
	}
	
	boolean checkArrayHasNullValue(RelDataType[] array){
		for(RelDataType obj:array){
			if(obj==null){
				return true;
			}
		}
		return false;
	}
	
	void convertAllNullToStringType(RelDataType[] array,JavaTypeFactory typeFactory){
		for(int i=0;i<array.length;i++){
			if(array[i]==null){
				RelDataType type = typeFactory.createJavaType(String.class);
				array[i]=type;
			}
		}
	}
	
	List<CsvFieldType> convertTypesToExcelFieldType(RelDataType[] types){
		List<CsvFieldType> excelFieldType = new ArrayList<>();
		for(int i=0;i<types.length;i++){
			excelFieldType.add(CsvFieldType.of(types[i].getFullTypeString()));
		}
		return excelFieldType;
	}
	
	public Object getValue(Cell cell) {

		switch (cell.getCellType()) {
		
		case Cell.CELL_TYPE_NUMERIC: {
			return cell.getNumericCellValue();
		}
		
		case Cell.CELL_TYPE_BOOLEAN:{
			return cell.getBooleanCellValue();
		
		}
		
		case Cell.CELL_TYPE_STRING:{
			
		}
		return cell;
	}
		return cell;
	}
}
