package com.hc.wx.mp.controller;

import com.hc.wx.mp.service.HtmlGeneratorService;
import com.hc.wx.mp.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    SearchService searchService;
    @Autowired
    HtmlGeneratorService htmlGeneratorService;

    public static void main(String[] args) throws Exception {
        SearchService searchService=new SearchService();
        System.out.println(searchService.resultMsg("驻站"));
    }
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateReport(@RequestBody Map<String, Object> conditions) {
        String text = null;
        try {
            text = searchService.resultMsg("驻站");
            System.out.println(text);
            String fileName = "report_" + System.currentTimeMillis() + ".html";
            String outputPath = "output/" + fileName;
            //生成 html 文件

            htmlGeneratorService.generateHtml(text, outputPath);
            //返回文件的访问链接
            String link =   fileName;
            //构建响应
            Map<String, String> response = Map.of("link", link);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
