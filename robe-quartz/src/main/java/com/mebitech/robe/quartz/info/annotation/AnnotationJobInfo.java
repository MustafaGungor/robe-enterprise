package com.mebitech.robe.quartz.info.annotation;

import com.mebitech.robe.quartz.RobeJob;
import com.mebitech.robe.quartz.RobeTrigger;
import com.mebitech.robe.quartz.info.JobInfo;
import com.mebitech.robe.quartz.info.JobInfoProvider;
import com.mebitech.robe.quartz.info.TriggerInfo;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.List;

public class AnnotationJobInfo implements JobInfo {
    private String name;
    private String group;
    private String description;
    private List<TriggerInfo> triggers;
    private Class<? extends Job> jobClass;

    public AnnotationJobInfo(RobeJob ann, Class<? extends Job> jobClass) {
        name = ann.name();
        description = ann.description();
        triggers = new ArrayList<>(ann.triggers().length);
        group = ann.group();
        for (RobeTrigger tAnn : ann.triggers()) {
            triggers.add(new AnnotationTriggerInfo(tAnn));
        }
        this.jobClass = jobClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    @Override
    public void setTriggers(List<TriggerInfo> triggers) {
        this.triggers = triggers;
    }

    @Override
    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    @Override
    public Class<? extends JobInfoProvider> getProvider() {
        return AnnotationJobInfoProvider.class;
    }
}
