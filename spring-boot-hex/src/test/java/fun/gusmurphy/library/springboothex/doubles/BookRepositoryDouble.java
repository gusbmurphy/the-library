package fun.gusmurphy.library.springboothex.doubles;

import fun.gusmurphy.library.springboothex.adapter.persistence.ListBookRepository;

public class BookRepositoryDouble extends ListBookRepository {

    public void clear() {
        bookList.clear();
    }
}
