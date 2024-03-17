package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mypackage.demoproject.models.Phone;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
}
