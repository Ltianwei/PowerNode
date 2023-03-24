package com.node.aspects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.node.async.AsyncSysOperLogService;
import com.node.controller.BaseController;
import com.node.domain.SysOperLog;
import com.node.domain.SysUser;
import com.node.enums.StateEnums;
import com.node.utils.HttpUtils;
import com.node.utils.ServletUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import com.node.annotations.Log;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class LogAspect extends BaseController {

    // 用指定类初始化日志对象，在日志输出的时候，可以打印出日志信息所在类 打印日志信息
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Resource
    private AsyncSysOperLogService asyncSysOperLogService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 方法执行完毕后执行
     * 定义切点在自定义的注解上 返回值
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * 同样是在自定义注解处添加的切点 接受异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }
    private void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {

        try {
            //获取当前的用户
            SysUser userInfo = getUserInfo();

            //创建日志对象
            SysOperLog sysOperLog = new SysOperLog();
            //设置时间
            sysOperLog.setOperTime(new Date());
            //设置状态
            sysOperLog.setStatus(StateEnums.REQUEST_SUCCESS.getCode());
            //请求地址
            String ip = HttpUtils.getIpAddr(ServletUtils.getRequest());
            sysOperLog.setOperIp(ip);
            String url = HttpUtils.getRequestUrl(ServletUtils.getRequest());
            sysOperLog.setOperUrl(url);
            //设置用户相关消息
            if (userInfo != null) {
                sysOperLog.setUserId(userInfo.getUserId());
                sysOperLog.setOperName(userInfo.getUserName());
            }
            //设置异常信息
            if (e != null) {
                sysOperLog.setStatus(StateEnums.REQUEST_ERROR.getCode());
                //截取前2000个字符
                sysOperLog.setErrorMsg(e.getMessage().substring(0, 2000));
            }
            //设置方法名称 (通过反射获取类名 方法名字)
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysOperLog.setMethod(className + "." + methodName + "()");
            //设置请求方式
            sysOperLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解的参数
            getControllerMethodDescription(joinPoint, controllerLog, sysOperLog, jsonResult);
            //保存到数据库
            asyncSysOperLogService.saveOperLogService(sysOperLog);
        } catch (Exception ex) {
            //记录本地日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：" + ex.getMessage());
            ex.printStackTrace();
        }


    }

    /**
     * 获取注解中对方法的描述信息 用于controller层注解
     *
     * @param joinPoint
     * @param controllerLog 日志
     * @param sysOperLog    操作日志
     * @param jsonResult
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, Log controllerLog, SysOperLog sysOperLog, Object jsonResult) throws Exception {

        //设置action动作
        sysOperLog.setBusinessType(controllerLog.businessType().getCode());
        //设置标题
        sysOperLog.setTitle(controllerLog.title());
        //是否需要保存request 参数和值
        if (controllerLog.isSaveRequestData()) {
            //参数的信息放入数据库
            setRequestValue(joinPoint, sysOperLog);
        }
        //是否需要保存response 参数和值
        if (controllerLog.isSaveResponseData() && jsonResult != null) {
            String result = objectMapper.writeValueAsString(jsonResult);
            if (result.length() > 2000) {
                result = result.substring(0, 2000);
            }
            //设置请求的参数 放到log中
            sysOperLog.setJsonResult(result);
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysOperLog sysOperLog) throws Exception {
        String requestMethod = sysOperLog.getRequestMethod();
        //枚举 HttpMethod返回请求的类型 如果是get/post请求
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            //获取参数 将参数由数组转为字符串
            String params = argsArrayToString(joinPoint.getArgs());
            if (params != null && params.length() > 2000) {
                params = params.substring(0, 2000);
            }
            sysOperLog.setOperParam(params);
        }
    }


    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (o != null && isFilterObject(o)) {
                    try {
                        String jsonObj = objectMapper.writeValueAsString(o);
                        params += jsonObj + " ";
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return params;
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param o 对象信息
     * @return 如果是需要过滤的对象 返回true 否则为false
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

}
