let tableSize = 96;
let totalRows = 0;

displayTable();


$("#button-print-from-table").click(function () {
    let labelsFormat = $("#input-labels-format").val();
    let employees = getEmployees();
    let editorType = $('input[name="editor-type"]:checked').val();

    let inputPlantNumber = $("#input-plant-number");
    let alertPlantNumber = $("#alert-input-plant-number");
    let plantNumber = getPlantNumber();
    if (isPlantNumberCorrect(plantNumber, labelsFormat)) {
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

    let labelSize = $('input[name="label-size"]:checked').val();
    let numbersFormat = $('input[name="numbers-format"]:checked').val();
    let lockersCapacity = $('input[name="locker-capacity"]:checked').val();
    let labelsOrientation = $('input[name="labels-orientation"]:checked').val();
    let beginNumber = inputBeginNumber.val();
    let endNumber = inputEndNumber.val();

    if (isPassedNumbersCorrect(beginNumber, endNumber)) {
        snuffInput(inputBeginNumber);
        snuffInput(inputEndNumber);
        hideAlert(alertNumbersRange);
        generateAndPrintLabelsWithNumbersOnlyFromRangeInZPL2(
            beginNumber, endNumber, lockersCapacity, numbersFormat, labelsOrientation,
            labelSize);
    } else {
        highlightInput(inputBeginNumber);
        highlightInput(inputEndNumber);
        displayAlert(alertNumbersRange, "Niewłaściwe numery szaf.")
    }
});

$("#button-print-locker-with-custom-boxes-range").click(function () {
    let inputLockerNumber = $('#input-locker-number');
    let inputStartingBoxNumber = $('#input-starting-box-number');
    let inputEndBoxNumber = $('#input-end-box-number');
    let inputCornerContent = $('#input-corner-content');
    let alertArea = $('#alert-for-cbr');

    let lockerNumber = inputLockerNumber.val();
    let startingBoxNumber = inputStartingBoxNumber.val();
    let endBoxNumber = inputEndBoxNumber.val();
    let labelsOrientation = $('input[name="labels-orientation-for-cbr"]:checked').val();
    let labelSize = $('input[name="label-size-for-cbr"]:checked').val();

    let cornerContentType = $('#select-corner-content-type').val();
    let cornerContent = inputCornerContent.val();

    if(labelsOrientation == "VERTICAL") {
        alert("Pionowy format tych etykiet nie jest aktywny.");
    } else if(cornerContentType == "NONE") {
        alert("Musisz wybrać typ zawartości narożnika.");
    } else if (isPassedNumbersCorrect(startingBoxNumber, endBoxNumber) &&
        lockerNumber != "" && !isEmpty(cornerContent)) {
        snuffInput(inputLockerNumber);
        snuffInput(inputStartingBoxNumber);
        snuffInput(inputEndBoxNumber);
        snuffInput(inputCornerContent);
        generateAndPrintLabelsWithLockerWithCustomBoxesRangeAndCustomCornerContentInZPL2(
            lockerNumber,
            startingBoxNumber,
            endBoxNumber,
            labelsOrientation,
            cornerContentType,
            cornerContent,
            labelSize);
    } else {
        highlightInput(inputLockerNumber);
        highlightInput(inputStartingBoxNumber);
        highlightInput(inputEndBoxNumber);
        highlightInput(inputCornerContent);
        displayAlert(alertArea, "Niewłaściwe numery szaf.");
    }
});

$("#button-print-for-189").click(function () {
    const topLeft = $('#text-input-189-top-left')
    const topRight = $('#text-input-189-top-right')
    const middle = $('#text-input-189-middle')
    const bottom = $('#text-input-189-bottom')

    const topLeftVal = topLeft.val()
    const topRightVal = topRight.val()
    const middleVal = middle.val()
    const bottomVal = bottom.val()

    const label189 = {
        topLeft: topLeftVal,
        topRight: topRightVal,
        middle: middleVal,
        bottom: bottomVal
    }

    generateAndPrintLabelsFor189Plant(label189);
})

$("#button-download-template").click(function () {
    const templateName = getSelectedTemplateName()
        window.location.href = '/excel-templates/'+ templateName + '.xlsx';
})

$("#button-print-from-file").click(function () {
    const labelsFormat = $('#input-labels-format-from-file').val()
    const file = $('#input-excel-file')[0].files[0]

    generateAndPrintLabelsFromFile(labelsFormat, file)
})

const getSelectedTemplateName = () => {
    const selectedTemplateVal = $("#input-labels-format-from-file").val();
    if (selectedTemplateVal === 'FOR_189_PLANT') {
        return '189-szablon'
    } else {
        console.error("Nie wybrano szablonu")
    }
}

$("#button-print-custom-content").click(function () {
    let inputTextField = $('#input-text-field');
    let inputLabelsAmount = $('#input-labels-amount');

    let alertForTextField = $('#alert-text-field');

    let contentToPrint = inputTextField.val();
    let labelsAmount = inputLabelsAmount.val();

    if (parametersAreCorrect(contentToPrint, labelsAmount)) {
        snuffInput(inputTextField);
        snuffInput(inputLabelsAmount);
        hideAlert(alertForTextField);
        generateAndPrintLabelsWithCustomContentInZPL2(
            contentToPrint, labelsAmount);
    } else if (textInputIsEmpty(contentToPrint) && labelsAmountIsWrong(labelsAmount)) {
        highlightInput(inputTextField);
        highlightInput(inputLabelsAmount);
        displayAlert(alertForTextField, "Coś poszło nie tak ;-)")
    } else if (textInputIsEmpty(contentToPrint)) {
        highlightInput(inputTextField);
        displayAlert(alertForTextField, "Pole tekstowe jest puste.");
    } else if (labelsAmountIsWrong(labelsAmount)) {
        highlightInput(inputLabelsAmount);
        displayAlert(alertForTextField, "Podano niewłaściwą ilość naklejek.")
    }

    function parametersAreCorrect(contentToPrint, labelsAmount) {
        if (textInputIsEmpty(contentToPrint) || labelsAmountIsWrong(labelsAmount)) {
            return false;
        } else {
            return true;
        }
    }

    function textInputIsEmpty(contentToPrint) {
        if (contentToPrint == "") {
            return true;
        } else {
            return false;
        }
    }

    function labelsAmountIsWrong(labelsAmount) {
        if ((labelsAmount <= 0) || ((labelsAmount % 1) != 0)) {
            return true;
        } else {
            return false;
        }
    }
});

function getPlantNumber() {
    let plantNumber = $('#input-plant-number').val();
    if (plantNumber === "") {
        return 0;
    } else {
        return plantNumber;
    }
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

function isPassedNumbersCorrect(beginNumber, endNumber) {
    if (beginNumber == "") {
        return false;
    } else if ((parseInt(beginNumber) >= 0) && (endNumber == "")) {
        return true;
    } else if (parseInt(beginNumber)<= parseInt(endNumber)) {
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

function isPlantNumberCorrect(plantNumber, labelsFormat) {
    if ((plantNumber > 99)
        && (plantNumber < 10000)) {
        return true;
    } else if (labelsFormat == "FIRST_NAME_AND_LAST_NAME") {
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
        url: getActualLocation() +
            `/labels/create_from_table/spread_sheet/${labelsFormat}/${editorType}/${plantNumber}`,
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
        url: getActualLocation() +
            `/labels/create-from-table/zpl2/${labelsFormat}/${plantNumber}`,
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

function generateAndPrintLabelsWithNumbersOnlyFromRangeInZPL2(
    beginNumber, endNumber, capacity, numbersFormat, labelsOrientation, labelSize) {
    if (endNumber === "") {
        endNumber = 0;
    }
    $.ajax({
        url: getActualLocation() +
            `/labels/create-from-range/zpl2` +
            `/${beginNumber}` +
            `/${endNumber}` +
            `/${capacity}` +
            `/${numbersFormat}` +
            `/${labelsOrientation}` +
            `/${labelSize}`,
        method: "post",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki");
        }
    })
}

function generateAndPrintLabelsWithLockerWithCustomBoxesRangeAndCustomCornerContentInZPL2(
    lockerNumber, startingBoxNumber, endBoxNumber, labelsOrientation, cornerContentType, cornerContent, labelSize){
    if(endBoxNumber == "") endBoxNumber = 0;
    $.ajax({
        url: getActualLocation() +
            `/labels/create-with-custom-boxes-range-and-custom-corner-content/zpl2` +
            `/${lockerNumber}` +
            `/${startingBoxNumber}` +
            `/${endBoxNumber}` +
            `/${labelsOrientation}` +
            `/${cornerContentType}` +
            `/${cornerContent}` +
            `/${labelSize}`,
        method: 'post',
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki");
        }
    })
}

function generateAndPrintLabelsFor189Plant(label189) {
    $.ajax({
        url: getActualLocation() +
            `/labels/create-for-189/zpl2`,
        method: 'post',
        data: JSON.stringify(label189),
        contentType: "application/json",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki");
        }
    })
}

function generateAndPrintLabelsFromFile(labelsFormat, file) {
    const formData = new FormData()
    formData.append('file', file)
    $.ajax({
        url: getActualLocation() +
            `/labels/create-from-file/zpl2/${labelsFormat}`,
        method: 'post',
        data: formData,
        contentType: false,
        processData: false,
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki");
        }
    })
}

function generateAndPrintLabelsWithCustomContentInZPL2(contentToPrint, labelsAmount) {
    $.ajax({
        url: getActualLocation() +
            `/labels/create-from-custom-content/zpl2/${contentToPrint}/${labelsAmount}`,
        method: "post",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
            console.log(ZPLGeneratedExpression);
            alert("Etykiety wysłano do drukarki.");
        }
    })
}

$(document).ready(function () {
    $('input').on('paste', function (e) {
        let $this = $(this);
        console.log(e);
        $.each(e.originalEvent.clipboardData.items, function (index, value) {
            if (value.type === 'text/plain') {
                value.getAsString(function (text) {
                    let x = $this.closest('td').index();
                    let y = $this.closest('tr').index() + 1;
                    text = text.trim('\r\n');
                    console.log(text.split('\n'));
                    $.each(text.split('\n'), function (i2, v2) {
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
            console.log("empty");
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
    let firstName = row.find("#cell-first-name").val();
    let lastName = row.find("#cell-last-name").val();
    let locker = row.find("#cell-locker-number").val();
    let box = row.find("#cell-box-number").val();
    if (firstName.length >= 1 || lastName.length >= 1 || locker >= 1 || box >= 1) {
        return true;
    } else {
        return false;
    }
};



