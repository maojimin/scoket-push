/**
 * 
 */
package com.yutian.sm.push.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * @since 2014年4月26日 下午2:34:57
 */
public class DateUtil {

/** 精确到毫秒的日期格式 yyyy-MM-dd HH:mm:ss.S */ 
private static final String DATE_YMDHMSS_FORMAT = "yyyy-MM-dd HH:mm:ss.S";  
/** 精确到秒的日期格式 yyyy-MM-dd HH:mm:ss */ 
private static final String DATE_YMDHMSFORMAT = "yyyy-MM-dd HH:mm:ss"; 
/** 精确到天的日期格式 yyyy-MM-dd */
private static final String DATE_YMDFORMAT = "yyyy-MM-dd";
/** 精确到分钟的日期格式 MM-dd HH:mm */ 
public static final String DATE_MDHM_FORMAT = "MM-dd HH:mm";
/**
* 转化为精确到毫秒的日期格式 yyyy-MM-dd HH:mm:ss.S
*/
private static final ThreadLocal<SimpleDateFormat> dateYMDHMSSFormat = 
new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDHMSS_FORMAT);
        }
    };
    
/**
* 转化为精确到秒的日期格式 yyyy-MM-dd HH:mm:ss
*/
private static final ThreadLocal<SimpleDateFormat> dateYMDHMSFormat = 
new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDHMSFORMAT);
        }
    };
    
/**
* 转化为精确到天的日期格式 yyyy-MM-dd
*/
private static final ThreadLocal<SimpleDateFormat> dateYMDFormat = 
new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_YMDFORMAT);
        }
    };
    
/**
* 转化为精确到秒的日期格式 MM-dd HH:mm
*/
private static final ThreadLocal<SimpleDateFormat> dateMDHMFormat = 
new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_MDHM_FORMAT);
        }
    };
    
    /**
     * 
    * <p>Description: 转化为精确到毫秒的日期格式 yyyy-MM-dd HH:mm:ss.S</p>
    * @return
    * DateFormat
    * @author tom
    * @下午02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDHMSSFormat() {  
        return (DateFormat) dateYMDHMSSFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: 转化为精确到秒的日期格式 yyyy-MM-dd HH:mm:ss</p>
    * @return
    * DateFormat
    * @author tom
    * @下午02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDHMSFormat() {  
        return (DateFormat) dateYMDHMSFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: 转化为精确到分钟的日期格式 MM-dd HH:mm</p>
    * @return
    * DateFormat
    * @author tom
    * @下午02:13:23 - 2010-9-8
     */
    public static DateFormat getDateMDHMFormat() {  
        return (DateFormat) dateMDHMFormat.get();  
    }  
    
    /**
     * 
    * <p>Description: 转化为精确到天的日期格式 yyyy-MM-dd</p>
    * @return
    * DateFormat
    * @author tom
    * @下午02:13:23 - 2010-9-8
     */
    public static DateFormat getDateYMDFormat() {  
        return (DateFormat) dateYMDFormat.get();  
    }  
  
    /**
     * 计算两个日期间隔的分钟数
     * 
     * @param firstDate 小者
     * @param lastDate 大者
     * @return int 默认-1
     */
    public static int getTimeIntervalMins(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }
        
        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (int) (intervalMilli / (60 * 1000));
    }
}
