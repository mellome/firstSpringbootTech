//解决编辑器弹出层文本框不能输入的问题
$('#articleModal').off('shown.bs.modal').on('shown.bs.modal', function (e) {
    $(document).off('focusin.modal');
});
//KindEditor变量
var editor;
$(function () {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    //详情编辑器
    editor = KindEditor.create('textarea[id="editor"]', {
        items: ['source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
            'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
            'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
            'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
            'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
            'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
            'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
            'anchor', 'link', 'unlink'],
        uploadJson: 'images/upload',
        filePostName: 'file'
    });

    $('#articleModal').on('hidden.bs.modal', function () {
        editor.html('please enter...');
    })

    $('#articleModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: 'articles/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true},
            {label: 'articleTitle', name: 'articleTitle', index: 'articleTitle', width: 240},
            {label: 'addName', name: 'addName', index: 'addName', width: 120},
            {label: 'createTime', name: 'createTime', index: 'createTime', width: 120},
            {label: 'updateTime', name: 'updateTime', index: 'updateTime', width: 120}
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'loading...',
        rownumbers: false,
        rownumWidth: 20,
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
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
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
        var id = $("#articleId").val();
        var title = $("#articleName").val();
        var addName = $("#articleAuthor").val();
        var content = editor.html();
        var data = {"articleTitle": title, "articleContent": content, "addName": addName};
        var url = 'articles/save';
        var method = 'POST';
        //id>0表示编辑操作
        if (id > 0) {
            data = {"id": id, "articleTitle": title, "articleContent": content, "addName": addName};
            url = 'articles/update';
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
                    $('#articleModal').modal('hide');
                    swal("saved successfully", {
                        icon: "success",
                    });
                    reload();
                }
                else {
                    $('#articleModal').modal('hide');
                    swal("saved failed", {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("operation error", {
                    icon: "error",
                });
            }
        });

    }
});

function articleAdd() {
    reset();
    $('.modal-title').html('add');
    $('#articleModal').modal('show');
}

function articleEdit() {
    reset();
    $('.modal-title').html('edit');

    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.get("articles/info/" + id, function (r) {
        if (r.resultCode == 200 && r.data != null) {
            //填充数据至modal
            $('#articleId').val(r.data.id);
            $('#articleName').val(r.data.articleTitle);
            $('#articleAuthor').val(r.data.addName);
            editor.html('');
            editor.html(r.data.articleContent);
        }
    });
    //显示modal
    $('#articleModal').modal('show');
}

/**
 * 数据验证
 */
function validObject() {
    var articleName = $('#articleName').val();
    if (isNull(articleName)) {
        showErrorInfo("title can't be empty!");
        return false;
    }
    if (!validLength(articleName, 120)) {
        showErrorInfo("Title character cannot be greater than 120!");
        return false;
    }
    var articleAuthor = $('#articleAuthor').val();
    if (isNull(articleAuthor)) {
        showErrorInfo("author name can't be empty!");
        return false;
    }
    if (!validLength(articleAuthor, 10)) {
        showErrorInfo("author character cannot be greater than 10!");
        return false;
    }
    var ariticleContent = editor.html();
    if (isNull(ariticleContent) || ariticleContent == 'please enter...') {
        showErrorInfo("content can't be empty!");
        return false;
    }
    if (!validLength(ariticleContent, 8000)) {
        showErrorInfo("content character cannot be greater than 8000!");
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
    $('#articleId').val(0);
    $('#articleName').val('');
    $('#articleAuthor').val('');
    $('#ariticleContent').val('');
}

function deleteArticle() {
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
                url: "articles/delete",
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
