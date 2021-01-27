package com.codegym.cms.controller;

import com.codegym.cms.model.Customer;
import com.codegym.cms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ModelAndView listCustomers( @RequestParam("s") Optional<String> keyword, @RequestParam("page") Optional<Integer> page) {
        Page<Customer> customers;
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        int pageNum = 0;
        if (page.isPresent() && page.get() > 1) {
            pageNum = page.get() - 1;
        }

        //new PageRequest(int pageNum, int pageSize, Sort.Direction direction, String... properties)
        PageRequest pageSplitter = new PageRequest(pageNum, 2, new Sort("firstName"));
        if (keyword.isPresent()) {
            customers = customerService.findAllByFirstNameContaining(keyword.get(), pageSplitter);
            modelAndView.addObject("keyword", keyword.get());
        } else {
            customers = customerService.findAll(pageSplitter);
        }

        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showCreateForm() {
        return new ModelAndView("/customer/add", "customer", new Customer());
    }

    @PostMapping("/add")
    public ModelAndView addCustomer(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasFieldErrors()) {
            return new ModelAndView("/500", "xemesis", "Hiếu lợn");
        }
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "Added");
        return new ModelAndView("redirect:/customers");
    }
}
