package org.job.hostfully.common.service;

import java.util.List;

public interface CrudService<Entity> {

    Entity save(Entity entity);

    List<Entity> findAll();

    Entity findById(Long id);

    void delete(Long id);

}
