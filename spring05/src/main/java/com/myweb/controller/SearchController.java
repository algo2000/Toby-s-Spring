package com.myweb.controller;

import com.myweb.service.TagService;
import com.myweb.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController
{
    @Autowired
    private TagService tagService;

    @RequestMapping("/search")
    public String boardList(Model model) throws Exception {

        List<TagVO> list = tagService.selectTagList();
        model.addAttribute("list", list);
        return "search/search";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test()
    {
        System.out.println("asdf");
        return "success";
    }

}