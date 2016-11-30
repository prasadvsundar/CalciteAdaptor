
package com.nanobi.calcite.excel;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.impl.AbstractTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for table that reads Excel files.
 */
/**
 * @author Prasad V S(EmpId:1036)
 * @version 1.0
 * @organization NanoBi Analytics
 * @Date Nov 17, 2014
 */
public abstract class ExcelTable extends AbstractTable {
  protected final File file;
  protected final RelProtoDataType protoRowType;
  protected List<ExcelFieldType> fieldTypes;
  protected final String sheet;

  /** Creates a ExcelAbstractTable. */
  ExcelTable(File file, RelProtoDataType protoRowType) {
    this.file = file;
    this.protoRowType = protoRowType;
    this.sheet=null;
  }
  
  ExcelTable(File file, RelProtoDataType protoRowType, String sheet) {
	    this.file = file;
	    this.protoRowType = protoRowType;
	    this.sheet = sheet;
	  }

  public RelDataType getRowType(RelDataTypeFactory typeFactory) {
    if (protoRowType != null) {
      return protoRowType.apply(typeFactory);
    }
    
    if(sheet==null){
    	if (fieldTypes == null) {
    	      fieldTypes = new ArrayList<ExcelFieldType>();
    	      return ExcelEnumerator.deduceRowType((JavaTypeFactory) typeFactory, file,
    	          fieldTypes);
    	    } else {
    	      return ExcelEnumerator.deduceRowType((JavaTypeFactory) typeFactory,
    	          file,
    	          null);
    	    }
    }else{
    	if (fieldTypes == null) {
    	      fieldTypes = new ArrayList<ExcelFieldType>();
    	      return ExcelEnumerator.deduceRowType((JavaTypeFactory) typeFactory, file, sheet,fieldTypes);
    	    } else {
    	      return ExcelEnumerator.deduceRowType((JavaTypeFactory) typeFactory,file,sheet,null);
    	    }
    }
    
  }

  /** Various degrees of table "intelligence". */
  public enum Flavor {
    SCANNABLE, FILTERABLE, TRANSLATABLE
  }
}

