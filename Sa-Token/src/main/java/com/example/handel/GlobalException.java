package com.example.handel;
import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.example.pojo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalException {

    // 全局异常拦截（拦截项目中的所有异常）
    @ResponseBody
    @ExceptionHandler
    public Result handlerException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 打印堆栈，以供调试
        System.out.println("全局异常---------------");
        e.printStackTrace();

        // 不同异常返回不同状态码
        Result aj = null;
        if (e instanceof NotLoginException) {	// 如果是未登录异常
            NotLoginException ee = (NotLoginException) e;
//            aj = Result.getNotLogin().setMsg(ee.getMessage());
            aj = new Result(500,e.getMessage());
        }
        else if(e instanceof NotRoleException) {		// 如果是角色异常
            NotRoleException ee = (NotRoleException) e;
//            aj = Result.getNotJur("无此角色：" + ee.getRole());
            aj = new Result(500,e.getMessage());

        }
        else if(e instanceof NotPermissionException) {	// 如果是权限异常
            NotPermissionException ee = (NotPermissionException) e;
//            aj = Result.getNotJur("无此权限：" + ee.getCode());
            aj = new Result(500,e.getMessage());
        }
        else if(e instanceof DisableLoginException) {	// 如果是被封禁异常
            DisableLoginException ee = (DisableLoginException) e;
//            aj = Result.getNotJur("账号被封禁：" + ee.getDisableTime() + "秒后解封");
            aj = new Result(500,e.getMessage());
        }

        else {	// 普通异常, 输出：500 + 异常信息
//            aj = Result.getError(e.getMessage());
            aj = new Result(500,e.getMessage());
        }

        // 返回给前端
        return aj;
    }

}

