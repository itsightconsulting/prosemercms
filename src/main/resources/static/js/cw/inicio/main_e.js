var $table = $('#tblRegistros');
			
			var pageNumber = 0;
			var pageSize = 0;
			var tipoEstudio = "";
			
			var errorClass = 'invalid';
			var errorElement = 'em';
			
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
					
					//V A L I D A T E
					validarBusqueda();
				    
				    $("#btnFiltrar").click(function () {
				    	listarRegistros();
				    });
					
				});
		
				function listarRegistros() {
					
				    if ($("#frm_busqueda").valid()) {
					
					    $table.bootstrapTable('destroy');
					    var dataRpta;
				
				    	var comodin = $("#txtFiltro").val();
				        var tipo = $("#Tipo").val();
				        var beneficiario = $("#Beneficiario").val();
				        if(comodin == null || comodin == ""){comodin = "0";}
				        if(tipo == null || tipo == ""){tipo = "0";}
				        if(beneficiario == null || beneficiario == ""){beneficiario = "0";}
				        
				        $table.bootstrapTable({
				        	url: _ctx+'gestion/contenido-web/obtenerListado/estudio/'+comodin+'/'+tipo+'/'+beneficiario,
				        	pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: function (){
				        		tipoEstudio = tipo;
				        	},
				        	onLoadError: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        	},
				        });
					}
				}
				
				function desactivar(id, flagInicio, ix) {
						
						$.SmartMessageBox({
							title : "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
							content : "<br/>¿Estás seguro que quieres cambiar el estado inicio de este contenido?",
							buttons : '[No][Si]'
						}, function(ButtonPressed) {
							if (ButtonPressed === "Si") {
		                        var actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[2].children[0].textContent;

					        	var params = new Object();
					        	params.id = id;
						        if (actualStatus == "Activo") {
					            	params.flagInicio = false;
					            }else {
					            	params.flagInicio = true;	
					            }
					            params.tipoEstudioId = tipoEstudio;
					            params.tipoContenido = 1;
					            
					            
					            $("#load_pace").show();
					            $.ajax({
					                type: 'PUT',
					    	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					                url: _ctx+'gestion/contenido-web/desactivar/inicio',
					                dataType: "json",
					                data: params,
					                success: function (data, textStatus) {
					                    if (textStatus == "success") {
					                    	if(data == "-1"){
							                	exception("ERROR 500", "La operación no ha podido ser procesada. Por favor contactar con el administror del sistema.");
					                    	}else{
						                        $.smallBox({
						                        	content: "<i> Se actualizó el registro...</i>",
						                        });	

						                        if (actualStatus == "Activo") {
						                        	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[2].innerHTML = '<span class="label label-danger">Inactivo</span>';
						    		            } else {
						    		            	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[2].innerHTML = '<span class="label label-success">Activo</span>';
						    		            }
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
				
				function Opciones(value, row, index) {
		            var desactivar = '';
		            	desactivar 	= "<a href='#' onclick='javascript:desactivar(" + row.id +"," +row.flagInicio+"," +index+");' title='Agregar a Inicio/Elimnar de Inicio'><i class='glyphicon glyphicon-refresh'></i></a>";
		            return desactivar;
		        }
				function isActived(value, row, index) {
					if (row.flagInicio) {
		                return '<span class="label label-success">Activo</span>';
		            } else {
		                return '<span class="label label-danger">Inactivo</span>';
		            }
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
				}