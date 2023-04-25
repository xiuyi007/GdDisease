package util;

import com.li.gddisease.R;
import com.li.gddisease.ui.DataFragment;
import com.li.gddisease.ui.MapFragment;
import com.li.gddisease.ui.MeFragment;

public class MyUtil {
    public static int ConvertType_toInt(String type)
    {
        int tmp = 0;
        switch (type)
        {
            case "线裂":
                tmp = 1;
                break;
            case "坑洞":
                tmp = 2;
                break;
            case "跳车":
                tmp = 3;
                break;
            case "错台":
                tmp = 4;
                break;
            case "沉陷":
                tmp = 5;
                break;
        }
        return tmp;
    }
    public static String ConvertType_toString(int type)
    {
        String tmp = null;
        switch (type)
        {
            case 1:
                tmp = "线裂";
                break;
            case 2:
                tmp = "坑洞";
                break;
            case 3:
                tmp = "跳车";
                break;
            case 4:
                tmp = "错台";
                break;
            case 5:
                tmp = "沉陷";
                break;
        }
        return tmp;
    }
    public static int convertStatus_toInt(String mstatus)
    {
        int status = 0;
        switch (mstatus)
        {

            case "修复中":
                status = 1;
                break;
            case "已修复":
                status = 2;
                break;
            case "未分配":
                status = 3;
                break;

        }
        return status;
    }
    public static String convertStatus_toString(int mstatus)
    {
        String status = null;
        switch (mstatus)
        {

            case 1:
                status = "修复中";
                break;
            case 2:
                status = "已修复";
                break;
            case 3:
                status = "未分配";
                break;
        }
        return status;
    }

}
