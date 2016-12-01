package com.nanobi.calcite.excel;


import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.tree.Primitive;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Type of a field in a Excel file.
 *
 * <p>Usually, and unless specified explicitly in the header row, a field is
 * of type {@link #STRING}. But specifying the field type in the header row
 * makes it easier to write SQL.</p>
 */
/**
 * @author Prasad V S(EmpId:1036)
 * @version 1.0
 * @organization NanoBi Analytics
 * @Date Nov 17, 2014
 */
enum ExcelFieldType {
  STRING(String.class, "string"),
  BOOLEAN(Primitive.BOOLEAN),
  BYTE(Primitive.BYTE),
  CHAR(Primitive.CHAR),
  SHORT(Primitive.SHORT),
  INT(Primitive.INT),
  LONG(Primitive.LONG),
  FLOAT(Primitive.FLOAT),
  DOUBLE(Primitive.DOUBLE),
  DATE(java.sql.Date.class, "date"),
  TIME(java.sql.Time.class, "time"),
  TIMESTAMP(java.sql.Timestamp.class, "timestamp");

  private final Class clazz;
  private final String simpleName;

  private static final Map<String, ExcelFieldType> MAP =
    new HashMap<String, ExcelFieldType>();

  static {
    for (ExcelFieldType value : values()) {
      MAP.put(value.simpleName, value);
    }
  }

  ExcelFieldType(Primitive primitive) {
    this(primitive.boxClass, primitive.primitiveClass.getSimpleName());
  }

  ExcelFieldType(Class clazz, String simpleName) {
    this.clazz = clazz;
    this.simpleName = simpleName;
  }

  public RelDataType toType(JavaTypeFactory typeFactory) {
    return typeFactory.createJavaType(clazz);
  }

  public static ExcelFieldType of(Cell cell) {
	  if(Cell.CELL_TYPE_NUMERIC == cell.getCellType()){
		  try{
			  if (HSSFDateUtil.isCellDateFormatted(cell)){
				  return MAP.get("date");
			   }
			   try{
					if(cell.getStringCellValue().contains(".")){
						Double.parseDouble(cell.getStringCellValue());
						return MAP.get("double");
					}else{
						 return MAP.get("int");
					}
				}catch(Exception e1){
					return MAP.get("int");
				}
			  }catch(Exception e){
				  return MAP.get("int");
			  }
		  
	  }else if(Cell.CELL_TYPE_STRING == cell.getCellType()){
		  try{
		  if (HSSFDateUtil.isCellDateFormatted(cell)){
			  return MAP.get("date");
		    }
		  	return MAP.get("string");
		  }catch(Exception e){
			  return MAP.get("string");
		  }
	  }else if(Cell.CELL_TYPE_BOOLEAN == cell.getCellType()){
		  return MAP.get(Primitive.BOOLEAN);
	  }else if(Cell.CELL_TYPE_BLANK == cell.getCellType()){
		  return MAP.get("string");
	  }else{
		  return MAP.get("string");
	  }
	  //return MAP.get("int");
  }
  
	public static ExcelFieldType of(String clazz) {
		clazz = clazz.replace("JavaType(class ",""); 
		clazz = clazz.replace(")",""); 
		try {
			Class clas = Class.forName(clazz);
			for (String key : MAP.keySet()) {
				if (MAP.get(key).clazz == clas)
					return MAP.get(key);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
		// return MAP.get("int");
	}
  
  public static ExcelFieldType of(Integer typeString, String value) {
	  if(Cell.CELL_TYPE_NUMERIC == typeString){
		  return MAP.get("int");
	  }else if(Cell.CELL_TYPE_STRING == typeString){
		  try{
			  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			  Date date = format.parse(value);
			  return MAP.get("date");
		  }catch(Exception e){
			  return MAP.get("string");
		  }
	  }else if(Cell.CELL_TYPE_BOOLEAN == typeString){
		  return MAP.get(Primitive.BOOLEAN);
	  }else if(Cell.CELL_TYPE_BLANK == typeString){
		  return MAP.get("string");
	  }else{
		  return MAP.get("string");
	  }
	  //return MAP.get("int");
  }
  public static void main(String[] args) {
	System.out.println(MAP);
}
}

// End CsvFieldType.java
