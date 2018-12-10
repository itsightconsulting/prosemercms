			var $table = $('#tblRegistros');
			var $index = $('#hIdParametro');
			
				$(function() {
					
					pageSetUp();
					listarRegistros();
					
					//V A L I D A T E
					validarRegistros();
					validarBusqueda();
					
					$("#btnNuevo").click(function () {
				    	$('#DescargarImagenFromForm').attr('hidden','hidden');
						irRegistro();
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
				        
				        if(comodin == null || comodin == ""){comodin = "0";}
				        
				        $table.bootstrapTable({		            
				        	url: _ctx+'gestion/parametro/obtenerListado/'+comodin,
				        	pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: function(data){
				        	},
				        	onLoadError: function (xhr, ajaxOptions, thrownError) {
				        		exception(xhr, xhr);
				        	},
				        });
					}
				}
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				        var params = new Object();
				        params.id = $("#hIdParametro").val();
						params.clave = $("#Clave").val();
						params.valor = $("#Valor").val();
				                 
				        return $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+'gestion/parametro/agregar',
				            dataType: "json",
				            data: params,
				            success: function (data, textStatus) {
				                	if(params.id>0){
				                		$.smallBox({
											content : "<i class='fa fa-child'></i> <i>Actualización exitosa...!</i>",
										});
				                	}else{
				                		$.smallBox({}); 
				                	}
				            },
				            error: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				            },
				            complete: function (data) {
				            	$('#hIdParametro').val(0);
				            	limpiarBusqueda();
				            	irListado($index);
				            	listarRegistros();
				            }
				        });
				    }
				}
				
				function editar(id) {
				    
				    var param = new Object();
				    param.id = id;
				    				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'GET',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+'gestion/parametro/obtenerParametro',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdParametro").val(data["id"]);
				                $("#Clave").val(data["clave"]);
				                $("#Valor").val(data["valor"]); 
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
			        return String(`<a class='' href='#' onclick='editar(${row.id})' title='Editar'>${row.clave}</a>`);
			    }
				
				function validarRegistros()
				{   
				    $("#frm_registro").validate({
				    	errorClass		: errorClass,
						errorElement	: errorElement,
						highlight: function(element) {
					        $(element).parent().removeClass('state-success').addClass("state-error");
					        $(element).removeClass('valid');
					    },
					    unhighlight: function(element) {
					        $(element).parent().removeClass("state-error").addClass('state-success');
					        $(element).addClass('valid');
					    },
				        ignore: ".ignore",
				        rules: {                    
				        	Clave: {
								required: true,
								maxlength: $("#Clave").prop("maxlength"),
							},
							Valor: {
								required: true,
								maxlength: $("#Valor").prop("maxlength"),
							},
				        },
				        messages: {
				            Clave: {
				                required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            Valor: {
				                required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            }
				        },
				        submitHandler: function () {
				            registro();
				        }
				    });
				}
				
				function validarBusqueda()
				{            
				    $("#frm_busqueda").validate({
				        ignore: ".ignore",
				        errorClass: "my-error-class",
				        validClass: "my-valid-class",
				        rules: {
				            Filtro:{
				            	required: true,
				            },
				        },
				        messages: {
				        	Filtro:{
				        		required: "Debes ingresar un texto a buscar",
				            },
				        },
				        submitHandler: function () {
							
				        }
				    });
				}

				function limpiarBusqueda(){
					$('#txtFiltro').val("");
				}