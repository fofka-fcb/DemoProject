package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.UserRoleJunction;

@Repository
public interface UserRoleJunctionRepository extends JpaRepository<UserRoleJunction, Integer> {

    UserRoleJunction findUserRoleJunctionByUser(ApplicationUser user);

}
