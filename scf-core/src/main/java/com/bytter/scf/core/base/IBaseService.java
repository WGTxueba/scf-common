package com.bytter.scf.core.base;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * <p>
 * 方便以后做扩展的Service接口
 * </p>
 *
 * @author zhoul
 * @Date 2018-06-01
 */
public interface IBaseService<T> extends IService<T> {


    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param pageable 翻页对象
     * @return
     */
    Page<T> selectPage(Pageable pageable);

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param page    翻页对象
     * @param wrapper {@link Wrapper}
     * @return
     */
    @SuppressWarnings("rawtypes")
    Page<Map<String, Object>> selectMapsPage(Pageable pageable, Wrapper<T> wrapper);

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param page    翻页对象
     * @param wrapper 实体包装类 {@link Wrapper}
     * @return
     */
    Page<T> selectPage(Pageable pageable, Wrapper<T> wrapper);

}
