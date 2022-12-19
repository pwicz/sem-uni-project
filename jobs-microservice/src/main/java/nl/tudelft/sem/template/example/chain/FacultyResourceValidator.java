package nl.tudelft.sem.template.example.chain;

import commons.Job;
import commons.Resource;
import java.time.LocalDate;
import nl.tudelft.sem.template.example.models.JobChainModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FacultyResourceValidator extends BaseValidator {

    @Override
    public boolean handle(JobChainModel jobChainModel) throws JobRejectedException {
        Job job = jobChainModel.getJob();
        String faculty = jobChainModel.getAuthFaculty();
        LocalDate localDate = LocalDate.now(); // TODO: this has to be changed to job schedule time

        String requestPath = "/resources?faculty=" + faculty + "&day=" + localDate;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> resourceResponseEntity = restTemplate
                .getForEntity(requestPath, Resource.class, "");
        if (!resourceResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new JobRejectedException("BAD_REQUEST");
        }
        Resource resource = resourceResponseEntity.getBody();
        if (resource == null) {
            throw new JobRejectedException("INVALID_BODY");
        }
        if (job.getCpuUsage() > resource.getCpu() || job.getGpuUsage() > resource.getGpu()
                || job.getMemoryUsage() > resource.getMem()) {
            return super.checkNext(jobChainModel);
        }
        return true; // end of chain of responsibility
    }
}
