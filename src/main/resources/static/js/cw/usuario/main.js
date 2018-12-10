var $table = $('#tblRegistros');
var $index = $('#hIdUsuario');

$(function () {

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

	//FK CBO
	listarPerfiles();


	$("#btnNuevo").click(function () {
		$('#Username').removeAttr('disabled');
		irRegistro();
	});

	$("#btnGuardar").click(function () {
		registro();
	});

	$("#btnCancelar").click(function () {
		$('#Username').removeAttr('disabled');
		irListado($index);
		$('#frm_registro').trigger("reset");
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
		var estado = $("#Estado").val();
		var perfil = $("#Perfil").val();


		if (comodin == null || comodin == "") { comodin = "0"; }
		if (estado == null) { estado = "-1"; }
		if (perfil == null || perfil == "") { perfil = "0"; }

		$table.bootstrapTable({
			url: _ctx+'gestion/usuario/obtenerListado/' + comodin + '/' + estado + '/' + perfil,
			pagination: true,
			sidePagination: 'client',
			pageSize: 10,
			onLoadError: function (xhr, ajaxOptions, thrownError) {
				exception(xhr["status"], xhr["responseJSON"]["error"]);
			},
		});
	}
}

function registro() {
	if ($("#frm_registro").valid()) {
		var params = new Object();
		params.id = $("#hIdUsuario").val();
		params.nombres = $("#Nombres").val();
		params.apellidoPaterno = $("#ApellidoPaterno").val();
		params.apellidoMaterno = $("#ApellidoMaterno").val();
		params.fkPerfil = $("#PerfilId").val();
		params.username = $("#Username").val();
		params.password = $("#Password").val();
		params.flagActivo = $('#FlagActivo').is(':checked');

		$.ajax({
			type: 'POST',
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			url: _ctx+'gestion/usuario/agregar',
			dataType: "json",
			data: params,
			success: function (data, textStatus) {
				if (textStatus == "success") {
					if (data == "-9") {
						$.smallBox({
							content: "<i> El registro ha fallado debido a que el nombre de usuario ya existe en el sistema...</i>",
							timeout: 4500,
							color: "alert",
						});
					} else if (data == "2") {
						$.smallBox({
							content: "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
						});
					} else {
						$.smallBox({});
					}
				}
			},
			error: function (xhr, ajaxOptions, thrownError) {
				exception(xhr["status"], xhr["responseJSON"]["error"]);
			},
			complete: function (data) {
				$('#hIdUsuario').val(0);
				limpiarBusqueda();
				irListado($index);
				listarRegistros();
				$('#frm_registro').trigger("reset");
			}
		});
	}
}

function editar(id) {

	var param = new Object();
	param.id = id;

	$('#Username').attr('disabled', 'disabled');

	$("#load_pace").show();
	$.ajax({
		type: 'GET',
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		url: _ctx+'gestion/usuario/obtenerUsuario',
		dataType: "json",
		data: param,
		success: function (data, textStatus) {
			if (textStatus == "success") {
				$("#hIdUsuario").val(data["id"]);
				$("#PerfilId").val(data["perfil"]["id"]);
				$("#Username").val(data["username"]);
				$("#Password").val(data["password"].substring(0, 10));
				$("#CheckPassword").val(data["password"].substring(0, 10));
				$("#ApellidoPaterno").val(data["apellidoPaterno"]);
				$("#ApellidoMaterno").val(data["apellidoMaterno"]);
				$("#Nombres").val(data["nombres"]);
				if (data["flagActivo"] === true) {
					$('#FlagActivo').prop('checked', true);
				} else { $('#FlagActivoOff').prop('checked', true); }
			}
		},
		error: function (xhr, ajaxOptions, thrownError) {
			exception(xhr["status"], xhr["responseJSON"]["error"]);
		},
		complete: function (data) {
			limpiarBusqueda();
			$('#Username-error').attr('hidden', 'hidden');
			$('#Username-error').attr('style', 'display:none');
			$('#Password-error').attr('hidden', 'hidden');
			$('#Password-error').attr('style', 'display:none');
			irRegistro();
			$("#load_pace").show();
		}
	});
}

function desactivar(id, ix) {

	$.SmartMessageBox({
		title: "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
		content: "<br/>¿Quieres activar o desactivar el registro?",
		buttons: '[No][Si]'
	}, function (ButtonPressed) {
		if (ButtonPressed === "Si") {
			var param = new Object();
			param.id = id;

			$("#load_pace").show();
			$.ajax({
				type: 'POST',
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				url: _ctx+'gestion/usuario/desactivarUsuario',
				dataType: "json",
				data: param,
				success: function (data, textStatus) {
					if (textStatus == "success") {
						$.smallBox({
							content: "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
						});
                        actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[6].children[0].textContent;

                        if (actualStatus == "Activo") {
                        	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[6].innerHTML = '<span class="label label-danger">Inactivo</span>';
    		            } else {
    		            	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[6].innerHTML = '<span class="label label-success">Activo</span>';
    		            }
					}
				},
				error: function (xhr, ajaxOptions, thrownError) {
					exception(xhr["status"], xhr["responseJSON"]["error"]);
				},
				complete: function (data) {
					limpiarBusqueda();
				}
			});
		} else { }
	})

}

function listarPerfiles() {
	var combo = $("#PerfilId");
	var combo2 = $("#Perfil");

	$.ajax({
		type: 'GET',
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		url: _ctx+'gestion/perfil/listarTodos',
		dataType: "json",
		success: function (data, textStatus) {
			if (textStatus == "success") {
				combo.html('');
				combo.append($("<option />").text("  -- Seleccione -- "));
				combo2.append($("<option />").val("").text("TODOS"));
				$.each(data, function () {
					combo.append($("<option />").val(this.id).text(this.nombre));
					combo2.append($("<option />").val(this.id).text(this.nombre));
				});


			}
		},
	});
}

function linkFormatter(value, row, index) {
	return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id + ')" title="Editar">' + row.nombreCompleto + '</a>', value);
}

function Opciones(value, row, index) {
	var desactivar = '';

	if (row.id > 0) {
		desactivar = "<a href='#' onclick='javascript:desactivar(" + row.id +","+ index +");' title='Actualizar status'><i class='glyphicon glyphicon-refresh'></i></a>";
	}

	return desactivar;
}
function isActived(value, row, index) {

	if (row.flagActivo) {

		return '<span class="label label-success">Activo</span>';
	} else {
		return '<span class="label label-danger">Inactivo</span>';
	}
}

$.validator.addMethod("pwcheck", function (value, options) {
	if (value != "") {
		return value.match(/^(?=.*\d)(?=.*[a-z])[0-9a-zA-Z!@#\$%\^\&*\)\(+=._-]{8,}$/)
	}
	return true;
});


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
			Nombres: {
				required: true,
				maxlength: $("#Nombres").prop("maxlength"),
			},
			ApellidoPaterno: {
				required: true,
				maxlength: $("#ApellidoPaterno").prop("maxlength"),
			},
			ApellidoMaterno: {
				required: true,
				maxlength: $("#ApellidoMaterno").prop("maxlength"),
			},
			Username: {
				required: true,
				email: true,
				maxlength: $("#Username").prop("maxlength"),
			},
			Password: {
				required: true,
				maxlength: $("#Password").prop("maxlength"),
				pwcheck: true,
			},
			CheckPassword: {
				equalTo: '#Password',
			},
			PerfilId: {
				required: true,
				digits: true,
			},
		},
		messages: {
			Nombres: {
				required: "El campo es obligatorio",
				maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
			},
			ApellidoPaterno: {
				required: "El campo es obligatorio",
				maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
			},
			ApellidoMaterno: {
				required: "El campo es obligatorio",
				maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
			},
			Username: {
				required: "<i class='form-control-feedback glyphicon glyphicon-remove'></i>El campo es obligatorio",
				maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres."),
				email: "Formato inválido",
			},
			Password: {
				required: "El campo es obligatorio",
				maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres."),
				pwcheck: 'Password inválida, revisar <a style="color:#dd4814;" href="javascript:MostrarRequisitosPassword();">requisitos</a>'
			},
			CheckPassword: {
				equalTo: "Por favor ingresar la misma password nuevamente",
			},
			PerfilId: {
				required: "El campo es obligatorio",
				digits: "Seleccione un perfil válido",
			}
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
			Perfil: {
				digits: true,
			}
		},
		messages: {
			Filtro: {
				required: "Debe seleccionar un tipo de formato",
			},
			Perfil: {
				digits: "Debe seleccionar un perfil",
			}
		},
		submitHandler: function () {

		}
	});
}

