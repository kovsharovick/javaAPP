package org.example.cli;

import org.example.cli.Get.GetAllDepartments;
import org.example.cli.Get.GetDepartmentById;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private static Scanner scn = new Scanner(System.in);
    private static Command[] commands = new Command[]{
            new GetAllDepartments(),
            new GetDepartmentById(),
    };

    public static void run(){
        while (true){
            System.out.println();
            for (int i = 1; i <= commands.length; i++) {
                System.out.println(i + " " + commands[i - 1].getCommandName());
            }
            int inputCommand = 0;
            try {
                inputCommand = scn.nextInt();
            } catch (InputMismatchException ime){
                System.out.println("wrong command");
                continue;
            }

            if(inputCommand == -1){
                System.out.println("Program exit");
                return;
            }

            if(inputCommand > commands.length){
                System.out.println("Wrong command");
                continue;
            }

            commands[inputCommand - 1].execute();

        }

    }
}
