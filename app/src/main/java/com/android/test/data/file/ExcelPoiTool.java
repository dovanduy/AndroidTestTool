package com.android.test.data.file;

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFRichTextString;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;

public class ExcelPoiTool {
//
//    /**
//     *
//     * @param excelStructure
//     * @param dataJsonArray
//     * @param excelFilePath
//     * @param fileName
//     */
//    public static boolean makeExcel(ArrayList<ColumnInfo> excelStructure, JSONArray dataJsonArray,
//                                    String excelFilePath,String fileName) {
//        // 检查文件
//        if (!fileName.endsWith(".xls")&&!fileName.endsWith(".xxls")) {
//            System.out.println("Excel文件名格式不正确！");
//            return false;
//        }
//        File excelFileDir = new File(excelFilePath);
//        if (!excelFileDir.exists()) {
//            excelFileDir.mkdirs();
//        }
//        File excelFile = new File(excelFileDir, fileName);
//        if (excelFile.exists()) {
//            excelFile.delete();
//        }
//        if (!excelFile.exists()) {
//            try {
//                excelFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Excel文件创建失败！");
//                return false;
//            }
//        }
//        FileOutputStream out;
//        try {
//            out = new FileOutputStream(excelFile);
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//            System.out.println("Excel输出路径不满足格式要求！");
//            return false;
//        }
//        // 创建新的Excel 工作簿
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        // 在Excel 工作簿中建一工作表
//        HSSFSheet sheet = workbook.createSheet("Sheet1");
//        // 设置单元格格式
//        HSSFCellStyle normalHeaderCellStyle = workbook.createCellStyle();
//        normalHeaderCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
//        normalHeaderCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
//        normalHeaderCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
//        normalHeaderCellStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
//        normalHeaderCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置单元格的背景颜色．
//        normalHeaderCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));// 设置为文本格式
//        HSSFCellStyle normalContentCellStyle = workbook.createCellStyle();
//        normalContentCellStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));// 设置为文本格式
//        // 设置Header
//        HSSFRow headerRow = sheet.createRow(0);
//        for (int j = 0; j < excelStructure.size(); j++) {
//            String headerValue = excelStructure.get(j).getColumnName();
//            HSSFCell nowCell = headerRow.createCell(j);
//            nowCell.setCellValue(new HSSFRichTextString(headerValue));
//            nowCell.setCellStyle(normalHeaderCellStyle);
//        }
//        // 写数据
//        for (int i = 0; i < dataJsonArray.size(); i++) {
//            JSONObject dataJson = dataJsonArray.getJSONObject(i);
//            HSSFRow nowRow = sheet.createRow(i + 1);
//            for (int j = 0; j < excelStructure.size(); j++) {
//                String columnKey = excelStructure.get(j).getColumnKey();
//                String nowValue = dataJson.getString(columnKey);
//                HSSFCell nowCell = nowRow.createCell(j);
//                nowCell.setCellValue(new HSSFRichTextString(nowValue));
//                nowCell.setCellStyle(normalContentCellStyle);
//            }
//        }
//        //写文件
//        try {
//            workbook.write(out);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    public static class ColumnInfo {
//
//        String columnName;
//        String columnKey;
//
//        public ColumnInfo(String columnName, String columnKey) {
//            super();
//            this.columnName = columnName;
//            this.columnKey = columnKey;
//        }
//
//        public String getColumnName() {
//            return columnName;
//        }
//
//        public void setColumnName(String columnName) {
//            this.columnName = columnName;
//        }
//
//        public String getColumnKey() {
//            return columnKey;
//        }
//
//        public void setColumnKey(String columnKey) {
//            this.columnKey = columnKey;
//        }
//
//    }
}
