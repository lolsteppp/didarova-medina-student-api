package kz.didarova.studentapi.config;

import kz.didarova.studentapi.model.Student;
import kz.didarova.studentapi.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Individual seed data for Didarova Medina. Inserted only when the table is empty. */
@Configuration
@Profile("!test")
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedStudents(StudentRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Student("Medina", "Didarova", "medina.didarova@student.kz", "IT2-2313", 3));
                repository.save(new Student("Aruzhan", "Didarova-Test", "aruzhan.test37944@student.kz", "IT2-2313", 3));
                log.info("Seed data for Didarova Medina (ID 37944) inserted");
            }
            log.info("=== Student API started by Didarova Medina, group IT2-2313 ===");
        };
    }
}
