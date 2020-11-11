package com.myweb.service;

import com.myweb.dao.TagDAO;
import com.myweb.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService
{
    @Autowired
    private TagDAO tagDAO;

    @Override
    public List<TagVO> selectTagList() throws Exception
    {
        return tagDAO.selectTagList();
    }
}
