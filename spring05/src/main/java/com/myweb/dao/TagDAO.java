package com.myweb.dao;

import com.myweb.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDAO
{
    List<TagVO> selectTagList() throws Exception;
}
