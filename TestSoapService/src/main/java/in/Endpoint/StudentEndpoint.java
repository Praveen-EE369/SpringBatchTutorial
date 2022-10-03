package in.Endpoint;

import org.soaptest.student.AddStudentRequest;
import org.soaptest.student.AddStudentResponse;
import org.soaptest.student.CallStatus;
import org.soaptest.student.DeleteStudentRequest;
import org.soaptest.student.DeleteStudentResponse;
import org.soaptest.student.GetStudentByIdRequest;
import org.soaptest.student.GetStudentResponse;
import org.soaptest.student.Student;
import org.soaptest.student.UpdateStudentRequest;
import org.soaptest.student.UpdateStudentResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import in.dao.StudentDao;
import in.service.StudentService;

@Endpoint
public class StudentEndpoint {
	private static final String NAMESPACE = "http://www.soapTest.org/student";

	@Autowired
	private StudentService studentService;

	@PayloadRoot(namespace = NAMESPACE, localPart = "addStudentRequest")
	@ResponsePayload
	public AddStudentResponse addStudent(@RequestPayload AddStudentRequest request) {
		AddStudentResponse studentResponse = new AddStudentResponse();
		CallStatus callStatus = new CallStatus();
		StudentDao studentDao = new StudentDao();
		Student studentData = request.getStudentData();
		BeanUtils.copyProperties(studentData, studentDao);

		studentService.addStudent(studentDao);
		callStatus.setStatus("SUCCESS");
		callStatus.setMessage("Student Added into the database");
		studentResponse.setCallStatus(callStatus);
		studentResponse.setStudentData(studentData);
		return studentResponse;
	}
	
	@PayloadRoot(namespace = NAMESPACE, localPart = "getStudentByIdRequest")
	@ResponsePayload
	public GetStudentResponse getStudentById(@RequestPayload GetStudentByIdRequest request) throws BeansException, Exception {
		GetStudentResponse getStudentResponse = new GetStudentResponse();
		Student student = new Student();
		BeanUtils.copyProperties(studentService.getStudentById(request.getStudentId()), student);
		getStudentResponse.setStudentData(student);
		return getStudentResponse;
	}

	@PayloadRoot(namespace = NAMESPACE, localPart = "updateStudentRequest")
	@ResponsePayload
	public UpdateStudentResponse updateStudent(@RequestPayload UpdateStudentRequest request) {
		UpdateStudentResponse studentResponse = new UpdateStudentResponse();
		StudentDao studentDao = new StudentDao();

		BeanUtils.copyProperties(request.getStudentData(), studentDao);
		studentService.updateStudent(studentDao);
		CallStatus callStatus = new CallStatus();
		callStatus.setMessage("Student Updated");
		callStatus.setStatus("SUCCESS");
		studentResponse.setCallStatus(callStatus);
		return studentResponse;

	}

	@PayloadRoot(namespace = NAMESPACE, localPart = "deleteStudentRequest")
	@ResponsePayload
	public DeleteStudentResponse deleteStudentById(@RequestPayload DeleteStudentRequest request) {
		DeleteStudentResponse deleteStudentResponse = new DeleteStudentResponse();
		CallStatus callStatus = new CallStatus();
		studentService.deleteStudentById(request.getStudentId());
		callStatus.setMessage("Student Deleted");
		callStatus.setStatus("SUCCESS");
		deleteStudentResponse.setCallStatus(callStatus);
		return deleteStudentResponse;
	}

}
