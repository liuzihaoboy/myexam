package com.learning.exam.util;

import com.learning.exam.framework.exception.ValidationJsonException;
import com.learning.exam.model.result.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuzihao
 * @date 2019-02-19 14:46
 */
@Slf4j
public class DateTimeUtils {
    public  static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static  final String SINGLE_MINUTE_PATTERN = "yyyy-M-dd HH:mm";
    public static   final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static  final String DATE_PATTERN = "yyyy-MM-dd";
    public static  final String WEIXIN_DATE_PATTERN = "MMddyyyy";
    public static  final String CH_DATE_PATTERN = "yyyy年MM月dd日";
    public static  final String CH_DATE_MONTH_PATTERN = "MM月dd日";
    public static  final String CH_DATE_TIME_PATTERN = "yyyy年MM月dd日 HH:mm";
    public static   final String MONTH_PATTERN = "yyyy-MM";
    public static   final String HOUR_MINUTE_ONLY_PATTERN = "HH:mm";
    private DateTimeUtils(){

    }
    public static DateToString getStringInstance(){
        return DateToString.DATETOSTRING;
    }
    public static StringToDate getDateInstance(){
        return StringToDate.STRINGTODATE;
    }

    /**
     * date转化string
     */
    public static class DateToString{
        public String dateFormat(Date date, String pattern) {
            if(StringUtils.isEmpty(pattern)){
                pattern = DateTimeUtils.DATE_TIME_PATTERN;
            }
          SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        public String dateDefaultFormat(Date date) {
            return dateFormat(date,DateTimeUtils.DATE_TIME_PATTERN);
        }
        static final DateToString DATETOSTRING = new DateToString();
    }
    /**
     * string转化date
     */
    public static class StringToDate{
        public Date dateParse(String dateTimeString, String pattern)  {
            try{
                if(StringUtils.isEmpty(pattern)){
                    pattern = DateTimeUtils.DATE_TIME_PATTERN;
                }
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(dateTimeString);
            }catch (ParseException e){
                log.error("[日期时间格式化]日期时间信息不正确,param={},{}",dateTimeString,pattern);
                throw new ValidationJsonException(CodeMsg.DATE_TIME_FORMAT);
            }
        }
        public Date dateDefaultParse(String dateTimeString)   {
            return dateParse(dateTimeString,DateTimeUtils.DATE_TIME_PATTERN);
        }
        public  Date getSomeDay(Date date, int day){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, day);
            return calendar.getTime();
        }
        static final StringToDate STRINGTODATE = new StringToDate();
    }
}
