package com.example.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.core.StringUtil;
import com.example.dto.EsCreateIndexDto;
import com.example.dto.EsGeoPointDto;
import com.example.dto.EsGeoPointSortDto;
import com.example.dto.EsQueryDto;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: distribute-es
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/
@Slf4j
public class BusinessEsUtil {
    
    private RestClient restClient;
    
    private Boolean esSwitch;
    
    private Boolean esTypeSwitch;
    
    
    public BusinessEsUtil(RestClient businessEsRestClient, Boolean esSwitch, Boolean esTypeSwitch){
        this.restClient = businessEsRestClient;
        this.esSwitch = esSwitch;
        this.esTypeSwitch = esTypeSwitch;
    }
    
    /**
     * 创建索引
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param list 参数集合
     */
    public void createIndex(String indexName, String indexType, List<EsCreateIndexDto> list) throws IOException {
        if (!esSwitch) {
            return;
        }
        if (list == null) {
            return;
        }
        IndexRequest indexRequest = new IndexRequest();
        XContentBuilder builder = JsonXContent.contentBuilder().startObject().startObject("mappings");
        if (esTypeSwitch) {
            builder = builder.startObject(indexType);
        }
        builder = builder.startObject("properties");
        for (EsCreateIndexDto esCreateIndexDto : list) {
            String paramName = esCreateIndexDto.getParamName();
            String paramType = esCreateIndexDto.getParamType();
            if ("text".equals(paramType)) {
                Map<String,Map<String,Object>> map1 = new HashMap<>();
                Map<String,Object> map2 = new HashMap<>();
                map2.put("type","keyword");
                map2.put("ignore_above",256);
                map1.put("keyword",map2);
                builder = builder.startObject(paramName).field("type", "text").field("fields",map1).endObject();
            }else {
                builder = builder.startObject(paramName).field("type", paramType).endObject();
            }
        }
        if (esTypeSwitch) {
            builder = builder.endObject();
        }
        builder = builder.endObject().endObject().startObject("settings").field("number_of_shards", 3)
                .field("number_of_replicas", 1).endObject().endObject();
    
        indexRequest.source(builder);
        String source = indexRequest.source().utf8ToString();
        HttpEntity entity = new NStringEntity(source, ContentType.APPLICATION_JSON);
        Request request = new Request("PUT","/"+indexName+"");
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response performRequest = restClient.performRequest(request);
    }
    
    /**
     * 检查索引是否存在
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @throws IOException
     */
    public boolean checkIndex(String indexName, String indexType) throws IOException {
        if (!esSwitch) {
            return false;
        }
        try {
            String path = "";
            if (esTypeSwitch) {
                path = "/" + indexName + "/" + indexType + "/_mapping?include_type_name";
            }else {
                path = "/" + indexName + "/_mapping";
            }
            Request request = new Request("GET", path);
            request.addParameters(Collections.<String, String>emptyMap());
            Response response = restClient.performRequest(request);
            return response.getStatusLine().getReasonPhrase().equals("OK");
        }catch (ResponseException e) {
            if (e.getResponse().getStatusLine().getStatusCode() == RestStatus.NOT_FOUND.getStatus()) {
                log.warn("index not exist ! indexName:{}, indexType:{}",indexName,indexType);
            }else {
                throw e;
            }
            return false;
        }
    }
    /**
     * 删除索引
     *
     * @param indexName 索引名字
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        if (!esSwitch) {
            return false;
        }
        Request request = new Request("DELETE", "/" + indexName + "");
        request.addParameters(Collections.<String, String>emptyMap());
        Response response = restClient.performRequest(request);
        return response.getStatusLine().getReasonPhrase().equals("OK");
    }
    
    /**
     * 清空索引下所有数据
     *
     * @param indexName 索引名字
     * @throws IOException
     */
    public void deleteData(String indexName) throws IOException {
        if (!esSwitch) {
            return;
        }
        deleteIndex(indexName);
    }
    
    /**
     * 添加
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param params 参数 key:字段名 value:具体值
     * @return
     * @throws IOException
     */
    public boolean add(String indexName, String indexType,Map<String,Object> params) throws IOException {
        return add(indexName, indexType, params, null);
    }
    
