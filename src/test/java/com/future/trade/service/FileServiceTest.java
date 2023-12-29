package com.future.trade.service;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.future.exception.TradeCustomException;
import com.future.trade.vo.ReportVO;


@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {
	
	@Test(expected = FileNotFoundException.class)
	public void exceptionFNFTest() throws Exception {

		FileService fileService = new FileService();
		fileService.getReportDetails("abc.txt");

		throw new FileNotFoundException("Input File Not Found");

	}

	@Test(expected = TradeCustomException.class)
	public void exceptionFileTypeTest() throws Exception {
		try {
			FileService fileService = new FileService();
			fileService.getReportDetails("abc.xls");

		} catch (TradeCustomException e) {
			assertEquals("Input File Type Not Correct", e.getMessage().toString());
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test(expected = TradeCustomException.class)
	public void exceptionEmptyFileTest() throws Exception {
		try {
			FileService fileService = new FileService();

			fileService.getReportDetails("empty.txt");
		} catch (TradeCustomException e) {
			assertEquals("Empty Input File", e.getMessage().toString());
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test(expected = TradeCustomException.class)
	public void exceptionFileRecordCodeTest() throws Exception {
		try {
			FileService fileService = new FileService();

			fileService.getReportDetails("diff.txt");
		} catch (TradeCustomException e) {
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test(expected = TradeCustomException.class)
	public void exceptionFileRecordOpenCloseCodeTest() throws Exception {
		try {
			FileService fileService = new FileService();

			fileService.getReportDetails("diff1.txt");
		} catch (TradeCustomException e) {
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test(expected = TradeCustomException.class)
	public void exceptionFileRecordClientCodeTest() throws Exception {
		try {
			FileService fileService = new FileService();

			fileService.getReportDetails("diff2.txt");
		} catch (TradeCustomException e) {
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test(expected = TradeCustomException.class)
	public void exceptionFileAmountTest() throws Exception {
		try {
			FileService fileService = new FileService();
			fileService.getReportDetails("diff3.txt");
		} catch (TradeCustomException e) {
			throw new TradeCustomException(e.getMessage());
		}
	}

	@Test
	public void successFileReadTest() throws Exception {
		FileService fileService = new FileService();
		List<ReportVO> listReport = fileService.getReportDetails("input.txt");

		ReportVO reportVo = listReport.get(0);
       	assertEquals("CL  432100020001", reportVo.getClientInfo().toString().trim());
	}
	
	@Test
	public void writeCSVFileTest() throws Exception {
		FileService fileService = new FileService();
		List<ReportVO> listReport = fileService.getReportDetails("input.txt");
		
	 
		ReportVO reportVo = listReport.get(0);
        assertEquals("CL  432100020001", reportVo.getClientInfo().toString().trim());
		
		Writer writer = new OutputStreamWriter(new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				// TODO Auto-generated method stub
				
			}
		}, "UTF-8");
		
		ICsvBeanWriter beanWriter = new CsvBeanWriter(writer,  CsvPreference.STANDARD_PREFERENCE);
		fileService.writeCSVFile(beanWriter,listReport);
		
		
	}
}
