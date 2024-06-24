package org.job.hostfully.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.owner.model.dto.OwnerDTO;
import org.job.hostfully.property.facade.PropertyFacade;
import org.job.hostfully.property.model.dto.CreatePropertyDTO;
import org.job.hostfully.property.model.dto.PropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PropertyController.class)
public class PropertyControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyFacade propertyFacade;

    @MockBean
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    private PropertyDTO propertyDTO;
    private CreatePropertyDTO createPropertyDTO;

    @BeforeEach
    public void setup() {
        propertyDTO = new PropertyDTO(1L, "Property 1", "123 Main St", new OwnerDTO(1l, "John Doe", "email@email.com"));
        createPropertyDTO = new CreatePropertyDTO("Property 1", "123 Main St", null, "John Doe", "email@email.com");

        Mockito.when(messageSource.getMessage(any(String.class), any(), any(String.class), any(Locale.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the message code for simplicity
    }

    @Test
    public void testGetPropertyById() throws Exception {
        given(propertyFacade.findById(anyLong())).willReturn(propertyDTO);

        mockMvc.perform(get("/properties/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(propertyDTO.id())).andExpect(jsonPath("$.name").value(propertyDTO.name()));
    }

    @Test
    public void testGetPropertyByIdNotFound() throws Exception {
        given(propertyFacade.findById(anyLong())).willThrow(new NotFoundException("Property not found"));

        mockMvc.perform(get("/properties/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value("Property not found"));
    }

    @Test
    public void testCreateProperty() throws Exception {
        given(propertyFacade.create(any(CreatePropertyDTO.class))).willReturn(propertyDTO);

        mockMvc.perform(post("/properties").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createPropertyDTO))).andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    public void testCreatePropertyMissingFields() throws Exception {
        CreatePropertyDTO invalidCreatePropertyDTO = new CreatePropertyDTO(null, "123 Main St", null, null, null);

        mockMvc.perform(post("/properties").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCreatePropertyDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(jsonPath("$.instance").value("/properties"))
                .andExpect(jsonPath("$.validation-errors[0].timestamp").exists())
                .andExpect(jsonPath("$.validation-errors[0].status").value(400));
    }

    @Test
    public void testFindAllProperties() throws Exception {
        given(propertyFacade.findAll()).willReturn(Collections.singletonList(propertyDTO));

        mockMvc.perform(get("/properties").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(propertyDTO.id())).andExpect(jsonPath("$[0].name").value(propertyDTO.name()));
    }
}
