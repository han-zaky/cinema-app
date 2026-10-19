package com.example.demo.cinema.service;


import com.example.demo.cinema.entity.Cinema;
import com.example.demo.cinema.repository.CinemaRepository;
import com.example.demo.cinema.dto.CinemaRequestDto;
import com.example.demo.cinema.dto.CinemaResponseDto;
import com.example.demo.common.entity.Address;
import com.example.demo.common.dto.AddressDto;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    private CinemaResponseDto mapToResponseDto(Cinema cinema) {
        return new CinemaResponseDto(
                cinema.getId(),
                cinema.getName(),
                mapToAddressDto(cinema.getAddress()));
    }

    private AddressDto mapToAddressDto(Address address) {
        return new AddressDto(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getZipCode());
    }

    private Address mapToAddress(AddressDto addressDto) {
        return new Address(
                addressDto.getCountry(),
                addressDto.getCity(),
                addressDto.getStreet(),
                addressDto.getHouseNumber(),
                addressDto.getZipCode());
    }

    public List<CinemaResponseDto> getAllCinemas() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return cinemas.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CinemaResponseDto createCinema(CinemaRequestDto requestDto) {
        Cinema cinema = new Cinema();
        cinema.setName(requestDto.getName());
        cinema.setAddress(mapToAddress(requestDto.getAddress()));

        Cinema savedCinema = cinemaRepository.save(cinema);

        return mapToResponseDto(savedCinema);
    }

    public CinemaResponseDto updateCinema(Long id, CinemaRequestDto requestDto) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + id));

        if (requestDto.getName() != null) {
            cinema.setName(requestDto.getName());
        }
        if (requestDto.getAddress() != null) {
            AddressDto addrDto = requestDto.getAddress();
            Address existingAddress = cinema.getAddress();

            if (addrDto.getCountry() != null)
                existingAddress.setCountry(addrDto.getCountry());
            if (addrDto.getCity() != null)
                existingAddress.setCity(addrDto.getCity());
            if (addrDto.getStreet() != null)
                existingAddress.setStreet(addrDto.getStreet());
            if (addrDto.getHouseNumber() != null)
                existingAddress.setHouseNumber(addrDto.getHouseNumber());
            if (addrDto.getZipCode() != null)
                existingAddress.setZipCode(addrDto.getZipCode());
        }

        Cinema updatedCinema = cinemaRepository.save(cinema);

        return mapToResponseDto(updatedCinema);
    }

    public void deleteCinema(Long id) {
        if (!cinemaRepository.existsById(id)) {
            throw new RuntimeException("Cinema not found with id: " + id);
        }
        cinemaRepository.deleteById(id);
    }

    public CinemaResponseDto getCinemaById(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + id));

        return mapToResponseDto(cinema);
    }

}
