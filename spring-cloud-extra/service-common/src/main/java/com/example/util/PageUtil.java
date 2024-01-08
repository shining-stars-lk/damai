package com.example.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dto.BasePageDto;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-08
 **/
public class PageUtil {
    
    public static IPage getPageParams(BasePageDto basePageDto) {
        return getPageParams(basePageDto.getPageNumber(), basePageDto.getPageSize());
    }
    
    public static IPage getPageParams(int pageNumber, int pageSize) {
        return new Page(pageNumber, pageSize);
    }
    
    public static <T> IPage<T> convertPage(IPage page, List<T> list){
        IPage<T> newPage = new Page<T>();
        BeanUtil.copyProperties(page,newPage);
        newPage.setRecords(list);
        return newPage;
    }
}
