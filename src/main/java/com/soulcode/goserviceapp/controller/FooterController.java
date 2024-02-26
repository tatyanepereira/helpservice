package com.soulcode.goserviceapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/footer")
public class FooterController {

    @GetMapping(value = "/faq")
    public String faqs(){
        return "perguntasFrequentes";
    }
}
