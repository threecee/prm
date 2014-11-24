package utils;

/**
 * Created by carl c. christensen on 24/11/14.
 */
public class ExcelCell {
    public int row;
    public int column;
    public xssfDataType type;
    public Object value;
    public ExcelCell(int row, int column, Object value, xssfDataType type)
    {
        this.row = row;
        this.column = column;
        this.value = value;
        this.type = type;

    }
    public ExcelCell()
    {

    }
}
