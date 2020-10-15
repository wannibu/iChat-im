package com.ichat.wannibu.ichat.Legality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//邮箱正则
public class EmailCheck {
    public boolean emailCheck(String str) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}
