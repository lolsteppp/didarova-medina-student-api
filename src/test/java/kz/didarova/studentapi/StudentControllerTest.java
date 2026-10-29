package kz.didarova.studentapi;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kz.didarova.studentapi.model.Student;
import kz.didarova.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    private static final String MEDINA_JSON = """
            {"firstName":"Medina","lastName":"Didarova","email":"medina.didarova@test.kz",
             "groupName":"IT2-2313","course":3}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    private Student saveMedina() {
        return repository.save(new Student("Medina", "Didarova", "medina.didarova@test.kz", "IT2-2313", 3));
    }

    @Test
    void infoShowsStudentName() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is("Didarova Medina")))
                .andExpect(jsonPath("$.studentId", is("37944")));
    }

    @Test
    void createStudent() throws Exception {
        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(MEDINA_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.lastName", is("Didarova")));
    }

    @Test
    void createStudentWithInvalidEmailReturns400() throws Exception {
        String bad = """
                {"firstName":"Medina","lastName":"Didarova","email":"not-an-email"}
                """;
        mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(bad))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllStudents() throws Exception {
        saveMedina();
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Medina")));
    }

    @Test
    void getStudentById() throws Exception {
        Student s = saveMedina();
        mockMvc.perform(get("/api/students/" + s.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("medina.didarova@test.kz")));
    }

    @Test
    void updateStudent() throws Exception {
        Student s = saveMedina();
        String changes = """
                {"firstName":"Medina","lastName":"Didarova","email":"medina.didarova@test.kz",
                 "groupName":"IT2-2313","course":4}
                """;
        mockMvc.perform(put("/api/students/" + s.getId()).contentType(MediaType.APPLICATION_JSON).content(changes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course", is(4)));
    }

    @Test
    void updateNonexistentStudentReturns404() throws Exception {
        mockMvc.perform(put("/api/students/99999").contentType(MediaType.APPLICATION_JSON).content(MEDINA_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent() throws Exception {
        Student s = saveMedina();
        mockMvc.perform(delete("/api/students/" + s.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/students/" + s.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void nonexistentStudentReturns404() throws Exception {
        mockMvc.perform(get("/api/students/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99999")));
    }
}
