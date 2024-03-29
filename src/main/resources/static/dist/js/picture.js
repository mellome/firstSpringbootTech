$(function () {
    //隐藏弹框
    $('#pictureModal').modal('hide');
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    //modal绑定hide事件
    $('#pictureModal').on('hide.bs.modal', function () {
        reset();
    })
    $("#jqGrid").jqGrid({
        url: 'pictures/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, sortable: false, hidden: true, key: true},
            {label: 'path', name: 'path', index: 'path', sortable: false, width: 105, formatter: imgFormatter},
            {label: 'remark', name: 'remark', index: 'remark', sortable: false, width: 105},
            {label: 'createTime', name: 'createTime', index: 'createTime', sortable: true, width: 80}
        ],
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        styleUI: 'Bootstrap',
        loadtext: 'loading...',
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    function imgFormatter(cellvalue) {
        return "<a href='" + cellvalue + "'> <img src='" + cellvalue + "' height=\"120\" width=\"135\" alt='pic'/></a>";
    }

    new AjaxUpload('#upload', {
        action: 'images/upload',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                swal('only support pg, png, gif format！', {
                    icon: "error",
                });
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r.resultCode == 200) {
                swal("uploaded successfully", {
                    icon: "success",
                });
                $("#picturePath").val(r.data);
                $("#img").attr("src", r.data);
                $("#img").attr("style", "width: 100px;height: 100px;display:block;");
                return false;
            } else {
                swal(r.message, {
                    icon: "error",
                });
            }
        }
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});


//绑定modal上的保存按钮
$('#saveButton').click(function () {
    //验证数据
    if (validObject()) {
        //一切正常后发送网络请求
        //ajax
        var id = $("#pictureId").val();
        var picturePath = $("#picturePath").val();
        var pictureRemark = $("#pictureRemark").val();
        var data = {"path": picturePath, "remark": pictureRemark};
        var url = 'pictures/save';
        var method = 'POST';
        //id>0表示编辑操作
        if (id > 0) {
            data = {"id": id, "path": picturePath, "remark": pictureRemark};
            url = 'pictures/update';
            method = 'PUT';
        }
        $.ajax({
            type: method,//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: url,//url
            contentType: "application/json; charset=utf-8",
            beforeSend: function (request) {
                //设置header值
                request.setRequestHeader("token", getCookie("token"));
            },
            data: JSON.stringify(data),
            success: function (result) {
                checkResultCode(result.resultCode);
                if (result.resultCode == 200) {
                    $('#pictureModal').modal('hide');
                    swal("saved successfully", {
                        icon: "success",
                    });
                    reload();
                }
                else {
                    $('#pictureModal').modal('hide');
                    swal("saved failed", {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("operation failed", {
                    icon: "error",
                });
            }
        });

    }
});

function pictureAdd() {
    reset();
    $('.modal-title').html('addPhoto');
    $('#pictureModal').modal('show');
}

function pictureEdit() {
    reset();
    $('.modal-title').html('photoEdit');

    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.ajax({
        type: "GET",
        url: "pictures/info/" + id,
        contentType: "application/json",
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("token", getCookie("token"));
        },
        success: function (r) {
            checkResultCode(r.resultCode);
            if (r.resultCode == 200 && r.data != null) {
                //填充数据至modal
                $('#pictureId').val(r.data.id);
                $('#picturePath').val(r.data.path);
                $('#pictureRemark').val(r.data.remark);
            }
        }
    });
    //显示modal
    $('#pictureModal').modal('show');
}

/**
 * 数据验证
 */
function validObject() {
    var picturePath = $('#picturePath').val();
    if (isNull(picturePath)) {
        showErrorInfo("photo can't be empty!");
        return false;
    }
    var pictureRemark = $('#pictureRemark').val();
    if (isNull(pictureRemark)) {
        showErrorInfo("remark infos can't be empty!");
        return false;
    }
    if (!validLength(pictureRemark, 150)) {
        showErrorInfo("remark character cannot be greater than 150!");
        return false;
    }
    if (!validLength(picturePath, 120)) {
        showErrorInfo("upload error!");
        return false;
    }
    return true;
}

/**
 * 重置
 */
function reset() {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");
    //清空数据
    $('#pictureId').val(0);
    $('#picturePath').val('');
    $('#pictureRemark').val('');
    $("#img").attr("style", "display:none;");
}

function deletePicture() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "confirmation",
        text: "are you sure to delete?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "DELETE",
                url: "pictures/delete",
                contentType: "application/json",
                beforeSend: function (request) {
                    //设置header值
                    request.setRequestHeader("token", getCookie("token"));
                },
                data: JSON.stringify(ids),
                success: function (r) {
                    checkResultCode(r.resultCode);
                    if (r.resultCode == 200) {
                        swal("deleted successfully", {
                            icon: "success",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });
}

/**
 * jqGrid重新加载
 */
function reload() {
    reset();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}
