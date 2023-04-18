package com.hepta.funcionarios.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.hepta.funcionarios.entity.Funcionario;

public class FuncionarioDAO {
    

	public void save(Funcionario funcionario) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(funcionario);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	public Funcionario update(Funcionario funcionario) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Funcionario funcionarioAtualizado = null;
		try {
			em.getTransaction().begin();
			funcionarioAtualizado = em.merge(funcionario);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return funcionarioAtualizado;
	}

	public void delete(Integer id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Funcionario Funcionario = em.find(Funcionario.class, id);
			em.remove(Funcionario);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}

	}

	public Funcionario find(Integer id) throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		Funcionario Funcionario = null;
		try {
			Funcionario = em.find(Funcionario.class, id);
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return Funcionario;
	}

	@SuppressWarnings("unchecked")
	public List<Funcionario> getAll() throws Exception {
		EntityManager em = HibernateUtil.getEntityManager();
		List<Funcionario> Funcionarios = new ArrayList<>();
		try {
			Query query = em.createQuery("FROM Funcionario f join fetch f.setor ");
			Funcionarios = query.getResultList();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new Exception(e);
		} finally {
			em.close();
		}
		return Funcionarios;
	}

}
