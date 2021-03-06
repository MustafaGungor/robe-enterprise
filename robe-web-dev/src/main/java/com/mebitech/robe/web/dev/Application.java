package com.mebitech.robe.web.dev;

import com.mebitech.robe.web.WebApplication;
import com.mebitech.robe.web.dev.cli.sample.InitialCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * Created by kamilbukum on 03/03/2017.
 */

@ComponentScan({"com.mebitech.robe.web.dev"})
public class Application extends WebApplication {

    @Autowired
    StandardServletEnvironment environment;

    @Autowired
    InitialCommand initialCommand;

    public static void main(String[] args) {
        run(Application.class, args);
    }

    @Override
    public void init(ApplicationArguments applicationArguments) {
        initialCommand.run();
    }
}
