package commons;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "nodes")
@NoArgsConstructor
public class Node implements Comparable {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Getter @Setter, does this work still?
    private long id;

    @Column(name = "netId", nullable = false)
    private String name; //netId?

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "faculty", nullable = false)
    private String faculty;

    @Column(name = "token", nullable = false)
    private String token; //user auth token?

    @Column(name = "cpu", nullable = false)
    private int cpu;

    @Column(name = "gpu", nullable = false)
    private int gpu;

    @Column(name = "mem", nullable = false)
    private int mem;

    @Column(name = "realeasedStart", nullable = true)
    private LocalDate released = null;

    @Column(name = "realeasedEnd", nullable = true)
    private LocalDate releaseEnd = null;
    @Column(name = "removedDate", nullable = true)
    private LocalDate removedDate = null;

    /**
     * Constructor for the Node class, represent the nodes in the culster.
     *
     * @param name the netId of the user creating the cluster
     * @param faculty the faculty the user belongs to
     * @param token token of access
     * @param cpuUsage the amount of cpu units needed
     * @param gpuUsage the amount of gpu units needed
     * @param memUsage the amount of memory units needed
     */
    public Node(String name, String url, String faculty, String token, int cpuUsage, int gpuUsage, int memUsage) {
        this.name = name;
        this.url = url;
        this.faculty = faculty;
        this.token = token;
        this.cpu = cpuUsage;
        this.gpu  = gpuUsage;
        this.mem = memUsage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCpu() {
        return cpu;
    }

    public int getGpu() {
        return gpu;
    }

    public int getMemory() {
        return mem;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public LocalDate getReleaseEndTime() {
        return releaseEnd;
    }

    public void setReleaseTime(LocalDate releasEnd) {
        this.releaseEnd = releaseEnd;
    }

    public LocalDate getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(LocalDate removedDate) {
        this.removedDate = removedDate;
    }

    /**
     * Comparator for Node.
     *
     * @param otherNode other node you are comparing to
     */
    @Override
    public int compareTo(Object otherNode) {
        if (otherNode instanceof Node) {
            Node o = (Node) otherNode;
            return ((int) (this.id - o.id));
        } else {
            return -1;
        }
    }

}