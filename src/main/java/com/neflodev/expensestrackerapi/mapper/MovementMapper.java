package com.neflodev.expensestrackerapi.mapper;

import com.neflodev.expensestrackerapi.dto.movement.MovementDto;
import com.neflodev.expensestrackerapi.model.MovementEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mapping(target = "amount", expression = "java(source.getAmount().toPlainString())")
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "currencySymbol", source = "account.currencySymbol")
    MovementDto entityToDto(MovementEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(source.getCategory() != null ? source.getCategory() : target.getCategory())")
    @Mapping(target = "effectiveDate", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "comment", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovement(@MappingTarget MovementEntity target, MovementEntity source);

}
