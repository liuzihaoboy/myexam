var paper = {
    keys:null,
    i:0,
    sc:null,
    test_jiaojuan: function () {
        if(confirm("是否交卷？")){
            $.mask_fullscreen_submit();
            paper.get_key();
            $.ajax({
                url: '../submit/' + $("#paperId").val() + "/" + $("#tid").val() + ".json",
                method: 'post',
                data: {
                    'optKey': JSON.stringify(paper.keys)
                },
                success: function (res) {
                    if(res.status){
                        $.mask_close_all();
                        $('.alt-1').countdown('pause');
                        $.removeCookie('save_key',{path:'/user'});
                        alert('交卷成功');
                        window.location.href='../../detail/'+$("#paperId").val()+".html"
                    }else{
                        $.mask_close_all();
                        alert('交卷失败，'+res.msg);
                    }
                },
                error:function (xhr,msg) {
                    $.mask_close_all();
                    alert('连接主机失败！');
                }
            })
        }
    },
    end_jiaojuan:function(data){
        if(data==0){
            $.mask_fullscreen_submit();
            paper.get_key();
            $.ajax({
                url: '../submit/' + $("#paperId").val() + "/" + $("#tid").val() + ".json",
                method: 'post',
                data: {
                    'optKey': JSON.stringify(paper.keys)
                },
                success: function (res) {
                    if(res.status){
                        $.mask_close_all();
                        $.removeCookie('save_key',{path:'/user'});
                        alert('交卷成功');
                        window.location.href='../../detail/'+$("#paperId").val()+".html"
                    }else{
                        $.mask_close_all();
                        alert('交卷失败，'+res.msg);
                    }
                },
                error:function (xhr,msg) {
                    $.mask_close_all();
                    alert('连接主机失败！');
                }
            })
        }
    },
    send_cookie :function () {
        if($.cookie('save_key')!=null){
            $.ajax({
                url:'../save/'+$("#paperId").val()+"/"+$("#tid").val()+".json",
                method:'post',
                data:{
                    'saveKey':$.cookie('save_key')
                },
                error:function (xhr,msg) {
                    alert('连接主机失败！');
                }
            })
        }
    },
    save_opt:function (orderId,key) {
        paper.keys[orderId]=key;
        $.cookie('save_key',JSON.stringify(paper.keys),{path:'/user'})
    },
    sleep:function (numberMillis) {
        var now = new Date();
        var exitTime = now.getTime() + numberMillis;
        while (true) {
            now = new Date();
            if (now.getTime() > exitTime)
                return;
        }
    },
    get_key:function () {
        var i=0;
        $('li.option input').each(function() {
            var li =$(this).closest('.test_content_nr_main').closest('li');
            var key = [];
            var texts;
            if($(this).attr('type')=='text'){
                texts = $(this).parent().parent().find('input[type=text]');
                for (var i=0;i<texts.length;i++){
                    var text = texts.eq(i).val();
                    key.push(text);
                }
            }else if($(this).attr('type')=='checkbox'){
                texts = $(this).parent().parent().find('input[type=checkbox]');
                var flag=false;
                $(texts).each(function () {
                    if($(this).is(':checked')){
                        flag=true;
                        key.push($(this).val());
                    }
                });
            }else{
                key.push($(this).parent().parent().find('input[type=radio]:checked').val());
            }
        });
    }
};
$(function() {
    $.mask_fullscreen();
    var random=Math.floor((Math.random()*1000+Math.random()*1000)+1000);
    setTimeout(test_get(),random);
    function test_get() {
        $.ajax({
            url:'../start/'+$("#paperId").val()+"/"+$("#tid").val()+".json",
            method:'post',
            success:function (res) {
                if(res.status){
                    var questions = res.data.questions;
                    var endTime = new Date(res.data.endTime);
                    if($.cookie('save_key')!=null){
                        paper.keys = JSON.parse($.cookie('save_key'));
                    }else if(res.data.saveKey!=null){
                        paper.keys = JSON.parse(res.data.saveKey);
                        $.cookie('save_key',JSON.stringify(paper.keys),{path:'/user'})
                    }else{
                        paper.keys = new Array(questions.length);
                    }
                    var score=0;
                    for (var i=0;i<questions.length;i++){
                        var question =questions[i];
                        var id=question.id;
                        var key = paper.keys[i];
                        var keyFlag = key!=null&&key.length!==0;
                        var name="qu_opt_"+id;
                        score+=question.questionScore;
                        var html = "<li  id=\"qu_"+id+"\" order='"+i+"'>\n" +
                            "                                    <div class=\"test_content_nr_tt\">\n" +
                            "                                        <i>"+(i+1)+"</i><span>("+question.questionTypeName+")</span><span>("+question.questionScore+"分)</span><font>"+question.questionContent+"</font>\n" +
                            "                                    </div>\n" +
                            "                                    <div class=\"test_content_nr_main\">\n" +
                            "                                        <ul id=\""+name+"\">\n" +
                            "                                            \n" +
                            "                                        </ul>\n" +
                            "                                    </div>\n" +
                            "                                </li>";
                        $("#qu-list").append(html);
                        $("#qu-num").append("<li><a href=\"#qu_"+id+"\">"+(i+1)+"</a></li>");
                        var opts = question.opts;
                        var type = question.questionType;
                        var opt_html="";
                        if(type == 3 ){
                            opt_html="<li class=\"option\">\n" +
                                "                                                <input type=\"radio\" class=\"radioOrCheck\" value='T' name=\""+name+"\"\n" +
                                "                                                       id=\""+name+"_T\"\n" +
                                "                                                />\n" +
                                "                                                <label for=\""+name+"_T\">\n" +
                                "                                                    1.\n" +
                                "                                                    <p class=\"ue\" style=\"display: inline;\">正确</p>\n" +
                                "                                                </label>\n" +
                                "                                            </li><li class=\"option\">\n" +
                                "                                                <input type=\"radio\" class=\"radioOrCheck\" value='F' name=\""+name+"\"\n" +
                                "                                                       id=\""+name+"_F\"\n" +
                                "                                                />\n" +
                                "                                                <label for=\""+name+"_F\">\n" +
                                "                                                    2.\n" +
                                "                                                    <p class=\"ue\" style=\"display: inline;\">错误</p>\n" +
                                "                                                </label>\n" +
                                "                                            </li>"
                            $("#"+name).append(opt_html);
                            if(keyFlag){
                                var optKey = key[0];
                                if(optKey!=null&&optKey!=''){
                                    $("input:radio[name='"+name+"'][value='"+optKey+"']").attr("checked",true);
                                    $("a[href='#qu_" + id + "']").addClass('hasBeenAnswer');
                                }
                            }
                        }else if(type == 4){
                            var blankNum = question.blankNum;
                            for (var j=0;j<blankNum;j++){
                                opt_html = "                        <li class=\"option\">\n" +
                                    "                            <input type=\"text\" style='float: none;width: 750px;' class=\"tm_txt\" name=\""+name+"\"\n" +
                                    "                                   id=\""+name+"_"+(j+1)+"\"\n" +
                                    "                              />\n" +
                                    "                            <label for=\""+name+"_"+(j+1)+"\">\n" +
                                    "                            </label>\n" +
                                    "                        </li>";
                                $("#"+name).append(opt_html);
                            }
                            if(keyFlag){
                                var texts = $("input:text[name='"+name+"']");
                                var flag = true;
                                for (var j=0;j<key.length;j++){
                                    var optKey = key[j];
                                    if(optKey!=null&&optKey!=''){
                                        texts.eq(j).val(optKey);
                                    }else if(flag){
                                        flag=false;
                                    }
                                }
                                if(flag){
                                    $("a[href='#qu_" + id + "']").addClass('hasBeenAnswer');
                                }
                            }
                        }else if(type==1){
                            for(var j=0;j<opts.length;j++){
                                var opt = opts[j];
                                opt_html = "                        <li class=\"option\">\n" +
                                    "                            <input type=\"radio\" class=\"radioOrCheck\" value='"+opt.id+"' name=\""+name+"\"\n" +
                                    "                                   id=\""+name+"_"+opt.id+"/>\n" +
                                    "                            <label for=\""+name+"_"+opt.id+"\">\n" +
                                    "                                "+(j+1)+".\n" +
                                    "                                <p class=\"ue\" style=\"display: inline;\">"+opt.optionContent+"</p>\n" +
                                    "                            </label>\n" +
                                    "                        </li>";
                                $("#"+name).append(opt_html);
                            }
                            if(keyFlag){
                                var optKey = key[0];
                                if(optKey!=null&&optKey!=''){
                                    $("input:radio[name='"+name+"'][value='"+optKey+"']").attr("checked",true);
                                    $("a[href='#qu_" + id + "']").addClass('hasBeenAnswer');
                                }
                            }
                        }else if(type==2){
                            for(var j=0;j<opts.length;j++) {
                                var opt = opts[j];
                                opt_html = "                        <li class=\"option\">\n" +
                                    "                            <input type=\"checkbox\" class=\"radioOrCheck\" value='"+opt.id+"' name=\""+name+"\"\n" +
                                    "                                   id=\""+name+"_"+opt.id+"/>\n" +
                                    "                            <label for=\""+name+"_"+opt.id+"\">\n" +
                                    "                                "+(j+1)+".\n" +
                                    "                                <p class=\"ue\" style=\"display: inline;\">"+opt.optionContent+"</p>\n" +
                                    "                            </label>\n" +
                                    "                        </li>";
                                $("#"+name).append(opt_html);
                            }
                            if(keyFlag){
                                var flag = true;
                                for (var j=0;j<key.length;j++){
                                    var optKey = key[j];
                                    if(optKey!=null&&optKey!=''){
                                        $("input:checkbox[name='"+name+"'][value='"+optKey+"']").attr("checked",true);
                                    }else if(flag){
                                        flag=false;
                                    }
                                }
                                if(flag){
                                    $("a[href='#qu_" + id + "']").addClass('hasBeenAnswer');
                                }
                            }
                        }
                    }
                    $(".content_lit").html(questions.length);
                    $(".content_fs").html(score);
                    $('li.option input').on('change',function() {
                        var li =$(this).closest('.test_content_nr_main').closest('li');
                        var examId =$(li) .attr('id'); // 得到题目ID
                        var orderId = $(li).attr('order');
                        var key = [];
                        var cardLi = $("a[href='#" + examId + "']"); // 根据题目ID找到对应答题卡
                        var texts;
                        if($(this).attr('type')=='text'){
                            texts = $(this).parent().parent().find('input[type=text]');
                            var flag = true;
                            for (var i=0;i<texts.length;i++){
                                var text = texts.eq(i).val();
                                key.push(text);
                                if(text==''){
                                    flag=false;
                                }
                            }
                            // 设置已答题
                            if(flag&&!cardLi.hasClass('hasBeenAnswer')){
                                cardLi.addClass('hasBeenAnswer');
                            }
                            if(!flag&&cardLi.hasClass('hasBeenAnswer')){
                                cardLi.removeClass('hasBeenAnswer');
                            }
                        }else if($(this).attr('type')=='checkbox'){
                            texts = $(this).parent().parent().find('input[type=checkbox]');
                            var flag=false;
                            $(texts).each(function () {
                                if($(this).is(':checked')){
                                    flag=true;
                                    // 设置已答题
                                    if(!cardLi.hasClass('hasBeenAnswer')){
                                        cardLi.addClass('hasBeenAnswer');
                                    }
                                    key.push($(this).val());
                                }
                            });
                            if(!flag&&cardLi.hasClass('hasBeenAnswer')){
                                cardLi.removeClass('hasBeenAnswer');
                            }
                        }else{
                            key.push($(this).parent().parent().find('input[type=radio]:checked').val());
                            // 设置已答题
                            if(!cardLi.hasClass('hasBeenAnswer')){
                                cardLi.addClass('hasBeenAnswer');
                            }
                        }
                        paper.save_opt(orderId,key);
                        paper.send_cookie();
                    });
                    $.mask_close_all();
                    $('.alt-1').countdown(endTime).on('update.countdown', function(event) {
                        var format = '%H:%M:%S';
                        if(event.offset.totalDays > 0) {
                            format = '%D天' + format;
                        }
                        $(this).html(event.strftime(format));
                    }).on('finish.countdown', function () {
                        paper.end_jiaojuan($(this).attr('data'));
                    });
                }else {
                    if(paper.i>3){
                        $.mask_close_all();
                        alert('获取题目失败，请刷新重试');
                    }else{
                        paper.sleep(1000);
                        test_get(++paper.i);
                    }
                }
            },
            error:function (xhr,msg) {
                alert('请求发生错误，请刷新重试');
            }
        });
    }

});