(function () {
    const tableThead = document.getElementById('table-thead');
    const tableTbody = document.getElementById('table-tbody');
    const pdfLinkDiv = document.getElementById('pdf-link-div');
    const buttonDiv = document.getElementById('button-div');

    function removeAllChildNodes(element) {
        while (element.hasChildNodes()) {
            element.removeChild(element.lastChild);
        }
    }

    function clear(elements) {
        for (const element of elements) {
            removeAllChildNodes(element);
        }
    }

    function addRow(parent, type, values) {
        const tr = document.createElement('tr');
        parent.appendChild(tr);
        for (const value of values) {
            const child = document.createElement(type);
            child.innerText = value;
            tr.appendChild(child);
        }
    }

    function addHeaderRow(values) {
        addRow(tableThead, 'th', values);
    }

    function addBodyRow(values) {
        addRow(tableTbody, 'td', values);
    }

    function addButtonBooks(url) {
        const button = document.createElement('button');
        buttonDiv.appendChild(button);
        button.innerText = 'Książki';
        button.addEventListener('click', function () {
            clear([tableThead, tableTbody, pdfLinkDiv, buttonDiv]);
            showLink('/books/pdf', 'Książki');
            $.ajax({url: url + '/books', dataType: 'json', type: 'get'}).done(function (array) {
                addHeaderRow(['id', 'tytuł', 'autor', 'rok', 'wydawnictwo', 'kategoria', 'opis']);
                for (const object of array) {
                    addBodyRow(getBookValues(object));
                }
            })
        });
    }

    function addButtonDelete(url) {
        const button = document.createElement('button');
        buttonDiv.appendChild(button);
        button.innerText = 'Usuń';
        button.addEventListener('click', function () {
            $.ajax({url: url, type: 'delete'})
                .done(function () {
                    alert('Element został usunięty.');
                    clear([tableThead, tableTbody, pdfLinkDiv, buttonDiv]);
                })
                .fail(function () {
                    alert('Wystąpił błąd.');
                })
        });
    }

    function addBodyRowButton(url, getValues) {
        const td = document.createElement('td');
        const button = document.createElement('button');
        td.appendChild(button);
        tableTbody.lastChild.appendChild(td);
        button.innerText = '->';
        button.addEventListener('click', function () {
            removeAllChildNodes(tableTbody);
            if (!url.startsWith('/books')) addButtonBooks(url);
            addButtonDelete(url);
            $.ajax({url: url, dataType: 'json', type: 'get'}).done(function (object) {
                addBodyRow(getValues(object));
            })
        });
    }

    function generateGetValuesFunction(keys) {
        return function (object) {
            const values = [];
            for (const key of keys) {
                values.push(object[key]);
            }
            return values;
        }
    }

    function getBookValues(book) {
        const category = book.category ? book.category.name : null;
        const publisher = book.publisher ? book.publisher.name : null;
        let authors = '';
        if (book.bookAuthors) {
            for (const ba of book.bookAuthors) {
                authors += (ba.author.firstName ? ba.author.firstName + ' ' : '') + ba.author.lastName + ', ';
            }
            authors = authors.slice(0, -2);
        } else {
            authors = null;
        }
        return [book.id, book.title, authors, book.year, publisher, category, book.description];
    }

    function showLink(href, innerText) {
        const a = document.createElement('a');
        a.href = href;
        a.innerText = innerText;
        pdfLinkDiv.appendChild(a);
    }

    function setOnClickGET(buttonId, url, headerValues, getValues) {
        $(buttonId).on('click', function () {
            clear([tableThead, tableTbody, pdfLinkDiv, buttonDiv]);
            showLink(url + '/pdf', this.innerText);
            $.ajax({url: url, dataType: 'json', type: 'get'}).done(function (array) {
                addHeaderRow(headerValues);
                for (const object of array) {
                    addBodyRow(getValues(object));
                    addBodyRowButton(url + '/' + object.id, getValues);
                }
            })
        });
    }

    setOnClickGET('#authors-button', '/authors', ['id', 'imię', 'nazwisko'], generateGetValuesFunction(['id', 'firstName', 'lastName']));
    setOnClickGET('#books-button', '/books', ['id', 'tytuł', 'autor', 'rok', 'wydawnictwo', 'kategoria', 'opis'], getBookValues);
    setOnClickGET('#categories-button', '/categories', ['id', 'nazwa'], generateGetValuesFunction(['id', 'name']));
    setOnClickGET('#publishers-button', '/publishers', ['id', 'nazwa'], generateGetValuesFunction(['id', 'name']));
})();