function MostrarRequisitosPassword() {

	var mjeValidaciones = "";
	mjeValidaciones = mjeValidaciones + "La contraseña debe cumplir con las siguientes condiciones:" + "</br></br>";
	mjeValidaciones = mjeValidaciones + "<i class='fa fa-check'></i> Tener mínimo 8 caracteres." + "</br>";
	mjeValidaciones = mjeValidaciones + "<i class='fa fa-check'></i> Tener mínimo un dígito." + "</br>";
	mjeValidaciones = mjeValidaciones + "<i class='fa fa-check'></i> Tener mínimo un caracter en minúscula o mayúscula." + "</br>";
	mjeValidaciones = mjeValidaciones + "<i class='fa fa-check'></i> No debe contar con espacios." + "</br>";
	mjeValidaciones = mjeValidaciones + "<i class='fa fa-clock-o'></i> <bold style=\"color:#dd4814\">Ejemplo: Pablo24rio</bold> ";

	$.SmartMessageBox({
		title: "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
		content: "<br/>" + mjeValidaciones,
		buttons: '[Aceptar]',
	}, function (ButtonPressed) {
		if (ButtonPressed === "Aceptar") { }

	});
}

function limpiarBusqueda() {
	$('#txtFiltro').val("");
	$('#Estado').val("-1");
	$('#Perfil').val("");
}