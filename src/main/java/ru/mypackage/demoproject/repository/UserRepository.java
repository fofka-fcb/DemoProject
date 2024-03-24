package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.ApplicationUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"authorities", "phone"})
    Optional<ApplicationUser> findByUsername(String username);

    Optional<ApplicationUser> findByUsernameStartingWith(String username);

    Optional<ApplicationUser> findById(int id);

}
