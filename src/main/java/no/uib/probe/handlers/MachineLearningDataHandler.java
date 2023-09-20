package no.uib.probe.handlers;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.type.DataType;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.data.type.StructType;

/**
 *
 * @author yfa041
 */
public class MachineLearningDataHandler implements Serializable {

    /**
     * The schema of data structure.
     */
    private static StructType schema;
    /**
     * Charset of file.
     */
    private static final Charset charset = StandardCharsets.UTF_8;
    
    public static DataFrame initDataFrame(double[][] data, String[] dataId, String[] columnIds, String[] dataClass) {
        if (schema == null) {
            // infer the schema from top 1000 rows.
            schema = inferSchema(data, columnIds);
        }
        
        return readData(data, dataId, dataClass);
    }
    
    private static StructType inferSchema(double[][] data, String[] columnIds) {

        DataType[] types = new DataType[columnIds.length];
        types[0] = DataType.infer("String");
        int lastIndex = types.length - 1;        
        types[lastIndex] = DataType.infer("String");
        int cIndex = 1;
        for (Object obj : data[0]) {
            types[cIndex++] = DataType.infer((obj + "").trim());
        }
        
        
        StructField[] fields = new StructField[types.length];
        fields[0] = new StructField(columnIds[0] , types[0] == null ? DataTypes.StringType : types[0]);
        for (int i = 1; i < fields.length-1; i++) {
            fields[i] = new StructField(columnIds[i] + "", types[i] == null ? DataTypes.StringType : types[i]);
        }        
        fields[lastIndex] = new StructField(columnIds[lastIndex] , types[lastIndex] == null ? DataTypes.StringType : types[lastIndex]);       
        return DataTypes.struct(fields);
        
    }
    
    private static DataFrame readData(double[][] data, String[] dataId, String[] dataClass) {
        if (schema == null) {
            // infer the schema from top 1000 rows.
            throw new IllegalStateException("The schema is not set or inferred.");
        }
        
        List<Tuple> rows = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            String title = dataId[i];
            String rowClass = dataClass[i];
            Object[] row = new Object[data[0].length + 2];
            row[0] = title;
            row[row.length - 1] = rowClass;
            for (int j = 1; j < row.length - 1; j++) {
                row[j] = data[i][j - 1];
            }
            rows.add(Tuple.of(row, schema));
            
        }
        schema = schema.boxed(rows);
        
        return DataFrame.of(rows, schema);
        
    }
}
