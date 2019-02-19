package com.learning.exam.model.result;

/**
 * @author liu_yeye
 * @date 2018-05-21 11:15
 */
public class CodeMsg {
	private int code;
	private String msg;
    /**
     * //通用 5000XX异常
     */
    public static final CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static final CodeMsg SERVER_ERROR = new CodeMsg(500001, "服务端异常");
	//登录模块 5001XX
	public static final CodeMsg VERIFY_ERROR = new CodeMsg(500101,"验证码错误");
	public static final CodeMsg INFO_ERROR = new CodeMsg(500102,"用户名或密码错误");
	public static final CodeMsg ROLE_TYPE_ERROR = new CodeMsg(500103,"角色类型错误");
    public static final CodeMsg NOT_LOGIN_ERROR = new CodeMsg(500104,"您已离线!");
	public static final CodeMsg LOGIN_OTHER_ERROR = new CodeMsg(500105,"您已在其它地方登录!");
	//权限模块 5002XX
	public static final CodeMsg AUTH_CHECK_ERROR = new CodeMsg(500201,"访问权限限制");
	//题库管理模块 5003XX
	public static final CodeMsg COURSE_CHECK_ERROR = new CodeMsg(500301,"没有该课程!");
	public static final CodeMsg DB_UPDATE_ERROR = new CodeMsg(500302,"题库更新失败!");
	public static final CodeMsg DB_INSERT_ERROR = new CodeMsg(500303,"题库添加失败!");
	public static final CodeMsg DB_SELECT_ERROR = new CodeMsg(500304,"题库查询失败!");
	public static final CodeMsg COURSE_NAME_ERROR = new CodeMsg(500305,"课程名不能为空");
	public static final CodeMsg Q_SELECT_ERROR = new CodeMsg(500306,"题目查询失败");
	public static final CodeMsg Q_TYPE_ERROR = new CodeMsg(500307,"题目类型错误");
	public static final CodeMsg Q_LEVEL_ERROR = new CodeMsg(500308,"题目等级错误");
	public static final CodeMsg Q_KEY_ERROR_ONE = new CodeMsg(500308,"单选题答案只能有一个");
	public static final CodeMsg Q_KEY_ERROR_TWO = new CodeMsg(500308,"多选题答案需有多个");
	public static final CodeMsg Q_KEY_ERROR_THREE = new CodeMsg(500308,"判断题答案只能为T或F");
	public static final CodeMsg Q_KEY_ERROR_FOUR = new CodeMsg(500308,"填空题答案与选项不匹配");
	public static final CodeMsg Q_OPT_ERROR = new CodeMsg(500308,"选项不匹配");
	//用户管理模块 5004XX
	
	//试卷管理模块 5005XX

	//成绩管理模块 5006XX

	//成绩管理模块 5007XX



	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
