package commons;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FacultyResource {
    private String faculty;
    private LocalDate date;
    private int cpuUsage;
    private int gpuUsage;
    private int memoryUsage;
}
