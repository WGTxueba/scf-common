package com.bytter.scf.core.json;

/**
 * 用于@JsonView注解，配合实体显示指定的内容
 */
public class View {

    /**
     * 总体的视图
     */
    public interface Summary {
    }

    /**
     * 详情视图
     */
    public interface Detail extends Summary {
    }
}


