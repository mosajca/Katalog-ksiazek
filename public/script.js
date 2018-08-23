(function () {
    const emailInput = document.getElementById('e-mail');
    const linkDiv = document.getElementById('link-div');
    const tableThead = document.getElementById('table-thead');
    const tableTbody = document.getElementById('table-tbody');
    const buttonDiv = document.getElementById('button-div');
    const mainForm = document.getElementById('main-form');

    const authorHeaderRowValues = ['id', 'imię', 'nazwisko'];
    const bookHeaderRowValues = ['id', 'tytuł', 'autor', 'rok', 'wydawnictwo', 'kategoria', 'opis'];
    const categoryPublisherHeaderRowValues = ['id', 'nazwa'];
    const getAuthorValues = generateGetValuesFunction('id', 'firstName', 'lastName');
    const getCategoryPublisherValues = generateGetValuesFunction('id', 'name');

    setOnClickSendPdf();
    setOnClickGET('#authors-button', '/authors', authorHeaderRowValues, getAuthorValues);
    setOnClickGET('#books-button', '/books', bookHeaderRowValues, getBookValues);
    setOnClickGET('#categories-button', '/categories', categoryPublisherHeaderRowValues, getCategoryPublisherValues);
    setOnClickGET('#publishers-button', '/publishers', categoryPublisherHeaderRowValues, getCategoryPublisherValues);

    function setOnClickGET(buttonId, url, headerValues, getValues) {
        $(buttonId).on('click', function () {
            clear(linkDiv, tableThead, tableTbody, buttonDiv, mainForm);
            addSpan(this.innerText);
            addLink(url + '/pdf', 'pdf');
            addLink(url + '/csv', 'csv');
            $.ajax({url: url, dataType: 'json', type: 'get'}).done(function (array) {
                linkDiv.firstChild.innerText += ' ' + array.length;
                addHeaderRow(headerValues);
                for (const object of array) {
                    addBodyRow(getValues(object));
                    addBodyRowButton(url + '/' + object.id, getValues);
                }
            });
            addForm(url, 'post', false);
        });
    }

    function setOnClickSendPdf() {
        $('#send-pdf').on('click', function () {
            $.ajax({
                url: '/email', type: 'post', contentType: 'application/json',
                data: JSON.stringify({email: emailInput.value})
            }).done(function () {
                alert('OK.');
            }).fail(function () {
                alert('Wystąpił błąd.');
            });
        });
    }

    function generateGetValuesFunction(...keys) {
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

    function addLink(href, innerText) {
        linkDiv.appendChild(createLink(href, innerText));
    }

    function addSpan(innerText) {
        linkDiv.appendChild(createSpan(innerText));
    }

    function addHeaderRow(values) {
        tableThead.appendChild(createRow('th', values));
    }

    function addBodyRow(values) {
        tableTbody.appendChild(createRow('td', values));
    }

    function addBodyRowButton(url, getValues) {
        const td = document.createElement('td');
        td.className = 'td-with-button';
        td.appendChild(createButton('->', function () {
            clear(tableTbody, mainForm);
            if (!url.startsWith('/books')) addButtonBooks(url);
            addButtonModify(url);
            addButtonDelete(url);
            $.ajax({url: url, dataType: 'json', type: 'get'}).done(function (object) {
                addBodyRow(getValues(object));
            })
        }));
        tableTbody.lastChild.appendChild(td);
    }

    function addButtonBooks(url) {
        buttonDiv.appendChild(createButton('Książki', function () {
            clear(linkDiv, tableThead, tableTbody, buttonDiv, mainForm);
            addLink('/books/pdf', 'Książki');
            $.ajax({url: url + '/books', dataType: 'json', type: 'get'}).done(function (array) {
                addHeaderRow(bookHeaderRowValues);
                for (const object of array) {
                    addBodyRow(getBookValues(object));
                }
            })
        }));
    }

    function addButtonDelete(url) {
        buttonDiv.appendChild(createButton('Usuń', function () {
            $.ajax({url: url, type: 'delete'})
                .done(function () {
                    alert('Element został usunięty.');
                    clear(linkDiv, tableThead, tableTbody, buttonDiv, mainForm);
                })
                .fail(function () {
                    alert('Wystąpił błąd.');
                })
        }));
    }

    function addButtonModify(url) {
        buttonDiv.appendChild(createButton('Modyfikuj', function () {
            clear(mainForm);
            addForm(url, 'put', true);
        }));
    }

    function addForm(url, type, fill) {
        let add;
        if (url.includes('books')) {
            add = addFormBook;
        } else if (url.includes('authors')) {
            add = addFormAuthor;
        } else {
            add = addFormCategoryPublisher;
        }
        add(url, type, fill);
    }

    function addFormCategoryPublisher(url, type, fill) {
        mainForm.appendChild(createLabel('name', 'Nazwa:'));
        const input = createInput('name', 'text');
        mainForm.appendChild(input);
        if (fill) {
            input.value = tableTbody.firstChild.lastChild.innerText;
        }
        mainForm.appendChild(createButton('OK', function () {
            sendAndGetData(url, type, JSON.stringify({name: input.value}), getCategoryPublisherValues);
        }));
    }

    function addFormAuthor(url, type, fill) {
        mainForm.appendChild(createLabel('firstName', 'Imię:'));
        const firstNameInput = createInput('firstName', 'text');
        mainForm.appendChild(firstNameInput);
        mainForm.appendChild(createLabel('lastName', 'Nazwisko:'));
        const lastNameInput = createInput('lastName', 'text');
        mainForm.appendChild(lastNameInput);
        if (fill) {
            firstNameInput.value = tableTbody.firstChild.childNodes[1].innerText;
            lastNameInput.value = tableTbody.firstChild.childNodes[2].innerText;
        }
        mainForm.appendChild(createButton('OK', function () {
            sendAndGetData(url, type, JSON.stringify({
                firstName: firstNameInput.value,
                lastName: lastNameInput.value
            }), getAuthorValues);
        }));
    }

    function addFormBook(url, type, fill) {
        mainForm.appendChild(createLabel('title', 'Tytuł:'));
        const titleInput = createInput('title', 'text');
        mainForm.appendChild(titleInput);
        mainForm.appendChild(createLabel('author', 'Autor:'));
        const authorSelect = createSelect('author', true);
        mainForm.appendChild(authorSelect);
        mainForm.appendChild(createLabel('year', 'Rok:'));
        const yearInput = createInput('year', 'number');
        mainForm.appendChild(yearInput);
        mainForm.appendChild(createLabel('publisher', 'Wydawnictwo:'));
        const publisherSelect = createSelect('publisher', false);
        mainForm.appendChild(publisherSelect);
        mainForm.appendChild(createLabel('category', 'Kategoria:'));
        const categorySelect = createSelect('category', false);
        mainForm.appendChild(categorySelect);
        mainForm.appendChild(createLabel('description', 'Opis:'));
        const descriptionInput = createInput('description', 'text');
        mainForm.appendChild(descriptionInput);

        getAndAddOptions(authorSelect, publisherSelect, categorySelect, function () {
            if (fill) {
                const elements = tableTbody.firstChild.childNodes;
                titleInput.value = elements[1].innerText;
                selectOptions(authorSelect, elements[2].innerText.split(', '));
                yearInput.value = elements[3].innerText;
                selectOption(publisherSelect, elements[4].innerText);
                selectOption(categorySelect, elements[5].innerText);
                descriptionInput.value = elements[6].innerText;
            }
        });

        mainForm.appendChild(createButton('OK', function () {
            const book = {title: titleInput.value, year: yearInput.value, description: descriptionInput.value};
            if (categorySelect.selectedIndex > 0) {
                book.category = {id: categorySelect.value};
            }
            if (publisherSelect.selectedIndex > 0) {
                book.publisher = {id: publisherSelect.value};
            }
            if (authorSelect.selectedIndex > 0) {
                const authors = [];
                const options = authorSelect.options;
                const len = options.length;
                for (let i = 0; i < len; ++i) {
                    if (options[i].selected) {
                        authors.push({authorId: options[i].value});
                    }
                }
                book.bookAuthors = authors;
            }
            sendAndGetData(url, type, JSON.stringify(book), getBookValues);
        }));
    }

    function getAndAddOptions(authorSelect, publisherSelect, categorySelect, func) {
        $.ajax({url: '/authors', dataType: 'json', type: 'get'})
            .done(function (array) {
                addOptionsToSelect(authorSelect, array.map(function (x) {
                    const innerText = (x.firstName ? x.firstName + ' ' : '') + x.lastName;
                    return {innerText: innerText, value: x.id};
                }));
            })
            .always(function () {
                $.ajax({url: '/publishers', dataType: 'json', type: 'get'})
                    .done(function (array) {
                        addOptionsToSelect(publisherSelect, array.map(x => ({innerText: x.name, value: x.id})));
                    })
                    .always(function () {
                        $.ajax({url: '/categories', dataType: 'json', type: 'get'})
                            .done(function (array) {
                                addOptionsToSelect(categorySelect, array.map(x => ({innerText: x.name, value: x.id})));
                            })
                            .always(func);
                    });
            });
    }

    function sendAndGetData(url, type, data, getValues) {
        $.ajax({url: url, type: type, contentType: 'application/json', data: data}).done(function () {
            alert('OK.');
            clear(tableTbody);
            $.ajax({url: url, dataType: 'json', type: 'get'}).done(function (data) {
                if (data instanceof Array) {
                    linkDiv.firstChild.innerText = linkDiv.firstChild.innerText.split(' ')[0] + ' ' + data.length;
                    for (const object of data) {
                        addBodyRow(getValues(object));
                        addBodyRowButton(url + '/' + object.id, getValues);
                    }
                } else {
                    addBodyRow(getValues(data));
                }
            });
        }).fail(function () {
            alert('Wystąpił błąd.');
        });
    }

    function addOptionsToSelect(select, objects) {
        for (const object of objects) {
            const option = document.createElement('option');
            option.innerText = object.innerText;
            option.value = object.value;
            select.appendChild(option);
        }
    }

    function selectOption(select, innerText) {
        const options = select.options;
        const len = options.length;
        for (let i = 0; i < len; ++i) {
            if (options[i].innerText === innerText) {
                options[i].selected = true;
                break;
            }
        }
    }

    function selectOptions(select, array) {
        select.selectedIndex = -1;
        const options = select.options;
        const len = options.length;
        for (let i = 0; i < len; ++i) {
            if (array.includes(options[i].innerText)) {
                options[i].selected = true;
            }
        }
    }

    function clear(...elements) {
        for (const element of elements) {
            while (element.hasChildNodes()) {
                element.removeChild(element.lastChild);
            }
        }
    }

    function createButton(buttonInnerText, clickFunction) {
        const button = document.createElement('button');
        button.type = 'button';
        button.innerText = buttonInnerText;
        button.addEventListener('click', clickFunction);
        return button;
    }

    function createInput(inputId, inputType) {
        const input = document.createElement('input');
        input.id = inputId;
        input.type = inputType;
        return input;
    }

    function createLabel(labelHtmlFor, labelInnerText) {
        const label = document.createElement('label');
        label.htmlFor = labelHtmlFor;
        label.innerText = labelInnerText;
        return label;
    }

    function createLink(aHref, aInnerText) {
        const a = document.createElement('a');
        a.href = aHref;
        a.innerText = aInnerText;
        return a;
    }

    function createRow(childType, values) {
        const tr = document.createElement('tr');
        for (const value of values) {
            const child = document.createElement(childType);
            child.innerText = value;
            tr.appendChild(child);
        }
        return tr;
    }

    function createSelect(selectId, selectMultiple) {
        const select = document.createElement('select');
        select.id = selectId;
        select.multiple = selectMultiple;
        addOptionsToSelect(select, [{innerText: '-----', value: -1}]);
        select.selectedIndex = 0;
        return select;
    }

    function createSpan(innerText) {
        const span = document.createElement('span');
        span.innerText = innerText;
        return span;
    }

})();
