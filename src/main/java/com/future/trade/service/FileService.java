package com.future.trade.service;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanWriter;

import com.future.constant.TradeConstants;
import com.future.exception.TradeCustomException;
import com.future.trade.vo.ReportVO;

@Service
public class FileService {
	
	@Autowired
	private Environment env;
	
	Logger logger = LoggerFactory.getLogger(FileService.class);
	
	
	
	public List<ReportVO> getReportDetails(String fileName)  throws TradeCustomException,Exception{
		
		Resource resource = new ClassPathResource(fileName);

		logger.info("Inside getReportDetails");
		
		StringBuilder client = new StringBuilder();
		StringBuilder product = new StringBuilder();
		double total = 0 ;
		List<ReportVO> listReport = new ArrayList<ReportVO>();
		List<ReportVO> reportList = null;
	    String line;
		int lineCount = 1;
			 if(!resource.getFile().exists()) {
				 throw new TradeCustomException("Input File Not Found");
			 }
			 if(!Files.probeContentType(Paths.get(resource.getFile().getPath())).equalsIgnoreCase("text/plain")) {
				 throw new TradeCustomException("Input File Type Not Correct");
			 }
			 if(resource.getFile().length()<=0) {
				 throw new TradeCustomException("Empty Input File");
			 }
			try( BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));){
			
			while((line =  br.readLine()) != null) {
				
			
				if(!line.startsWith(TradeConstants.RECORD_CODE)) {
					throw new TradeCustomException("Record Code not correct for Input file at line number :: "+lineCount);
				}
				if(!(line.charAt(175) == TradeConstants.OPEN_CLOSE_CODE)) {
					throw new TradeCustomException("Open Close Code not correct at line number :: "+lineCount);
				}
			
				if(!(line.substring(7,11).toString().equals("1234") || line.substring(7,11).toString().equals("4321"))) {
					throw new TradeCustomException("Client Number not correct at line number :: "+lineCount);
				}
						
				client.append(line.substring(3,7).toString()+ line.substring(7,11).toString()+line.substring(11,15).toString()+ line.substring(15,19).toString());
				
				
				product.append(line.substring(27,31).toString()+ line.substring(25,27).toString()+line.substring(31,37).toString()+ line.substring(37,45).toString());
				
				try {
				total = Double.parseDouble(line.substring(52,62)) - Double.parseDouble(line.substring(63,73)) ;
				}catch(Exception exp) {
					br.close();
					throw new TradeCustomException("Number Format exception at line number :: "+lineCount);
				
				}
				ReportVO reportVO = new ReportVO();
				reportVO.setClientInfo(client.toString());
				reportVO.setProductInfo(product.toString());
				reportVO.setTotalAmt(total);
				listReport.add(reportVO);
				client.setLength(0);
				product.setLength(0);
				lineCount++;
				
			}
			
			reportList = new ArrayList<>(listReport.stream()
	                .collect(Collectors.toMap(
	                        e -> new AbstractMap.SimpleEntry<>(e.getClientInfo(), e.getProductInfo()),
	                        Function.identity(),
	                        (a, b) -> new ReportVO(a.getClientInfo(), a.getProductInfo(), a.getTotalAmt()+b.getTotalAmt())))
	                .values());
			 
			    
			}  
		 
		 return reportList;
	        
	}
	
	public void writeCSVFile(ICsvBeanWriter beanWriter, List<ReportVO> reportVOs) {
	    
	    CellProcessor[] processors = new CellProcessor[] {
	            new NotNull(), 
	            new NotNull(),
	            new ParseDouble() 
	    };
	 
	    try {
	      	String[] header = {"Client_Information", "Product_Information", "Total_Transaction_Amount"};
	    	String[] fieldMapping  = {"clientInfo", "productInfo", "totalAmt"};
	        beanWriter.writeHeader(header);
	          
	        for (ReportVO reportVO : reportVOs) {
	            beanWriter.write(reportVO, fieldMapping , processors);
	        }
	 
	    } catch (IOException ex) {
	        logger.error("Error writing the CSV file: " + ex);
	    } finally {
	        if (beanWriter != null) {
	            try {
	                beanWriter.close();
	            } catch (IOException ex) {
	            	 logger.error("Error closing the writer: " + ex);
	            }
	        }
	    }
	}

}
