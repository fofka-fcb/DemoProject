package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByUsernameStartingWith(String username);

    Optional<ApplicationUser> findById(int id);

}
