package com.tianqiauto.textile.weaving.controller.jihuaguanli;

import com.tianqiauto.textile.weaving.model.sys.JiHua_BuJi;
import com.tianqiauto.textile.weaving.repository.dao.DictDao;
import com.tianqiauto.textile.weaving.service.jihuaguanli.BujijihuaxiadaServer;
import com.tianqiauto.textile.weaving.service.jihuaguanli.BujijihuazhixingServer;
import com.tianqiauto.textile.weaving.util.log.Logger;
import com.tianqiauto.textile.weaving.util.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author bjw
 * @Date 2019/4/24 8:47
 */
@RestController
@RequestMapping("jihuaguanli/bujijihuazhixing")
public class BujijihuazhixingController {

    @Autowired
    private BujijihuazhixingServer bujijihuazhixingServer;

    @Autowired
    private DictDao dictDao;

    @GetMapping("query_page")
    public Result findAll(JiHua_BuJi jiHuaBuJi, @PageableDefault( sort = { "youxianji","id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        jiHuaBuJi.setStatus(dictDao.findByTypecodeAndValue("bjjh_zhaungtai","1"));
        return Result.ok(bujijihuazhixingServer.findAll(jiHuaBuJi,pageable));
    }

//    @PostMapping("update")
//    @Logger(msg = "计划管理-修改布机计划信息")
//    @ApiOperation("计划管理-修改布机计划信息")
//    public Result update(@RequestBody JiHua_BuJi jiHuaBuJi) {
//        bujijihuaxiadaServer.update(jiHuaBuJi);
//        return Result.ok("修改成功！", jiHuaBuJi);
//    }

}
