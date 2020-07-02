package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @GetMapping("/customers")
    public ModelAndView listCustomers(@RequestParam("s") Optional<String> keyword, @RequestParam("page") Optional<Integer> page) {
        Page<Customer> customers;
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        int pageNum = 0;
        if(page.isPresent() && page.get() > 1) pageNum = page.get() - 1;
        PageRequest pageSplitter = new PageRequest(pageNum, 3);
        if(keyword.isPresent()){
            customers = customerService.findAllByFirstNameContaining(keyword.get(), pageSplitter);
            modelAndView.addObject("keyword", keyword.get());
        } else {
            customers = customerService.findAll(pageSplitter);
        }

        modelAndView.addObject("customers", customers);
        return modelAndView;
    }
}
