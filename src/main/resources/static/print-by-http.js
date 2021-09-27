let printerIp = "192.168.0.96";
const printerPort = 9100;
let labelsToPrint = "";

function sendLabelsToPrinter(labelsInZPL2) {
    console.log("wysłano");
    if (labelsInZPL2.length == 0) {
        alert("Coś poszło nie tak");
    } else {
        $.ajax({
            url: `http://` + printerIp + `:` + printerPort,
            method: "post",
            data: labelsInZPL2.toString(),
        });
    }
};