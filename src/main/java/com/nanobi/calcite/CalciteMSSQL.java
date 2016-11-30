package com.nanobi.calcite;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.linq4j.tree.Types;
import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.RelTraitDef;
import org.apache.calcite.rel.RelCollationTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.TableFunction;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.apache.calcite.schema.impl.TableFunctionImpl;
import org.apache.calcite.sql.SqlExplainLevel;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.RelBuilder;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.RuleSets;
import org.apache.calcite.tools.ValidationException;
import org.apache.commons.dbcp.BasicDataSource;

import com.nanobi.calcite.csv.CsvSchema;
import com.nanobi.calcite.csv.CsvTable.Flavor;

public class CalciteMSSQL {
	public static final Method MAZE_METHOD = 
		      Types.lookupMethod(MazeTable.class, "generate", int.class, int.class, 
		          int.class); 
		  public static final Method SOLVE_METHOD = 
		      Types.lookupMethod(MazeTable.class, "solve", int.class, int.class, 
		          int.class); 
    public static void main(String[] args) throws ClassNotFoundException, SQLException, ValidationException, RelConversionException {

        BasicDataSource dataSource = new BasicDataSource();
        
        /* SQL Server */
       /* dataSource.setUrl("jdbc:sqlserver://172.16.0.201:1433");
        dataSource.setUsername("TRUNKDBADMIN");
        dataSource.setPassword("TrunkDBAdmin$123");
        dataSource.setDefaultCatalog("TRUNKDB");
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");*/
        
        /* Postgres Server */
        /*dataSource.setUrl("jdbc:postgresql://172.16.0.202:5432/trunkdb");
        dataSource.setUsername("postgres");
        dataSource.setPassword("n@n0b1");
        dataSource.setDefaultCatalog("trunkdb");
        dataSource.setDriverClassName("org.postgresql.Driver");*/
        
        /* Mysql Server */
        dataSource.setUrl("jdbc:mysql://localhost:3306");
        dataSource.setUsername("root");
        dataSource.setPassword("Welcome1");
        dataSource.setDefaultCatalog("test_db");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        
       // System.out.println(calciteConnection.getRootSchema());
        
       
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection =
                DriverManager.getConnection("jdbc:calcite:");
        CalciteConnection calciteConnection =
                connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema(); 
        
        /*JDBC Schema*/
        JdbcSchema jdbcSchema = JdbcSchema.create(rootSchema,  dataSource.getDefaultCatalog(), dataSource,  null, null);       
        rootSchema.add(dataSource.getDefaultCatalog(), jdbcSchema);
        
        
      /*  JdbcSchema jdbcSchema1 = JdbcSchema.create(rootSchema,  dataSource.getDefaultCatalog(), dataSource,  null, "bvJgAMqRJTbpySX");       
        rootSchema.add("p1.bvJgAMqRJTbpySX", jdbcSchema1);
        
        JdbcSchema jdbcSchema2 = JdbcSchema.create(rootSchema,  dataSource.getDefaultCatalog(), dataSource,  null, "bvJgAMqRJTbpySX");       
        rootSchema.add("p2.bvJgAMqRJTbpySX", jdbcSchema2);*/
        
        /*CSV Schema*/
       // File csvDir = new File("/home/nanobi/Drill/CSV/");
       // SchemaPlus schema = rootSchema.add("s", new CsvSchema(csvDir,null)); 
       // rootSchema.add("CSV", new CsvSchema(csvDir, Flavor.SCANNABLE));
       // rootSchema.add("CSV", new CsvSchema(csvDir, Flavor.TRANSLATABLE));
        

     // Create the query planner with sales schema. conneciton.getSchema returns default schema name specified in sales.json
      //  SimpleQueryPlanner queryPlanner = new SimpleQueryPlanner(rootSchema.getSubSchema("CSV"));
       // RelNode loginalPlan = queryPlanner.getLogicalPlan("select * from nbmdc_nanomarts WHERE row_id = '00009e35-35bc-4486-a5ca-19ab0aae3438'");
        //RelNode loginalPlan = queryPlanner.getLogicalPlan("INSERT into CSV.p(Name) select name from TRUNKDB.nbmdc_nanomarts WHERE row_id = '00009e35-35bc-4486-a5ca-19ab0aae3438'");
       // RelNode loginalPlan = queryPlanner.getLogicalPlan("insert into TRUNKDB.table3(name) select b.BRANCH_CODE from CSV.branchmerge b");
       // System.out.println(RelOptUtil.toString(loginalPlan));
       // System.out.println(RelOptUtil.toString(loginalPlan, SqlExplainLevel.ALL_ATTRIBUTES));
        
                
        Statement statement = connection.createStatement();
       // ResultSet resultSet =   statement.executeQuery("select nm.*, b.BRANCH_CODE from \"TRUNKDB\".\"nbmdc_nanomarts\" nm, \"CSV\".\"branchmerge\" b WHERE \"row_id\" = '00009e35-35bc-4486-a5ca-19ab0aae3438'");
        
        ResultSet resultSet =   statement.executeQuery("select * from \"test_db\".\"delear_details\"");
        
       /* SimpleQueryPlanner queryPlanner = new SimpleQueryPlanner(rootSchema);
        RelNode loginalPlan = queryPlanner.getLogicalPlan("select * from TRUNKDB.nbmdc_nanomarts");
        System.out.println(RelOptUtil.toString(loginalPlan));*/
        
      //  ResultSet resultSet =   statement.executeQuery("select b.BRANCH_CODE, t.* from \"CSV\".\"branchmerge\" b, \"TRUNKDB\".\"nbmdc_nanomarts\" t");
       /* 
        final FrameworkConfig config = ;
        final RelBuilder builder = RelBuilder.create(config);
        final RelNode node = builder
        		  .scan("CSV")
        		  .project(builder.field("DEPTNO"), builder.field("ENAME"))
        		  .build();
        		System.out.println(RelOptUtil.toString(node));*/
        
       // int resultSet =   statement.executeUpdate(RelOptUtil.);
        //int resultSet =   statement.executeUpdate("insert into \"TRUNKDB\".\"table3\"(\"name\") select b.BRANCH_CODE from \"CSV\".\"branchmerge\" b");
       // int resultSet =   statement.executeUpdate("insert into \"TRUNKDB\".\"table3\"(\"name\") select \"name\" from \"TRUNKDB\".\"nbmdc_nanomarts\" WHERE \"row_id\" = '00009e35-35bc-4486-a5ca-19ab0aae3438'");
        
       /* List<RelTraitDef> traitDefs = new ArrayList<RelTraitDef>();
        traitDefs.add(ConventionTraitDef.INSTANCE);
        traitDefs.add(RelCollationTraitDef.INSTANCE);
        FrameworkConfig calciteFrameworkConfig = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.configBuilder()
                    // Lexical configuration defines how identifiers are quoted, whether they are converted to upper or lower
                    // case when they are read, and whether identifiers are matched case-sensitively.
                    .setLex(Lex.SQL_SERVER)
                    .build())
                // Sets the schema to use by the planner
                .defaultSchema(schema)
                .traitDefs(traitDefs)
                // Context provides a way to store data within the planner session that can be accessed in planner rules.
                .context(Contexts.EMPTY_CONTEXT)
                // Rule sets to use in transformation phases. Each transformation phase can use a different set of rules.
                .ruleSets(RuleSets.ofList())
                // Custom cost factory to use during optimization
                .costFactory(null)
                .typeSystem(RelDataTypeSystem.DEFAULT)
                .build();
        RelOptPlanner planner;
        RelOptPlanner.Executor executor= calciteFrameworkConfig.getExecutor();
        planner = loginalPlan.getCluster().getPlanner();
        planner.setExecutor(executor);*/
        
       // SqlInsert s = new SqlInsert(pos, keywords, targetTable, source, columnList)
      /*  public SqlInsert(SqlParserPos pos,
        	      SqlNodeList keywords,
        	      SqlNode targetTable,
        	      SqlNode source,
        	      SqlNodeList columnList) {*/
        
      //  calciteConnection.
        
        
       /* boolean resultSet =
                statement.execute("insert into \"TRUNKDB\".\"table1\" ('a', 'aaaa') select '1','2' \"TRUNKDB\".\"table1\"");
        final StringBuilder buf = new StringBuilder();
        
        
        
        final String sql = "insert into \"TRUNKDB\".\"table2\" values (1)";
     
        
        String p = jdbcSchema.getDataSource().getConnection().getSchema();
        	System.out.println(p);	
        	/*	prepareStatement(sql);
            try (PreparedStatement p = connection.prepareStatement(sql)) {
              p.setString(1, "foo");
              p.setString(2, "foo");
              final int count = p.executeUpdate();
            }*/
        
       final StringBuilder buf = new StringBuilder();
/*       while (resultSet.next()) {
            int n = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= n; i++) {
                buf.append(i > 1 ? "; " : "")
                        .append(resultSet.getMetaData().getColumnLabel(i))
                        .append("=")
                        .append(resultSet.getObject(i));
            }
            System.out.println(buf.toString());
            buf.setLength(0);
        }
*/      
       while (resultSet.next()) {
	  	 int n = resultSet.getMetaData().getColumnCount();
	  	 for (int i = 1; i <= n; i++) {
	  		 System.out.println(resultSet.getMetaData().getColumnName(i)+" "+resultSet.getMetaData().getColumnTypeName(i));
	  	 }
         
          //System.out.println(resultSet.getObject("date"));
         // Date d =(Date) resultSet.getObject("date");
          //System.out.println(d);
      }
       resultSet.close();
      //  statement.close();
     //   connection.close();
    }
}