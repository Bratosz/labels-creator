let tableSize = 96;
let totalRows = 0;
let printerIp = "192.168.0.96";
const printerPort = 9100;


$("#button-change-ip").click(function () {
    let ip = $("#input-ip").val();
    changeIP(ip);
    alert("Ustawiono nowe IP: " + printerIp + " port: " + printerPort);
});

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

$("#button-print-labels").click(function () {
    let labelsFormat = $("#labels-format-input").val();
    let employees = getEmployees();
    let editorType = $('input[name="editor-type"]:checked').val();
    console.log(employees);
    if (editorType == "PRINTER") {
        generateLabelsInZPL2AndPrint(labelsFormat, employees);
    } else {
        generateSpreadSheetFile(labelsFormat, editorType, employees);
    }
});

function generateSpreadSheetFile(labelsFormat, editorType, employees) {
    $.ajax({
        // url: `http://localhost:8080/labels/create/from_table/${labelsFormat}/${editorType}`,
        url: `http://naklejkomat.herokuapp.com/labels/create/from_table/${labelsFormat}/${editorType}`,
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

function generateLabelsInZPL2AndPrint(labelsFormat, employees) {
    $.ajax({
        url: `http://naklejkomat.herokuapp.com/labels/generate_in_zpl2/${labelsFormat}`,
        // url: `http://localhost:8080/labels/generate_in_zpl2/${labelsFormat}`,
        method: "post",
        data: JSON.stringify(employees),
        contentType: "application/json",
        success: function (ZPLGeneratedExpression) {
            sendLabelsToPrinter(ZPLGeneratedExpression);
        }
    });
};


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
    location.reload();
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
        (cellNameValue == "IMIĘ") ||
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
    if (firstCell.length > 1) {
        return true;
    } else {
        return false;
    }
};


displayTable();
