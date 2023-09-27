package com.example.controller;


import com.example.annotation.Auth;
import com.example.config.DefaultConfig;
import com.example.constant.KoConstant;
import com.example.model.CpuInfo;
import com.example.model.ExceptionInfo;
import com.example.model.ExceptionNode;
import com.example.model.HeapMemoryInfo;
import com.example.model.MethodInfo;
import com.example.model.ParamMetric;
import com.example.model.PhysicalMemoryInfo;
import com.example.model.SystemStatistic;
import com.example.model.TextParam;
import com.example.model.ThreadInfo;
import com.example.model.UserInfo;
import com.example.service.ClassService;
import com.example.service.GraphService;
import com.example.service.SysUsageService;
import com.example.service.ThreadUsageService;
import com.example.util.Context;
import com.example.util.InvalidAuthInfoException;
import com.example.util.KoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    @Value("${ko-time.user-name:}")
    private String userName;
    @Value("${ko-time.password:}")
    private String password;

    private static Logger log = Logger.getLogger(KoTimeController.class.toString());
    private final String uiKitCssText = getResourceText("kostatic/uikit.min.css");
    private final String uiKitJsText = getResourceText("kostatic/uikit.min.js");
    private final String metricFlowJsText = getResourceText("kostatic/Metricflow.js");
    private final String jQueryJsText = getResourceText("kostatic/JQuery.min.js");
    private final String uiKitIconsJs = getResourceText("kostatic/uikit-icons.js");

    @PostMapping("/login")
    @ResponseBody
    public Map login(@RequestBody UserInfo userInfo) {
        if (null == userInfo || !StringUtils.hasText(userInfo.getUserName()) || !StringUtils.hasText(userInfo.getPassword())) {
            throw new InvalidAuthInfoException("failed to login for kotime,please fill userName and password!");
        }
        Map map = new HashMap();
        if (userName.equals(userInfo.getUserName()) && password.equals(userInfo.getPassword())) {
            String token = KoUtil.login(userInfo.getUserName());
            map.put("state", 1);
            map.put("token", token);
            return map;
        }
        map.put("state", 0);
        return map;
    }

    @GetMapping("/isLogin")
    @ResponseBody
    public Map isLogin(String kotoken) {
        Map map = new HashMap();
        map.put("state", 1);
        boolean checkLogin = false;
        if (StringUtils.hasText(kotoken)) {
            if (kotoken.equals(Context.getConfig().getStaticToken())) {
                checkLogin = true;
            } else {
                checkLogin = KoUtil.isLogin(kotoken);
            }
        }
        map.put("isLogin", checkLogin ? 1 : 0);
        return map;
    }


    @GetMapping
    public void index(String kotoken, String test,String charset, String language,HttpServletResponse response, HttpServletRequest request) {
        if (!Context.getConfig().getEnable()) {
            return;
        }
        if (null != test) {
            return;
        }
        boolean staticTokenVisit = false;
        if (StringUtils.hasText(kotoken)) {
            staticTokenVisit = true;
        }
        if (!StringUtils.hasText(charset)) {
            charset = "utf-8";
        }
        response.setContentType("text/html;charset="+charset);
        ClassPathResource classPathResource = new ClassPathResource(KoConstant.getViewName(language));
        try (
                InputStream inputStream = classPathResource.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader reader = new BufferedReader(streamReader);
                PrintWriter out = response.getWriter()) {

            String context = request.getContextPath();
            if (StringUtils.hasText(Context.getConfig().getContextPath())) {
                context = Context.getConfig().getContextPath();
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(KoConstant.globalThreshold) > -1) {
                    line = line.replace(KoConstant.globalThreshold, Context.getConfig().getThreshold() + "");
                } else if (line.indexOf(KoConstant.globalNeedLogin) > -1) {
                    line = line.replace(KoConstant.globalNeedLogin, Context.getConfig().getAuthEnable() + "");
                } else if (line.indexOf(KoConstant.contextPath) > -1) {
                    line = line.replace(KoConstant.contextPath, context);
                } else if (line.indexOf(KoConstant.exceptionTitleStyle) > -1) {
                    line = line.replace(KoConstant.exceptionTitleStyle, Context.getConfig().getExceptionEnable() == true ? "" : "display:none;");
                } else if (line.indexOf("UIKitCss") > -1) {
                    line = line.replace("UIKitCss", uiKitCssText);
                } else if (line.indexOf("UIKitJs") > -1) {
                    line = line.replace("UIKitJs", uiKitJsText);
                } else if (line.indexOf("MetricFlowJs") > -1) {
                    line = line.replace("MetricFlowJs", metricFlowJsText);
                } else if (line.indexOf("jQueryJs") > -1) {
                    line = line.replace("jQueryJs", jQueryJsText);
                } else if (line.indexOf("uiKitIconsJs") > -1) {
                    line = line.replace("uiKitIconsJs", uiKitIconsJs);
                } else if (line.indexOf("staticTokenVisitValue") > -1) {
                    line = line.replace("staticTokenVisitValue", staticTokenVisit + "");
                } else if (line.indexOf("staticTokenValue") > -1) {
                    line = line.replace("staticTokenValue", "'" + kotoken + "'");
                }
                stringBuilder.append(line + "\n");
            }
            line = stringBuilder.toString();
            out.write(line);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getResourceText(String fileName) {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        try (InputStream inputStream = classPathResource.getInputStream();
             InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @GetMapping("/getConfig")
    @ResponseBody
    @Auth
    public DefaultConfig getConfig() {
        DefaultConfig config = Context.getConfig();
        return config;
    }

    @GetMapping("/getStatistic")
    @ResponseBody
    @Auth
    public SystemStatistic getStatistic() {
        GraphService graphService = GraphService.getInstance();
        SystemStatistic system = graphService.getRunStatistic();
        return system;
    }

    @GetMapping("/getApis")
    @ResponseBody
    @Auth
    public List<MethodInfo> getApis(String question) {
        GraphService graphService = GraphService.getInstance();
        List<MethodInfo> list = null;
        if (StringUtils.hasText(question)) {
            list = graphService.searchMethods(question);
        } else {
            list = graphService.getControllers();
        }
        Collections.sort(list);
        return list;
    }

    @GetMapping("/getParamGraph")
    @ResponseBody
    @Auth
    public Map<String, ParamMetric> getParamGraph(String methodId) {
        GraphService graphService = GraphService.getInstance();
        Map<String, ParamMetric> list = graphService.getMethodParamGraph(methodId);
        return list;
    }

    @GetMapping("/getApiTips")
    @ResponseBody
    @Auth
    public List<String> getApiTips(String question) {
        GraphService graphService = GraphService.getInstance();
        List<String> list = graphService.getCondidates(question);
        return list;
    }


    @GetMapping("/getExceptions")
    @ResponseBody
    @Auth
    public List<ExceptionNode> getExceptions() {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        return exceptionList;
    }

    @GetMapping("/getTree")
    @ResponseBody
    @Auth
    public MethodInfo getTree(String methodName) {
        GraphService graphService = GraphService.getInstance();
        MethodInfo tree = graphService.getTree(methodName);
        return tree;
    }

    @GetMapping("/getMethodsByExceptionId")
    @ResponseBody
    @Auth
    public List<ExceptionInfo> getMethodsByExceptionId(String exceptionId, String message) {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionInfo> exceptionInfos = graphService.getExceptionInfos(exceptionId, message);
        return exceptionInfos;
    }

    @PostMapping("/updateConfig")
    @ResponseBody
    @Auth
    public boolean updateConfig(@RequestBody DefaultConfig config) {
        DefaultConfig koTimeConfig = Context.getConfig();
        if (config.getEnable() != null) {
            koTimeConfig.setEnable(config.getEnable());
        }
        if (config.getExceptionEnable() != null) {
            koTimeConfig.setExceptionEnable(config.getExceptionEnable());
        }
        if (config.getLogEnable() != null) {
            koTimeConfig.setLogEnable(config.getLogEnable());
        }
        if (config.getMailEnable() != null) {
            koTimeConfig.setMailEnable(config.getMailEnable());
        }
        if (config.getThreshold() != null) {
            koTimeConfig.setThreshold(config.getThreshold());
        }
        if (config.getLanguage() != null) {
            koTimeConfig.setLanguage(config.getLanguage());
        }
        return true;
    }

    @PostMapping("/updateClass")
    @ResponseBody
    @Auth
    public Map updateClass(@RequestParam("classFile") MultipartFile classFile, String className) {
        Map map = new HashMap();
        if (classFile == null || classFile.isEmpty()) {
            map.put("state", 0);
            map.put("message", "文件不能为空");
            return map;
        }
        if (!StringUtils.hasText(className)) {
            map.put("state", 0);
            map.put("message", "类名不能为空");
            return map;
        }
        className = className.trim();
        File file = null;
        try {
            String originalFilename = classFile.getOriginalFilename();
            if (!originalFilename.endsWith(".class")) {
                map.put("state", 0);
                map.put("message", "仅支持.class文件");
                return map;
            }
            String[] filename = originalFilename.split("\\.");
            String substring = className.substring(className.lastIndexOf(".") + 1);
            if (!substring.equals(filename[0])) {
                map.put("state", 0);
                map.put("message", "请确认类名是否正确");
                return map;
            }
            file = uploadFile(classFile.getBytes(), filename[0]);
        } catch (IOException e) {
            log.severe("Error class file!");
            map.put("state", 0);
            map.put("message", "无法解析文件");
            return map;
        }
        final ClassService classService = ClassService.getInstance();
        classService.updateClass(className, file.getAbsolutePath());
        file.deleteOnExit();

        map.put("state", 1);
        map.put("message", "更新成功");
        return map;
    }


    private static File uploadFile(byte[] file, String fileName) throws IOException {
        FileOutputStream out = null;
        try {
            File targetFile = File.createTempFile(fileName, ".class", new File(System.getProperty("java.io.tmpdir")));
            out = new FileOutputStream(targetFile.getAbsolutePath());
            out.write(file);
            out.flush();
            return targetFile;
        } catch (Exception e) {
            log.severe("" + e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

    @GetMapping("/getCpuInfo")
    @ResponseBody
    @Auth
    public CpuInfo getCpuInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        CpuInfo cpuInfo = usageService.getCpuInfo();
        return cpuInfo;
    }

    @GetMapping("/getHeapMemoryInfo")
    @ResponseBody
    @Auth
    public HeapMemoryInfo getHeapMemoryInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        HeapMemoryInfo heapMemoryInfo = usageService.getHeapMemoryInfo();
        return heapMemoryInfo;
    }

    @GetMapping("/getPhysicalMemoryInfo")
    @ResponseBody
    @Auth
    public PhysicalMemoryInfo getPhysicalMemoryInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        PhysicalMemoryInfo physicalMemoryInfo = usageService.getPhysicalMemoryInfo();
        return physicalMemoryInfo;
    }

    @PostMapping("/clearData")
    @ResponseBody
    @Auth
    public boolean clearData() {
        GraphService graphService = GraphService.getInstance();
        graphService.clearAll();
        return true;
    }

    @GetMapping("/getThreadsInfo")
    @ResponseBody
    @Auth
    public Map getThreadsInfo(String state) {
        ThreadUsageService usageService = ThreadUsageService.newInstance();
        List<ThreadInfo> threads = usageService.getThreads();
        threads = threads.stream().sorted(Comparator.comparing(ThreadInfo::getState)).collect(Collectors.toList());

        Map<String, Long> stateCounting = threads.stream().collect(Collectors.groupingBy(ThreadInfo::getState, Collectors.counting()));
        stateCounting.put("all",(long)threads.size());

        Map map = new HashMap();
        map.put("statistics", stateCounting);
        if (StringUtils.hasText(state)) {
            threads = threads.stream().filter(a -> a.getState().equals(state)).collect(Collectors.toList());
        }
        map.put("threads", threads);
        return map;
    }

    @PostMapping("/updateDynamicProperties")
    @ResponseBody
    @Auth
    public boolean updateDynamicProperties(@RequestBody TextParam textParam) {
        if (!StringUtils.hasText(textParam.getText())) {
            return false;
        }
        String[] textSplit = textParam.getText().trim().split("\n");
        Properties dynamicProperties = Context.getDynamicProperties();
        for (String line : textSplit) {
            line = line.trim();
            if (line.length()==0 || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            int i = line.indexOf("=");
            if (i<1) {
                continue;
            }
            String propertyStr = line.substring(0, i).trim();
            String valueStr = line.substring(i+1).trim();
            log.info("updated property: "+propertyStr+"=("+dynamicProperties.get(propertyStr)+"->"+valueStr+")");
            dynamicProperties.setProperty(propertyStr,valueStr);
        }

        return true;
    }

    @GetMapping("/getDynamicProperties")
    @ResponseBody
    @Auth
    public Map getDynamicProperties() {
        Map map = new HashMap();
        map.put("state", 0);
        map.put("message", "文件不能为空");
        Properties dynamicProperties = Context.getDynamicProperties();
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : dynamicProperties.stringPropertyNames()) {
            String value = dynamicProperties.getProperty(key);
            if (value!=null) {
                stringBuilder.append(key+"="+value+"\n");
            }
        }
        map.put("data", stringBuilder.toString());
        return map;
    }
}
