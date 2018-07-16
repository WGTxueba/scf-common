package com.bytter.scf.core.apdater;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MyBatisPlusPageAdapter<T> {

    private Pageable pageable;

    public MyBatisPlusPageAdapter(Pageable pageable) {
        this.pageable = pageable;
    }

    public Page<T> toPage(){
        Sort sort = pageable.getSort();
        Page<T> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize());
        return page;
    }
}
