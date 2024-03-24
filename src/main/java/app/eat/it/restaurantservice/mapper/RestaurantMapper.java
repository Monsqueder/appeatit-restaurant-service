package app.eat.it.restaurantservice.mapper;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.dto.RestaurantResponseDto;
import app.eat.it.restaurantservice.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    Restaurant toModel(RestaurantDto restaurantDto);

    RestaurantResponseDto toDto(Restaurant restaurant);

}
