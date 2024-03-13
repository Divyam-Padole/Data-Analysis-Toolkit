package Controller.Algorithms.Imputaion;

import Controller.Descriptor;
import tech.tablesaw.api.Table;

public class drop_row_column {


    public Table drop_row(Table data)
    {
        String[] attr=data.columnNames().toArray(new String[0]);
        int totalRows=data.rowCount();
        for(int i=0;i<totalRows;i++) {
            int count=0;
            for(int j=0;j<attr.length;j++) {
                if(data.column(attr[j]).isMissing(i)) {
                    count+=1;
                }
            }
            if(count>=(0.6*attr.length)) {
                data=data.dropRows(i);
                totalRows-=1;
            }
        }
        return  data;
    }

    public Table drop_column(Table data)
    {
        Table summary = Descriptor.get_Summary_Table();
        String[] attr= (String[]) summary.column(1).asObjectArray();
        int totalRows=data.rowCount();
        int i=0;
        for (String s : attr) {
            int count = Integer.parseInt(summary.get(i, 5).toString().split("/")[0]);
            if (count >= (0.6 * totalRows)) {
                data = data.removeColumns(s);
            }
            i++;
        }
        return  data;

    }

    public Table drop_column_manual(Table data,String[] attri)
    {
        data=data.removeColumns(attri);
        return  data;
    }








}
