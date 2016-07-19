var exec = require('cordova/exec');

exports.getRfid = function(success, error) {
    exec(success, error, "Rfid", "getRfid", []);
};