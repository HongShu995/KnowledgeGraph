package com.hongshu.constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  公共常量
 *
 * @author HongShu995
 * @create 2022-01-12
 */
public class CommonConstant
{
    /*
    INT否
     */
    public static final int INT_FALSE = 0;

    /*
    INT是
     */
    public static final int INT_TRUE = 1;

    /*
    布尔否
     */
    public static final Boolean BOOLEAN_FALSE = false;

    /*
    布尔是
     */
    public static final Boolean BOOLEAN_TRUE = true;

    /*
    空
     */
    public static final String NULL = "NULL";

    /*
    默认头像
     */
    public static final String avatar = "http://120.77.241.193:9999/images/tx/tx_default.jpg";

    /*
    当前页码
     */
    public static final String CURRENT = "current";

    /*
    页码条数
     */
    public static final String SIZE = "size";

    /*
    默认条数
     */
    public static final String DEFAULT_SIZE = "10";

    /*
    前端组件名
     */
    public static final String COMPONENT = "Layout";

    /*
    上传文件后缀
     */
    public static final String FILE_XLS =".xls";

    public static final String FILE_XLSX =".xlsx";

    public static final String FILE_ZIP = ".zip";

    /*
    获取当前时间
     */
    public static String getCurrentTime()
    {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
