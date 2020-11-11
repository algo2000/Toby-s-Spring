package com.myweb.service;

import com.myweb.vo.TagVO;

import java.util.List;

public interface TagService
{
    List<TagVO> selectTagList() throws Exception;
}
