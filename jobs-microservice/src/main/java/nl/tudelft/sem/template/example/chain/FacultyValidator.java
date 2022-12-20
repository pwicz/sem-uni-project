package nl.tudelft.sem.template.example.chain;

import commons.*;
import nl.tudelft.sem.template.example.models.JobChainModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FacultyValidator extends BaseValidator {

    @Override
    public boolean handle(JobChainModel jobChainModel) throws JobRejectedException {
        Job job = jobChainModel.getJob();
        RoleType role = jobChainModel.getAuthRole();
        List<Faculty> faculty = jobChainModel.getAuthFaculty();

        FacultyRequestModel requestModel = new FacultyRequestModel();
        requestModel.setNetId(job.getNetId().toString());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<FacultyResponseModel> response = restTemplate
                .postForEntity("http://localhost:8081/faculty",requestModel, FacultyResponseModel.class);


        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new JobRejectedException("BAD_REQUEST");
        }
        FacultyResponseModel responseModel = response.getBody();
        if (responseModel == null) {
            throw new JobRejectedException("INVALID_BODY");
        }

        List<Faculty> userFaculty = responseModel.getFaculty().stream().map(Faculty::new).collect(Collectors.toList());
        Set<Faculty> commonFaculties = userFaculty.stream().distinct().filter(faculty::contains).collect(Collectors.toSet());

        if (commonFaculties.isEmpty() || !role.equals(RoleType.Faculty)) {
            throw new JobRejectedException("BAD_CREDENTIALS");
        }
        if (jobChainModel.getDirectiveJob().equals(DirectiveJob.Reject)) {
            return false;
        }
        return super.checkNext(jobChainModel);
    }
}
