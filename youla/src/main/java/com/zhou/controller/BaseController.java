package com.zhou.controller;

import com.zhou.common.ResultVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/base")
public class BaseController {

    @RequestMapping("/setCookie")
    public ResultVO setCookie(HttpServletResponse response,String selectArea) {
        ResultVO resultVO=new ResultVO();
        // 创建 Cookie 对象
        Cookie cookie = new Cookie("selectArea", selectArea);

        // 设置 Cookie 的过期时间，单位是秒，例如设置为一天的有效期
        //int maxAge = 24 * 60 * 60;
        //zhou 这里设永久
        cookie.setMaxAge(-1);

        // 设置 Cookie 的路径，可根据实际情况设置
        // 如果要在整个域名下都有效，可以设置为 "/"
        cookie.setPath("/");

        // 将 Cookie 添加到 HTTP 响应头中
        response.addCookie(cookie);

        return resultVO;
    }
}
