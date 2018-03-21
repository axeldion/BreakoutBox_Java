package persistentie;

import java.util.List;

public interface GenericDao<T> {
    public List<T> findAll();
    public <U> T get(U id);
    public void update(T object);
    public void delete(T object);
    public void insert(T object);
    public <U> boolean exists(U id);
}