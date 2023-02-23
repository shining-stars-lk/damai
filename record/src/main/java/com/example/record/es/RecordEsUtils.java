package com.example.record.es;

import com.example.dto.CreateIndexDto;
import com.example.util.BusinessEsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lk
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
        List<CreateIndexDto> list = new ArrayList<CreateIndexDto>();
        
        CreateIndexDto operatorName = new CreateIndexDto();
        operatorName.setParamName("operator_name");
        operatorName.setParamType("keyword");
        list.add(operatorName);
        
        CreateIndexDto contentType = new CreateIndexDto();
        contentType.setParamName("content_type");
        contentType.setParamType("keyword");
        list.add(contentType);
        
        CreateIndexDto requestBody = new CreateIndexDto();
        requestBody.setParamName("request_body");
        requestBody.setParamType("keyword");
        list.add(requestBody);
        
        CreateIndexDto method = new CreateIndexDto();
        method.setParamName("method");
        method.setParamType("keyword");
        list.add(method);
        
        CreateIndexDto parameter = new CreateIndexDto();
        parameter.setParamName("parameter");
        parameter.setParamType("keyword");
        list.add(parameter);
        
        CreateIndexDto requestURI = new CreateIndexDto();
        requestURI.setParamName("request_uri");
        requestURI.setParamType("keyword");
        list.add(requestURI);
        
        CreateIndexDto ipAddress = new CreateIndexDto();
        ipAddress.setParamName("ip_address");
        ipAddress.setParamType("keyword");
        list.add(ipAddress);
        
        CreateIndexDto timestamp = new CreateIndexDto();
        timestamp.setParamName("@timestamp");
        timestamp.setParamType("date");
        list.add(timestamp);
        
        CreateIndexDto content = new CreateIndexDto();
        content.setParamName("content");
        content.setParamType("text");
        list.add(content);
        
        CreateIndexDto result = new CreateIndexDto();
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
