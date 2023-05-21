package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;
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
	/**
	 * This method returns all the projects in database
	 * @return List<Project>
	 */
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}
	/**
	 * This method returns specific project
	 * @param projectId
	 * @return Project
	 */
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fecthProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException(
						"Project with project ID=" + projectId
							+ " does not exist."));
		
	}
	/**
	 * This method project will update an existing data
	 * @param project
	 */
	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID="+project.getProjectId()+" does not exist");
		}
	}
	/**
	 * This method will delete a project in database
	 * @param projectId
	 */
	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID="+projectId+" does not exist");
		}
		
	}

}
