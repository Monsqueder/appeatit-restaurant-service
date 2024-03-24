package app.eat.it.restaurantservice.mapper;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.dto.RestaurantResponseDto;
import app.eat.it.restaurantservice.model.Restaurant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-23T22:30:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public Restaurant toModel(RestaurantDto restaurantDto) {
        if ( restaurantDto == null ) {
            return null;
        }

        String name = null;
        String address = null;

        name = restaurantDto.name();
        address = restaurantDto.address();

        Long id = null;

        Restaurant restaurant = new Restaurant( id, name, address );

        return restaurant;
    }

    @Override
    public RestaurantResponseDto toDto(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String address = null;

        id = restaurant.getId();
        name = restaurant.getName();
        address = restaurant.getAddress();

        RestaurantResponseDto restaurantResponseDto = new RestaurantResponseDto( id, name, address );

        return restaurantResponseDto;
    }
}
