package com.future.trade.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.future.trade.service.FileService;
import com.future.trade.vo.ReportVO;
 
@Controller
@RequestMapping("/api")
public class ReportController {
 
    @Autowired
	FileService fileService;
 
    Logger logger = LoggerFactory.getLogger(ReportController.class);
     
    @GetMapping("/report")
    public void exportToCSV(HttpServletResponse response) throws Exception {
    	
    	logger.info("Inside Controller");
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=report_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        
        String fileName = "input.txt";
    
        List<ReportVO> reportVO =  fileService.getReportDetails(fileName);
        
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        fileService.writeCSVFile(csvWriter, reportVO);
    
        csvWriter.close();
         
    }
     
}