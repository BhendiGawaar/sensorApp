package com.example.sensorApp;

import com.example.sensorApp.domain.dto.MeasurementDto;
import com.example.sensorApp.domain.dto.StatusDto;
import com.example.sensorApp.util.StatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SensorApp.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SensorAppTests {

	@Autowired
	private MockMvc mockMvc;
	ObjectMapper mapper;

	@BeforeAll
	public void setup() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		mockMvc.perform(get("/api/v1/sensors/greeting"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));
	}

	@Test
	public void shouldReturnOk() throws Exception{
		MeasurementDto m1 = new MeasurementDto(1100, ZonedDateTime.of(2019,2,1,18,55,0,0, ZoneId.of("UTC")));
		StatusDto expectedStatus = new StatusDto(StatusEnum.OK.name());
		String sensorID = UUID.randomUUID().toString();

		mockMvc.perform(post("/api/v1/sensors/"+ sensorID +"/mesurements")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m1)))
				.andExpect(status().isOk());
		Thread.sleep(3000);
		mockMvc.perform(get("/api/v1/sensors/"+sensorID))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(expectedStatus)));
	}

	//If the CO2 level exceeds 2000 ppm the sensor status should be set to WARN
	@Test
	public void shouldReturnWarn() throws Exception{
		MeasurementDto m1 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,0,0, ZoneId.of("UTC")));
		StatusDto expectedStatus = new StatusDto(StatusEnum.WARN.name());
		String sensorID = UUID.randomUUID().toString();

		mockMvc.perform(post("/api/v1/sensors/"+ sensorID +"/mesurements")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m1)))
				.andExpect(status().isOk());
		Thread.sleep(3000);
		mockMvc.perform(get("/api/v1/sensors/"+sensorID))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(expectedStatus)));
	}

	//If the service receives 3 or more consecutive measurements higher than
	//2000 the sensor status should be set to ALERT
	@Test
	public void shouldReturnAlert() throws Exception{
		MeasurementDto m1 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,1,0, ZoneId.of("UTC")));
		MeasurementDto m2 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,2,0, ZoneId.of("UTC")));
		MeasurementDto m3 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,3,0, ZoneId.of("UTC")));
		StatusDto expectedStatus = new StatusDto(StatusEnum.ALERT.name());
		String sensorID = UUID.randomUUID().toString();
		String URL = "/api/v1/sensors/"+ sensorID +"/mesurements";

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m1)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m2)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m3)))
				.andExpect(status().isOk());
		Thread.sleep(3000);
		mockMvc.perform(get("/api/v1/sensors/"+sensorID))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(expectedStatus)));
	}

	//When the sensor reaches to status ALERT it stays in this state until it receives 3
	//consecutive measurements lower than 2000; then it moves to OK
	@Test
	public void fromAlertToOk() throws Exception{
		//first, three consecutive high measurements to trigger alert
		MeasurementDto m1 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,1,0, ZoneId.of("UTC")));
		MeasurementDto m2 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,2,0, ZoneId.of("UTC")));
		MeasurementDto m3 = new MeasurementDto(2100, ZonedDateTime.of(2019,2,1,18,55,3,0, ZoneId.of("UTC")));
		StatusDto expectedStatus = new StatusDto(StatusEnum.ALERT.name());
		String sensorID = UUID.randomUUID().toString();
		String URL = "/api/v1/sensors/"+ sensorID +"/mesurements";

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m1)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m2)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m3)))
				.andExpect(status().isOk());
		Thread.sleep(3000);
		mockMvc.perform(get("/api/v1/sensors/"+sensorID))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(expectedStatus)));
		//next, three consecutive low measurements to return to ok status
		MeasurementDto m4 = new MeasurementDto(1950, ZonedDateTime.of(2019,2,1,18,55,4,0, ZoneId.of("UTC")));
		MeasurementDto m5 = new MeasurementDto(1900, ZonedDateTime.of(2019,2,1,18,55,5,0, ZoneId.of("UTC")));
		MeasurementDto m6 = new MeasurementDto(1850, ZonedDateTime.of(2019,2,1,18,55,6,0, ZoneId.of("UTC")));
		expectedStatus = new StatusDto(StatusEnum.OK.name());

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m4)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m5)))
				.andExpect(status().isOk());
		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(m6)))
				.andExpect(status().isOk());
		Thread.sleep(3000);
		mockMvc.perform(get("/api/v1/sensors/"+sensorID))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(mapper.writeValueAsString(expectedStatus)));
	}
}
