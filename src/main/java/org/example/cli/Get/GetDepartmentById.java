package org.example.cli.Get;

import org.example.cli.Command;
import org.example.service.DepartmentService;
import org.example.service.DepartmentServiceImpl;

import java.util.Scanner;

public class GetDepartmentById implements Command {
    private Scanner scn = new Scanner(System.in);
    // поле с сервисом

    public GetDepartmentById() {
        //объявление сервиса
    }

    @Override
    public void execute() {
        //вызов метода из сервиса
    }

    @Override
    public String getCommandName() {
        return "Get department by id";
    }
}
