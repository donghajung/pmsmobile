package pms.com.mobile.globalclass.log;

import android.app.Activity;
import android.util.Log;

/**
 * Created by jungdongha on 15. 1. 6..
 */
public class RbLog {

    /* disable debug*/
    //public static boolean D = false;
    /* enable debug true */
    public static boolean D = true;
    public static boolean DD = true;
    public static boolean DDD = true;

    public static boolean II = false;
    public static boolean TEST = false;
    public static boolean TEST1 = false;
    public static boolean TEST2 = false;
    public static boolean TEST3 = false;
    public static boolean TEST4 = true;
    public static boolean TEST5 = false;
    public static boolean TEST6 = true;

    public static boolean E = true;
    public static boolean EE = true;


    public static void d(String tag,String msg){
        if(D && tag!=null && msg !=null){
            Log.d(tag, msg);
        }
    }

    public static void e(String tag,String msg){
        if(E && tag!=null && msg !=null){
            Log.e(tag, msg);
        }
    }

    public static void d(Activity activity,String msg){
        if(D && activity != null && msg !=null)
            Log.d(activity.getLocalClassName(),msg);
    }

    public static void dd(String msg,String msg2){
        if(D && msg !=null)
            Log.d("jdh","경로는=   *******  "+msg+"   **********    \n"+msg2);
    }

    public static void dddd(String msg,String msg2){
        if(DDD && msg !=null)
            Log.d("jdh","경로는=   *******  "+msg+"   **********    \n"+msg2);
    }
    public static void jdhd(String msg,String msg2){
        if(DDD && msg !=null)
            Log.d("jdh","경로는=   *******  "+msg+"   **********    \n"+msg2);
    }

    public static void ii(String msg,String msg2){
        if(II && msg !=null)
            Log.i("jdh","경로는="+msg+"    \n"+msg2);
    }
    public static void ww(String msg,String msg2){
        if(D && msg !=null)
            Log.w("jdh","경로는="+msg+"    \n"+msg2);
    }

    public static void ddd(String msg,String msg2){
        if(DD && msg !=null)
            Log.d("jdh","경로는="+msg+"    \n"+msg2);
    }

    public static void d1(String msg){
        if(DD && msg !=null)
            Log.d("jdh","#########################################\n                                    "+msg+"   \n#########################################");
    }

    public static void ee(String msg,String msg2){
        if(E && msg !=null)
            Log.e("jdh","경로는="+msg+"    \n"+msg2);
    }
    public static void eee(String msg,String msg2){
        if(EE && msg !=null)
            Log.e("jdh","경로는="+msg+"    \n"+msg2);
    }

    //쓰레드용
    public static void td(String tag,String msg){
        if(D && tag!=null && msg !=null){
            Log.d(tag, msg);
        }
    }

}
