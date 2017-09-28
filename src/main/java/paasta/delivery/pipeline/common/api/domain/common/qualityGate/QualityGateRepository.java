package paasta.delivery.pipeline.common.api.domain.common.qualityGate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hrjin on 2017-06-22.
 */
@Repository
public interface QualityGateRepository extends JpaRepository<QualityGate, Long> {

    /**
     * Find all by service instances id  order by  list.
     *
     * @param serviceInstancesId
     * @return the list
     */
    List<QualityGate> findByserviceInstancesIdOrDefaultYn(String serviceInstancesId,String defaultYn);

    QualityGate findByServiceInstancesIdAndDefaultYn(String serviceInstancesId,String defaultYn);
}
