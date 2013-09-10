package org.ourgrid.portal.server.logic.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ourgrid.common.statistics.util.hibernate.HibernateUtil;
import org.ourgrid.portal.server.logic.model.persistance.User;

public class UserDAO {

	public UserDAO () {}
	
	public void removeUserByLogin(String login) {
		User user = getUserByLogin(login);
		removeUser(user);
	}
	
	public void removeUser(User user) {
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
		session.delete(user);
		HibernateUtil.commitTransaction();
	}
	
	public User getUserByLogin(String login) {
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("login", login));
		
		User user = (User) criteria.uniqueResult();
		HibernateUtil.commitTransaction();
		
		return user;
	}
	
	public User getUserByMail(String email) {
		HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
		Criteria criteria = HibernateUtil.getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email));
		
		User user = (User) criteria.uniqueResult();
		HibernateUtil.commitTransaction();
		
		return user;
	}
	
	public void updateUser(String login, String passwd, String email, String profile, Boolean pending, Integer storageSize) {
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
		
		User user = new User();
		user.setLogin(login);
		user.setPassw(passwd);
		user.setEmail(email);
		user.setProfile(profile);
		user.setPending(pending);
		user.setStorageSize(storageSize);
		
		session.saveOrUpdate(user);
		session.flush();
		HibernateUtil.commitTransaction();
	}
	
	public boolean addUser(String login, String passwd, String email, String profile, Boolean pending, Integer storageSize){

		if (getUserByLogin(login) != null){
			return false;
		}
		
		if (getUserByMail(email) != null) {
			return false;
		}

		updateUser(login, passwd, email, profile, pending, storageSize);
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsers(int pageNumber, int limit, String adminLogin) {
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();
		
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.not(Restrictions.eq("login", adminLogin)));
		// once there is no limit restriction, it returns all users
		
		if (limit != 0) {
			criteria.setFirstResult(limit * (pageNumber));
			criteria.setMaxResults(limit);
		}
		
		List<User> results = (List<User>) criteria.list();
		
		HibernateUtil.commitTransaction();
		
		return results;
	}

	public void approveUser(String login) {
		User user = getUserByLogin(login);
		
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();

		user.setPending(false);
		
		session.update(user);
		session.flush();
		HibernateUtil.commitTransaction();
	}

	public void setStorageSize(String login, Integer storageSize) {
		User user = getUserByLogin(login);
		
		Session session = HibernateUtil.getSession();
		HibernateUtil.beginTransaction();

		user.setStorageSize(storageSize);
		
		session.update(user);
		session.flush();
		HibernateUtil.commitTransaction();
	}
}