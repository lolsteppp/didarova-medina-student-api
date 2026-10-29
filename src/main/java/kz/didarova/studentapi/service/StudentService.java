package kz.didarova.studentapi.service;

import java.util.List;

import kz.didarova.studentapi.exception.StudentNotFoundException;
import kz.didarova.studentapi.model.Student;
import kz.didarova.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(Student student) {
        student.setId(null);
        return repository.save(student);
    }

    /** UPDATE operation (added on feature/didarova-medina-crud by Didarova Medina). */
    public Student update(Long id, Student changes) {
        Student existing = findById(id);
        existing.setFirstName(changes.getFirstName());
        existing.setLastName(changes.getLastName());
        existing.setEmail(changes.getEmail());
        existing.setGroupName(changes.getGroupName());
        existing.setCourse(changes.getCourse());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
