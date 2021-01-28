let tableSize = 96;
let totalRows = 0;
let printerIp = "192.168.0.74";
const printerPort = 9100;

displayTable();

$("#button-print-from-table").click(function () {
    let labelsFormat = $("#input-labels-format").val();
    let employees = getEmployees();
    let editorType = $('input[name="editor-type"]:checked').val();

    let inputPlantNumber = $("#input-plant-number");
    let alertPlantNumber = $("#alert-input-plant-number");
    let plantNumber = inputPlantNumber.val();
    if (isPlantNumberCorrect(plantNumber)) {
        snuffInput(inputPlantNumber);
        hideAlert(alertPlantNumber);
        // console.log(employees);
        if (editorType == "PRINTER") {
            generateLabelsInZPL2AndPrint(labelsFormat, employees, plantNumber);
        } else {
            generateSpreadSheetFile(labelsFormat, editorType, employees, plantNumber);
        }
    } else {
        highlightInput(inputPlantNumber);
        displayAlert(alertPlantNumber, "Błędny nr zakładu.");
    }
});

$("#button-print-numbers-from-range").click(function () {
    let inputBeginNumber = $('#input-begin-number');
    let inputEndNumber = $('#input-end-number');
    let alertNumbersRange = $('#alert-numbers-range');

    let numbersFormat = $('input[name="numbers-format"]:checked').val();
    let lockersCapacity = $('input[name="locker-capacity"]:checked').val();
    let beginNumber = inputBeginNumber.val();
    let endNumber = inputEndNumber.val();

    if (isPassedNumbersCorrect(beginNumber, endNumber)) {
        snuffInput(inputBeginNumber);
        snuffInput(inputEndNumber);
        hideAlert(alertNumbersRange);
        generateAndPrintLabelsWithNumbersOnlyFromRangeInZPL2(
            beginNumber, endNumber, lockersCapacity, numbersFormat);
    } else {
        highlightInput(inputBeginNumber);
        highlightInput(inputEndNumber);
        displayAlert(alertNumbersRange, "Niewłaściwe numery szaf.")
    }
});

$("#button-print-custom-content").click(function () {
    let inputTextField = $('#input-text-field');
    let inputLabelsAmount = $('#input-labels-amount');

    let alertForTextField = $('#alert-text-field');

    let contentToPrint = inputTextField.val();
    let labelsAmount = inputLabelsAmount.val();

    if(parametersAreCorrect(contentToPrint, labelsAmount)) {
       snuffInput(inputTextField);
       snuffInput(inputLabelsAmount);
       hideAlert(alertForTextField);
       generateLabelsWithCustomContentInZPL2(
           contentToPrint, labelsAmount);
    } else if(contentIsEmpty(contentToPrint) && labelsAmountIsWrong(labelsAmount)){
        highlightInput(inputTextField);
        highlightInput(inputLabelsAmount);
        displayAlert(alertForTextField, "Coś poszło nie tak ;-)")
    } else if(contentIsEmpty(contentToPrint)) {
        highlightInput(inputTextField);
        displayAlert(alertForTextField, "Pole tekstowe jest puste.");
    } else if(labelsAmountIsWrong(labelsAmount)) {
        highlightInput(inputLabelsAmount);
        displayAlert(alertForTextField, "Podano niewłaściwą ilość naklejek.")
    }

    function parametersAreCorrect(contentToPrint, labelsAmount) {
        if(contentIsEmpty(contentToPrint) || labelsAmountIsWrong(labelsAmount)) {
            return false;
        } else {
            return true;
        }
    }

    function contentIsEmpty(contentToPrint) {
        if(contentToPrint == "") {
            return true;
        } else {
            return false;
        }
    }

    function labelsAmountIsWrong(labelsAmount) {
        if((labelsAmount <= 0) || ((labelsAmount % 1) != 0)) {
            return true;
        } else {
            return false;
        }
    }
});

$("#button-change-ip").click(function () {
    let ip = $("#input-ip").val();
    changeIP(ip);
    alert("Ustawiono nowe IP: " + printerIp + " port: " + printerPort);
});

function isPassedNumbersCorrect(beginNumber, endNumber) {
    if(beginNumber == "") {
        return false;
    } else if ((beginNumber >= 0) && (endNumber == "")) {
        return true;
    } else if (beginNumber <= endNumber) {
        return true;
    } else {
        return false;
    }
}

function hideAlert(a) {
    a.empty();
}

function displayAlert(alert, content) {
    alert.css("color", "#E20015");
    alert.css("font-weight", "700");
    alert.empty();
    alert.append(content);
}

function isPlantNumberCorrect(plantNumber) {
    if ((plantNumber > 99)
        && (plantNumber < 1000)) {
        return true;
    } else {
        return false;
    }
}

function highlightInput(b) {
    b.css("border-color", "#E20015")
}

function snuffInput(b) {
    b.css("border-color", "black");
}


function changeIP(ip) {
    printerIp = ip;
}

