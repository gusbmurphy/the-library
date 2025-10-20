package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.adapter.persistence.SetBookRepository;

public class BookRepositoryDouble extends SetBookRepository {

    public void clear() {
        bookList.clear();
    }
}
