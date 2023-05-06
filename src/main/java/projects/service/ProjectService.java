package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;
/**
 * 
 * This is service layer which will have the CRUD actions
 *
 */
public class ProjectService {
	
	ProjectDao projectDao = new ProjectDao();
	/**
	 * This method is used to add project details to database
	 * @param project
	 * @return Project
	 */
	public Project addProject(Project project) {
		
		return projectDao.insertProject(project);
	}

}
