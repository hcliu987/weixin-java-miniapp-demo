package com.hc.wx.mp.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;
import  org.thymeleaf.context.Context; //
import java.io.IOException;
import java.nio.file.Files;          // 文件操作类
import java.nio.file.Path;           // 表示文件路径
import java.nio.file.Paths;

@Service
public class HtmlGeneratorService {
    private final SpringTemplateEngine templateEngine;

    public HtmlGeneratorService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String generateHtml ( String data, String outputPath) throws IOException {
        Context context = new Context();
        context.setVariable("item", data);

        String htmlContent = templateEngine.process("report", context);

        // 将 HTML 内容写入文件
        Path path = Paths.get(outputPath);
        System.out.println(path.toAbsolutePath().toString());
        Files.createDirectories(path.getParent());
        Files.writeString(path, htmlContent);

        return path.toString();
    }
}
