package com.future.trade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.future.trade.controller.ReportController;
import com.future.trade.service.FileService;

@ExtendWith(MockitoExtension.class)
public class TradeApplicationTests {

private MockMvc mockMvc;

@Mock
private FileService fileService;

@InjectMocks
private ReportController  reportController = new ReportController();

@BeforeEach
public void init() {
mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
 	}

@Test
public void testExportToCSV() throws Exception {
	Mockito.when(fileService.getReportDetails(anyString())).thenReturn(new ArrayList<>());

	MvcResult result = mockMvc
	  				.perform(MockMvcRequestBuilders.get("/api/report").contentType(MediaType.APPLICATION_OCTET_STREAM))
	  				.andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

	assertEquals(200, result.getResponse().getStatus());
	assertEquals("text/csv", result.getResponse().getContentType());
	  	}

	  



@Test
public void testExportToCSVFail() throws Exception {
	
	MvcResult result = mockMvc
	  				.perform(MockMvcRequestBuilders.get("/api/report1").contentType(MediaType.APPLICATION_OCTET_STREAM))
	  				.andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

	assertEquals(404, result.getResponse().getStatus());

	  	}

	  

}

