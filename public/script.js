function clearTable() {
    removeAllChildNodes(document.getElementById('table-thead'));
    removeAllChildNodes(document.getElementById('table-tbody'));
}

function removeAllChildNodes(element) {
    while (element.hasChildNodes()) {
        element.removeChild(element.lastChild);
    }
}

function addHeaderRow(values) {
    addRow(document.getElementById('table-thead'), 'th', values);
}

function addBodyRow(values) {
    addRow(document.getElementById('table-tbody'), 'td', values);
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

function generateGetValuesFunction(keys) {
    return function (object) {
        const values = [];
        for (const key of keys) {
            values.push(object[key]);
        }
        return values;
    }
}

function showLink(href, innerText) {
    const div = document.getElementById('pdf-link');
    removeAllChildNodes(div);
    const a = document.createElement('a');
    a.href = href;
    a.innerText = innerText;
    div.appendChild(a);
}

function setOnClickGET(buttonId, getUrl, headerValues, getValues) {
    $(buttonId).on('click', function () {
        clearTable();
        showLink(getUrl + '/pdf', this.innerText);
        $.ajax({url: getUrl, dataType: 'json', type: 'get'}).done(function (array) {
            addHeaderRow(headerValues);
            for (const object of array) {
                addBodyRow(getValues(object));
            }
        })
    });
}

setOnClickGET('#authors-button', '/authors', ["id", "imię", "nazwisko"], generateGetValuesFunction(['id', 'firstName', 'lastName']));
setOnClickGET('#books-button', '/books', ["id", "tytuł", "autor", "rok", "wydawnictwo", "kategoria", "opis"], getBookValues);
setOnClickGET('#categories-button', '/categories', ["id", "nazwa"], generateGetValuesFunction(['id', 'name']));
setOnClickGET('#publishers-button', '/publishers', ["id", "nazwa"], generateGetValuesFunction(['id', 'name']));
