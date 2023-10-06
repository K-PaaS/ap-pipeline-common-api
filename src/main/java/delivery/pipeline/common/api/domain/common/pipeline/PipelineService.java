package delivery.pipeline.common.api.domain.common.pipeline;

import delivery.pipeline.common.api.common.CommonService;
import delivery.pipeline.common.api.common.Constants;
import delivery.pipeline.common.api.common.RestTemplateService;
import delivery.pipeline.common.api.domain.common.authority.GrantedAuthority;
import delivery.pipeline.common.api.domain.common.authority.GrantedAuthorityService;
import delivery.pipeline.common.api.domain.common.job.Job;
import delivery.pipeline.common.api.domain.common.job.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by user on 2017-05-18.
 */
@Service
public class PipelineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineService.class);
    private final CommonService commonService;
    private final JobService jobService;
    private final PipelineRepository pipelineRepository;
    private final GrantedAuthorityService grantedAuthorityService;
    private final RestTemplateService restTemplateService;

    @Autowired
    public PipelineService(CommonService commonService, JobService jobService, PipelineRepository pipelineRepository, GrantedAuthorityService grantedAuthorityService, RestTemplateService restTemplateService) {
        this.commonService = commonService;
        this.jobService = jobService;
        this.pipelineRepository = pipelineRepository;
        this.grantedAuthorityService = grantedAuthorityService;
        this.restTemplateService = restTemplateService;
    }


    public PipelineList getPipelineList(String suid, String reqName, Pageable pageable) {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LOGGER.info("  - PageNumber :: {}", pageable.getPageNumber());
        LOGGER.info("  - PageSize :: {}", pageable.getPageSize());
        LOGGER.info("  - Sort :: {}", pageable.getSort());
        LOGGER.info("  - Offset :: {}", pageable.getOffset());
        LOGGER.info("  - HasPrevious :: {}", pageable.hasPrevious());
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        PipelineList resultList;
        Page<Pipeline> pipelineListPage;

        if (reqName == null || "".equals(reqName)) {
            pipelineListPage = pipelineRepository.findByServiceInstancesId(suid, pageable);
        } else {
            pipelineListPage = pipelineRepository.findByServiceInstancesIdAndNameContaining(suid, reqName, pageable);
        }

        resultList = (PipelineList) commonService.setPageInfo(pipelineListPage, new PipelineList());
        resultList.setPipelines(pipelineListPage.getContent());

        return resultList;
    }


    public Pipeline getPipeline(Long id) {
        Pipeline getPipeline = pipelineRepository.findOne(id);
        return getPipeline;
    }

    public Pipeline createPipeline(@RequestBody Pipeline reqPipeline) {
        Pipeline createPipeline = pipelineRepository.save(reqPipeline);
        return createPipeline;
    }

    public Pipeline updatePipeline(Long id, @RequestBody Pipeline pipeline) {
        Pipeline modifyPipeline = pipelineRepository.findOne(id);
        modifyPipeline.setName(pipeline.getName());
        modifyPipeline.setDescription(pipeline.getDescription());
        return pipelineRepository.save(modifyPipeline);
    }

    public String deletePipeline(Long id) {

        List<GrantedAuthority> deleteGrantedAuthorities = grantedAuthorityService.findByAuthCode(id);
        grantedAuthorityService.deleteGrantedAuthorityRows(deleteGrantedAuthorities);

        pipelineRepository.delete(id);
        return Constants.RESULT_STATUS_SUCCESS;
    }

    public String setDeletePipeline(int pipelineId) {

        deletePipeline((long) pipelineId);

        // GET JOB LIST BY PIPELINE ID
        Job job = new Job();
        job.setPipelineId(pipelineId);
        String jobType = job.getJobType();

        // GETS DB JOB LIST
        List<Job> jobList = null;
        if (job.getPipelineId() != 0) {
            jobList = jobService.getJobListByPipelineIdOrderByGroupOrderAscJobOrderAsc(job.getPipelineId());

            if (null != jobType && !"".equals(jobType)) {
                jobList = jobService.getJobListPageable(null, job.getPipelineId(), jobType);
            }
        }

        // DELETE JOB INCLUDE JOB HISTORY
        for (int i = 0; i < jobList.size(); i++) {
            long jobId = jobList.get(i).getId();
            Job reqJob = new Job();
            reqJob.setId(jobId);

            restTemplateService.send(Constants.TARGET_DELIVERY_PIPELINE_API, "/jobs/" + reqJob.getId(), HttpMethod.DELETE, null, Job.class);
        }

        return Constants.RESULT_STATUS_SUCCESS;
    }

}
