package com.bixuebihui.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class BaseExcelOp {

    protected Workbook workbook = null;
    protected Sheet curSheet = null;

    public BaseExcelOp() {
        super();
    }

    public Sheet getNewSheet(String sheetName) {
        curSheet = workbook.createSheet(sheetName);
        return curSheet;
    }

    public Row getNewRow(int row) {
        return curSheet.createRow((short) row);
    }

    public Cell createStringCell(Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        return cell;
    }

    public Cell createNumericalCell(Row row, int col, int value) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        return cell;
    }

    public Cell createNumericalCell(Row row, int col, double value) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
        return cell;
    }

}
