package montessori.school.clientservice.controller;


import com.google.protobuf.Descriptors;
import lombok.AllArgsConstructor;
import montessori.school.clientservice.service.ProjectStudentClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class ProjectStudentController {

    ProjectStudentClientService projectStudentClientService;

    @GetMapping("/student/{id}")
    public Map<Descriptors.FieldDescriptor, Object> getStudent(@PathVariable String studentId) {
        return projectStudentClientService.getStudent(Integer.parseInt(studentId));
    }

}
