package org.example.repository;

import org.example.entities.Department;

public interface DepartmentRepository extends Repository<Department, Integer>{
    Department getDepartmentByName(String name);

}
