package app.eat.it.restaurantservice.integration;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.dto.RestaurantResponseDto;
import app.eat.it.restaurantservice.mapper.RestaurantMapper;
import app.eat.it.restaurantservice.model.Restaurant;
import app.eat.it.restaurantservice.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class RestaurantControllerIntegrationTest {

    private final String REQUEST_MAPPING_PREFIX = "/api/v1/restaurants";

    private MockMvc mockMvc;

    @MockBean
    private RestaurantMapper mapper;

    @MockBean
    private RestaurantService service;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getRestaurants_valid_params_should_return_restaurantList() throws Exception {
        Integer pageNumber = 1;
        Integer pageSize = 10;
        String sortParameter = "name";
        String sortType = "ASC";

        Restaurant restaurant = new Restaurant(1L, "Blue whale", "Paris");
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Blue whale", "Paris");

        when(service.getRestaurants(pageNumber, pageSize, sortParameter, sortType)).thenReturn(List.of(restaurant));
        when(mapper.toDto(restaurant)).thenReturn(responseDto);

        mockMvc.perform(
                get(REQUEST_MAPPING_PREFIX)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
                        .param("sort", sortParameter)
                        .param("sort_type", sortType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.[0].id").value(responseDto.id()));

        verify(service, times(1))
                .getRestaurants(eq(pageNumber), eq(pageSize), eq(sortParameter), eq(sortType));
        verify(mapper, times(1)).toDto(eq(restaurant));
    }

    @Test
    public void getRestaurants_invalid_params_should_return_status_400() throws Exception {
        Integer pageNumber = 0;
        Integer pageSize = 0;
        String sortParameter = "name";
        String sortType = "ASC";

        mockMvc.perform(
                        get(REQUEST_MAPPING_PREFIX)
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize))
                                .param("sort", sortParameter)
                                .param("sort_type", sortType))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRestaurant_should_return_restaurantResponseDto() throws Exception {
        Long id = 1L;

        Restaurant restaurant = new Restaurant(id, "Blue whale", "Paris");
        RestaurantResponseDto responseDto = new RestaurantResponseDto(id, "Blue whale", "Paris");

        when(service.getRestaurant(id)).thenReturn(restaurant);
        when(mapper.toDto(restaurant)).thenReturn(responseDto);

        mockMvc.perform(get(REQUEST_MAPPING_PREFIX + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(service, times(1)).getRestaurant(eq(id));
        verify(mapper, times(1)).toDto(eq(restaurant));
    }

    @Test
    public void createRestaurant_valid_data_should_return_restaurantResponseDto() throws Exception {
        Long id = 1L;

        Restaurant restaurant = new Restaurant(id, "Blue whale", "Paris");
        RestaurantResponseDto responseDto = new RestaurantResponseDto(id, "Blue whale", "Paris");
        RestaurantDto dto = new RestaurantDto("Blue whale", "Paris");

        when(service.createRestaurant(restaurant)).thenReturn(restaurant);
        when(mapper.toModel(dto)).thenReturn(restaurant);
        when(mapper.toDto(restaurant)).thenReturn(responseDto);

        mockMvc.perform(post(REQUEST_MAPPING_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id));

        verify(service, times(1)).createRestaurant(eq(restaurant));
        verify(mapper, times(1)).toModel(eq(dto));
        verify(mapper, times(1)).toDto(eq(restaurant));
    }

    @Test
    public void createRestaurant_invalid_data_should_return_status_400() throws Exception {
        RestaurantDto dto = new RestaurantDto("", "");

        mockMvc.perform(post(REQUEST_MAPPING_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service, mapper);
    }

    @Test
    public void updateRestaurant_valid_data_should_return_status_OK() throws Exception {
        Long id = 1L;

        RestaurantDto dto = new RestaurantDto("Blue whale", "Paris");

        mockMvc.perform(put(REQUEST_MAPPING_PREFIX + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service, times(1)).updateRestaurant(eq(id), eq(dto));
    }

    @Test
    public void updateRestaurant_invalid_data_should_return_status_400() throws Exception {
        RestaurantDto dto = new RestaurantDto("", "");

        mockMvc.perform(put(REQUEST_MAPPING_PREFIX + "/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    public void deleteRestaurant_should_return_status_OK() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(REQUEST_MAPPING_PREFIX + "/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service, times(1)).deleteRestaurant(eq(id));
    }

}
