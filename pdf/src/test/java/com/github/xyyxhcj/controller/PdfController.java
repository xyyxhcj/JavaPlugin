package com.github.xyyxhcj.controller;

import com.github.xyyxhcj.pdf.itext.PdfUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * pdf生成下载测试
 *
 * @author xyyxhcj@qq.com
 * @since 2018-05-16
 */
@Controller
@RequestMapping("pdf")
public class PdfController {
    /**
     * http://localhost:8080/pdf/download
     */
    @RequestMapping("download")
    public void download(HttpServletResponse response) {
        String ftlPath = "/templates/";
        String templateName = "demo.ftl";
        Map<String, String> data = new HashMap<String, String>(1);
        data.put("content", "测试正文内容");
        //测试生成pdf
        PdfUtil.createPDF(ftlPath, templateName, "F:\\tmp\\测试文件.pdf", data);
        //测试下载
        PdfUtil.exportToPdfResponse(ftlPath, templateName, "测试文件.pdf", data, response);
    }
}
