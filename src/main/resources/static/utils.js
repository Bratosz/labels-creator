function getActualLocation() {
    return window.location.origin;
}

function isEmpty(content) {
    if(content == null || content == undefined || content == "") {
        return true;
    } else {
        return false;
    }
}