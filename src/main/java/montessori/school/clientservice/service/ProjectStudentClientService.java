package montessori.school.clientservice.service;

import com.google.protobuf.Descriptors;
import com.grpcdemo.Project;
import com.grpcdemo.ProjectStudentServiceGrpc;
import com.grpcdemo.ProjectsStudentServiceGrpc;
import com.grpcdemo.Student;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectStudentClientService {
    @GrpcClient("grpc-montessori-service")
    ProjectStudentServiceGrpc.ProjectStudentServiceBlockingStub synchronousClient; //once the client makes the call to the service,the client doesn't  proceed with the rest of the cod execution until it gets the response back from the server

    @GrpcClient("grpc-montessori-service")
    ProjectsStudentServiceGrpc.ProjectsStudentServiceStub asynchronousClient; // opposite


    public Map<Descriptors.FieldDescriptor, Object> getStudent(int studentId) {
        Student studentRequest = Student.newBuilder().setStudentId(studentId).build();
        Student studentResponse = synchronousClient.getStudent(studentRequest);
        return studentResponse.getAllFields();
    }


    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByStudent(int studentId) throws InterruptedException {
        final Student studentRequest = Student.newBuilder().setStudentId(studentId).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        asynchronousClient.getProjectsByStudent(studentRequest, new StreamObserver<Project>() {
            @Override
            public void onNext(Project project) {  //sever will stream the projects here
                response.add(project.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                countDownLatch.countDown();

            }


        });

        boolean await = countDownLatch.await(1, TimeUnit.MINUTES);
        return await ? response : Collections.emptyList();

    }
}
