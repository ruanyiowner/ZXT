package com.yjedu.zxt.dataAdapter;
/**
 * 分页
 * @author Administrator
 *
 */
public class Pager {
    /**
     * 每页记录数
     */
    public int pageSize=20;
    /**
     * 当前页索引
     */
    public int currentPageIndex=0;
    /**
     * 总记录数
     */
    public int totalItemsCount=0;
    /**
     * 是否已经全部加载完成。
     */
    public Boolean allLoaded = false;
    /**
     * 是否加载成功。
     */
    public Boolean isLoadedSuccess = true;


}