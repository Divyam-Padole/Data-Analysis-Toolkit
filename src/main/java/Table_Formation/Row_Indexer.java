package Table_Formation;

import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

public class Row_Indexer {
    public Table indexer(Table dataframe){
        IntColumn column1 = IntColumn.indexColumn("Index",dataframe.rowCount(),0);
        dataframe.insertColumn(0,column1);
        return  dataframe;
    }
}
