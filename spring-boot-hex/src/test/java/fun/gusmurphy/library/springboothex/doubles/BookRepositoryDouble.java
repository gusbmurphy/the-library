package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.adapter.setpersistence.SetBookRepository;

public class BookRepositoryDouble extends SetBookRepository {

    public void clear() {
        bookSet.clear();
    }
}
