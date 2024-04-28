package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.Validation;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {
    Optional<Validation> findByCode(String code);
    Validation findByUser(User user);
}
