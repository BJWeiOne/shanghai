package com.tianqiauto.textile.weaving.repository;

import com.tianqiauto.textile.weaving.model.base.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DictRepository extends JpaRepository<Dict,Long>, JpaSpecificationExecutor<Dict> {

    @Modifying
    @Transactional
    @Query(value = "update base_dict set value=?1,sort=?2 where id=?3",nativeQuery = true)
    void updateDict(String value, Integer sort, Long id);

    @Query(value = "select count(*) from base_dict where name=?1 and type_id=?2 and id !=?3",nativeQuery = true)
    int existsByNameAndDictType(String name, Long id,Long dict_id);

    @Query(value = "select count(*) from base_dict where name=?1 and type_id=?2",nativeQuery = true)
    int existsByNameAndDictType(String name,Long id);

    @Query(value = "select * from base_dict where type_id=?1 order by sort",nativeQuery = true)
    List<Dict> findDictsByDict_type(Long dict_type_id);

}
