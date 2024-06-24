package org.job.hostfully.common.service;

import org.job.hostfully.common.model.entity.Person;

import java.util.Optional;

public interface PersonService<T extends Person> extends CrudService<T> {

    Optional<T> findByNameAndEmail(String name, String email);

    T save(String name, String email);
}
