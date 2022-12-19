package nl.tudelft.sem.template.example.domain;


import commons.Job;
import commons.NetId;
import commons.Status;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import exceptions.InvalidResourcesException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

/**
 * A DDD service for handling jobs.
 */

@Service
public class JobService {

    private static final String nullValue = "null";

    private String url = "http://localhost:8083";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private final transient JobRepository jobRepository;

    /**
     * Instantiates a new JobService.
     *
     * @param jobRepository the job repository
     */
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Create a new job.
     *
     * @param netId NetId of the job creator
     * @param authNetId NetId of the authenticated user
     * @param cpuUsage CPU usage
     * @param gpuUsage GPU usage
     * @param memoryUsage memory usage
     * @return a new Job
     * @throws Exception if the resources of NetId are invalid
     */
    public Job createJob(NetId netId, NetId authNetId, int cpuUsage, int gpuUsage,
                         int memoryUsage, String role) throws Exception {
        if (cpuUsage < 0 || gpuUsage < 0 || memoryUsage < 0) {
            throw new InvalidResourcesException(Math.min(cpuUsage, Math.min(gpuUsage, memoryUsage)));
        }
        if (netId == null) {
            throw new InvalidNetIdException(nullValue);
        }
        if (!netId.toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        if (!role.equals("employee")) {
            System.out.println(role);
            throw new BadCredentialsException(role);
        }

        Job newJob = new Job(netId, cpuUsage, gpuUsage, memoryUsage);
        jobRepository.save(newJob);

        return newJob;
    }

    /**
     * Create a new job.
     *
     * @param authNetId NetId of the authenticated user
     * @param job a job
     * @param role a role of a user who creates a job
     * @return a new Job
     * @throws Exception if the resources of NetId are invalid
     */
    public Job createJob(NetId authNetId, Job job, String role) throws Exception {
        if (job.getCpuUsage() < 0 || job.getGpuUsage() < 0 || job.getMemoryUsage() < 0) {
            throw new InvalidResourcesException(Math.min(job.getCpuUsage(),
                    Math.min(job.getGpuUsage(), job.getMemoryUsage())));
        }
        if (job.getNetId() == null) {
            throw new InvalidNetIdException(nullValue);
        }
        if (!job.getNetId().toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(job.getNetId().toString());
        }
        if (!role.equals("employee")) {
            System.out.println(role);
            throw new BadCredentialsException(role);
        }

        jobRepository.save(job);

        return job;
    }

    /**
     * Remove a job from the database.
     *
     * @param id the unique id of the Job.
     * @throws Exception if there is no Job with the provided id.
     */
    public void deleteJob(long id) throws Exception {
        if (!jobRepository.existsById(id)) {
            throw new InvalidIdException(id);
        }
        jobRepository.deleteById(id);
    }

    /**
     * Collect all the jobs in the database created by a specific user.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @return a list of Job corresponding to the NetId provided
     * @throws Exception if the NetId is invalid or there is no associated Job to the NetId
     */
    public List<Job> collectJobsByNetId(NetId netId, NetId authNetId) throws Exception {
        if (netId == null) {
            throw new InvalidNetIdException(nullValue);
        }
        Optional<List<Job>> jobs = jobRepository.findAllByNetId(netId);
        if (jobs.isEmpty() || !netId.toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        return jobs.get();
    }

    /**
     * Retrieve the status of a specific Job stored in the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param jobId the unique id of the Job
     * @return a String with the status of the Job
     * @throws Exception if the NetId is invalid or the NetId does not have permission to access the requested job.
     */
    public Status getJobStatus(NetId netId, NetId authNetId, long jobId) throws Exception {
        if (netId == null) {
            throw new InvalidNetIdException(nullValue);
        }

        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) {
            throw new InvalidIdException(jobId);
        }
        if (!netId.toString().equals(authNetId.toString()) || !job.get().getNetId().toString().equals(netId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        return job.get().getStatus();
    }

    /**
     * Retrieve all the Job entities from the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param role role of the request creator
     * @return a list of Job entities containing all jobs in the database.
     * @throws Exception if the NetId is invalid or the creator of the request does not have the admin role.
     */
    public List<Job> getAllJobs(NetId netId, NetId authNetId, String role) throws Exception {
        if (netId == null) {
            throw new InvalidNetIdException(nullValue);
        }
        if (!netId.toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        if (!role.equals("admin")) {
            throw new BadCredentialsException(role);
        }
        return jobRepository.findAll();
    }


    /**
     * Retrieve all the Job entities from the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param role role of the request creator
     * @return a list of Job entities containing all jobs in the database.
     * @throws Exception if the NetId is invalid or the creator of the request does not have the admin role.
     */
    public List<Job> getAllScheduledJobs(NetId netId, NetId authNetId, String role) throws Exception {
        if (netId == null) {
            throw new InvalidNetIdException(nullValue);
        }
        if (!netId.toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        if (!role.equals("admin")) {
            throw new BadCredentialsException(role);
        }
        return jobRepository.findAll().stream().filter(j -> j.getStatus() == Status.ACCEPTED).collect(Collectors.toList());
    }

    /**
     * Update information about the Job specified by a microservice.
     *
     * @param id id of the Job
     * @param status the new status of the Job
     * @param localDate the time the Job is scheduled to start
     * @throws Exception if the id does not exist in the database
     */
    public void updateJob(long id, Status status, LocalDate localDate) throws Exception {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isEmpty()) {
            throw new InvalidIdException(id);
        }
        Job job = jobOptional.get();
        job.setStatus(status);
        job.setScheduleDate(localDate);
        jobRepository.save(job);
    }
}
