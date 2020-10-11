package com.auspos.suburb.controller;

import com.auspos.suburb.dto.SuburbDto;
import com.auspos.suburb.repository.SuburbRepository;
import com.auspos.suburb.service.SuburbService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.CollectionUtils;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.auspos.suburb.domain.Suburb.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SuburbControllerITest {

    public static final String CREATE_SUBURB_REQUEST_TEMPLATE = "/template/createSuburbRequest.ftl";
    public static final String BASIC_AUTH_HEADER = "Basic YWRtaW46dGVzdA==";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SuburbRepository suburbRepository;

    @Mock
    private SuburbService suburbService;

    @BeforeEach
    public void setup() {
        suburbRepository.save(builder().suburbName("Point Cook").postCode(3030).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Werribee").postCode(3030).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Cremorne").postCode(2090).state("NSW").country("Australia").build());
        suburbRepository.save(builder().suburbName("Williams Landing").postCode(3027).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Hoppers Crossing").postCode(3029).state("Victoria").country("Australia").build());
        suburbRepository.save(builder().suburbName("Cremorne").postCode(3121).state("Victoria").country("Australia").build());
    }

    @AfterEach
    public void tearDown() {
        suburbRepository.deleteAll();
    }

    @Test
    public void getSuburbsByPostCode_validPostCode_shouldReturnSuburb() throws Exception {
        MvcResult result = mvc.perform(get("/v1/suburb/postcode/3027")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<SuburbDto> suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });

        assertThat(suburbDtos.size(), equalTo(1));
        assertThat(suburbDtos.get(0).getSuburbName(), equalTo("Williams Landing"));
        assertThat(suburbDtos.get(0).getPostCode(), equalTo(3027L));
        assertThat(suburbDtos.get(0).getState(), equalTo("Victoria"));
        assertThat(suburbDtos.get(0).getCountry(), equalTo("Australia"));

        result = mvc.perform(get("/v1/suburb/postcode/3030")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });
        assertThat(suburbDtos.size(), equalTo(2));
    }

    @Test
    public void getSuburbsByName_validName_shouldReturnSuburb() throws Exception {
        MvcResult result = mvc.perform(get("/v1/suburb/name/williams landing")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<SuburbDto> suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });

        assertThat(suburbDtos.size(), equalTo(1));
        assertThat(suburbDtos.get(0).getSuburbName(), equalTo("Williams Landing"));
        assertThat(suburbDtos.get(0).getPostCode(), equalTo(3027L));
        assertThat(suburbDtos.get(0).getState(), equalTo("Victoria"));
        assertThat(suburbDtos.get(0).getCountry(), equalTo("Australia"));

        result = mvc.perform(get("/v1/suburb/name/cremorne")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });
        assertThat(suburbDtos.size(), equalTo(2));
    }

    @Test
    public void createSuburb_validRequest_shouldCreateANewSuburb() throws Exception {
        // Given: No suburb present with postcode 3000
        MvcResult result = mvc.perform(get("/v1/suburb/postcode/3000")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        List<SuburbDto> suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });
        Assertions.assertTrue(CollectionUtils.isEmpty(suburbDtos));

        // When: We create a new suburb
        mvc.perform(post("/v1/suburb")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_HEADER)
                .content(buildRequestBody(Collections.emptyMap())))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        // Then: The suburb should be created in the DB
        result = mvc.perform(get("/v1/suburb/postcode/3000")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        suburbDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SuburbDto>>() { });

        assertThat(suburbDtos.size(), equalTo(1));
        assertThat(suburbDtos.get(0).getSuburbName(), equalTo("Melbourne"));
        assertThat(suburbDtos.get(0).getPostCode(), equalTo(3000L));
        assertThat(suburbDtos.get(0).getState(), equalTo("Victoria"));
        assertThat(suburbDtos.get(0).getCountry(), equalTo("Australia"));
    }

    @Test
    public void createSuburb_withoutAuthHeader_shouldThrowException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("suburbName", "Point Cook");
        data.put("postCode", "3030");

        mvc.perform(post("/v1/suburb")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_HEADER)
                .content(buildRequestBody(data)))
                .andExpect(MockMvcResultMatchers.status().is(401));
    }

    @Test
    public void createSuburb_suburbAlreadyExists_shouldThrowException() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("suburbName", "Point Cook");
        data.put("postCode", "3030");

        mvc.perform(post("/v1/suburb")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_HEADER)
                .content(buildRequestBody(data)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @ParameterizedTest
    @MethodSource("invalidDataScenarios")
    public void createSuburb_invalidData_shouldThrowException(String reqAttribute, String value) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put(reqAttribute, value);

        mvc.perform(post("/v1/suburb")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_HEADER)
                .content(buildRequestBody(data)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    private static Stream<Arguments> invalidDataScenarios() {
        return Stream.of (
                Arguments.of("suburbName", ";DROP TABLE SUBURB;"),
                Arguments.of("suburbName", "jljlkasjdlfkjlasjdfljlkasjlkdjflajsdjicojadskjfljlcml"),
                Arguments.of("postCode", "ABCD"),
                Arguments.of("postCode", "1234567")
        );
    }

    private String buildRequestBody(Map<String, String> data) throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        Template template = cfg.getTemplate(CREATE_SUBURB_REQUEST_TEMPLATE);

        StringWriter stringWriter = new StringWriter();
        template.process(data, stringWriter);
        return stringWriter.toString();
    }

}
