package org.job.hostfully.booking.model.mapper;

import org.job.hostfully.block.model.entity.Block;
import org.job.hostfully.booking.model.dto.BookingDTO;
import org.job.hostfully.booking.model.dto.CreateBookingDTO;
import org.job.hostfully.booking.model.dto.UpdateBookingDTO;
import org.job.hostfully.booking.model.entity.Booking;
import org.job.hostfully.guest.model.entity.Guest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BookingMapper {

    @Mapping(target = "block", source = "block")
    @Mapping(target = "guest", source = "guest")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    Booking toEntity(CreateBookingDTO dto, Guest guest, Block block);


    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void fromDTO(UpdateBookingDTO dto, @MappingTarget Booking entity);

    @Mapping(target = "startDate", source = "booking.block.startDate")
    @Mapping(target = "endDate", source = "booking.block.endDate")
    @Mapping(target = "property", source = "booking.block.property")
    BookingDTO toDTO(Booking booking);
    @Mapping(target = "startDate", source = "booking.block.startDate")
    @Mapping(target = "endDate", source = "booking.block.endDate")
    @Mapping(target = "property", source = "booking.property")
    List<BookingDTO> toDTO(List<Booking> booking);
}
