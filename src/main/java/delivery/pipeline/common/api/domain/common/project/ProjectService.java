package delivery.pipeline.common.api.domain.common.project;

import delivery.pipeline.common.api.common.Constants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by hrjin on 2017-06-23.
 */
@Service
public class ProjectService {
    private final Logger LOGGER = getLogger(getClass());

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    //시연후 수정
    public List<Project> getProjectsList(Project project) {
        return projectRepository.findByserviceInstancesId(project.getServiceInstancesId());
    }

    public List<Project> getProject(Project project) {
        return projectRepository.findByserviceInstancesIdAndPipelineId(project.getServiceInstancesId(), project.getPipelineId());
    }

    public Project createProjects(Project project) {
        return projectRepository.save(project);
    }

    public Project deleteProject(Project project) {
        projectRepository.delete(project.getId());
        project.setResultStatus(Constants.RESULT_STATUS_SUCCESS);
        return project;
    }

    // TODO
//    public Project updateProject(Project project) {
//        Project result = new Project();
//        result = projectRepository.findOne(project.getId());
//
//        result.setProjectName(project.getProjectName());
//        // TODO
////        result.setQualityProfileId(project.getQualityProfileId());
//        result.setQualityGateId(project.getQualityGateId());
//        result.setJobId(project.getJobId());
//
//        return projectRepository.save(result);
//    }


    public Project setqualityGateProjectLiked(Project project) {

        Project result = projectRepository.findOne(project.getId());
        if(!project.getLinked()){
            LOGGER.info("GATE REMOVE");
            result.setQualityGateId(0);
        }else{
            LOGGER.info("GATE UPDATE");
            result.setQualityGateId(project.getQualityGateId());
        }
        return projectRepository.save(result);
    }



    public Project qualityGateProjectLiked(Project project) {
        Project result = new Project();
        if (project.getLinked().equals(false)) {
            project.setQualityGateId(0);
        }

        result = projectRepository.findOne(project.getId());

        return projectRepository.save(result);
    }



    public Project qualityProfileProjectLiked(Project project) {
        Project result = new Project();
        LOGGER.info("COMMON : " + project.getId());;
        result = projectRepository.findOne(project.getId());
        //result.setQualityProfileKey(project.getQualityProfileKey());

        return projectRepository.save(result);
    }

    public String qualityProfileDelete(Project project) {
        // TODO
        project.setId(1L);
//        List<Project> result = new ArrayList<>();
//        int profileId = (int)(long)project.getId();
//        project.setQualityProfileId(profileId);
//        result = projectRepository.findByServiceInstancesIdAndQualityProfileId(project.getServiceInstancesId(), project.getQualityProfileId());
//
//        if(result.size() > 0){
//            for(int i=0;i<result.size();i++){
//                result.get(i).setQualityProfileId(0);
//                projectRepository.save(result.get(i));
//            }
//        }
//
        return Constants.RESULT_STATUS_SUCCESS;
    }

    public String qualityGateDelete(Project project) {
        List<Project> result = new ArrayList<>();
        int gateId = (int) (long) project.getId();
        project.setQualityGateId(gateId);

        result = projectRepository.findByServiceInstancesIdAndQualityGateId(project.getServiceInstancesId(), project.getQualityGateId());

        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                result.get(i).setQualityGateId(0);
                projectRepository.save(result.get(i));
            }
        }
        return Constants.RESULT_STATUS_SUCCESS;
    }

    //projectKey 값 가져오기
    public Project getProjectKey(Project project) {
        return projectRepository.findOne(project.getId());
    }


    /**
     * Sets update project.
     *
     * @param project the project
     * @return the update project
     */
    Project setUpdateProject(Project project) {
        Project projectDetail = projectRepository.findOne(project.getId());

        projectDetail.setQualityProfileKey(project.getQualityProfileKey());
        projectDetail.setQualityGateId(project.getQualityGateId());
        projectDetail.setJobId(project.getJobId());

        return projectRepository.save(projectDetail);
    }


    /**
     * Get project by id project.
     *
     * @param id the id
     * @return the project
     */
    Project getProjectById(long id) {
        return projectRepository.findOne(id);
    }

}
