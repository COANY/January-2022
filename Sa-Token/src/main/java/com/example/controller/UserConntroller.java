package com.example.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(tags = "用户测试")
public class UserConntroller {

    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @PostMapping("doLogin")
    public String doLogin(String username, String password, String id) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if ("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(id);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            System.out.println("token:" + tokenInfo);
            System.out.println("tokenName:" + StpUtil.getTokenName());
            System.out.println("tokenName:" + StpUtil.getTokenValue());
            System.out.println("token_idByToken" + StpUtil.getLoginIdByToken(StpUtil.getTokenValue()));
            System.out.println("re_id" + StpUtil.getLoginId());

            // 判断：当前账号是否含有指定权限, 返回true或false
//            System.out.println(StpUtil.hasPermission("101"));

            // 校验：当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
//            StpUtil.checkPermission("user-update");

            // 校验：当前账号是否含有指定权限 [指定多个，必须全部验证通过]
//            StpUtil.checkPermissionAnd("user-update", "user-delete");

            // 校验：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
//            StpUtil.checkPermissionOr("user-update", "user-delete");

            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @SaCheckLogin //鉴权 是否登录
    @SaCheckRole("admin") //角色
    @PostMapping("isLogin")
    public String isLogin() {
        String loginId = StpUtil.getLoginId(null);

        return "当前会话是否登录" + loginId + "：" + StpUtil.isLogin();
    }
    @SaCheckPermission("user-add") //权限
    @PostMapping("loginOut")
    public String loginOut(String id) {
        StpUtil.logout(id);
        return "" + id + "退出：" ;
    }

    @PostMapping("disable")
    public String disable(String id) {
        StpUtil.disable(id,1000);

        return "" + id + "退出：" ;
    }

}

