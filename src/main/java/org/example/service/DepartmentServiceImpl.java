package org.example.service;

import org.example.entities.Department;
import org.example.repository.DepartmentRepository;
import org.example.repository.DepartmentRepositoryInMemImpl;

import java.util.Arrays;
import java.util.List;

public class DepartmentServiceImpl implements DepartmentService {

    //
    private static DepartmentService obj;

    //поле с используемыми репозитроями

    private DepartmentServiceImpl() {
        //объявление репозиториев
    }

    //для синглтон-паттерна
    public static DepartmentService getInstance() {
        if (obj == null) {
            obj = new DepartmentServiceImpl();
        }
        return obj;
    }

    private Department setInfoForNewDepartment(String[] parameters) {
        //метод обработки входных данных и создания экземпляра сущности
        return null;
    }

    @Override
    public Department getById(int id) {
        //обращение к репозиторию
        return null;
    }

    @Override
    public List<Department> getAll() {
        //обращение к репозиторию
        return null;
    }
}
