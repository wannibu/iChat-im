package com.ichat.wannibu.ichat.Legality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//密码正则
public class PasswCheck {
    public boolean passwCheck(String str) {
        String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{6,18}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}
