package com.absmis.repository.enterprise;

import com.absmis.JpaRepository.MyRepository;
import com.absmis.domain.enterprise.DesignerQualification;
import org.springframework.stereotype.Repository;




@Repository
public interface DesignerQualificationRepository extends MyRepository<DesignerQualification, Long> {
}
