package com.ebao.hospitaldapp.rest.aspect;

import com.ebao.hospitaldapp.task.InitializationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

@Component
public class LogInterceptor implements HandlerInterceptor{

    private static final ThreadLocal<Long> startTimeThreadLocal =
            new NamedThreadLocal<Long>("ThreadLocal StartTime");

    final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        long beginTime = System.currentTimeMillis();//1、开始时间
        startTimeThreadLocal.set(beginTime);        //线程绑定变量（该数据只有当前请求的线程可见）
        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .format(beginTime), request.getRequestURI());
        //获取请求方式
        logger.debug(request.getMethod());
        //获取项目名称
        logger.debug(request.getContextPath());
        //获取完整请求路径
        logger.debug(request.getRequestURL().toString());
        //获取除了域名外的请求数据
        logger.debug(request.getRequestURI());
        //获取请求参数
        logger.debug(request.getQueryString());
        logger.debug("----------------------------------------------------------");
        //获取请求头
        String header = request.getHeader("user-Agent");
        logger.debug(header);
        header = header.toLowerCase();
        //获取所有的消息头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        //获取获取的消息头名称，获取对应的值，并输出
        while(headerNames.hasMoreElements()){
            String nextElement = headerNames.nextElement();
            logger.debug(nextElement+":"+request.getHeader(nextElement));
        }
        logger.debug("----------------------------------------------------------");
        //根据名称获取此重名的所有数据
        Enumeration<String> headers = request.getHeaders("accept");
        while (headers.hasMoreElements()) {
            String string = (String) headers.nextElement();
            logger.debug(string);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        // 打印JVM信息。
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        long endTime = System.currentTimeMillis();  //2、结束时间

        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(endTime), (endTime - beginTime),
                request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024,
                (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024);

    }
}
