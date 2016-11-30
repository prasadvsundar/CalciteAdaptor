/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nanobi.calcite.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.util.Pair;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import au.com.bytecode.opencsv.CSVReader;

import com.google.common.base.Throwables;
import com.monitorjbl.xlsx.StreamingReader;

/**
 * Enumerator that reads from a Excel file.
 *
 * @param <E>
 *            Row type
 */
/**
 * @author Prasad V S(EmpId:1036)
 * @version 1.0
 * @organization NanoBi Analytics
 * @Date Nov 17, 2016
 */
class ExcelEnumerator<E> implements Enumerator<E> {
	Iterator<Row> reader;
	private final String[] filterValues;
	private final AtomicBoolean cancelFlag;
	private final RowConverter<E> rowConverter;
	private E current;

	private static final FastDateFormat TIME_FORMAT_DATE;
	private static final FastDateFormat TIME_FORMAT_TIME;
	private static final FastDateFormat TIME_FORMAT_TIMESTAMP;

	static {
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		TIME_FORMAT_DATE = FastDateFormat.getInstance("yyyy-MM-dd", gmt);
		TIME_FORMAT_TIME = FastDateFormat.getInstance("HH:mm:ss", gmt);
		TIME_FORMAT_TIMESTAMP = FastDateFormat.getInstance(
				"yyyy-MM-dd HH:mm:ss", gmt);
	}

	public ExcelEnumerator(File file, AtomicBoolean cancelFlag,
			List<ExcelFieldType> fieldTypes) {
		this(file, cancelFlag, fieldTypes, identityList(fieldTypes.size()));
	}

	public ExcelEnumerator(File file, AtomicBoolean cancelFlag,
			List<ExcelFieldType> fieldTypes, int[] fields) {
		this(file, cancelFlag, false, null, (RowConverter<E>) converter(
				fieldTypes, fields));
	}

