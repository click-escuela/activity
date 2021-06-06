package click.escuela.activity.service;

import java.util.List;

import click.escuela.activity.exception.TransactionException;

public interface ActivityServiceGeneric<T, S> {

	public void create(T entity) throws TransactionException;

	public List<S> findAll();
}
