$(document).ready(function () {
    $.idcode.setCode("idcode");

    $("#login-form input").keydown(function (e) {
        if (e.keyCode == 13) {
            if ($(this).is("#btnSubmit")) {
                $("#login-form").ajaxSubmit;
            }
            else {
                $(this).next("input").focus();
            }
        }
    });

    $('#login-form').validator( {
        //validateOnSubmit: true,
        //H5validation: true,
        validate : function (validity) {
            if ($(validity.field).is('#inputIdcode')) {
                validity.valid = $.idcode.validateCode("inputIdcode");
                var cvalue = $(validity.field).val();
                //alert(validity.valid+":"+cvalue);
                if (cvalue != "" && !validity.valid) {
                    $("#err_msg p").html("验证码输入错误.");
                    $("#err_msg").removeClass("am-hide");
                    $.idcode.setCode("idcode");
                    $('#inputIdcode').focus();
                }
                else {
                    $("#err_msg p").html("");
                    $("#err_msg").addClass("am-hide");
                }
            }
        }
    });
});