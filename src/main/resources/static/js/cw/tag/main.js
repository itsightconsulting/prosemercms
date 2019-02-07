var $table = $('#tblRegistros');
var $index = $('#hIdTag');

let modalFileSort = $('#sortArchivos').get(0);
let modalImageSort = $('#sortImagenes').get(0);
//CK EDITOR BODY
var iFrameBody = '';

$(function () {
    $(document).on('keypress', function (e) {
        var tag = e.target.tagName.toLowerCase();
        if (e.keyCode === $.ui.keyCode.ENTER)
            e.preventDefault();
    });

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    pageSetUp();
    listarRegistros();

    //V A L I D A T E
    validarRegistros();
    validarBusqueda();

    $("#btnNuevo").click(function () {
        window.location.href = _ctx+'gestion/tag/nuevo';
    });

    $("#btnGuardar").click(function () {
        registro();
    });

    $("#btnCancelar").click(function () {
        irListado($index);
    });

    $("#btnFiltrar").click(function () {
        listarRegistros();
    });
});

function listarRegistros() {

    if ($("#frm_busqueda").valid()) {

        $table.bootstrapTable('destroy');
        var dataRpta;

        var comodin = $("#txtFiltro").val();
        var tipo = $("#TipoTag").val();

        if (comodin == null || comodin == "") {
            comodin = "0";
        }
        if (tipo == null || tipo == "") {
            tipo = "0";
        }

        $table.bootstrapTable({
            url: _ctx+'gestion/tag/obtenerListado/' + comodin + '/' + tipo,
            pagination: true,
            sidePagination: 'client',
            pageSize: 10,
            onLoadSuccess: d => {},
            onLoadError: function (xhr, ajaxOptions, thrownError) {
                exception(xhr["status"], xhr["responseJSON"]["error"]);
            },
        });
    }
}

function registro() {

    if ($("#frm_registro").valid()) {
        var tags = $("#Tags").val();
        var params = new Object();
        params.id = $("#hIdTag").val();
        params.nombre = $("#Nombre").val().charAt(0).toUpperCase() + $("#Nombre").val().slice(1);
        params.tipo = $("#hIdTipoTag").val();

        $.ajax({
            type: 'POST',
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            url: _ctx+'gestion/tag/agregar',
            dataType: "json",
            data: params,
            success: function (data, textStatus) {
                if (textStatus == "success") {
                    if (data == "-9") {
                        $.smallBox({
                            content: "<i> El registro ha fallado debido a que el nombre de tag ya existe en el sistema...</i>",
                            timeout: 4500,
                            color: "alert",
                        });
                    } else {
                        $.smallBox({
                            content: "El registro ha sido actualizado satisfactoriamente..."
                        });
                    }
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                exception(xhr["status"], xhr["responseJSON"]["error"]);
            },
            complete: function (data) {
                $('#hIdTag').val(0);
                limpiarBusqueda();
                irListado($index);
                listarRegistros();
            }
        });
    }

}

function editar(id, tipo) {
    var param = new Object();
    param.id = id;

    $("#load_pace").show();
    $.ajax({
        type: 'GET',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url: _ctx+'gestion/tag/obtener',
        dataType: "json",
        data: param,
        success: function (data, textStatus) {
            if (textStatus == "success") {
                $("#hIdTag").val(data["id"]);
                $("#hIdTipoTag").val(tipo);
                $("#Nombre").val(data["nombre"]);
                $("#TipoTagId").val(tipo);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            exception(xhr["status"], xhr["responseJSON"]["error"]);
        },
        complete: function (data) {
            limpiarBusqueda();
            irRegistro();
            $("#load_pace").show();
        }
    });
}

function linkFormatter(value, row, index) {
    return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id + ',' + row.tipoTag.id + ')" title="Editar">' + row.nombre + '</a>', value);
}

function Opciones(value, row, index) {
    opciones = `<a href='#' onclick='javascript:baja(${row.id});' title='Eliminar Tag'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
    return opciones;
}

function baja(id, tipo, index) {
    $.smallBox({
        content: "¿Estás seguro de querer eliminar el registro? <p class='text-align-right'><a href='javascript:confirmarBaja(" + id + ");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
        color: "#296191",
        timeout: 10000,
        icon: "fa fa-bell swing animated",
        iconSmall: "",
    });
}

function confirmarBaja(id) {

    $.ajax({
        type: 'GET',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url: _ctx+'gestion/tag/eliminar/' + id,
        dataType: "json",
        success: function (data, textStatus) {
            if (textStatus == "success") {
                if (data == "-9") {
                    $.smallBox({
                        content: "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
                        timeout: 4500,
                        color: "alert",
                    });
                } else {
                    $.smallBox({
                        content: "El registro se ha eliminado satisfactoriamente."
                    });
                }
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            exception(xhr["status"], xhr["responseJSON"]["error"]);
        },
        complete: function (data) {
            listarRegistros();
        }
    });
}

function validarRegistros() {
    $("#frm_registro").validate({
        errorClass: errorClass,
        errorElement: errorElement,
        highlight: function (element) {
            $(element).parent().removeClass('state-success').addClass("state-error");
            $(element).removeClass('valid');
        },
        unhighlight: function (element) {
            $(element).parent().removeClass("state-error").addClass('state-success');
            $(element).addClass('valid');
        },
        ignore: ".ignore",
        rules: {
            Nombre: {
                required: true,
                maxlength: $("#Nombre").prop("maxlength"),
            },
        },
        messages: {
            Nombre: {
                required: "El campo es obligatorio",
                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
            },
        },
        submitHandler: function () {
            registro();
        }
    });
}

function validarBusqueda() {
    $("#frm_busqueda").validate({
        ignore: ".ignore",
        errorClass: "my-error-class",
        validClass: "my-valid-class",
        rules: {
            Filtro: {
                required: true,
            },
            Tipo: {
                digits: true,
            }
        },
        messages: {
            Filtro: {
                required: "Debe seleccionar un tipo de formato",
            },
            Tipo: {
                digits: "Debe seleccionar un tipo",
            }
        },
        submitHandler: function () {

        }
    });
}

function limpiarBusqueda() {
    $('#txtFiltro').val("");
    $('#Estado').val("-1");
}