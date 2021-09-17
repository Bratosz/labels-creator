let printerIp = "192.168.0.67";
const printerPort = 9100;

function sendLabelsToPrinter(labelsInZPL2) {
    console.log("wysłano");
    if (labelsInZPL2.length == 0) {
        alert("Coś poszło nie tak");
    } else {
        $.ajax({
            url: `https://` + printerIp + `:` + printerPort,
            method: "post",
            data: labelsInZPL2.toString(),
        });
    }
};