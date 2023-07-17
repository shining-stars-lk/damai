package com.example.record.es;

import com.example.dto.EsCreateIndexDto;
import com.example.util.BusinessEsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: 星哥
 * @create: 2023-02-21
 **/
public class RecordEsUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(RecordEsUtils.class);
    
    private final static String INDEXNAME = "record";
    
    private final static String INDEXTYPE = "record";
    
    private BusinessEsUtil businessEsUtil;
    
    public RecordEsUtils(BusinessEsUtil businessEsUtil){
        this.businessEsUtil = businessEsUtil;
    }
    
    /**
     * 检查索引是否存在
     *
     * @throws IOException
     */
    public boolean checkIndex() throws IOException {
        boolean checkIndex = businessEsUtil.checkIndex(INDEXNAME, INDEXTYPE);
        return checkIndex;
    }
    
    public void createIndex() throws IOException {
        List<EsCreateIndexDto> list = new ArrayList<EsCreateIndexDto>();
        
        EsCreateIndexDto operatorName = new EsCreateIndexDto();
        operatorName.setParamName("operator_name");
        operatorName.setParamType("keyword");
        list.add(operatorName);
        
        EsCreateIndexDto contentType = new EsCreateIndexDto();
        contentType.setParamName("content_type");
        contentType.setParamType("keyword");
        list.add(contentType);
        
        EsCreateIndexDto requestBody = new EsCreateIndexDto();
        requestBody.setParamName("request_body");
        requestBody.setParamType("keyword");
        list.add(requestBody);
        
        EsCreateIndexDto method = new EsCreateIndexDto();
        method.setParamName("method");
        method.setParamType("keyword");
        list.add(method);
        
        EsCreateIndexDto parameter = new EsCreateIndexDto();
        parameter.setParamName("parameter");
        parameter.setParamType("keyword");
        list.add(parameter);
        
        EsCreateIndexDto requestURI = new EsCreateIndexDto();
        requestURI.setParamName("request_uri");
        requestURI.setParamType("keyword");
        list.add(requestURI);
        
        EsCreateIndexDto ipAddress = new EsCreateIndexDto();
        ipAddress.setParamName("ip_address");
        ipAddress.setParamType("keyword");
        list.add(ipAddress);
        
        EsCreateIndexDto timestamp = new EsCreateIndexDto();
        timestamp.setParamName("@timestamp");
        timestamp.setParamType("date");
        list.add(timestamp);
        
        EsCreateIndexDto content = new EsCreateIndexDto();
        content.setParamName("content");
        content.setParamType("text");
        list.add(content);
        
        EsCreateIndexDto result = new EsCreateIndexDto();
        result.setParamName("result");
        result.setParamType("keyword");
        list.add(result);
        businessEsUtil.createIndex(INDEXNAME, INDEXTYPE, list);
    }
    
    /**
     * 删除索引
     *
     * @return
     * @throws IOException
     */
    public boolean deleteIndex() throws IOException {
        businessEsUtil.deleteData(INDEXNAME);
        return true;
    }
    
    /**
     * 新增记录
     *
     * @param map
     * @return
     * @throws IOException
     */
    public boolean add(Map<String,Object> map) throws IOException {
        // 检查索引是否存在 并清空所有数据
        boolean checkIndex = false;
        try {
            checkIndex = checkIndex();
        } catch (Exception e1) {
            createIndex();
        }
        if (!checkIndex) { // 索引不存在 初始化索引
            try {
                deleteIndex(); // 先执行下删除
            } catch (Exception e) {
                logger.warn("新增记录删除索引异常 错误信息:{}", e.getMessage());
            }
            createIndex();;
        }
        if (null != map) {
            boolean add = businessEsUtil.add(INDEXNAME, INDEXTYPE, map);
            return add;
        }
        
        return true;
    }
}
