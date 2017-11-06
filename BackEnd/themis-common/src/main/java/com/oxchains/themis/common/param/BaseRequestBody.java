package com.oxchains.themis.common.param;

/**
 * @author ccl
 * @time 2017-10-20 14:08
 * @name BaseRequestBody
 * @desc:
 */
public class BaseRequestBody {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Integer pageNo;
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
