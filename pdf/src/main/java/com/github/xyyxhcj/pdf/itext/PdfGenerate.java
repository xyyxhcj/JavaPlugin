package com.github.xyyxhcj.pdf.itext;

import com.github.xyyxhcj.utils.FreemarkerUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Pdf生成
 *
 * @author CJ
 */
public class PdfGenerate {

    /**
     * PDF页眉,页脚定制类
     */
    private BaseHeaderFooter baseHeaderFooter;

    /**
     * 导出pdf到文件
     *
     * @param ftlPath          ftl模板文件夹在classes下的相对路径(不含文件名)
     * @param templateName     ftl模板文件名
     * @param saveFileWithPath 生成的pdf保存文件名(包含路径)
     * @param data             输入到FTL中的数据
     */
    public void exportToFile(String ftlPath, String templateName, String saveFileWithPath, Object data) {
        String htmlStr = FreemarkerUtils.generate(ftlPath, templateName, data);
        File file = new File(saveFileWithPath);
        file.getParentFile().mkdirs();
        FileOutputStream outputStream = null;
        try {
            //设置输出路径
            outputStream = new FileOutputStream(saveFileWithPath);
            //设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            //设置页眉页脚
            PdfBuilder builder = new PdfBuilder(baseHeaderFooter);
            writer.setPageEvent(builder);
            //输出为PDF文件
            convertToPDF(writer, document, htmlStr);
        } catch (Exception ex) {
            throw new RuntimeException("导出PDF文件失败", ex);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }


    /**
     * 生成PDF到输出流中(用于下载PDF)
     *
     * @param ftlPath      ftl模板文件夹在classes下的相对路径(不含文件名)
     * @param templateName ftl模板文件名
     * @param data         输入到FTL中的数据
     * @param response     HttpServletResponse
     */
    public void exportToResponse(String ftlPath, String templateName, Object data, HttpServletResponse response) {
        String htmlStr = FreemarkerUtils.generate(ftlPath, templateName, data);
        try {
            OutputStream out = null;
            out = response.getOutputStream();
            //设置文档大小
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            //设置页眉页脚
            PdfBuilder builder = new PdfBuilder(baseHeaderFooter);
            writer.setPageEvent(builder);
            //输出为PDF文件
            convertToPDF(writer, document, htmlStr);
        } catch (Exception e) {
            throw new RuntimeException("PDF导出流失败", e);
        }

    }

    /**
     * @description PDF文件生成
     */
    private void convertToPDF(PdfWriter writer, Document document, String htmlString) {
        //获取字体设置路径
        String fontPath = PdfGenerate.class.getClassLoader().getResource("fonts").getPath();
        document.open();
        try {
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlString.getBytes()), XMLWorkerHelper.class.getResourceAsStream("/default.css"), Charset.forName("UTF-8"), new XMLWorkerFontProvider(fontPath));
        } catch (IOException e) {
            throw new RuntimeException("PDF文件生成异常", e);
        } finally {
            document.close();
        }

    }

    public BaseHeaderFooter getBaseHeaderFooter() {
        return baseHeaderFooter;
    }

    public void setBaseHeaderFooter(BaseHeaderFooter baseHeaderFooter) {
        this.baseHeaderFooter = baseHeaderFooter;
    }
}