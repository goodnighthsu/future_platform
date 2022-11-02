package site.xleon.platform.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import site.xleon.platform.config.app.AppConfig;
import site.xleon.platform.core.JWT;
import site.xleon.platform.core.MyException;
import site.xleon.platform.core.Utils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected Utils utils;

    @Autowired
    protected JWT jwt;

    @Autowired
    protected AppConfig appConfig;

    protected Integer getUserId() throws MyException {
        return  jwt.getUserId(request);
    }
}
