package com.ffvreborn.core;
import java.util.*;
public final class JobService {
    private final Map<String, JobDefinition> jobs;
    public JobService(Collection<JobDefinition> definitions) {
        Map<String, JobDefinition> map = new TreeMap<>();
        for (JobDefinition d : definitions) if (map.putIfAbsent(d.id(), d) != null) throw new IllegalArgumentException("Duplicate job: " + d.id());
        if (!map.containsKey("freelancer")) throw new IllegalArgumentException("Freelancer is required");
        jobs = Map.copyOf(map);
    }
    public Collection<JobDefinition> all() { return jobs.values(); }
    public JobDefinition get(String id) { JobDefinition d=jobs.get(id); if(d==null) throw new IllegalArgumentException("Unknown job: "+id); return d; }
    public void changeJob(PlayerRpgData player, String jobId) { get(jobId); player.setActiveJob(jobId); }
    public int grantActionXp(PlayerRpgData player, long xp) { return player.progressFor(player.activeJob()).addXp(xp); }
}
