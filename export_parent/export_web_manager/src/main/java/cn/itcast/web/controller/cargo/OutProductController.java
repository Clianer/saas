package cn.itcast.web.controller.cargo;

import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.lang.invoke.SwitchPoint;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author han
 * @Date 2020/3/20 17:39
 * @Version 1.0
 **/
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {
    @Reference
    private ContractProductService contractProductService;

    //1. 进入出货表打印页面
    @RequestMapping("/print")
    public String print() {
        return "cargo/print/contract-print";
    }

    /**
     * 普通样式
     * @param inputDate
     * @throws Exception
     */
/*    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //第一步，导出第一行
        //创建一个工作本
        Workbook workbook = new XSSFWorkbook();
        //创建一个表
        Sheet sheet = workbook.createSheet();
        //设置列宽
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 26 * 256);
        sheet.setColumnWidth(2, 12 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8));
        //创建第一行
        Row row = sheet.createRow(0);
        //设置行高
        row.setHeightInPoints(36);
        //创建第一行第二列
        Cell cell = row.createCell(1);
        String title = inputDate.replace("-0", "-").replace("-", "年") + "月份出货表";
        cell.setCellValue(title);
        cell.setCellStyle(this.bigTitle(workbook));
        //第二步，导出第二行
        String titles[] = new String[]{"客户", "订单号", "货号", "数量", "工厂",
                "工厂交期", "船期", "贸易条款"};
        //创建第二行
        row = sheet.createRow(1);
        row.setHeightInPoints(27);
        for (int i = 0; i < titles.length; i++) {
            //创建第二行的每一列
            cell = row.createCell(i + 1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.title(workbook));
        }
        //第三部，到处第三行
        //查询数据库得到所有合同
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list != null && list.size() > 0) {
            //遍历里面所有的信息
            for (ContractProductVo cp : list) {
                //创建第三行
                int index = 2;
                row = sheet.createRow(index++);
                row.setHeightInPoints(24);
                //设置第三行第一列
                cell = row.createCell(1);
                cell.setCellValue(cp.getCustomName());
                cell.setCellStyle(this.text(workbook));
                //设置第三行第二列
                cell = row.createCell(2);
                cell.setCellValue(cp.getContractNo());
                cell.setCellStyle(this.text(workbook));
                //设置第三行第三列
                cell = row.createCell(3);
                cell.setCellValue(cp.getProductNo());
                cell.setCellStyle(this.text(workbook));
                //设置第三行第四列
                cell = row.createCell(4);
                if (cp.getCnumber() != null) {
                    cell.setCellValue(cp.getCnumber());
                }
                cell.setCellStyle(this.text(workbook));
                //设置第三行第五列
                cell = row.createCell(5);
                cell.setCellValue(cp.getFactoryName());
                cell.setCellStyle(this.text(workbook));
                //设置第三行第六列
                cell = row.createCell(6);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cp.getDeliveryPeriod()));
                cell.setCellStyle(this.text(workbook));
                //设置第三行第七列
                cell = row.createCell(7);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cp.getShipTime()));
                cell.setCellStyle(this.text(workbook));
                //设置第三行第八列
                cell = row.createCell(8);
                cell.setCellValue(cp.getTradeTerms());
                cell.setCellStyle(this.text(workbook));
            }
        }


        //下载
        //字符编码
        response.setCharacterEncoding("UTF-8");
        //下载头
        response.setHeader("content-disposition", "attachment;fileName=exprot.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        //把文件流下到响应输出流
        workbook.write(outputStream);
        //关闭资源
        outputStream.close();
        workbook.close();
    }*/

    /**
     * 模版样式
     * @return
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {
        //第一步，导出第一行
        //创建输入流
        InputStream in =
                session.getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");
        //创建一个工作本
        Workbook workbook = new XSSFWorkbook(in);
        //获得一个表
        Sheet sheet = workbook.getSheetAt(0);
        //获得第一行
        Row row = sheet.getRow(0);
        //获得第一行第二列
        Cell cell = row.getCell(1);
        String title = inputDate.replace("-0", "-").replace("-", "年") + "月份出货表";
        cell.setCellValue(title);
        //第二步，导出第二行
        row = sheet.getRow(2);
        CellStyle[] cellStyles = new CellStyle[8];
        for (int i = 0; i < cellStyles.length; i++) {
            cellStyles[i] = row.getCell(i+1).getCellStyle();
        }
        //第三部，到处第三行
        //查询数据库得到所有合同
        List<ContractProductVo> list = contractProductService.findByShipTime(getLoginCompanyId(), inputDate);
        if (list != null && list.size() > 0) {
            //遍历里面所有的信息
            for (ContractProductVo cp : list) {
                //创建第三行
                int index = 2;
                row = sheet.createRow(index++);
                row.setHeightInPoints(24);
                //设置第三行第一列
                cell = row.createCell(1);
                cell.setCellValue(cp.getCustomName());
                cell.setCellStyle(cellStyles[0]);
                //设置第三行第二列
                cell = row.createCell(2);
                cell.setCellValue(cp.getContractNo());
                cell.setCellStyle(cellStyles[1]);
                //设置第三行第三列
                cell = row.createCell(3);
                cell.setCellValue(cp.getProductNo());
                cell.setCellStyle(cellStyles[2]);
                //设置第三行第四列
                cell = row.createCell(4);
                if (cp.getCnumber() != null) {
                    cell.setCellValue(cp.getCnumber());
                }
                cell.setCellStyle(cellStyles[3]);
                //设置第三行第五列
                cell = row.createCell(5);
                cell.setCellValue(cp.getFactoryName());
                cell.setCellStyle(cellStyles[4]);
                //设置第三行第六列
                cell = row.createCell(6);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cp.getDeliveryPeriod()));
                cell.setCellStyle(cellStyles[5]);
                //设置第三行第七列
                cell = row.createCell(7);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(cp.getShipTime()));
                cell.setCellStyle(cellStyles[6]);
                //设置第三行第八列
                cell = row.createCell(8);
                cell.setCellValue(cp.getTradeTerms());
                cell.setCellStyle(cellStyles[7]);
            }
        }
        //下载
        //字符编码
        response.setCharacterEncoding("UTF-8");
        //下载头
        response.setHeader("content-disposition", "attachment;fileName=exprot.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        //把文件流下到响应输出流
        workbook.write(outputStream);
        //关闭资源
        outputStream.close();
        workbook.close();
    }




    //大标题的样式
    public CellStyle bigTitle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);                //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);                //横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);        //纵向居中
        style.setBorderTop(BorderStyle.THIN);                        //上细线
        style.setBorderBottom(BorderStyle.THIN);                    //下细线
        style.setBorderLeft(BorderStyle.THIN);                        //左细线
        style.setBorderRight(BorderStyle.THIN);                        //右细线
        return style;
    }
}
