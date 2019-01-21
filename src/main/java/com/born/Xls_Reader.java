package com.born;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xls_Reader
{
  public static String filename = "src/config/testcases/TestData.xlsx";
  public String path;
  public FileInputStream fis = null;
  public FileOutputStream fileOut = null;
  private XSSFWorkbook workbook = null;
  private XSSFSheet sheet = null;
  private XSSFRow row = null;
  private XSSFCell cell = null;
  
  public Xls_Reader(String path)
  {
    this.path = path;
    try {
      fis = new FileInputStream(path);
      workbook = new XSSFWorkbook(fis);
      sheet = workbook.getSheetAt(0);
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public int getRowCount(String sheetName)
  {
    int index = workbook.getSheetIndex(sheetName);
    if (index == -1) {
      return 0;
    }
    sheet = workbook.getSheetAt(index);
    int number = sheet.getLastRowNum() + 1;
    return number;
  }
  

  public String getCellData(String sheetName, String colName, int rowNum)
  {
    try
    {
      if (rowNum <= 0) {
        return "";
      }
      int index = workbook.getSheetIndex(sheetName);
      int col_Num = -1;
      if (index == -1) {
        return "";
      }
      sheet = workbook.getSheetAt(index);
      row = sheet.getRow(0);
      for (int i = 0; i < row.getLastCellNum(); i++)
      {
        if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
          col_Num = i;
      }
      if (col_Num == -1) {
        return "";
      }
      sheet = workbook.getSheetAt(index);
      row = sheet.getRow(rowNum - 1);
      if (row == null)
        return "";
      cell = row.getCell(col_Num);
      
      if (cell == null) {
        return "";
      }
      if (cell.getCellType() == 1)
        return cell.getStringCellValue();
      if ((cell.getCellType() == 0) || (cell.getCellType() == 2))
      {
        String cellText = String.valueOf(cell.getNumericCellValue());
        Calendar cal = null; if (HSSFDateUtil.isCellDateFormatted(cell))
        {
          double d = cell.getNumericCellValue();
          
          cal = Calendar.getInstance();
          cal.setTime(HSSFDateUtil.getJavaDate(d));
          
          cellText = String.valueOf(cal.get(1)).substring(2);
        }
        return cal.get(5) + "/" + cal.get(2) + 1 + "/" + cellText;
      }
      







      if (cell.getCellType() == 3) {
        return "";
      }
      return String.valueOf(cell.getBooleanCellValue());

    }
    catch (Exception e)
    {
      e.printStackTrace(); }
    return "row " + rowNum + " or column " + colName + " does not exist in xls";
  }
  
  public String getCellData(String sheetName, int colNum, int rowNum)
  {
    try
    {
      if (rowNum <= 0) {
        return "";
      }
      int index = workbook.getSheetIndex(sheetName);
      
      if (index == -1) {
        return "";
      }
      
      sheet = workbook.getSheetAt(index);
      row = sheet.getRow(rowNum - 1);
      if (row == null)
        return "";
      cell = row.getCell(colNum);
      if (cell == null) {
        return "";
      }
      if (cell.getCellType() == 1)
        return cell.getStringCellValue();
      if ((cell.getCellType() == 0) || (cell.getCellType() == 2))
      {
        String cellText = String.valueOf(cell.getNumericCellValue());
        Calendar cal = null; if (HSSFDateUtil.isCellDateFormatted(cell))
        {
          double d = cell.getNumericCellValue();
          
          cal = Calendar.getInstance();
          cal.setTime(HSSFDateUtil.getJavaDate(d));
          
          cellText = String.valueOf(cal.get(1)).substring(2);
        }
        return cal.get(2) + 1 + "/" + cal.get(5) + "/" + cellText;
      }
      







      if (cell.getCellType() == 3) {
        return "";
      }
      return String.valueOf(cell.getBooleanCellValue());
    }
    catch (Exception e)
    {
      e.printStackTrace(); }
    return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
  }
  
  public boolean setCellData(String sheetName, String colName, int rowNum, String data)
  {
    try
    {
      fis = new FileInputStream(path);
      workbook = new XSSFWorkbook(fis);
      
      if (rowNum <= 0) {
        return false;
      }
      int index = workbook.getSheetIndex(sheetName);
      int colNum = -1;
      if (index == -1) {
        return false;
      }
      
      sheet = workbook.getSheetAt(index);
      

      row = sheet.getRow(0);
      for (int i = 0; i < row.getLastCellNum(); i++)
      {
        if (row.getCell(i).getStringCellValue().trim().equals(colName))
          colNum = i;
      }
      if (colNum == -1) {
        return false;
      }
      sheet.autoSizeColumn(colNum);
      row = sheet.getRow(rowNum - 1);
      if (row == null) {
        row = sheet.createRow(rowNum - 1);
      }
      cell = row.getCell(colNum);
      if (cell == null) {
        cell = row.createCell(colNum);
      }
      



      cell.setCellValue(data);
      
      fileOut = new FileOutputStream(path);
      
      workbook.write(fileOut);
      
      fileOut.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  

  public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url)
  {
    try
    {
      fis = new FileInputStream(path);
      workbook = new XSSFWorkbook(fis);
      
      if (rowNum <= 0) {
        return false;
      }
      int index = workbook.getSheetIndex(sheetName);
      int colNum = -1;
      if (index == -1) {
        return false;
      }
      
      sheet = workbook.getSheetAt(index);
      
      row = sheet.getRow(0);
      for (int i = 0; i < row.getLastCellNum(); i++)
      {
        if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
          colNum = i;
        }
      }
      if (colNum == -1)
        return false;
      sheet.autoSizeColumn(colNum);
      row = sheet.getRow(rowNum - 1);
      if (row == null) {
        row = sheet.createRow(rowNum - 1);
      }
      cell = row.getCell(colNum);
      if (cell == null) {
        cell = row.createCell(colNum);
      }
      cell.setCellValue(data);
      XSSFCreationHelper createHelper = workbook.getCreationHelper();
      


      CellStyle hlink_style = workbook.createCellStyle();
      XSSFFont hlink_font = workbook.createFont();
      hlink_font.setUnderline((byte)1);
      hlink_font.setColor(IndexedColors.BLUE.getIndex());
      hlink_style.setFont(hlink_font);
      

      org.apache.poi.xssf.usermodel.XSSFHyperlink link = createHelper.createHyperlink(4);
      link.setAddress(url);
      cell.setHyperlink(link);
      cell.setCellStyle(hlink_style);
      
      fileOut = new FileOutputStream(path);
      workbook.write(fileOut);
      
      fileOut.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  



  public boolean addSheet(String sheetname)
  {
    try
    {
      workbook.createSheet(sheetname);
      FileOutputStream fileOut = new FileOutputStream(path);
      workbook.write(fileOut);
      fileOut.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false; }
    FileOutputStream fileOut;
    return true;
  }
  
  public boolean removeSheet(String sheetName)
  {
    int index = workbook.getSheetIndex(sheetName);
    if (index == -1) {
      return false;
    }
    try
    {
      workbook.removeSheetAt(index);
      FileOutputStream fileOut = new FileOutputStream(path);
      workbook.write(fileOut);
      fileOut.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false; }
    FileOutputStream fileOut;
    return true;
  }
  
  public boolean addColumn(String sheetName, String colName)
  {
    try
    {
      fis = new FileInputStream(path);
      workbook = new XSSFWorkbook(fis);
      int index = workbook.getSheetIndex(sheetName);
      if (index == -1) {
        return false;
      }
      XSSFCellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor((short)55);
      style.setFillPattern((short)1);
      
      sheet = workbook.getSheetAt(index);
      
      row = sheet.getRow(0);
      if (row == null) {
        row = sheet.createRow(0);
      }
      


      if (row.getLastCellNum() == -1) {
        cell = row.createCell(0);
      } else {
        cell = row.createCell(row.getLastCellNum());
      }
      cell.setCellValue(colName);
      cell.setCellStyle(style);
      
      fileOut = new FileOutputStream(path);
      workbook.write(fileOut);
      fileOut.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    
    return true;
  }
  
  public boolean removeColumn(String sheetName, int colNum)
  {
    try
    {
      if (!isSheetExist(sheetName))
        return false;
      fis = new FileInputStream(path);
      workbook = new XSSFWorkbook(fis);
      sheet = workbook.getSheet(sheetName);
      XSSFCellStyle style = workbook.createCellStyle();
      style.setFillForegroundColor((short)55);
      XSSFCreationHelper createHelper = workbook.getCreationHelper();
      style.setFillPattern((short)0);
      


      for (int i = 0; i < getRowCount(sheetName); i++) {
        row = sheet.getRow(i);
        if (row != null) {
          cell = row.getCell(colNum);
          if (cell != null) {
            cell.setCellStyle(style);
            row.removeCell(cell);
          }
        }
      }
      fileOut = new FileOutputStream(path);
      workbook.write(fileOut);
      fileOut.close();
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  public boolean isSheetExist(String sheetName)
  {
    int index = workbook.getSheetIndex(sheetName);
    if (index == -1) {
      index = workbook.getSheetIndex(sheetName.toUpperCase());
      if (index == -1) {
        return false;
      }
      return true;
    }
    
    return true;
  }
  

  public int getColumnCount(String sheetName)
  {
    if (!isSheetExist(sheetName)) {
      return -1;
    }
    sheet = workbook.getSheet(sheetName);
    row = sheet.getRow(0);
    
    if (row == null) {
      return -1;
    }
    return row.getLastCellNum();
  }
  




  public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url, String message)
  {
    url = url.replace('\\', '/');
    if (!isSheetExist(sheetName)) {
      return false;
    }
    sheet = workbook.getSheet(sheetName);
    
    for (int i = 2; i <= getRowCount(sheetName); i++) {
      if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName))
      {
        setCellData(sheetName, screenShotColName, i + index, message, url);
        break;
      }
    }
    

    return true;
  }
  
  public int getCellRowNum(String sheetName, String colName, String cellValue) {
    for (int i = 2; i <= getRowCount(sheetName); i++) {
      if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
        return i;
      }
    }
    return -1;
  }
  


  public static void main(String[] arg)
    throws java.io.IOException
  {
    Xls_Reader datatable = null;
    

    datatable = new Xls_Reader("src/Framework_XL_Files/Controller.xlsx");
    for (int col = 0; col < datatable.getColumnCount("TC5"); col++) {
      System.out.println(datatable.getCellData("TC5", col, 1));
    }
  }
}
