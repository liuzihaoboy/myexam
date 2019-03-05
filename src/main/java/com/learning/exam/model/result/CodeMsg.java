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
    public static final CodeMsg DATE_TIME_FORMAT = new CodeMsg(500002,"格式化时间信息不正确");
	//登录模块 5001XX
	public static final CodeMsg VERIFY_ERROR = new CodeMsg(500101,"验证码错误");
	public static final CodeMsg INFO_ERROR = new CodeMsg(500102,"用户名或密码错误");
	public static final CodeMsg ROLE_TYPE_ERROR = new CodeMsg(500103,"角色类型错误");
    public static final CodeMsg NO_STUDENT = new CodeMsg(500104,"没有找到该学生!");
	public static final CodeMsg LOGIN_OTHER_ERROR = new CodeMsg(500105,"您已在其它地方登录!");
	public static final CodeMsg HAD_REGISTER = new CodeMsg(500106,"该学号已注册！");
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
	public static final CodeMsg MAJOR_SELECT_ERROR = new CodeMsg(500401,"专业查询失败");
	//试卷管理模块 5005XX
	public static final CodeMsg PAPER_TYPE_ERROR = new CodeMsg(500501,"试卷类型错误");
	public static final CodeMsg PAPER_SELECT_ERROR = new CodeMsg(500502,"试卷查询失败!");
	public static final CodeMsg SECTION_SELECT_ERROR = new CodeMsg(500503,"章节查询失败");
	public static final CodeMsg SECTION_SCALE_ERROR = new CodeMsg(500504,"章节难度比例错误");
	public static final CodeMsg PAPER_STARTTIME_LIMIT = new CodeMsg(500505,"开始考试前2小时不能修改");
	public static final CodeMsg PAPER_STARTTIME_OVER = new CodeMsg(500506,"考试时间已过不能修改");
	//考试管理模块 5006XX
	public static final CodeMsg PAPER_USER_STATUS_ERROR = new CodeMsg(500601,"用户考试状态错误");
	public static final CodeMsg PAPER_USER_JOIN_ERROR = new CodeMsg(500602,"您没有权限参加该考试");
	public static final CodeMsg PAPER_URL_ERROR = new CodeMsg(500603,"考试链接验证失败");
	public static final CodeMsg PAPER_TEST_NO_START = new CodeMsg(500604,"考试未开始");
	public static final CodeMsg PAPER_TEST_HAD_END = new CodeMsg(500605,"考试已结束");
	public static final CodeMsg PAPER_TEST_GET_ERROR= new CodeMsg(500606,"获取试卷失败请重试！");
	public static final CodeMsg PAPET_TEST_HAD_JOIN = new CodeMsg(500607,"您已参加过该考试");
	public static final CodeMsg PAPER_USER_NO_JOIN = new CodeMsg(500608,"您没有参加该考试");
	public static final CodeMsg PAPER_NO_RESULT = new CodeMsg(500609,"考试结果未出");
	public static final CodeMsg PAPER_HAD_SUBMIT = new CodeMsg(500610,"您已提交！");
	public static final CodeMsg PAPER_MINUTE_OVER = new CodeMsg(500611,"考试时长已过！系统已强制交卷");
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
