package app.eat.it.restaurantservice.controller;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.dto.RestaurantResponseDto;
import app.eat.it.restaurantservice.mapper.RestaurantMapper;
import app.eat.it.restaurantservice.model.Restaurant;
import app.eat.it.restaurantservice.service.RestaurantService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@Validated
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper mapper;

    public RestaurantController(RestaurantService restaurantService, RestaurantMapper mapper) {
        this.restaurantService = restaurantService;
        this.mapper = mapper;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleException(ConstraintViolationException exception) {}

    @GetMapping
    public List<RestaurantResponseDto> getRestaurants(@RequestParam(name = "page", defaultValue = "1") @Positive Integer pageNumber,
                                                      @RequestParam(name = "size", defaultValue = "10") @Positive Integer pageSize,
                                                      @RequestParam(name = "sort", required = false) String sortParameter,
                                                      @RequestParam(name = "sort_type", required = false) String sortType) {
        return restaurantService
                .getRestaurants(pageNumber, pageSize, sortParameter, sortType)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public RestaurantResponseDto getRestaurantById(@PathVariable(name = "id") Long id) {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        return mapper.toDto(restaurant);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RestaurantResponseDto createRestaurant(@RequestBody @Valid RestaurantDto restaurantDto) {
        Restaurant restaurant = mapper.toModel(restaurantDto);
        return mapper.toDto(restaurantService.createRestaurant(restaurant));
    }

    @PutMapping("/{id}")
    public void updateRestaurantById(@PathVariable(name = "id") Long id,
                                     @RequestBody @Valid RestaurantDto restaurantDto) {
        restaurantService.updateRestaurant(id, restaurantDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRestaurantById(@PathVariable(name = "id") Long id) {
        restaurantService.deleteRestaurant(id);
    }

}
