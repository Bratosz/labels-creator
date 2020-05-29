var tableSize = 150;
var totalRows = 0;
var totalCols = 4;


function displayTable() {
    // $("#table-rows > tr:not (#row-template)").remove();
    const $rowTemplate = $("#row-template");
    for(let i = 0; i < tableSize; i++) {
        const $row = $rowTemplate.clone();
        $row.css("display", "table-row");
        $("#table-rows").append($row);
    }
};

function displayTableInSize(size) {
    tableSize = size;
    displayTable();
}

$("#button-generate-table").click(function () {
    let tableSize = $("#table-size-input").val;
    displayTableInSize(tableSize);
})

$("#button-generate-labels").click(function () {
    let labelsFormat = $("#labels-format-input").val();
    let employees = [];
    for(let i = 1; i < totalRows; i++) {
        let row = $('#example tbody tr').eq(i);
        let firstName = row.find('.cell-first-name input').val();
        let lastName = row.find('.cell-last-name input').val();
        let lockerNumber = row.find('.cell-locker-number input').val();
        let boxNumber = row.find('.cell-box-number input').val();

        let employee = {
            firstName: firstName,
            lastName: lastName,
            lockerNumber: lockerNumber,
            boxNumber: boxNumber
        };
        employees.push(employee);
    }
    console.log(employees);
    $.ajax({
        url: `http://localhost:8080/labels/create/from_list/${labelsFormat}`,
        method: "post",
        data: JSON.stringify(employees),
        contentType: "application/json",
        success: function (response) {
            console.log(employees);
            console.log(response);
            alert("Plik z najklejkami został pobrany i jest gotowy do druku");
            window.open(response.fileDownloadUri);
        }
    })
});

$(document).ready(function () {
    $('td input').bind('paste', null, function (e) {
        $txt = $(this);
        setTimeout(function () {
            var values = $txt.val().split(/\s+/);
            console.log(values);
            var currentRowIndex = 1;
            var currentColIndex = $txt.parent().index();

            totalRows = values.length / 4;
            totalCols = 4;
            var count = 0;
            for(let i = currentRowIndex; i < totalRows; i++) {
                for(let j = 0; j < totalCols; j++) {
                    let value = values[count];
                    let input = $('#example tbody tr').eq(i).find('td').eq(j).find('input');
                    input.val(value);
                    count++;
                }
            }
        }, 0);
    });
});

displayTable();
