package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

/**
 * This is a menu driven application that is used to perform 
 * CRUD operation on tables in schema project
 *  *
 */

public class ProjectsApp {
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project","2) List projects","3) Select a project","4) Update project details","5) Delete a project"
	);
	// @formatter:on
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	Project currProject = new Project();
	//Entry point for the application
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}
	/**
	 * This method is to get the users selections
	 */
	private void processUserSelections() {
		boolean done=false;
		while(!done) {
			try {
				int selection = getUserSelection();
				switch(selection) {
				case -1:
					done=exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println("\n "+ selection + " is not a valid selection. Try again.");
					break;
				}
			}catch(NoSuchElementException ex) {
				System.out.println("\nError: "+ex+" Try again.");
			}
		}
	}
	/**
	 * This method is used to delete projects from database
	 */
	private void deleteProject() {
		listProjects();
		
		Integer projectId=getIntInput("Enter a project ID to select a project");
		
		projectService.deleteProject(projectId);
		System.out.println("Project "+projectId+" was deleted successfully");
		if(Objects.nonNull(currProject) && currProject.getProjectId().equals(projectId)) {
			currProject=null;
		}
		
		
	}
	/**
	 * This method is used to update and existing row in database
	 */
	private void updateProjectDetails() {
		if(Objects.isNull(currProject)) {
			System.out.println("\nPlease select a project.");
		}
		
		String projectName = getStringInput("Enter the project name ["+currProject.getProjectName()+"]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours ["+currProject.getEstimatedHours()+"]");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours ["+currProject.getActualHours()+"]");
		Integer difficult = getIntInput("Enter project difficulty(1-5)  ["+currProject.getDifficulty()+"]");
		String notes = getStringInput("Enter project notes ["+currProject.getNotes()+"]");
		
		Project project = new Project();
		project.setProjectName(Objects.isNull(projectName)?currProject.getProjectName():projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours)?currProject.getEstimatedHours():estimatedHours);
		project.setActualHours(Objects.isNull(actualHours)?currProject.getActualHours():actualHours);
		project.setDifficulty(Objects.isNull(difficult)?currProject.getDifficulty():difficult);
		project.setNotes(Objects.isNull(notes)?currProject.getNotes():notes);
		project.setProjectId(currProject.getProjectId());
		
		projectService.modifyProjectDetails(project);
		currProject = projectService.fetchProjectById(currProject.getProjectId());
	}
	
	/*
	 * This method will list a specified project from database
	 */
	private void selectProject() {
		listProjects();
		
		Integer projectId=getIntInput("Enter a project ID to select a project");
		
		currProject = null;
		
		currProject = projectService.fetchProjectById(projectId);
		
	}
	/**
	 * This method is to list all projects from database
	 */
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out
				.println(" " + project.getProjectId()
						+ ": " + project.getProjectName()));
		
	}
	/**
	 * This method is for saving the project details in the table project
	 */
	private void createProject() {
		String projectName=getStringInput("Enter the project name");
		BigDecimal estimatedHours=getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours=getDecimalInput("Enter the actual hours");
		Integer difficulty=getIntInput("Enter the project difficulty (1-5)");
		String notes=getStringInput("Enter the project notes");
		
		Project project = new Project();
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: "+dbProject);
	}
	/**
	 * This method formats a input string and return BigDecimal if the input is valid
	 * @param prompt
	 * @return BigDecimal
	 */
	private BigDecimal getDecimalInput(String prompt) {
		String input = 	getStringInput(prompt);
		try {
			return new BigDecimal(input).setScale(2);
		} catch (Exception e) {
			throw new DbException(input+" is not a valid decimal number");
		}
	}
	/**
	 * This method is called when user makes incorrect selection
	 * @return boolean
	 */
	private boolean exitMenu() {
		// TODO Auto-generated method stub
		System.out.println("Exiting the menu.");
		return true;
	}
	/**
	 * This method returns the users selection based on option given
	 * @return int
	 */
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input)? -1: input;
		
	}
	/**
	 * This method formats and returns the integer value of the user entry
	 * @param prompt
	 * @return Integer
	 */
	private Integer getIntInput(String prompt) {
		String input = 	getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException nfe) {
			throw new DbException(input+" is not a valid number. Try again.");
		}
		
	}
	/**
	 * This method  captures the entry given by user in the console
	 * @param prompt
	 * @return String
	 */
	private String getStringInput(String prompt) {
		System.out.println(prompt +": ");
		String input = scanner.nextLine();
		
		return input.isBlank()?null:input.trim();
		
	}
	/**
	 * This method prints the users menu options
	 */
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter key to quit:");
		operations.forEach(line -> System.out.println(" "+line));
		
		if(Objects.isNull(currProject)){
			System.out.println("\nYou are not working with a project.");
		}else {
			System.out.println("\nYou are working with project: " + currProject);
		}
		
	}

}
