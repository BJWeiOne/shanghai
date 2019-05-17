package com.tianqiauto.textile.weaving.controller.xitongshezhi;

import com.tianqiauto.textile.weaving.model.base.Dict;
import com.tianqiauto.textile.weaving.model.base.Dict_Type;
import com.tianqiauto.textile.weaving.repository.DictRepository;
import com.tianqiauto.textile.weaving.repository.Dict_TypeRepository;
import com.tianqiauto.textile.weaving.util.result.Result;
import io.github.biezhi.excel.plus.Writer;
import io.github.biezhi.excel.plus.exception.WriterException;
import io.github.biezhi.excel.plus.writer.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Dict
 * @Description 数据字典
 * @Author xingxiaoshuai
 * @Date 2019-03-16 17:50
 * @Version 1.0
 **/

@RestController
@RequestMapping("xitongshezhi/shujuzidian")
public class DictController {


    @Autowired
    private Dict_TypeRepository dict_typeRepository;

    @Autowired
    private DictRepository dictRepository;


    public Specification getSpecification(Dict_Type dict_type) {
        return (Specification<Dict_Type>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            if (!StringUtils.isEmpty(dict_type.getName())) {
                predicates.add(criteriaBuilder
                        .like(root.get("name"), "%" + dict_type.getName() + "%"));
            }
            if (!StringUtils.isEmpty(dict_type.getCode())) {
                predicates.add(criteriaBuilder
                        .like(root.get("code"), "%" + dict_type.getCode() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    @GetMapping("query_page")
    public Result query_page(Dict_Type dict_type, Pageable pageable) {
        return Result.ok(dict_typeRepository.findAll(getSpecification(dict_type), pageable));
    }


    @GetMapping("edit")
    public Result edit(Dict_Type dict_type) {

        Dict_Type former = dict_typeRepository.getOne(dict_type.getId());

        former.setCode(dict_type.getCode());
        former.setName(dict_type.getName());

        former.setFixed(dict_type.getFixed());


        return Result.ok("修改成功", dict_typeRepository.save(former));

    }

    @GetMapping("edit_fixed")
    public Result edit(String id, String value) {

        Dict_Type former = dict_typeRepository.getOne(Long.parseLong(id.trim()));

        former.setFixed(Integer.parseInt(value));

        return Result.ok("修改成功", dict_typeRepository.save(former));

    }


    @GetMapping("export")
    public void export(Dict_Type dict_type, HttpServletResponse servletResponse) throws WriterException {
        Writer.create()
                .sheet("数据字典类别")
                .headerTitle("数据字典类别")
                .withRows(dict_typeRepository.findAll(getSpecification(dict_type)))
                .to(ResponseWrapper.create(servletResponse, "数据字典类别.xls"));
    }


    //通用方法 select下拉数据加载
    @GetMapping("formSelect")
    public Result formSelect(String code) {
        return Result.ok(dict_typeRepository.findByCode(code));
    }


    //获取数据字典类别中的所有code
    @GetMapping("getcodes")
    public Result getCodes() {
        return Result.ok(dict_typeRepository.findAll());

    }

    @PostMapping("upd_dict")
    @ApiOperation(value = "修改数据字典的子类")
    public Result upd_dict(@RequestBody Dict dict) {
        //判断数据字典名是否存在
        int num = 0;
        if(StringUtils.isEmpty(dict.getId())){
            num = dictRepository.existsByNameAndDictType(dict.getName().trim(), dict.getDict_type_id());
        }else{
            num = dictRepository.existsByNameAndDictType(dict.getName().trim(), dict.getDict_type_id(), dict.getId());
        }
        if (num==0) {
            Dict_Type dict_type = new Dict_Type();
            dict_type.setId(dict.getDict_type_id());
            dict.setDict_type(dict_type);
            dictRepository.save(dict);
            return Result.ok("操作成功",dict);
        } else {
            return Result.result(666, dict.getName() + "已存在", dict);
        }
    }

    @PostMapping("del_dict")
    @ApiOperation(value = "删除数据字典子类")
    public Result del_dict(@RequestBody Dict dict){
        dictRepository.delete(dict);
        return Result.ok("删除成功",dict);
    }

    @GetMapping("query_dict")
    @ApiOperation(value = "根据dictType id查询dict")
    public Result query_dict(Long dict_type_id){
        List<Dict> dicts = dictRepository.findDictsByDict_type(dict_type_id);
        return Result.ok(dicts);
    }


}