	public ExcelEnumerator(File file, AtomicBoolean cancelFlag, boolean stream,
			String[] filterValues, RowConverter<E> rowConverter) {
		this.cancelFlag = cancelFlag;
		this.rowConverter = rowConverter;
		this.filterValues = filterValues;

		try {
			if (stream) {
				// this.reader = new CsvStreamReader(file);
			} else {
				this.reader = openExcel(file);
			}
			this.reader.next();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static RowConverter<?> converter(List<ExcelFieldType> fieldTypes,
			int[] fields) {
		if (fields.length == 1) {
			final int field = fields[0];
			return new SingleColumnRowConverter(fieldTypes.get(field), field);
		} else {
			return new ArrayRowConverter(fieldTypes, fields);
		}
	}

	static RelDataType deduceRowType(JavaTypeFactory typeFactory, File file,
			List<ExcelFieldType> fieldTypes) {
		return deduceRowType(typeFactory, file, fieldTypes, false);
	}

	static RelDataType deduceRowType(JavaTypeFactory typeFactory, File file,
			String sheet, List<ExcelFieldType> fieldTypes) {
		return deduceRowType(typeFactory, file, sheet, fieldTypes, false);
	}

	static RelDataType deduceRowType(JavaTypeFactory typeFactory, File file,
			List<ExcelFieldType> fieldTypes, Boolean stream) {
		final List<RelDataType> types = new ArrayList<>();
		final List<String> names = new ArrayList<>();
		Iterator<Row> reader = null;
		if (stream) {
			names.add(ExcelSchemaFactory.ROWTIME_COLUMN_NAME);
			types.add(typeFactory.createSqlType(SqlTypeName.TIMESTAMP));
		}
		try {
			reader = openExcel(file);
			ExcelDataType excelDataType = new ExcelDataType(reader);
			Map<String, Object> excelFieldTypes = excelDataType
					.getColumnNameAndType(typeFactory);

			names.addAll((List<String>) excelFieldTypes.get("columnNames"));
			types.addAll((List<RelDataType>) excelFieldTypes
					.get("columnDataTyeps"));
			if (fieldTypes != null) {
				fieldTypes.addAll((List<ExcelFieldType>) excelFieldTypes
						.get("excelFieldTypes"));
			}

		} catch (IOException e) {
			// ignore
		} finally {
			if (reader != null) {

			}
		}
		if (names.isEmpty()) {
			names.add("line");
			types.add(typeFactory.createJavaType(String.class));
		}
		return typeFactory.createStructType(Pair.zip(names, types));
	}

	static RelDataType deduceRowType(JavaTypeFactory typeFactory, File file,
			String sheet, List<ExcelFieldType> fieldTypes, Boolean stream) {
		final List<RelDataType> types = new ArrayList<>();
		final List<String> names = new ArrayList<>();
		Iterator<Row> reader = null;
		if (stream) {
			names.add(ExcelSchemaFactory.ROWTIME_COLUMN_NAME);
			types.add(typeFactory.createSqlType(SqlTypeName.TIMESTAMP));
		}
		try {
			reader = openExcel(file, sheet);
			ExcelDataType excelDataType = new ExcelDataType(reader);
			Map<String, Object> excelFieldTypes = excelDataType
					.getColumnNameAndType(typeFactory);

			names.addAll((List<String>) excelFieldTypes.get("columnNames"));
			types.addAll((List<RelDataType>) excelFieldTypes
					.get("columnDataTyeps"));
			if (fieldTypes != null) {
				fieldTypes.addAll((List<ExcelFieldType>) excelFieldTypes
						.get("excelFieldTypes"));
			}
		} catch (IOException e) {
			// ignore
		} finally {
			if (reader != null) {
			}
		}
		if (names.isEmpty()) {
			names.add("line");
			types.add(typeFactory.createJavaType(String.class));
		}
		return typeFactory.createStructType(Pair.zip(names, types));
	}

	public static Iterator<Row> openExcel(File file) throws IOException {
		final Reader fileReader;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		if (file.getName().endsWith(".gz")) {
			final GZIPInputStream inputStream = new GZIPInputStream(
					new FileInputStream(file));
			fileReader = new InputStreamReader(inputStream);
		} else {
			FileInputStream fileIn = new FileInputStream(file);
			Workbook workbook = StreamingReader.builder().rowCacheSize(1000)
					.bufferSize(4096).open(fileIn);
			workbook.getNumberOfSheets();
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
		}
		return rowIterator;
	}

	public static Iterator<Row> openExcel(File file, String sheetName)
			throws IOException {
		final Reader fileReader;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		if (file.getName().endsWith(".gz")) {
			final GZIPInputStream inputStream = new GZIPInputStream(
					new FileInputStream(file));
			fileReader = new InputStreamReader(inputStream);
		} else {
			// fileReader = new FileReader(file);
			FileInputStream fileIn = new FileInputStream(file);
			Workbook workbook = StreamingReader.builder().rowCacheSize(1000)
					.bufferSize(4096).open(fileIn);
			sheet = workbook.getSheet(sheetName);
			rowIterator = sheet.iterator();
		}
		return rowIterator;
	}

	public E current() {
		return current;
	}

	public boolean moveNext() {
		try {
			outer: for (;;) {
				if (cancelFlag.get()) {
					return false;
				}
				// final String[] strings = reader.readNext();
				Row columnValues = null;
				try {
					if (reader.hasNext())
						columnValues = reader.next();
				} catch (Exception e) {
					columnValues = null;
				}

				if (columnValues == null) {
					if (reader instanceof ExcelStreamReader) {
						try {
							Thread.sleep(ExcelStreamReader.DEFAULT_MONITOR_DELAY);
						} catch (InterruptedException e) {
							throw Throwables.propagate(e);
						}
						System.out.println("Stream");
						continue;
					}
					current = null;
					return false;
				}
				/*
				 * if (filterValues != null) { for (int i = 0; i <
				 * strings.length; i++) { String filterValue = filterValues[i];
				 * if (filterValue != null) { if
				 * (!filterValue.equals(strings[i])) { continue outer; } } } }
				 */
				current = rowConverter.convertRow(columnValues);
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// return true;
	}

	public void reset() {
		throw new UnsupportedOperationException();
	}

	public void close() {
		/*
		 * try { //reader.close(); } catch (IOException e) { throw new
		 * RuntimeException("Error closing CSV reader", e); }
		 */
	}

	/** Returns an array of integers {0, ..., n - 1}. */
	static int[] identityList(int n) {
		int[] integers = new int[n];
		for (int i = 0; i < n; i++) {
			integers[i] = i;
		}
		return integers;
	}

	/** Row converter. */
	abstract static class RowConverter<E> {
		abstract E convertRow(Row rows);

		protected Object convert(ExcelFieldType fieldType, Cell cell) {
			if (fieldType == null) {
				return cell;
			}
			try{
				switch (fieldType) {
				case BOOLEAN:
					if (cell == null) {
						return null;
					}
					return cell.getBooleanCellValue();
				case BYTE:
					if (cell == null) {
						return null;
					}
					return Byte.parseByte(cell.getStringCellValue());
				case SHORT:
					if (cell == null) {
						return null;
					}
					return Short.parseShort(cell.getStringCellValue());
				case INT:
					if (cell == null) {
						return null;
					}
					return (Double.valueOf(cell.getNumericCellValue()).intValue());
				case LONG:
					if (cell == null) {
						return null;
					}
					return Long.parseLong(cell.getStringCellValue());
				case FLOAT:
					if (cell == null) {
						return null;
					}
					return Float.parseFloat(cell.getStringCellValue());
				case DOUBLE:
					if (cell == null) {
						return null;
					}
					return cell.getNumericCellValue();
				case DATE:
					if (cell == null) {
						return null;
					}
					try {
						Date date = cell.getDateCellValue();
						return new java.sql.Date(date.getTime());
					} catch (Exception e) {
						return null;
					}
				case TIME:
					if (cell == null) {
						return null;
					}
					try {
						Date date = TIME_FORMAT_TIME.parse(cell
								.getStringCellValue());
						return new java.sql.Time(date.getTime());
					} catch (ParseException e) {
						return null;
					}
				case TIMESTAMP:
					if (cell == null) {
						return null;
					}
					try {
						Date date = TIME_FORMAT_TIMESTAMP.parse(cell
								.getStringCellValue());
						return new java.sql.Timestamp(date.getTime());
					} catch (ParseException e) {
						return null;
					}
				case STRING:
				default:
					return cell.getStringCellValue();
				}
			}catch(Exception e){
				return cell.getStringCellValue();
			}
			
		}
	}

	/** Array row converter. */
	static class ArrayRowConverter extends RowConverter<Object[]> {
		private final ExcelFieldType[] fieldTypes;
		private final int[] fields;
		private final boolean stream;

		ArrayRowConverter(List<ExcelFieldType> fieldTypes, int[] fields) {
			this.fieldTypes = fieldTypes.toArray(new ExcelFieldType[fieldTypes
					.size()]);
			this.fields = fields;
			this.stream = false;
		}

		ArrayRowConverter(List<ExcelFieldType> fieldTypes, int[] fields,
				boolean stream) {
			this.fieldTypes = fieldTypes.toArray(new ExcelFieldType[fieldTypes
					.size()]);
			this.fields = fields;
			this.stream = stream;
		}

		public Object[] convertRow(Row row) {
			if (stream) {
				return convertStreamRow(row);
			} else {
				return convertNormalRow(row);
			}
		}

		public Object[] convertNormalRow(Row row) {
			Iterator<Cell> cells = row.cellIterator();

			final Object[] objects = new Object[fields.length];
			while (cells.hasNext()) {
				Cell cell = cells.next();

				int field = fields[cell.getColumnIndex()];
				objects[field] = convert(fieldTypes[field], cell);

			}

			return objects;
		}

		public Object[] convertStreamRow(Row row) {
			final Object[] objects = new Object[fields.length + 1];
			objects[0] = System.currentTimeMillis();
			for (int i = 0; i < fields.length; i++) {
				int field = fields[i];
			}
			return objects;
		}
	}

	/** Single column row converter. */
	private static class SingleColumnRowConverter extends RowConverter {
		private final ExcelFieldType fieldType;
		private final int fieldIndex;

		private SingleColumnRowConverter(ExcelFieldType fieldType,
				int fieldIndex) {
			this.fieldType = fieldType;
			this.fieldIndex = fieldIndex;
		}

		public Object convertRow(Row row) {
			return convert(fieldType, row.getCell(fieldIndex));
		}

	}
}
