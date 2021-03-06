package com.mebitech.robe.web.dev.controller;

import com.mebitech.robe.web.dev.dao.ExampleDao;
import com.mebitech.robe.web.dev.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kamilbukum on 21/03/2017.
 */
@RestController
@RequestMapping(value = "examples")
public class ExampleController {
    @Autowired
    ExampleDao exampleDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<Example> findAll() {
        return exampleDao.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Example create(@RequestBody Example todo) {
        return exampleDao.create(todo);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public Example delete(@PathVariable("oid") String id) {
        return exampleDao.delete(id);
    }
}
