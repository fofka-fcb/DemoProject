package ru.mypackage.demoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mypackage.demoproject.models.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {
}
