package paasta.delivery.pipeline.common.api.domain.common.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paasta.delivery.pipeline.common.api.common.Constants;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by hrjin on 2017-06-23.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    //시연후 수정
    public List<Project> getProjectsList(Project project) {
        return projectRepository.findByserviceInstancesId(project.getServiceInstancesId());
    }

    public List<Project> getProject(Project project){
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

    public Project updateProject(Project project) {
        Project result = new Project();
        result = projectRepository.findOne(project.getId());

        result.setProjectName(project.getProjectName());
        result.setQualityProfileId(project.getQualityProfileId());
        result.setQualityGateId(project.getQualityGateId());
        result.setJobId(project.getJobId());

        return projectRepository.save(result);
    }


    public Project qualityGateProjectLiked(Project project) {
        Project result = new Project();
        if (project.getLinked().equals(false)) {
            project.setQualityGateId(0);
        }

        result = projectRepository.findOne(project.getId());
        result.setQualityGateId(project.getQualityGateId());

        return projectRepository.save(result);
    }


    public Project qualityProfileProjectLiked(Project project) {
        Project result = new Project();
        if (project.getLinked().equals(false)) {
            project.setQualityProfileId(0);
        }

        result = projectRepository.findOne(project.getId());
        result.setQualityProfileId(project.getQualityProfileId());

        return projectRepository.save(result);
    }

    public String qualityProfileDelete(Project project){
        List<Project> result = new ArrayList<>();
        int profileId = (int)(long)project.getId();
        project.setQualityProfileId(profileId);
        result = projectRepository.findByServiceInstancesIdAndQualityProfileId(project.getServiceInstancesId(), project.getQualityProfileId());

        if(result.size() > 0){
            for(int i=0;i<result.size();i++){
                result.get(i).setQualityProfileId(0);
                projectRepository.save(result.get(i));
            }
        }

        return Constants.RESULT_STATUS_SUCCESS;
    }

    public String qualityGateDelete(Project project){
        List<Project> result = new ArrayList<>();
        int gateId = (int)(long)project.getId();
        project.setQualityGateId(gateId);

        result = projectRepository.findByServiceInstancesIdAndQualityGateId(project.getServiceInstancesId(), project.getQualityGateId());

        if(result.size() > 0){
            for(int i = 0; i<result.size(); i++){
                result.get(i).setQualityGateId(0);
                projectRepository.save(result.get(i));
            }
        }
        return Constants.RESULT_STATUS_SUCCESS;
    }

    //projectKey 값 가져오기
    public Project getProjectKey(Project project){
        return projectRepository.findOne(project.getId());
    }


}
