function sendLabelsToPrinter(labelsInZPL2) {
    if (labelsInZPL2.length == 0) {
        return 0;
    } else {
        printZpl(labelsInZPL2.toString());
    }
};

function printZpl(zpl) {
    var printWindow = window.open();
    printWindow.document.open('text/plain')
    printWindow.document.write(zpl);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
    printWindow.close();
}