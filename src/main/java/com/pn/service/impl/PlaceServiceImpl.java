package com.pn.service.impl;

import com.pn.entity.Place;
import com.pn.mapper.PlaceMapper;
import com.pn.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ljj
 * @create 2023/8/26 9:01
 */
//指定缓存的名称即键的前缀,一般是@CacheConfig标注的类的全类名
@CacheConfig(cacheNames = "com.pn.service.impl.PlaceServiceImpl")
@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private PlaceMapper placeMapper;
    /*
      查询所有产地的业务方法
     */
    //对查询到的所有产地进行缓存,缓存到redis的键为all:place
    @Cacheable(key = "'all:place'")
    @Override
    public List<Place> queryAllPlace() {
        return placeMapper.findAllPlace();
    }
}
