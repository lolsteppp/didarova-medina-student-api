package kz.didarova.studentapi.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Shows who owns this application (individualization requirement). */
@RestController
public class InfoController {

    @Value("${app.student.name}")
    private String studentName;

    @Value("${app.student.id}")
    private String studentId;

    @Value("${app.student.group}")
    private String studentGroup;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping({"/", "/api/info"})
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("application", "didarova-medina-student-api");
        info.put("author", studentName);
        info.put("studentId", studentId);
        info.put("group", studentGroup);
        info.put("version", appVersion);
        info.put("message", "Student CRUD API by " + studentName);
        return info;
    }
}
