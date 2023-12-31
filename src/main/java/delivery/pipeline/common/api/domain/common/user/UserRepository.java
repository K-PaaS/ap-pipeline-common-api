package delivery.pipeline.common.api.domain.common.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * apDeliveryPipelineApi
 * delivery.pipeline.common.api.user
 *
 * @author REX
 * @version 1.0
 * @since 5/11/2017
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Page<User> findByNameContaining(String reqName, Pageable pageable);
}
