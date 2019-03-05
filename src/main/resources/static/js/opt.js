function qStatusInfo(value) {
    if(value==0||value=="0"){
        $("#qStatusInfo").html("学生练习可用");
    }else {
        $("#qStatusInfo").html("学生练习不可用");
    }
}
function addOption(qType,flag) {
    var tr="";
    var trList = $("#optTable tr");
    var length=trList.length+1;
    var tdArr = trList.eq(trList.length-1).find("td").eq(1);
    var aArr = tdArr.find("a").eq(0);
    aArr.remove();
    if(qType==1){
        tr =  "                    <tr>\n" +
            "                        <td width=\"10%\"><span>选项"+(length)+"&nbsp;</span>\n" +
            "                            <input type=\"radio\"  name=\"q_key\" value=':"+length+"'>\n" +
            "                            <input type='hidden' name=\"orderNum\" value='"+length+"'/>\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" optId=':"+length+"' name=\"q_options\" class=\"tm_txtx\"></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                            <a href=\"javascript:;\" onclick=\"remove('"+qType+"',this)\" class=\"tm_ico_delete\"></a>\n" +
            "                        </td>\n" +
            "                    </tr>\n";
    }else if(qType==2){
        tr = "                    <tr>\n" +
            "                        <td width=\"10%\"><span>选项"+(length)+"&nbsp;</span>\n" +
            "                            <input type=\"checkbox\"  name=\"q_key\" value=':"+length+"'>\n" +
            "                            <input type='hidden' name=\"orderNum\" value='"+length+"'/>\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" optId=':"+length+"' name=\"q_options\" class=\"tm_txtx\"></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                            <a href=\"javascript:;\" onclick=\"remove('"+qType+"',this)\" class=\"tm_ico_delete\"></a>\n" +
            "                        </td>\n" +
            "                    </tr>\n";
    }else if(qType==4){
        tr = "                    <tr>\n" +
            "                        <td width=\"10%\"><span>填空"+(length)+"&nbsp;</span>\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" name=\"q_key\" class=\"tm_txtx\"></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                            <a href=\"javascript:;\" onclick=\"remove('"+qType+"',this)\" class=\"tm_ico_delete\"></a>\n" +
            "                        </td>\n" +
            "                    </tr>\n";
    }
    $("#optTable tbody:last").append(tr);
    if(qType==4&&flag){
        $("#qContent").val($("#qContent").val()+"（____）");
    }
}
function remove(qType,node) {
    var msg = true;
    if(qType==4){
        msg = confirm("请注意删除题干填空项！");
    }
    if(msg){
        $(node).parent().parent().remove();
        var trList = $("#optTable tr");
        if(trList.length>1){
            var tdArr = trList.eq(trList.length-1).find("td").eq(1);
            tdArr.html(" <a href=\"javascript:;\" onclick=\"remove('"+qType+"',this)\" class=\"tm_ico_delete\"></a>");
        }
    }
}
function optTable(qType,opts,keys) {
    var table ="";
    if(qType==1){
        table = "<label class=\"control-label col-sm-3\">选项设置</label>\n" +
            "        <div class=\"col-sm-7\">\n" +
            "            <div>\n" +
            "                <input type=\"button\" class=\"tm_btn\" value=\"增加选项\" onclick=\"addOption("+qType+",true)\">\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <table id='optTable' class=\"tm_question_options\" style=\"width: 100%;text-align: center;\">\n" +
            "                    <tr>\n" +
            "                        <td width=\"10%\">选项1&nbsp;\n" +
            "                            <input type=\"radio\"  name=\"q_key\">\n" +
            "                            <input type='hidden' name=\"orderNum\" value='1'/>\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" optId='1' name=\"q_options\" class=\"tm_txtx\"></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "            </div>\n" +
            "        </div>";
    }else if(qType==2){
        table = "<label class=\"control-label col-sm-3\">选项设置</label>\n" +
            "        <div class=\"col-sm-7\">\n" +
            "            <div>\n" +
            "                <input type=\"button\" class=\"tm_btn\" value=\"增加选项\" onclick=\"addOption("+qType+",true)\">\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <table id='optTable' class=\"tm_question_options\" style=\"width: 100%;text-align: center;\">\n" +
            "                    <tr>\n" +
            "                        <td width=\"10%\">选项1&nbsp;\n" +
            "                            <input type=\"checkbox\"  name=\"q_key\">\n" +
            "                            <input type='hidden' name=\"orderNum\" value='1'/>\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" optId='1' name=\"q_options\" class=\"tm_txtx\" ></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "            </div>\n" +
            "        </div>";
    }else if(qType==3){
        table = "<label class=\"control-label col-sm-3\">答案设置</label>\n" +
            "        <div class=\"col-sm-7\">\n" +
            "            <div>\n" +
            "                <table id='optTable' class=\"tm_question_options\" style=\"width: 100%;text-align: center;\">\n" +
            "                    <tr>\n" +
            "                        <td width=\"50%\">\n" +
            "                            <label><input type=\"radio\"  name=\"q_key\" value=\"T\" data-rule=\"required;\">正确</label>\n" +
            "                        </td>\n" +
            "                        <th width=\"50%\">\n" +
            "                            <label><input type=\"radio\"  name=\"q_key\" value=\"F\" data-rule=\"required;\">错误</label>\n" +
            "                        </th>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "            </div>\n" +
            "        </div>";
    }else if(qType==4){
        table="<label class=\"control-label col-sm-3\">填空设置</label>\n" +
            "        <div class=\"col-sm-7\">\n" +
            "            <div>\n" +
            "                <input type=\"button\" class=\"tm_btn\" value=\"增加填空\" onclick=\"addOption("+qType+",true)\">\n" +
            "            </div>\n" +
            "            <div>\n" +
            "                <table id='optTable' class=\"tm_question_options\" style=\"width: 100%;text-align: center;\">\n" +
            "                    <tr>\n" +
            "                        <td width=\"10%\" >填空1&nbsp;\n" +
            "                        </td>\n" +
            "                        <th width=\"85%\">\n" +
            "                            <textarea rows=\"3\" name=\"q_key\" class=\"tm_txtx\"></textarea>\n" +
            "                        </th>\n" +
            "                        <td width=\"5%\">\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "            </div>\n" +
            "        </div>";
    }
    $("#opt").html(table);
    if(keys!=null){
        if(opts!=null&&opts.length!=0){
            for (var j=0;j<opts.length-1;j++) {
                addOption(qType,false);
            }
            var trList = $("#optTable tr");
            for (var i=0;i<trList.length;i++){
                var tdArr = trList.eq(i).find("td").eq(0);
                var thArr = trList.eq(i).find("th").eq(0);
                var textareaArr = thArr.find("textarea").eq(0);
                var radioArr = tdArr.find("input").eq(0);
                var inputArr = tdArr.find("input").eq(1);
                var opt = opts[i];
                textareaArr.val(opt.optionContent);
                textareaArr.attr("optId",opt.id);
                inputArr.val(i+1);
                radioArr.val(opt.id)
            }
            for (var i=0;i<keys.length;i++){
                $("input[name='q_key'][value='"+keys[i]+"']").attr('checked','true');
            }
        }else if(qType==3){
            $("input[name='q_key'][value='"+keys[0]+"']").attr('checked','true');
        }else if(qType==4){
            for (var j=0;j<keys.length-1;j++) {
                addOption(qType,false);
            }
            var trList = $("#optTable tr");
            for (var i=0;i<trList.length;i++){
                var thArr = trList.eq(i).find("th").eq(0);
                var textareaArr = thArr.find("textarea").eq(0);
                textareaArr.val(keys[i]);
            }
        }
    }
}