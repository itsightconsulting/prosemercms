			var $table = $('#tblRegistros');
			var $index = $('#hIdLogro');
			
			//CK EDITOR BODY
			var iFrameBody = '';
			
				$(function() {
					
					$(document).on('keypress', function(e) {
					    var tag = e.target.tagName.toLowerCase();
					    if (e.keyCode === $.ui.keyCode.ENTER) 
					       e.preventDefault();
					});
					
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					$(document).ajaxSend(function(e, xhr, options) {
						xhr.setRequestHeader(header, token);
					});
					
					
					pageSetUp();
					listarRegistros();
					//CKEDITOR
					try {cargarModalsParaCK();} catch (e) {console.log(e);}editor();
					
					$('#CKFileI').change(function(){//Found it in jsfuentes.js
						cargarImagen(this);
					});
					
					$('#CKFile').change(function(){//Found it in jsfuentes.js
						cargarFile(this);
					});
					
					//V A L I D A T E
					validarRegistros();
					validarBusqueda();
					
					$("#btnNuevo").click(function () {
				    	window.location.href = _ctx+'gestion/logro/nuevo';
				    });
					
					$("#btnGuardar").click(function () {
				        registro();
				    });
					
				    $("#btnCancelar").click(function () {
				    	$('#Username').removeAttr('disabled');
				        irListado($index);
				    });
				    
				    $("#btnFiltrar").click(function () {
				    	listarRegistros();
				    });
				    
					//Compare for reorder arrays
					compare = function compare(a,b) {
						if (a.orden < b.orden)
							return -1;
						if (a.orden > b.orden)
							return 1;
						return 0;
					}
					
					$('#CKFileCopy').click(function(){
						$('#CKFileRoute').select();
						document.execCommand('copy');
						$.smallBox({content: "La URL del archivo ha sido <strong>traspasado al portapapeles.</strong>", color: "#296191"})
					    $('#myModalEditor').modal('hide');
						$('#CKFile').val('');
						$('#CKFileRoute').val('');
					});

					$('#CKFileCopyI').click(function(){
						$('#CKFileRouteI').select();
						document.execCommand('copy');
						$.smallBox({content: "La URL de la imagen ha sido <strong>traspasada al portapapeles.</strong>", color: "#296191"})
					    $('#myModalEditorI').modal('hide');
						$('#CKFileI').val('');
						$('#CKFileRouteI').val('');
					});
				});
		
				function listarRegistros() {
					
				    if ($("#frm_busqueda").valid()) {
					
					    $table.bootstrapTable('destroy');
					    var dataRpta;
				
				    	var comodin = $("#txtFiltro").val();
				        var estado  = $("#Estado").val();
				        var beneficiario = $("#Beneficiario").val();
				        
				        if(comodin == null || comodin == ""){comodin = "0";}
				        if(estado == null){estado = "-1";}
				        if(beneficiario == null || beneficiario == ""){beneficiario = "0";}
				        
				        $table.bootstrapTable({
				        	url: _ctx+lang+'gestion/logro/obtenerListado/'+comodin+'/'+estado+'/'+beneficiario,
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
				    	iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				    	if(iFrameBody.textContent.length > 0){
				        var params = new Object();
				        params.id = $("#hIdLogro").val();
						params.descripcion = iFrameBody.innerHTML;
						var dt = $('#FechaLogro').val().split("-");
						params.fechaLogro = new Date(dt[0],Number(dt[1])-1,dt[2]);
						params.resumen = iFrameBody.textContent.substring(0,120).trim() +"...";
						
						params.estudioId = $('#hIdEstudio').val();
						params.beneficiarioId = $('#hIdBeneficiario').val();
				                 
				        $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+lang+'gestion/logro/agregar',
				            dataType: "json",
				            data: params,
				            success: function (data, textStatus) {
				                if (textStatus == "success") {   
				                	if(data=="-9" || data=="-88"){
				                		$.smallBox({
											content : "<i> El registro ha fallado debido a que el nombre de logro ya existe en el sistema...</i>",
											timeout: 4500,
											color:	"alert",
										});
				                	}else{
				                		$.smallBox({content: "El registro ha sido actualizado satisfactoriamente..."}); 
				                	}
				                }
				            },
				            error: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				            },
				            complete: function (data) {
				            	$('#hIdLogro').val(0);
				            	limpiarBusqueda();
				            	irListado($index);
				            	listarRegistros();
				            }
				        });
				    	}else{
				    		$.smallBox({
								content : "<i> El <strong> alcance del logro no actualizarse como vacío.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
				    	}
				    }
				}
				
				function editar(id, beneficiario) {
				    var param = new Object();
				    param.id = id;
				    				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'GET',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+lang+'gestion/logro/obtener',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdLogro").val(data["id"]);
				                $("#hIdBeneficiario").val(beneficiario);
				                if(data["estudio"] != null)
				                	$("#hIdEstudio").val(data["estudio"]["id"]);
				                else
				                	$("#hIdEstudio").val("");
				                $("#FechaLogro").val(data["fechaLogro"]);
								iFrameBody.innerHTML = data.descripcion;
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
				
				function desactivar(id, ix, multiple) {
						$.SmartMessageBox({
							title : "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
							content : "<br/>¿Quieres activar o desactivar el registro?",
							buttons : '[No][Si]'
						}, function(ButtonPressed) {
							if (ButtonPressed === "Si") {
					        	var params = new Object();
					            params.id = id;
					            if(multiple != "null"){
					            	params.lstIds = multiple.split(",");
					            }
					            $("#load_pace").show();
					            $.ajax({
					                type: 'GET',
					    	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					                url: _ctx+'gestion/logro/desactivar',
					                dataType: "json",
					                data: params,
					                traditional: true,/* IMPORTANT */
					                success: function (data, textStatus) {
					                    if (textStatus == "success") {
					                        $.smallBox({
												content : "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
					                        });
					                        actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].children[0].textContent;

					                        if (actualStatus == "Activo") {
					                        	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].innerHTML = '<span class="label label-danger">Inactivo</span>';
					    		            } else {
					    		            	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].innerHTML = '<span class="label label-success">Activo</span>';
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
							}else{}
						})
				}
				
				function getBodyCKEditor(){
					iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				}
				
				function linkFormatter(value, row, index) {
			        return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id +','+ row.beneficiario.id +')" title="Editar">'+row.resumen+'</a>', value);
			    }
				
				function Opciones(value, row, index) {
					opciones = `<a href='#' onclick='javascript:desactivar(${row.id}, ${index}, \"${row.multiple}\");' title='Actualizar status'><i class='glyphicon glyphicon-refresh'></i></a> &nbsp
								<a href='#' onclick='javascript:eliminar(${row.id}, ${index}, ${row.multiple != undefined ? true : false});' title='Eliminar logro'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
		            return  opciones;
		        }
				
				function isActived(value, row, index) {
					
					if (row.flagActivo) {
		                return '<span class="label label-success">Activo</span>';
		            } else {
		                return '<span class="label label-danger">Inactivo</span>';
		            }
				}
				
				function isShared(value, row, index) {
					if (row.multiple != null) {
		                return '<span class="label label-success">SI</span>';
		            } else {
		                return '<span class="label label-danger">NO</span>';
		            }
				}
				
				function eliminar(id, ix, isMultiple){
					$.smallBox({
						content : "¿Estás seguro de querer eliminar el registro? <p class='text-align-right'><a href='javascript:confirmarEliminacion("+id+","+ix+",\""+isMultiple+"\");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-question swing animated",
						iconSmall: "",
					});
				}
				
				function confirmarEliminacion(id, ix, isMultiple){
					$.ajax({
			            type: 'GET',
			            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
			            url: _ctx+'gestion/logro/eliminar/'+id+'/'+isMultiple,
			            dataType: "json",
			            success: function (data, textStatus) {
			                if (textStatus == "success") {   
			                	if(data=="-9" || data=="-88"){
			                		$.smallBox({
										content: "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
										timeout: 4500,
										color:	"alert",
									});
			                	}else{
			                		$.smallBox({content: "El registro se ha eliminado satisfactoriamente."});
			                		document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').remove();
			                	}
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            },
			            complete: function (data) {}
			        });
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
							TituloLargo: {
								required: true,
								maxlength: $("#TituloLargo").prop("maxlength"),
							},
							TituloPrincipal: {
								required: true,
								maxlength: $("#TituloPrincipal").prop("maxlength"),
							},
							FechaLogro: {
								required: true,
							},
				        },
				        messages: {
				            TituloLargo: {
				                required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            TituloPrincipal: {
				            	required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            FechaLogro: {
								required: "Seleccione una fecha válida",
							},
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
				            Tipo:{
				            	digits: true,
				            }
				        },
				        messages: {
				        	Filtro:{
				        		required: "Debe seleccionar un tipo de formato",
				            },
				            Tipo:{
				                digits: "Debe seleccionar un tipo",
				            }
				        },
				        submitHandler: function () {
							
				        }
				    });
				}

				function limpiarBusqueda(){
					$('#txtFiltro').val("");
					$('#Estado').val("-1");
				}