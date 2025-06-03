package model;

public interface Consulta<T> {
    public abstract boolean registrar(T obj);
    public abstract boolean modificar(T obj);
    public abstract boolean eliminar(T obj);
    public abstract boolean buscar(T obj);
}
