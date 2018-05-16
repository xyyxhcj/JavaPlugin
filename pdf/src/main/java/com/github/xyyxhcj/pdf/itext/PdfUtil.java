package com.github.xyyxhcj.pdf.itext;

import com.github.xyyxhcj.utils.ResponseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * PDF转换入口类,用于整合模板和数据,输出到流或文件
 *
 * @author CJ
 */
public class PdfUtil {
    /**
     * 输出到文件
     */
    public static void createPDF(String ftlPath, String templateName, String saveFileWithPath, Object data) {
        PdfGenerate pdfGenerate = getPdfGenerate();
        pdfGenerate.exportToFile(ftlPath, templateName, saveFileWithPath, data);
    }

    /**
     * 输出到流
     */
    public static void exportToPdfResponse(String ftlPath, String templateName, String fileName, Object data, HttpServletResponse response) {
        ResponseUtils.setupDownLoadResponse(response, fileName);
        PdfGenerate pdfGenerate = getPdfGenerate();
        pdfGenerate.exportToResponse(ftlPath, templateName, data, response);
    }

    /**
     * 获取PdfGenerate对象(设置了页眉页脚的生成器)
     *
     * @return PdfGenerate
     */
    private static PdfGenerate getPdfGenerate() {
        PdfGenerate pdfGenerate = new PdfGenerate();
        //设置自定义PDF页眉页脚
        pdfGenerate.setBaseHeaderFooter(new BaseHeaderFooter() {
            @Override
            public String getHeaderLeft() {
                return "左边的页眉";
            }

            @Override
            public String getHeaderRight() {
                return "右边的页眉";
            }

            /**
             * 自定义页眉页脚时可重写方法
             */
            @Override
            public void writeHeader(PdfWriter writer, Document document, Font font, PdfTemplate template) {
                super.writeHeader(writer, document, font, template);
            }
        });
        return pdfGenerate;
    }
}
