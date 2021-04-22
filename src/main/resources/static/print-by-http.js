let printerIp = "192.168.0.67";
const printerPort = 9100;

function sendLabelsToPrinter(labelsInZPL2) {
    if (labelsInZPL2.length == 0) {
        return 0;
    } else {
        $.ajax({
            url: `http://` + printerIp + `:` + printerPort,
            method: "post",
            data: labelsInZPL2.toString(),
            success: function () {
                alert("Etykiety wysłano do drukarki.");
            }
        });
    }
};