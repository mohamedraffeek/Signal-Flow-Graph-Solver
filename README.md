# Signal Flow Graph Solver
A web-based signal flow graph solver built using angular and spring boot frameworks.

## Contents:
- [Main Features](#Main-Features)
- [Sample Runs](#Sample-Runs)
- [User Guide](#User-Guide)
- [Frameworks and technology used](#Frameworks-and-technology-used)
- [How to run](#How-to-run)
- [Demo Video](#Demo-Video)

---

## Main Features:
Our program supports an exceptional UI powered by the JointJS Library.
It makes it easier for the user to interact with the flow graph, modifying edges and moving nodes around to make it clear and easy to deal with.

The main features of the program can be summed up as follows:
* A user-friendly UI built with Angular components.

![image](https://user-images.githubusercontent.com/96317608/236682723-8259baea-9c6c-47e9-abad-d37742068d1f.png)

* Interactive buttons and various CSS active and hover effects.

![image](https://user-images.githubusercontent.com/96317608/236682748-106628d3-2e2a-4699-aa38-c9827e398c8d.png)
![image](https://user-images.githubusercontent.com/96317608/236682757-fef41656-de1f-4889-a1a6-339a28a2747b.png)
![image](https://user-images.githubusercontent.com/96317608/236682763-7ecfdf4e-607d-46b1-a506-7ded434ef3c5.png)
![image](https://user-images.githubusercontent.com/96317608/236682770-e1b51e2d-0ac8-49c1-a8a0-4142ebf1d1c7.png)

* Draggable graph nodes, and smart dynamic edge routing mechanisms

![image](https://user-images.githubusercontent.com/96317608/236682824-4dce4761-b6e1-40bf-b769-50df2c2cc8fa.png)

* The ability to select different graph elements, change their colors, and display information about them in the data box located on the right side of the screen.

![image](https://user-images.githubusercontent.com/96317608/236682858-883359eb-5699-4bc1-a9b1-a784323820de.png)
![image](https://user-images.githubusercontent.com/96317608/236682866-84cc79be-acaf-4785-905d-543fe7a44b1a.png)

* The ability to delete selected edges, providing smart buttons that only are enabled if an edge is selected. (As the “set weight” button shown above, and the delete button shown below)

![image](https://user-images.githubusercontent.com/96317608/236682890-d7e468b1-74a0-4983-bb24-de34c4f5e911.png)
![image](https://user-images.githubusercontent.com/96317608/236682900-e6249f2c-b738-46e8-9cde-be0ada970485.png)

* Compatibility with self-loops.

![image](https://user-images.githubusercontent.com/96317608/236682926-2d95bcae-65a8-4eef-8f45-3ee83fe955c6.png)

* The main purpose of the program is solving a signal flow graph, displaying the results in a pop-up window after taking the desired source and destination nodes from the user.

![image](https://user-images.githubusercontent.com/96317608/236682961-ba147666-9482-4823-b6bc-a8ac1d173927.png)
![image](https://user-images.githubusercontent.com/96317608/236682967-d3e8e879-60cc-435b-a2d0-29ffeb77837e.png)
![image](https://user-images.githubusercontent.com/96317608/236682970-91382ea1-7b24-4e43-93c3-bcd41fe7cb06.png)
![image](https://user-images.githubusercontent.com/96317608/236682978-58c96a35-a3f7-4513-920e-3d72ade0e383.png)

* Efficient back-end algorithms that take almost no time to compute the results and send them back to the front-end using the suitable HTTP requests.

---

## Sample Runs:

Sample Run 1: Simple graph
Add nodes using the “Add Node” button.

![image](https://user-images.githubusercontent.com/96317608/236683072-46630ae7-7aa8-40ca-ba8f-34ee8adee521.png)


Now, connect them using the “Add Link” button.

![image](https://user-images.githubusercontent.com/96317608/236683380-2ca99b52-ec27-49cd-80de-baef48736694.png)


Then, select each individual edge using the “Select” button and update its weight as desired using the “Update Weight” button and the input text area.

![image](https://user-images.githubusercontent.com/96317608/236683110-438e29cc-ed6a-441d-b5f3-a1ecc8497990.png)


Click on the submit button and specify the source and destination nodes.

![image](https://user-images.githubusercontent.com/96317608/236683132-911ba0e9-3c4b-4c86-a2f7-11daa065b9a5.png)


Finally, submit to the backend and receive the results.

![image](https://user-images.githubusercontent.com/96317608/236683155-8aca9292-53bc-44fe-8bc6-d6f358f2a311.png)
![image](https://user-images.githubusercontent.com/96317608/236683165-a8f71960-3c52-48e9-a200-c474c14a1d6f.png)


Sample Run 2: Complex Graph
Repeat the same steps, but increase the number of nodes and add loops with negative gains to the flow graph.
The resulting graph becomes:

![image](https://user-images.githubusercontent.com/96317608/236683180-117c1e34-6055-4e60-a478-fa808e147f09.png)


Submit to the backend (source = 0, destination = 7), the correct results are received as follows:

![image](https://user-images.githubusercontent.com/96317608/236683196-5d72158d-99e5-4050-b50a-b9f1e2353a82.png)
![image](https://user-images.githubusercontent.com/96317608/236683205-7e93646c-db5a-4f73-89f9-91bf1d9a3984.png)

---

## User Guide

### Introduction
The signal flow graph solver web application is a powerful tool for solving signal flow graphs. With this application, you can easily define nodes and edges, and let the solver do the work to find the solution. This user guide will provide you with clear and comprehensive instructions on how to use the application.

### Getting Started
To access the application, follow the steps provided in the installation guide section.

### Input and Output
To input a signal flow graph, add nodes and edges to the graph using the provided buttons. Once you have defined the graph, click the "Submit" button to run the solver. The results will be displayed on the screen, showing the solution to the signal flow graph.
You can modify the graph at any time by adding or moving nodes and edges (edges can also be deleted).

### Troubleshooting
If you encounter any issues while using the signal flow graph solver web application, try the following troubleshooting tips:
•	Check that you have input the signal flow graph correctly
•	Ensure that the graph provided has a valid solution.
•	Double check the weights on each edge.

---

## Frameworks and technology used

This project is developed as a web environment.
•	For the frontend part: Angular Framework is used.
o	JointJS Library is integrated with the Angular environment to power the UI with distinctive features.
o	An HTTP client module is added to the front-end for linking purposes.
•	For the backend part: Spring Boot and java are used.

---

## How to run:

After cloning the repository from the GitHub link provided in the form, follow the following steps:
- Frontend:
  - This project was generated with [Angular CLI](https://github.com/angular/angular-cli) -> version 15.0.2.
  - Run "ng serve" (in a CMD navigated to the project directory) for a dev server.
  - Navigate to "http://localhost:4200/". The application will automatically reload if you change any of the source files.
  - Run "ng build" to build the project. The build artifacts will be stored in the "dist/" directory.
  - To get more help on the Angular CLI use "ng help" or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli)
  - You also must run the following commands (for windows: use windows PowerShell or CMD, for Linux: use the Linux Terminal Window) inside the project directory to add the JointJS library modules to the angular environment:
    •	npm install jointjs
    •	npm install jquery
    •	npm install backbone

- Backend:
  - Download IntelliJ IDEA from the [official website](https://www.jetbrains.com/idea/).
  - Open the backend directory from IntelliJ.
  - Navigate to src/main/java/com.cs2025.SignalFlowGraphBackend.
  - You will find a class named “SignalFlowGraphBackendApplication”, run its main method.
  - If any error occurs, it is likely that you did not allow IntelliJ to build the Maven project module, if that is the case, you will find a small box at the right bottom corner of the screen that says, “A Maven build is found.” and asks for permission to build the project. Give permission, wait for a couple of minutes and you should be good to go.
  - If any other errors occur, just copy the error code and Google it, you should find solutions easily.
  
---

## Demo Video:

https://user-images.githubusercontent.com/96317608/236683859-ff7109fd-854e-4964-9a14-aaeda7334924.mp4

