package com.github.xyyxhcj.pdf.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Pdf生成器,可设置页面附加属性(页眉,页脚,字体,字体大小)
 *
 * @author CJ
 */
public class PdfBuilder extends PdfPageEventHelper {

    private static Logger log = LoggerFactory.getLogger(PdfBuilder.class);

    /**
     * 字体文件名,pdf页眉页脚使用的字体
     */
    private String fontFileName;
    /**
     * 基础字体对象
     */
    private BaseFont baseFont;
    /**
     * 利用基础字体生成的字体对象，一般用于生成中文文字
     */
    private Font fontDetail;
    /**
     * 默认文档字体大小
     */
    private int fontSize = 12;
    /**
     * 模板
     */
    private PdfTemplate template;
    /**
     * 页眉页脚定制接口
     */
    private BaseHeaderFooter baseHeaderFooter;

    /**
     * 不允许使用空构造
     */
    private PdfBuilder() {

    }

    public PdfBuilder(BaseHeaderFooter baseHeaderFooter) {
        //提供默认字体
        this(baseHeaderFooter, "ping_fang_light.ttf");
    }

    public PdfBuilder(BaseHeaderFooter baseHeaderFooter, String fontFileName) {
        this(baseHeaderFooter, fontFileName, 12);
    }

    public PdfBuilder(BaseHeaderFooter baseHeaderFooter, String fontFileName, int fontSize) {
        this.baseHeaderFooter = baseHeaderFooter;
        this.fontFileName = fontFileName;
        this.fontSize = fontSize;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        template = writer.getDirectContent().createTemplate(50, 50);
    }

    /**
     * 关闭每页的时候，写入页眉,页脚等
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        this.addPage(writer, document);
    }

    //加分页
    private void addPage(PdfWriter writer, Document document) {
        if (baseHeaderFooter != null) {
            //1.初始化字体
            initFront();
            //2.写入页眉
            baseHeaderFooter.writeHeader(writer, document, fontDetail, template);
            //3.写入前半部分页脚
            baseHeaderFooter.writeFooter(writer, document, fontDetail, template);
        }
    }

    /**
     * 关闭文档时，替换模板，完成整个页眉页脚组件
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        if (baseHeaderFooter != null) {
            template.beginText();
            template.setFontAndSize(baseFont, fontSize);
            String replace = baseHeaderFooter.getReplaceOfTemplate(writer);
            template.showText(replace);
            template.endText();
            template.closePath();
        }
    }

    /**
     * @description 初始化字体
     */
    private void initFront() {
        if (StringUtils.isEmpty(fontFileName)) {
            throw new RuntimeException("PDF文档字体未设置!");
        }
        try {
            if (baseFont == null) {
                //添加字体，以支持中文
                String fontPath = PdfBuilder.class.getClassLoader().getResource("fonts").getPath() + "/" + fontFileName;
                //创建基础字体
                baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            }
            if (fontDetail == null) {
                // 数据体字体
                fontDetail = new Font(baseFont, fontSize, Font.NORMAL);
                log.info("PDF文档字体初始化完成!");
            }
        } catch (Exception e) {
            log.error("字体初始化失败", e);
            throw new RuntimeException("字体初始化失败", e);
        }
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFileName() {
        return fontFileName;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }
}