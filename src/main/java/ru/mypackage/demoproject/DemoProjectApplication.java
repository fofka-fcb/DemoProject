package ru.mypackage.demoproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mypackage.demoproject.models.ApplicationUser;
import ru.mypackage.demoproject.models.Role;
import ru.mypackage.demoproject.repository.RoleRepository;
import ru.mypackage.demoproject.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DemoProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoProjectApplication.class, args);
    }

//    	@Bean
//        CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode){
//		return args ->{
//			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
//			Role roleAdmin = roleRepository.save(new Role("ADMIN"));
//			Role roleOperator = roleRepository.save(new Role("OPERATOR"));
//			Role roleUser = roleRepository.save(new Role("USER"));
//
//			Set<Role> adminRoles = new HashSet<>();
//            adminRoles.add(roleAdmin);
//
//            Set<Role> operatorRoles = new HashSet<>();
//            operatorRoles.add(roleOperator);
//
//			Set<Role> userRoles = new HashSet<>();
//			userRoles.add(roleUser);
//
//			ApplicationUser admin = new ApplicationUser("admin", passwordEncode.encode("password"), adminRoles);
//            ApplicationUser operator = new ApplicationUser("operator", passwordEncode.encode("password"), operatorRoles);
//            ApplicationUser user = new ApplicationUser("user", passwordEncode.encode("password"), userRoles);
//
//			userRepository.save(admin);
//			userRepository.save(operator);
//			userRepository.save(user);
//		};
//	}
}
