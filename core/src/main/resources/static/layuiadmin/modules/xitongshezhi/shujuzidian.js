layui.define(['table', 'form'], function(exports){
        var table = layui.table
            ,form = layui.form;

    var cols =  [[
        {field: 'id', title: 'id',hide:true}
        ,{field: 'name', title: '类别名称'}
        ,{field:'code', title: '类别编码'}
        ,{field:'fixed',title:'用户不可编辑',templet: '#tpl_edit'}
        ,{title: '操作', toolbar: '#caozuo'}
    ]];

    var dict_cols = [
        {field: 'id', title: 'id',hide:true}
        ,{field: 'name', width:150, title: '名称', edit:true}
        ,{field: 'value', width:150, title: '值', edit:true}
        ,{field: 'sort', width:150, title: '排序', edit:true}
        ,{fixed: 'right',width:150, align: 'center',title: '操作',toolbar: '#dict_caozuo'}
    ];


    initTable ("table", 'xitongshezhi/shujuzidian/query_page', 'get',cols, table,"form");


    //监听搜索
    form.on('submit(form_search)', function(data){
        var field = data.field;
        table.reload('table', {
            where: field
        });
        return false;
    });

   form.on('switch(form_fixed)',function(data){
       var id = data.elem.id;
       var value = this.checked?"1":"0";
       $.ajax({
           url:layui.setter.host+'xitongshezhi/shujuzidian/edit_fixed',
           type:'get',
           data:{id:id,value:value},
           success:function(data){
               ajaxSuccess(data,table);
           }
       });
   });


    //监听工具条
    table.on('tool(table)', function(obj){
        var data = obj.data;
        var dict_type_id = data.id;
        if(obj.event === 'del'){
            layer.confirm(
                "确定要删除"+data.name+'么?',{title:'删除提示'},function (index){
                    layer.close(index);
            });
        }else if(obj.event === 'edit'){
            //子类数据初始化
            var dicts = data.dicts;
            dicts = dicts.sort(sortPXH);

            var pageDataAry = [];
            pageDataAry = $.extend(true, pageDataAry, dicts);

            if(dicts.length>0){
                table.render({
                    elem: '#dict_table'
                    ,data:dicts
                    ,limit:10000
                    ,cols: [dict_cols]
                });
            }

            layer.open({
                 type: 1
                ,title: '编辑数据字典'
                ,content: $('#div_form_edit')
                ,area: ['90%', '90%']
                ,btnAlign: 'c'
                ,btn: ['修改', '取消']
                ,btn1: function(index, layero){
                     layer.confirm('确定要修改数据字典么?'
                         ,function(i){

                         form.on('submit(form_edit_submit)', function (data) {
                             var formData = data.field;
                             if(formData.hasOwnProperty('fixed')){
                                 formData.fixed = "1";
                             }else{
                                 formData.fixed = "0";
                             }
                             formData.id=dict_type_id;

                             $.ajax({
                                 url:layui.setter.host+'xitongshezhi/shujuzidian/edit',
                                 type:'get',
                                 data:formData,
                                 success:function(data){
                                     ajaxSuccess(data,table);
                                 }
                             });

                         });

                         $("#form_edit_submit").trigger('click');
                       layer.close(i);layer.close(index);

                     })
                }
                ,success: function(){
                     form.val('form_edit',data);
                }
            });

            //监听编辑
            table.on('edit(dict_table)', function(obj) {

                var re = /^(\-|\+)?\d+(\.\d+)?$/;
                //获取表格得原值
                var yz = pageDataAry[$(this).parents('tr').index()];

                //数据不能为空
                if(obj.value == "" ){
                    layer.open({
                        title:"消息提醒",content:"字段不能为空！",skin:"layui-layer-molv",offset: 'auto',time:3000,btn:[],shade: 0,anim: -1,icon:5
                    });
                    obj.data[obj.field] = yz[obj.field];
                    return false;
                }

                //如果格式错误把原值恢复
                if(obj.value != "") {
                    if(obj.field != 'name'){
                        if(!re.test(obj.value)) {
                            layer.open({
                                title:"消息提醒",content:"格式不正确，请输入正确的数字！",skin:"layui-layer-molv",offset: 'auto',time:3000,btn:[],shade: 0,anim: -1,icon:5
                            });
                            obj.data[obj.field] = yz[obj.field];
                            return false;
                        }
                    }
                }

                // layer.confirm('确定要修改'+obj.data.name+'信息吗?',function(i){

                    //获取父类
                    var dict_obj = obj.data;
                    dict_obj.dict_type_id=dict_type_id;
                    $.ajax({
                        type: "post",
                        url: layui.setter.host+'xitongshezhi/shujuzidian/upd_dict',
                        contentType:"application/json;charset=utf-8",
                        data: JSON.stringify(dict_obj),
                        success: function(data) {
                            layer.close(i);
                            if(data.code==0){
                                if(data.code == 0){  //正确
                                    layer.open({
                                        title:"消息提醒",content:data.message,skin:"layui-layer-molv",offset: 'rb',time:3000,btn:[],shade: 0,anim: -1,icon:6
                                    });
                                }else if(data.code == 666){  //业务逻辑错误
                                    layer.open({
                                        title:"错误提示",content:data.message,skin:"layui-layer-molv",btn:["确定"],anim: -1,icon:5,
                                        btn1:function(index){
                                            layer.close(index);
                                        }
                                    });
                                }else {
                                    layer.open({      //系统异常
                                        title:"消息提醒",content:data.message,skin:"layui-layer-molv",btn:["查看错误信息"],anim: -1,icon:5,
                                        btn1:function(index){
                                            layer.open({content:data.data});
                                            layer.close(index);
                                        }
                                    });
                                }
                            }
                        }
                    });
                // });
            });

            $('#add_dict_btn').on("click", function(){
                $('#add_dict_name').val("");
                $('#add_dict_value').val("");
                $('#add_dict_sort').val("");
                layer.open({
                    type: 1,
                    title: ['添加子类', 'font-size:12px;'],
                    content: $("#div_add_dict"),
                    shade: 0.8,
                    area: ['70%', '70%'],
                    btn: ['确定', '取消'],
                    btnAlign: 'c',
                    yes: function(index, layero) {
                        if($('#add_dict_name').val()==""){
                            layer.open({
                                title:"消息提醒",content:"子类名称不能为空",skin:"layui-layer-molv",offset: 'auto',time:1500,btn:[],shade: 0,anim: -1,icon:5
                            });
                            $('#add_dict_name').focus();
                            return false;
                        }
                        if($('#add_dict_value').val()==""){
                            layer.open({
                                title:"消息提醒",content:"子类值不能为空",skin:"layui-layer-molv",offset: 'auto',time:1500,btn:[],shade: 0,anim: -1,icon:5
                            });
                            $('#add_dict_value').focus();
                            return false;
                        }
                        if($('#add_dict_sort').val()==""){
                            layer.open({
                                title:"消息提醒",content:"排序号不能为空",skin:"layui-layer-molv",offset: 'auto',time:1500,btn:[],shade: 0,anim: -1,icon:5
                            });
                            $('#add_dict_sort').focus();
                            return false;
                        }
                        layer.confirm('确定新增子类?'
                            ,function(i){
                                form.on('submit(form_add_dict_submit)', function (data) {
                                    var formData = data.field;
                                    formData.dict_type_id = dict_type_id;
                                    $.ajax({
                                        url:layui.setter.host+'xitongshezhi/shujuzidian/upd_dict',
                                        type:'post',
                                        contentType:"application/json;charset=utf-8",
                                        data:JSON.stringify(formData),
                                        success:function(data){
                                            ajaxSuccess(data,table);
                                            table.render({
                                                elem: '#dict_table'
                                                ,limit:1000
                                                ,method:'GET'
                                                ,url: layui.setter.host + 'xitongshezhi/shujuzidian/query_dict'
                                                ,cols: [dict_cols]
                                                ,where:{dict_type_id:dict_type_id}
                                            });
                                            layer.close(i);layer.close(index);
                                        }
                                    });
                                });
                                $("#form_add_dict_submit").trigger('click');
                            });
                    }
                });

            });

            table.on("tool(dict_table)",function(obj){
                var data = obj.data;
                if(obj.event === 'del'){
                    layer.confirm(
                        "确定要删除"+data.name+'么?',function(i){
                            $.ajax({
                                url:layui.setter.host+'xitongshezhi/shujuzidian/del_dict',
                                type:'post',
                                contentType:"application/json;charset=utf-8",
                                data:JSON.stringify(data),
                                success:function(data){
                                    ajaxSuccess(data,table);
                                    table.render({
                                        elem: '#dict_table'
                                        ,limit:1000
                                        ,method:'GET'
                                        ,url: layui.setter.host + 'xitongshezhi/shujuzidian/query_dict'
                                        ,cols: [dict_cols]
                                        ,where:{dict_type_id:dict_type_id}
                                    });
                                    layer.close(i);
                                }
                            });
                        });
                }
            });


        }
    });



    //根据sort排序
    function sortPXH(a,b){
        return a.sort-b.sort;
    }

    exports('shujuzidian', {})
});