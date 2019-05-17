package com.tianqiauto.textile.weaving.repository;

import com.tianqiauto.textile.weaving.model.sys.Beam_JingZhou;
import com.tianqiauto.textile.weaving.model.sys.Beam_JingZhou_Current;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName BeamjingzhoucurrentRepository
 * @Description TODO
 * @Author lrj
 * @Date 2019/5/16 16:35
 * @Version 1.0
 **/
public interface BeamjingzhoucurrentRepository extends JpaRepository<Beam_JingZhou_Current,Long>,JpaSpecificationExecutor<Beam_JingZhou_Current> {

    Beam_JingZhou_Current findByJingZhou(Beam_JingZhou beam_jingZhou);

}
