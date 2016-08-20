package com.march.quickrvlibs.model;

/**
 * 存储类型信息的实体类
 *
 * @author chendong
分类适配器配置信息实体类,包内访问
 */
public class RvAdapterConfig {

    /**
     * @param type  item类型，int类型变量，Item是什么类型的就填写什么类型
     * @param resId 资源id，对应类型的资源id，你需要装载的资源文件的ID
     */
    public RvAdapterConfig(int type, int resId) {
        super();
        this.type = type;
        this.resId = resId;
        this.viewCount = 5;
    }

    private int type;
    private int resId;
    private int viewCount;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public int getViewCount() {
        return viewCount;
    }

}