
function showreporthalfadmin(file_type) {
    $('#reportdata').html("");
    $.post('getdatahalfadmin', {type: file_type}, function (data, status) {
        if (status === "success") {
            //console.log(data);
            if (file_type === "REPLIED_FILE") {
                var head = "<table class='table table-bordered'><tr><th>File No</th><th>Subject</th><th>Sent Date</th></tr>";
                var body = "";
                for (var i in data) {
                    var obj = data[i];
                    body += "<tr><td>" + obj.file_no + "</td><td>" + obj.subject + "</td><td>" + obj.send_date + "</td></tr>";
                }
                var tail = "</table>";
                $('#reportdata').html(head + body + tail);
            } else {
                var head = "<table class='table table-bordered'><tr><th>File No</th><th>Subject</th><th>Sent Date</th><th>Due Days</th></tr>";
                var body = "";
                for (var i in data) {
                    var obj = data[i];
                    var duedays = new Date(obj.target_date).getDate() - new Date(obj.send_date).getDate();
                    body += "<tr><td>" + obj.file_no + "</td><td>" + obj.subject + "</td><td>" + obj.send_date + "</td><td>" + duedays + "</td></tr>";
                }
                var tail = "</table>";
                $('#reportdata').html(head + body + tail);
            }
        }
    });
}

function showreport(file_type) {
    $('#reportdata').html("");
    $.post('getdata', {type: file_type}, function (data, status) {
        if (status === "success") {
           // console.log(data);
            var head = "<table class='table table-bordered'><tr><th>File No</th><th>Subject</th><th>Sent Date</th><th>Due Days</th></tr>";
            var body = "";
            for (var i in data) {
                var obj = data[i];
                var duedays = new Date(obj.target_date).getDate() - new Date(obj.send_date).getDate();
                body += "<tr><td>" + obj.file_no + "</td><td>" + obj.subject + "</td><td>" + obj.send_date + "</td><td>" + duedays + "</td></tr>";
            }
            var tail = "</table>";
            $('#reportdata').html(head + body + tail);
        }
    });
}
function showreportadmin(file_type) {
    $('#reportdata').html("");
    $.post('getdataadmin', {type: file_type}, function (data, status) {
        if (status === "success") {
            //console.log(data);
            if (file_type === 'REPLIED_FILE') {
                var head = "<table class='table table-bordered'><tr><th>File No</th><th>Subject</th><th>Received Date</th><th>Sent By</th></tr>";
                var body = "";
                for (var i in data) {
                    var obj = data[i];
                    body += "<tr><td>" + obj.file_no + "</td><td>" + obj.subject + "</td><td>" + obj.send_date + "</td><td>" + obj.sender_id + "</td></tr>";
                }
                var tail = "</table>";
                $('#reportdata').html(head + body + tail);
            } else {
                var head = "<table class='table table-bordered'><tr><th>File No</th><th>Subject</th><th>Sent Date</th><th>Due Days</th><th>Sent To</th></tr>";
                var body = "";
                var to_Admins = "";
                for (var i in data) {
                    var obj = data[i];
                    for (var j in obj.to_admins) {
                        to_Admins = to_Admins + ", " + obj.to_admins[j];
                    }
                    to_Admins = to_Admins.substring(1, to_Admins.length);
                    var duedays = new Date(obj.target_date).getDate() - new Date(obj.send_date).getDate();
                    body += "<tr><td>" + obj.file_no + "</td><td>" + obj.subject + "</td><td>" + obj.send_date + "</td><td>" + duedays + "</td><td>" + to_Admins + "</td></tr>";
                }
                var tail = "</table>";
                $('#reportdata').html(head + body + tail);
            }
        }
    });
}

function editProfile(ele) {

    document.getElementById('submitButton').type = 'submit';
    ele.style.display = 'none';
    document.getElementById('profileUpdate').style.display = 'none';
    document.forms.updateprofile.style.display = 'block';
}

function showreply(counter) {
    document.getElementById(counter + "inf").style.display = 'none';
    document.getElementById(counter + "rep").style.display = 'block';
}


function checkforuser(ele) {
    $.post("checkforuser", {id: ele.value}, function (data, status) {
        if (status === 'success') {
            if (String(data).trim() === 'true') {
                $("#checkuser").css('display', 'block');
                ele.value = "";
            } else {
                $("#checkuser").css('display', 'none');
            }
        }
    });
}

$('.marquee').marquee({
    duration: 6000,
    gap: 50,
    delayBeforeStart: 0,
    direction: 'up',
    duplicated: false,
    pauseOnHover: true
});
function diabledate() {
    $('#targetdate').attr('disabled', 'disabled');
}
function makeUse() {
    $('#targetdate').removeAttr('disabled');
}

function colorChange(ele) {
    var e = ele.parentElement.parentElement.parentElement.parentElement;
    $(e).toggleClass("bg-warning");
}

function colorChange2(ele) {
    var e = ele.parentElement.parentElement.parentElement.parentElement;
    $(e).toggleClass("bg-success");
}

setTimeout(function () {
    $(".close-alerts").click();
}, 2500);
try {
    function displayreportdates() {

        if (!document.forms.reports.dateselect.checked) {
            $("#showreportdates").hide();
        } else {
            $("#showreportdates").show();
        }

    }
} catch (err) {
    console.log(err);
}