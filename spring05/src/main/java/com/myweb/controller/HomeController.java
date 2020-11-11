package com.myweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController
{
    @RequestMapping("/")
    public String home(Model model)
    {
        model.addAttribute("message","Hello ");
        return "home";
    }
}
