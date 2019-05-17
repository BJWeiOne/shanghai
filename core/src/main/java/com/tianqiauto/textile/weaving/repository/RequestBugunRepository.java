package com.tianqiauto.textile.weaving.repository;

import com.tianqiauto.textile.weaving.model.sys.BuGun;
import com.tianqiauto.textile.weaving.model.sys.Request_BuGun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author bjw
 * @Date 2019/3/19 15:50
 */
public interface RequestBugunRepository extends JpaRepository<Request_BuGun,Long> ,JpaSpecificationExecutor<Request_BuGun> {

}
