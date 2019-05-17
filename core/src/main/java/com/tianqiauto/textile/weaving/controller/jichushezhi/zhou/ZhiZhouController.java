package com.tianqiauto.textile.weaving.controller.jichushezhi.zhou;

import com.tianqiauto.textile.weaving.model.base.Dict;
import com.tianqiauto.textile.weaving.model.sys.Beam_ZhiZhou;
import com.tianqiauto.textile.weaving.model.sys.Beam_ZhiZhou_Current;
import com.tianqiauto.textile.weaving.repository.BeamzhizhoucurrentRepository;
import com.tianqiauto.textile.weaving.repository.ZhiZhouRepository;
import com.tianqiauto.textile.weaving.service.dingdanguanli.OrderService;
import com.tianqiauto.textile.weaving.util.copy.MyCopyProperties;
import com.tianqiauto.textile.weaving.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ZhiZhouController
 * @Description TODO
 * @Author lrj
 * @Date 2019/3/21 10:31
 * @Version 1.0
 **/

@RestController
@RequestMapping("jichushuju/zhou/zhizhou")
@Api(description = "经轴管理")
public class ZhiZhouController {

    @Autowired
    ZhiZhouRepository zhiZhouRepository;

    @Autowired
    BeamzhizhoucurrentRepository beamzhizhoucurrentRepository;

    @Autowired
    OrderService orderService;

    @GetMapping("findAllZhiZhou")
    @ApiOperation(value = "查询织轴信息")
    public Result findAllZhiZhou(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.Direction.ASC, "zhouhao");
        Page<Beam_ZhiZhou> list = zhiZhouRepository.findAll(pageRequest);
        return Result.ok("查询成功!",list);
    }

    @GetMapping(value = "findAllZZBH")
    @ApiOperation(value = "查询织轴编号")
    public Result findAllZZBH(){
        List<Beam_ZhiZhou> list = zhiZhouRepository.findAll();
        return Result.ok("查询成功!",list);
    }

    @PostMapping("updateZhiZhou")
    @ApiOperation(value = "修改织轴信息")
    public Result updateZhiZhou(@RequestBody Beam_ZhiZhou zhiZhou){
        Beam_ZhiZhou zhiZhouDB = zhiZhouRepository.getOne(zhiZhou.getId());
        MyCopyProperties.copyProperties(zhiZhou,zhiZhouDB, Arrays.asList("zhouhao","jixing","zhoukuan","beizhu"));
        zhiZhouRepository.save(zhiZhouDB);
        return Result.ok("修改成功",zhiZhou);
    }

    @PostMapping("addZhiZhou")
    @ApiOperation(value = "新增织轴")
    public Result addZhiZhou(@RequestBody Beam_ZhiZhou zhiZhou){
        Boolean flag = zhiZhouRepository.existsByZhouhao(zhiZhou.getZhouhao());
        if(!flag){
            Beam_ZhiZhou new_zhizhou = zhiZhouRepository.save(zhiZhou);
            Beam_ZhiZhou_Current cur_zhizhou = new Beam_ZhiZhou_Current();
            cur_zhizhou.setZhizhou(new_zhizhou);

            Dict dict = orderService.findByTypenameAndValue("zhizhouzhuangtai","10");
            cur_zhizhou.setStatus(dict);
            beamzhizhoucurrentRepository.save(cur_zhizhou);
            return Result.ok("新增成功",zhiZhou);
        }else{
            return Result.result(666,"织轴号已存在",zhiZhou);
        }
    }

    @PostMapping("deleteZhiZhou")
    @ApiOperation(value = "删除织轴")
    public Result deleteZhiZhou(@RequestBody Beam_ZhiZhou zhiZhou){
        Beam_ZhiZhou_Current cur_zhizhou = beamzhizhoucurrentRepository.findByZhizhou(zhiZhou);
        beamzhizhoucurrentRepository.delete(cur_zhizhou);
        zhiZhouRepository.delete(zhiZhou);
        return Result.ok("删除成功",zhiZhou);
    }

}