    /**
     * 添加
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param params 参数 key:字段名 value:具体值
     * @param id 文档id 如果为空，则使用es默认id
     * @return
     * @throws IOException
     */
    public boolean add(String indexName, String indexType,Map<String,Object> params, String id) throws IOException {
        if (!esSwitch) {
            return false;
        }
        if (params == null || params.size() == 0) {
            return false;
        }
        String jsonString = JSON.toJSONString(params);
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        String endpoint = "";
        if (esTypeSwitch) {
            endpoint = "/" + indexName + "/" + indexType;
        }else {
            endpoint = "/" + indexName + "/_doc";
        }
        if (StringUtil.isNotEmpty(id)) {
            endpoint = endpoint + "/" + id;
        }
        
        Request request = new Request("POST",endpoint);
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response indexResponse = restClient.performRequest(request);
        String reasonPhrase = indexResponse.getStatusLine().getReasonPhrase();
        if (reasonPhrase.equalsIgnoreCase("created") || reasonPhrase.equalsIgnoreCase("ok")) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esQueryDtoList 参数
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, List<EsQueryDto> esQueryDtoList, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esQueryDtoList, null, null, null, null, null, clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esQueryDtoList 参数
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsQueryDto> esQueryDtoList, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, esGeoPointDto, esQueryDtoList, null, null, null, null,null,clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esQueryDtoList 参数
     * @param sortParam 普通参数排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, List<EsQueryDto> esQueryDtoList, String sortParam, SortOrder sortOrder, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esQueryDtoList, sortParam, null, sortOrder, null, null, clazz);
    }
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esQueryDtoList 参数
     * @param geoPointDtoSortParam 经纬度參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, List<EsQueryDto> esQueryDtoList, EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        return query(indexName, indexType, null, esQueryDtoList, null, geoPointDtoSortParam, sortOrder,null,null, clazz);
    }
    
    
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esQueryDtoList 参数
     * @param sortParam 普通參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param geoPointDtoSortParam 经纬度參數排序 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageSize searchAfterSort搜索的页大小
     * @param searchAfterSort sort值
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsQueryDto> esQueryDtoList, String sortParam, EsGeoPointSortDto geoPointDtoSortParam, SortOrder sortOrder, Integer pageSize, Object[] searchAfterSort, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (sortOrder == null) {
            sortOrder = SortOrder.DESC;
        }
        if (sortParam != null) {
            //		排序
            FieldSortBuilder sort = SortBuilders.fieldSort(sortParam);
            sort.order(sortOrder);
            sourceBuilder.sort(sort);
        }
        if (geoPointDtoSortParam != null) {
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("geoPoint", geoPointDtoSortParam.getLatitude().doubleValue(), geoPointDtoSortParam.getLongitude().doubleValue());
            sort.unit(DistanceUnit.METERS);
            sort.order(sortOrder);
            sourceBuilder.sort(sort);
        }
        // 经纬度匹配
        if (esGeoPointDto != null) {
            QueryBuilder geoQuery = new GeoDistanceQueryBuilder(esGeoPointDto.getParamName()).distance(Long.MAX_VALUE, DistanceUnit.KILOMETERS)
                    .point(esGeoPointDto.getLatitude().doubleValue(), esGeoPointDto.getLongitude().doubleValue()).geoDistance(GeoDistance.PLANE);
            sourceBuilder.query(geoQuery);
        }
        // 匹配
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (EsQueryDto esQueryDto : esQueryDtoList) {
            String paramName = esQueryDto.getParamName();
            Object paramValue = esQueryDto.getParamValue();
            Date startTime = esQueryDto.getStartTime();
            Date endTime = esQueryDto.getEndTime();
            boolean analyse = esQueryDto.isAnalyse();
            
            if (paramValue != null) {
                if (analyse) {
                    QueryBuilder builds = QueryBuilders.matchQuery(paramName, paramValue);
                    boolQuery.must(builds);
                } else {
                    QueryBuilder builds = QueryBuilders.termQuery(paramName, paramValue);
                    boolQuery.must(builds);
                }
            }
            if (startTime != null) {
                QueryBuilder builds = QueryBuilders.rangeQuery(paramName).from(startTime).includeLower(true);
                boolQuery.must(builds);
            }
            if (endTime != null) {
                QueryBuilder builds = QueryBuilders.rangeQuery(paramName).to(endTime).includeUpper(true);
                boolQuery.must(builds);
            }
        }
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.query(boolQuery);
        String string = sourceBuilder.toString();
        HttpEntity entity = new NStringEntity(string, ContentType.APPLICATION_JSON);
        String endpoint = "";
        if (esTypeSwitch) {
            endpoint = "/" + indexName + "/" + indexType + "/_search";
        }else {
            endpoint = "/" + indexName + "/_search";
        }
        
        Request request = new Request("POST",endpoint);
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response response = restClient.performRequest(request);
        String results = EntityUtils.toString(response.getEntity());
        if (StringUtil.isEmpty(results)) {
            return null;
        }
        List<T> list = new ArrayList<>();
        JSONObject parse = (JSONObject) JSONObject.parse(results);
        if (parse != null) {
            JSONObject hits = parse.getJSONObject("hits");
            if (hits != null) {
                // 数据
                JSONArray hitsArr = hits.getJSONArray("hits");
                if (null != hitsArr && hitsArr.size() > 0) {
                    for (int i = 0, size = hitsArr.size(); i < size; i++) {
                        JSONObject data = hitsArr.getJSONObject(i);
                        if (null != data) {
                            JSONObject jsonObject = data.getJSONObject("_source");
                            
                            JSONArray jsonArray = data.getJSONArray("sort");
                            if (null != jsonArray && jsonArray.size() > 0) {
                                Long sort = jsonArray.getLong(0);
                                jsonObject.put("sort",sort);
                            }
                            list.add(JSONObject.parseObject(jsonObject.toJSONString(),clazz));
                        }
                    }
                }
            }
        }
        return list;
    }
    
    
    /**
     * 查询
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param dsl es的dsl语句
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> List<T> query(String indexName, String indexType, String dsl, Class<T> clazz) throws IOException {
        if (!esSwitch) {
            return new ArrayList<>();
        }
        if (StringUtil.isEmpty(dsl)) {
            return new ArrayList<>();
        }
        HttpEntity entity = new NStringEntity(dsl, ContentType.APPLICATION_JSON);
        String endpoint = "";
        if (esTypeSwitch) {
            endpoint = "/" + indexName + "/" + indexType + "/_search";
        }else {
            endpoint = "/" + indexName + "/_search";
        }
        
        Request request = new Request("POST",endpoint);
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response response = restClient.performRequest(request);
        String results = EntityUtils.toString(response.getEntity());
        if (StringUtil.isEmpty(results)) {
            return null;
        }
        List<T> list = new ArrayList<>();
        JSONObject parse = (JSONObject) JSONObject.parse(results);
        if (parse != null) {
            JSONObject hits = parse.getJSONObject("hits");
            if (hits != null) {
                // 数据
                JSONArray hitsArr = hits.getJSONArray("hits");
                if (null != hitsArr && hitsArr.size() > 0) {
                    for (int i = 0, size = hitsArr.size(); i < size; i++) {
                        JSONObject data = hitsArr.getJSONObject(i);
                        if (null != data) {
                            JSONObject jsonObject = data.getJSONObject("_source");
                            
                            JSONArray jsonArray = data.getJSONArray("sort");
                            if (null != jsonArray && jsonArray.size() > 0) {
                                Long sort = jsonArray.getLong(0);
                                jsonObject.put("sort",sort);
                            }
                            list.add(JSONObject.parseObject(jsonObject.toJSONString(),clazz));
                        }
                    }
                }
            }
        }
        return list;
    }
    
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esQueryDtoList 参数
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, List<EsQueryDto> esQueryDtoList, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, null, esQueryDtoList, null, null, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esQueryDtoList 参数
     * @param sortParam 排序参数 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, List<EsQueryDto> esQueryDtoList, String sortParam, SortOrder sortOrder, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, null, esQueryDtoList, sortParam, sortOrder, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esQueryDtoList 参数
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsQueryDto> esQueryDtoList, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        return queryPage(indexName, indexType, esGeoPointDto, esQueryDtoList, null, null, pageNo, pageSize, clazz);
    }
    
    /**
     * 查询(分页)
     *
     * @param indexName 索引名字
     * @param indexType 索引类型
     * @param esGeoPointDto 经纬度查询参数
     * @param esQueryDtoList 参数
     * @param sortParam 排序参数 不排序则为空 如果进行了排序，会返回es中的排序字段sort，需要用户在返回的实体类中添加sort字段
     * @param sortOrder 升序还是降序，为空则降序
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param clazz 返回的类型
     * @return
     * @throws IOException
     */
    public <T> PageInfo<T> queryPage(String indexName, String indexType, EsGeoPointDto esGeoPointDto, List<EsQueryDto> esQueryDtoList, String sortParam, SortOrder sortOrder, Integer pageNo, Integer pageSize, Class<T> clazz) throws IOException {
        List<T> list = new ArrayList<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        if (!esSwitch) {
            return pageInfo;
        }
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        if (sortOrder == null) {
            sortOrder = SortOrder.DESC;
        }
        if (StringUtil.isNotEmpty(sortParam)) {
            //		排序
            FieldSortBuilder sort = SortBuilders.fieldSort(sortParam);
            sort.order(sortOrder);
            sourceBuilder.sort(sort);
        }
        // 经纬度匹配
        if (esGeoPointDto != null) {
            QueryBuilder geoQuery = new GeoDistanceQueryBuilder(esGeoPointDto.getParamName()).distance(Long.MAX_VALUE, DistanceUnit.KILOMETERS)
                    .point(esGeoPointDto.getLatitude().doubleValue(), esGeoPointDto.getLongitude().doubleValue()).geoDistance(GeoDistance.PLANE);
            sourceBuilder.query(geoQuery);
        }
        // 匹配
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (EsQueryDto esQueryDto : esQueryDtoList) {
            String paramName = esQueryDto.getParamName();
            Object paramValue = esQueryDto.getParamValue();
            Date startTime = esQueryDto.getStartTime();
            Date endTime = esQueryDto.getEndTime();
            boolean analyse = esQueryDto.isAnalyse();
            
            if (paramValue != null) {
                if (analyse) {
                    QueryBuilder builds = QueryBuilders.matchQuery(paramName, paramValue);
                    boolQuery.must(builds);
                } else {
                    QueryBuilder builds = QueryBuilders.termQuery(paramName, paramValue);
                    boolQuery.must(builds);
                }
            }
            if (startTime != null) {
                QueryBuilder builds = QueryBuilders.rangeQuery(paramName).from(startTime).includeLower(true);
                boolQuery.must(builds);
            }
            if (endTime != null) {
                QueryBuilder builds = QueryBuilders.rangeQuery(paramName).to(endTime).includeUpper(true);
                boolQuery.must(builds);
            }
        }
        sourceBuilder.trackTotalHits(true);
        sourceBuilder.query(boolQuery);
        sourceBuilder.from((pageNo - 1) * pageSize);
        sourceBuilder.size(pageSize);
        String string = sourceBuilder.toString();
        HttpEntity entity = new NStringEntity(string, ContentType.APPLICATION_JSON);
        String endpoint = "";
        if (esTypeSwitch) {
            endpoint = "/" + indexName + "/" + indexType + "/_search";
        }else {
            endpoint = "/" + indexName + "/_search";
        }
        
        Request request = new Request("POST",endpoint);
        request.setEntity(entity);
        request.addParameters(Collections.<String, String>emptyMap());
        Response response = restClient.performRequest(request);
        String results = EntityUtils.toString(response.getEntity());
        if (StringUtil.isEmpty(results)) {
            return null;
        }
        JSONObject parse = (JSONObject) JSONObject.parse(results);
        if (parse != null) {
            JSONObject hits = parse.getJSONObject("hits");
            if (hits != null) {
                // 总条数
                Long value = null;
                if (esTypeSwitch) {
                    value = hits.getLong("total");
                }else {
                    JSONObject totalJSONObject = hits.getJSONObject("total");
                    if (totalJSONObject != null) {
                        value = totalJSONObject.getLong("value");
                    }
                }
                pageInfo.setTotal(value);
                pageInfo.setPageNum(pageNo);
                pageInfo.setPageSize(pageSize);
                // 数据
                JSONArray hitsArr = hits.getJSONArray("hits");
                if (null != hitsArr && hitsArr.size() > 0) {
                    for (int i = 0, size = hitsArr.size(); i < size; i++) {
                        JSONObject data = hitsArr.getJSONObject(i);
                        if (null != data) {
                            JSONObject jsonObject = data.getJSONObject("_source");
    
                            JSONArray jsonArray = data.getJSONArray("sort");
                            if (null != jsonArray && jsonArray.size() > 0) {
                                Long sort = jsonArray.getLong(0);
                                jsonObject.put("sort",sort);
                            }
                            list.add(JSONObject.parseObject(jsonObject.toJSONString(),clazz));
                        }
                    }
                }
            }
        }
        return pageInfo;
    }
}
