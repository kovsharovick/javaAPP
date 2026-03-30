package org.example.cli.Get;

import org.example.cli.Command;
import org.example.service.DepartmentService;
import org.example.service.DepartmentServiceImpl;

public class GetAllDepartments implements Command {
    // поле с сервисом

    public GetAllDepartments() {
        //объявление сервиса
    }

    @Override
    public void execute() {
        //вызов метода из сервиса
    }

    @Override
    public String getCommandName() {
        return "Get all departments";
    }
}
