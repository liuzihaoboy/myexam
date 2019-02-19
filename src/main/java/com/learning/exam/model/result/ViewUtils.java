package com.learning.exam.model.result;


/**
 * @author liu_yeye
 * @date 2018-07-30 16:09
 */
public class ViewUtils {
    public static final String SUCCESS_PAGE="common/success";
    public static final String  ERROR_PAGE="common/error";
    public static final String  SUCCESS_CLOSE_PAGE="common/success_close";
    public static void setSuccessMessage(ViewModelAdapter viewModelAdapter,String message){
        viewModelAdapter.setStatus(true);
        viewModelAdapter.setCode(0);
        viewModelAdapter.setMsg(message);
    }
    public static void setSuccessMessage(ViewModelAdapter viewModelAdapter){
        viewModelAdapter.setStatus(true);
        viewModelAdapter.setCode(0);
        viewModelAdapter.setMsg("成功");
    }
    public static void setErrorMessage(ViewModelAdapter viewModelAdapter){
        viewModelAdapter.setStatus(false);
        viewModelAdapter.setCode(1);
        viewModelAdapter.setMsg("失败");
    }
    public static void setErrorMessage(ViewModelAdapter viewModelAdapter,String message){
        viewModelAdapter.setStatus(false);
        viewModelAdapter.setCode(-1);
        viewModelAdapter.setMsg(message);
    }
}
