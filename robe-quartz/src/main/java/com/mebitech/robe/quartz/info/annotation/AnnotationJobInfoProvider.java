package com.mebitech.robe.quartz.info.annotation;

import com.mebitech.robe.quartz.RobeJob;
import com.mebitech.robe.quartz.info.JobInfo;
import com.mebitech.robe.quartz.info.JobInfoProvider;
import org.quartz.Job;

/**
 * A Util class that helps converting annotations to quartz objects.
 */
public class AnnotationJobInfoProvider extends JobInfoProvider {

    @Override
    public JobInfo getJob(Class<? extends Job> clazz) {
        RobeJob jAnn = clazz.getAnnotation(RobeJob.class);
        if (jAnn == null)
            return null;// TODO throw exception ?
        return new AnnotationJobInfo(jAnn, clazz);
    }
}