function displayTable() {
    // $("#table-rows > tr:not (#row-template)").remove();
    let $rowTemplate = $("#row-template");
    for (let i = 0; i < tableSize; i++) {
        let $row = $rowTemplate.clone();
        $row.css("display", "table-row");
        $("#table-rows").append($row);
        totalRows++;
        if (totalRows > 3000) {
            alert("Maksymalny rozmiar tabeli to 3000 wierszy");
            return;
        }
    }
}


function displayTableInSize(size) {
    tableSize = size;
    displayTable();
}

$("#button-generate-table").click(function () {
    let tableSize = $("#input-table-size").val();
    displayTableInSize(tableSize);
});


function generateSpreadSheetFile(labelsFormat, editorType, employees, plantNumber) {
    $.ajax({
        // url: `http://localhost:8080/labels/create_from_table/spread_sheet/${labelsFormat}/${editorType}/${plantNumber}`,
        url: `http://naklejkomat.herokuapp.com/labels/create_from_table/spread_sheet/${labelsFormat}/${editorType}/${plantNumber}`,
        method: "post",
        data: JSON.stringify(employees),
        contentType: "application/json",
        success: function (response) {
            console.log(employees);
            console.log(response);
            alert("Plik z najklejkami został pobrany i jest gotowy do druku");
            window.open(response.fileDownloadUri);
        }
    });
}

function generateLabelsInZPL2AndPrint(labelsFormat, employees, plantNumber) {
    $.ajax({
        url: `http://naklejkomat.herokuapp.com/labels/create_from_table/zpl2/${labelsFormat}/${plantNumber}`,
        // url: `http://localhost:8080/labels/create_from_table/zpl2/${labelsFormat}/${plantNumber}`,
        method: "post",
        data: JSON.stringify(employees),
        contentType: "application/json",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki.");
        }
    });
};

function generateAndPrintLabelsWithNumbersOnlyFromRangeInZPL2(beginNumber, endNumber, capacity, numbersFormat) {
    if(endNumber === "") {
        endNumber = 0;
    }
    $.ajax({
        url: `http://naklejkomat.herokuapp.com/labels/create_from_range/zpl2/${beginNumber}/${endNumber}/${capacity}/${numbersFormat}`,
        // url: `http://localhost:8080/labels/create_from_range/zpl2/${beginNumber}/${endNumber}/${capacity}/${numbersFormat}`,
        method: "post",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki.");
        }
    })
}

function generateLabelsWithCustomContentInZPL2(contentToPrint, labelsAmount) {
    $.ajax({
        url: `http://naklejkomat.herokuapp.com/labels/create_from_custom_content/zpl2/${contentToPrint}/${labelsAmount}`,
        // url: `http://localhost:8080/labels/create_from_custom_content/zpl2/${contentToPrint}/${labelsAmount}`,
        method: "post",
        success: function(ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki");
        }
    })
}


function sendLabelsToPrinter(labelsInZPL2) {
    if (labelsInZPL2.length == 0) {
        return 0;
    } else {
        $.ajax({
            url: `http://` + printerIp + `:` + printerPort,
            method: "post",
            data: labelsInZPL2.toString(),
        });
    }
    // location.reload();

};

$(document).ready(function () {
    $('input').on('paste', function (e) {
        let $this = $(this);
        $.each(e.originalEvent.clipboardData.items, function (index, value) {
            if (value.type === 'text/plain') {
                value.getAsString(function (text) {
                    let x = $this.closest('td').index();
                    let y = $this.closest('tr').index() + 1;
                    text = text.trim('\r\n');
                    $.each(text.split('\r\n'), function (i2, v2) {
                        $.each(v2.split('\t'), function (i3, v3) {
                            let cellNameValue = v3.trim().toUpperCase();
                            if (isItHeaderRow(cellNameValue)) {
                                y -= 1;
                                return false;
                            }
                            let row = y + i2;
                            let col = x + i3;
                            $this.closest('table').find('tr:eq(' + row + ') td:eq(' + col + ') input').val(v3);
                        });

                    });
                });
            }
        });
        return false;
    });
});

function isItHeaderRow(cellNameValue) {
    if ((cellNameValue == "IMIE") ||
        (cellNameValue == "IMIE:") ||
        (cellNameValue == "IMIE :") ||
        (cellNameValue == "IMIĘ") ||
        (cellNameValue == "IMIĘ:") ||
        (cellNameValue == "IMIĘ :") ||
        (cellNameValue == "NAME") ||
        (cellNameValue == "FIRSTNAME")) {
        return true;
    } else {
        return false;
    }
};

function getEmployees() {
    let employees = [];
    $(".table-row").each(function (index) {
        $this = $(this);
        if (isRowFilled($this)) {
            let firstName = $this.find("#cell-first-name").val();
            let lastName = $this.find("#cell-last-name").val();
            let lockerNumber = $this.find("#cell-locker-number").val();
            let boxNumber = $this.find("#cell-box-number").val();

            employee = {
                firstName: firstName,
                lastName: lastName,
                lockerNumber: lockerNumber,
                boxNumber: boxNumber
            };

            employees.push(employee);
        }
    });
    return employees;
};

function isRowFilled(row) {
    let firstCell = row.find("#cell-first-name").val();
    if (firstCell.length >= 1) {
        return true;
    } else {
        return false;
    }
};



