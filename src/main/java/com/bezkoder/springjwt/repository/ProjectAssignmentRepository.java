package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.ProjectAssignment;
import com.bezkoder.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, Long> {
    //List<ProjectAssignment> findByUserAndStartDateBetween(User user, LocalDate startDate, LocalDate endDate);
    void deleteByUser(User user);
    List<ProjectAssignment> findByUser(User user);
}
