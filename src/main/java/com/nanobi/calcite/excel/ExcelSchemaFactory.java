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

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.util.Map;

/**
 * Factory that creates a {@link ExcelSchema}.
 *
 * <p>Allows a custom schema to be included in a <code><i>model</i>.json</code>
 * file.
 */
@SuppressWarnings("UnusedDeclaration")
public class ExcelSchemaFactory implements SchemaFactory {
  /** Name of the column that is implicitly created in a CSV stream table
   * to hold the data arrival time. */
  static final String ROWTIME_COLUMN_NAME = "ROWTIME";

  /** Public singleton, per factory contract. */
  public static final ExcelSchemaFactory INSTANCE = new ExcelSchemaFactory();

  private ExcelSchemaFactory() {
  }

  public Schema create(SchemaPlus parentSchema, String name,
      Map<String, Object> operand) {
    final String directory = (String) operand.get("directory");
    final File base =
        (File) operand.get(ModelHandler.ExtraOperand.BASE_DIRECTORY.camelName);
    File directoryFile = new File(directory);
    if (base != null && !directoryFile.isAbsolute()) {
      directoryFile = new File(base, directory);
    }
    String flavorName = (String) operand.get("flavor");
    ExcelTable.Flavor flavor;
    if (flavorName == null) {
      flavor = ExcelTable.Flavor.SCANNABLE;
    } else {
      flavor = ExcelTable.Flavor.valueOf(flavorName.toUpperCase());
    }
    return new ExcelSchema(directoryFile, flavor);
  }
}

// End CsvSchemaFactory.java
