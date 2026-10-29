package kz.didarova.studentapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "firstName is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    private String groupName;

    @Min(value = 1, message = "course must be between 1 and 4")
    @Max(value = 4, message = "course must be between 1 and 4")
    private Integer course;

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String groupName, Integer course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupName = groupName;
        this.course = course;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Integer getCourse() { return course; }
    public void setCourse(Integer course) { this.course = course; }
}
