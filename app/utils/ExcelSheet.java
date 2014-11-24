package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carl c. christensen on 24/11/14.
 */
public class ExcelSheet{
    public int index;
    public int numColumns;
    public int numRows;
    public List<ExcelCell> cells;
    ExcelCell[][] fastCells;

    public ExcelSheet()
    {
        this.cells = new ArrayList<ExcelCell>();
    }


     public void finish(){
         fastCells = new ExcelCell[numRows][numColumns];

         for(ExcelCell cell : cells)
         {
             fastCells[cell.row][cell.column] = cell;
         }

     }

    public ExcelCell getCell(int row, int column)
    {
       if(fastCells != null)
       {
           return fastCells[row][column];
       }
        else {
           for (ExcelCell cell : cells) {
               if (cell.row == row && cell.column == column) {
                   return cell;
               }
           }
       }
        return null;
    }



    public String getCellAsString(int row, int column)
    {
        String value = getCellAsStringOption(row, column);
        if(value == null)
        {
            return "";
        }

        return value;
    }

    public String getCellAsStringOption(int row, int column)
    {
        ExcelCell cell = getCell(row, column);
        if(cell == null)
        {
            return null;
        }

        return (String) cell.value;
    }


    public String getCellAsDouble(int row, int column)
    {
        String value = getCellAsStringOption(row, column);
        if(value == null)
        {
            return "";
        }

        return value;
    }

    public Double getCellAsDoubleOption(int row, int column)
    {
        ExcelCell cell = getCell(row, column);
        if(cell != null && cell.type.equals(xssfDataType.NUMBER)) {
            return (Double) cell.value;
        }
            return null;
    }



}
