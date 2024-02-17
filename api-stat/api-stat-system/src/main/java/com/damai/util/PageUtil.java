package com.damai.util;

import com.damai.dto.PageDto;
import com.damai.info.PageInfo;

public class PageUtil {

    public static PageInfo getPageInfo(PageDto pageDto, RecordTotalRun recordTotalRun) {
        Long pageNo = pageDto.getPageNo();
        Long pageSize = pageDto.getPageSize();
        Long totalRecord = recordTotalRun.totalRecord();

        Long pageTotal = totalRecord/pageSize + 1;

        // 计算起始索引和结束索引
        long start = (pageNo - 1) * pageSize;
        long end = start + pageSize - 1;

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNo(pageNo);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotalRecord(totalRecord);
        pageInfo.setPageTotal(pageTotal);
        pageInfo.setStart(start);
        pageInfo.setEnd(end);
        return pageInfo;
    }
}
