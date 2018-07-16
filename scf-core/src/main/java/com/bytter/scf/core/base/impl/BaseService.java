package com.bytter.scf.core.base.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bytter.scf.core.apdater.MyBatisPlusPageAdapter;
import com.bytter.scf.core.base.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * <p>
 * BaseService 实现子类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 * <br/>
 * 方便后期扩展
 * </p>
 *
 * @author zhoul
 * @Date 2018-06-01
 */
public class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private MyBatisPlusPageAdapter<T> getMyBatisPlusPageAdapter(Pageable pageable) {
        return new MyBatisPlusPageAdapter<>(pageable);
    }

    /**
     * <p>
     * 翻页查询
     * </p>
     *
     * @param pageable 翻页对象
     * @return
     */
    @Override
    public Page<T> selectPage(Pageable pageable) {
        return super.selectPage(getMyBatisPlusPageAdapter(pageable).toPage());
    }

    @Override
    public Page<Map<String, Object>> selectMapsPage(Pageable pageable, Wrapper<T> wrapper) {
        return super.selectMapsPage(getMyBatisPlusPageAdapter(pageable).toPage(), wrapper);
    }

    @Override
    public Page<T> selectPage(Pageable pageable, Wrapper<T> wrapper) {
        return super.selectPage(getMyBatisPlusPageAdapter(pageable).toPage(), wrapper);
    }
}
