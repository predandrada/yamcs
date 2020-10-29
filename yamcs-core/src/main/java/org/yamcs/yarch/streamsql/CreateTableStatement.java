package org.yamcs.yarch.streamsql;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.yamcs.yarch.PartitioningSpec;
import org.yamcs.yarch.TableDefinition;
import org.yamcs.yarch.Tuple;
import org.yamcs.yarch.TupleDefinition;
import org.yamcs.yarch.YarchDatabase;
import org.yamcs.yarch.YarchDatabaseInstance;
import org.yamcs.yarch.YarchException;

public class CreateTableStatement  extends SimpleStreamSqlStatement  {

    boolean ifNotExists;
    String tableName;
    TupleDefinition tupleDefinition;
    ArrayList<String> primaryKey;
    ArrayList<String> histoColumns;
    PartitioningSpec partitioningSpec;
    String tablespace;
    String engine;

    private boolean compressed = false;

    public CreateTableStatement(boolean ifNotExists, String tableName, TupleDefinition tupleDefinition,
            ArrayList<String> primaryKey) {
        this.ifNotExists = ifNotExists;
        this.tableName = tableName;
        this.tupleDefinition = tupleDefinition;
        this.primaryKey = primaryKey;
    }

    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;

    }

    public void setPartitioning(PartitioningSpec pspec) {
        this.partitioningSpec = pspec;
    }

    public void setCompressed(boolean c) {
        this.compressed = c;
    }

    public void addHistogramColumn(String columnName) {
        if (histoColumns == null) {
            histoColumns = new ArrayList<>();
        }
        histoColumns.add(columnName);
    }

    @Override
    protected void execute(ExecutionContext context, Consumer<Tuple> consumer) throws StreamSqlException {
        YarchDatabaseInstance ydb = context.getDb();
        synchronized (ydb) {
            TableDefinition tableDefinition = new TableDefinition(tableName, tupleDefinition, primaryKey);
            tableDefinition.validate();

            if (engine != null) {
                tableDefinition.setStorageEngineName(engine);
            } else {
                tableDefinition.setStorageEngineName(YarchDatabase.getDefaultStorageEngineName());
            }

            tableDefinition.setCompressed(compressed);
            if (partitioningSpec != null) {
                tableDefinition.setPartitioningSpec(partitioningSpec);
            } else {
                tableDefinition.setPartitioningSpec(PartitioningSpec.noneSpec());
            }
            if (histoColumns != null) {
                tableDefinition.setHistogramColumns(histoColumns);
            }

            try {
                if (!ifNotExists || ydb.getTable(tableName) == null) {
                    ydb.createTable(tableDefinition);
                }
            } catch (YarchException e) {
                throw new GenericStreamSqlException("Cannot create table: " + e.getMessage());
            }
        }
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

}
