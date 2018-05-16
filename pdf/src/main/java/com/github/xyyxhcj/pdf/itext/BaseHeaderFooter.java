package com.github.xyyxhcj.pdf.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * 模板类,通过继承该类配置页眉页脚
 *
 * @author CJ
 */
public abstract class BaseHeaderFooter {
    /**
     * 写入页眉
     *
     * @param writer   PDF编写类
     * @param document PDF文档对象
     * @param font     字体设置
     * @param template PDF模板
     * @description PDF页脚设置类
     */
    public void writeFooter(PdfWriter writer, Document document, Font font, PdfTemplate template) {
        //写入页码
        int pageS = writer.getPageNumber();
        int currentPage = pageS - 1;
        if (currentPage < 0) {
            return;
        }
        PdfContentByte pdfContentByte = writer.getDirectContent();
        ColumnText.showTextAligned(pdfContentByte, Element.ALIGN_CENTER, new Phrase("第" + pageS + "页", font), (document.left() + 250), document.bottom() - 10, 0);
        //ColumnText.showTextAligned(pdfContentByte, Element.ALIGN_RIGHT, null, (document.right() - 30), document.bottom() - 20, 0);
        // 可设置模板位置
        //pdfContentByte.addTemplate(template, document.left() +250, document.bottom()-30);
    }

    /**
     * 写入页脚
     *
     * @param writer   PDF编写类
     * @param document PDF文档对象
     * @param font     字体设置
     * @param template PDF模板
     */
    public void writeHeader(PdfWriter writer, Document document, Font font, PdfTemplate template) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(getHeaderLeft(), font),
                document.left(), document.top() + 10, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(getHeaderRight(), font),
                document.right(), document.top() + 10, 0);
    }

    /**
     * 关闭文档前，获取设置的页眉页脚模板文本
     *
     * @param writer PDF编写类
     */
    public String getReplaceOfTemplate(PdfWriter writer) {
        int total = writer.getPageNumber() - 1;
        return total + "";
    }

    public abstract String getHeaderLeft();

    public abstract String getHeaderRight();
}
