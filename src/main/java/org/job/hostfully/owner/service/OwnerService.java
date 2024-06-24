package org.job.hostfully.owner.service;

import lombok.RequiredArgsConstructor;
import org.job.hostfully.common.service.PersonService;
import org.job.hostfully.common.util.exceptions.NotFoundException;
import org.job.hostfully.owner.model.entity.Owner;
import org.job.hostfully.owner.model.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerService implements PersonService<Owner> {

    private final OwnerRepository repository;

    @Override
    public Owner save(Owner owner) {
        if (owner.getEmail() == null || owner.getName() == null)
            throw new IllegalArgumentException("Owner must have an Email and Name set");
        return repository.save(owner);
    }

    @Override
    public List<Owner> findAll() {
        return repository.findAll();
    }

    @Override
    public Owner findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Owner not found"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Owner> findByNameAndEmail(String name, String email) {
        return repository.findTopByNameAndEmail(name, email);
    }

    @Override
    public Owner save(String name, String email) {
        return save(new Owner(name, email));
    }
}
