package montessori.school.clientservice.service;

import com.google.protobuf.Descriptors;
import com.grpcdemo.ProjectStudentServiceGrpc;
import com.grpcdemo.Student;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProjectStudentClientService {
    @GrpcClient("grpc-montessori-service")
    ProjectStudentServiceGrpc.ProjectStudentServiceBlockingStub synchronousClient;

    public Map<Descriptors.FieldDescriptor, Object> getStudent(int studentId) {
        Student studentRequest = Student.newBuilder().setStudentId(studentId).build();
        Student studentResponse = synchronousClient.getStudent(studentRequest);
        return studentResponse.getAllFields();
    }

    @GrpcClient("grpc-montessori-service")

}

