package cn.itcast.web.aspect;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @Author han
 * @Date 2020/3/12 19:17
 * @Version 1.0
 **/
@Component
@Aspect
public class LogAscpet {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;

    @Around("bean(*Controller) && !bean(sysLogController)")
    public Object insertLog(ProceedingJoinPoint pjp){
        try {
            //放行
            Object retV = pjp.proceed();
            //设置日志内容
            SysLog sysLog = new SysLog();
            //设置IP
            sysLog.setIp(request.getRemoteAddr());
            //设置时间
            sysLog.setTime(new Date());
            //设置执行方法
            sysLog.setMethod(pjp.getSignature().getName());
            //设置当前执行的目标类
            sysLog.setAction(pjp.getTarget().getClass().getName());
            //从
            User user = (User) request.getSession().getAttribute("loginUser");
            if(user != null){
                sysLog.setCompanyId(user.getCompanyId());
                sysLog.setCompanyName(user.getCompanyName());
                sysLog.setUserName(user.getUserName());
            }
            //调用业务层记录日志
            sysLogService.save(sysLog);
            return retV;

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
