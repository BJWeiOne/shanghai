package com.tianqiauto.textile.weaving.caiji.PicanolLoomModule.job;

import com.tianqiauto.textile.weaving.util.procedure.core.ProcedureParamUtlis;
import com.tianqiauto.textile.weaving.util.procedure.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @Author bjw
 * @Date 2019/5/17 14:55
 */
@Slf4j
@Component
@EnableScheduling
public class SysCurrentJob {

    @Autowired
    private BaseService baseService;

//    @Scheduled(fixedRate = 10000)
    private void run(){
        ProcedureParamUtlis ppu=new ProcedureParamUtlis();
        baseService.callProcedure("sys_current_job",ppu.getList());
    }
}
