package org.job.hostfully.guest.service;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.common.service.PersonService;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.guest.model.entity.Guest;
import org.job.hostfully.guest.model.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements PersonService<Guest> {
    private final GuestRepository repository;

    @Override
    public Guest save(Guest guest) {
        return repository.save(guest);
    }

    @Override
    public List<Guest> findAll() {
        return repository.findAll();
    }

    @Override
    public Guest findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Guest not found."));
    }

    @Override
    public Optional<Guest> findByNameAndEmail(String name, String email) {
        return repository.findTopByNameAndEmail(name, email);
    }

    @Override
    public Guest save(String name, String email) {
        return save(new Guest(name, email));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
