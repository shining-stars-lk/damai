/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.damai.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 分页Vo
 * @author: 阿宽不是程序员
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserVo", description ="分页返回数据")
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="pageNum", dataType ="String", value ="页码")
    private long pageNum;
    
    @ApiModelProperty(name ="pageSize", dataType ="String", value ="页大小")
    private long pageSize;
    
    @ApiModelProperty(name ="totalSize", dataType ="String", value ="记录总数")
    private long totalSize;
    
    @ApiModelProperty(name ="list", dataType ="List", value ="数据")
    private List<T> list;
}
