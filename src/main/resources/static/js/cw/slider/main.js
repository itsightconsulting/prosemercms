			var $table = $('#tblRegistros');
			var $tableTipoImagenes = $('#tblTipoImagenes');
			var $index = $('#hIdSlider');
			var photoMobile = "", photoWeb = "";
			
				$(function() {
					
					pageSetUp();
					listarRegistros();
					eventoSubirImagen();
					
					//V A L I D A T E
					validarRegistros();
					
					$("#btnNuevo").click(function () {
						$("#Imagenes").css('display','none');
				        irRegistro();
				    });
					
					$("#btnGuardar").click(function () {
				        registro();
				    });
					
					$("#btnActualizarMargenes").click(function(){
						actualizacionMargenes();
					});
					
				    $("#btnCancelar").click(function () {
				        irListado($index);
				        $('#HyperVinculo').val('');
		            	$('#FlagPrincipal').val('');
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
				        var estado  = $("#Estado").val();
				        
				        if(estado == null){estado = "-1";}

					    $table.bootstrapTable({
					        url: _ctx+'gestion/slider/obtenerListado'+'/'+estado,
					        pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: function(data){},
				        	onLoadError: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        	},
					    });
					}
				}
				
				function listarTipoImagenes() {
					
					    $tableTipoImagenes.bootstrapTable('destroy');
					    var dataRpta;

					    $tableTipoImagenes.bootstrapTable({
				            
					        url: _ctx+'gestion/tipo-imagen/listarTodos',
					        pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: function(data){},
				        	onLoadError: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        	},
					    });
				}
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				        var params = new Object();
				        params.id = $("#hIdSlider").val();
						params.hyperVinculo = $("#HyperVinculo").val();
						params.flagPrincipal = $("#FlagPrincipal").val();
						params.flagActivo = $('#FlagActivo').is(':checked');
						params.rutaImagenWeb = photoWeb;
						params.rutaImagenMobile = photoMobile;
				                 
				        
				        $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+'gestion/slider/agregar',
				            dataType: "json",
				            data: params,
				            success: function (data, textStatus) {
				                if (textStatus == "success") {    
				                	if(data=="2"){
				                		$.smallBox({
											content : "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
										}); 				  
				                	}else{
				                		$.smallBox({}); 
				                	}
				                }
				            },
				            error: function (xhr) {
				            	exception(xhr["status"], xhr["responseJSON"] != undefined ? xhr["responseJSON"]["error"] : "Ha ocurrido un error, comunicarse con el administrador. Gracias.");
				            },
				            complete: function () {
				            	limpiarBusqueda();
				            	irListado($index);
				            	listarRegistros();
				            	$('#HyperVinculo').val('');
				            	$('#FlagPrincipal').val('');
				            }
				        });
				    }
				}
				
				function editar(id) {
				    
					$('#Imagenes').css('display','');
				    var param = new Object();
				    param.id = id;
				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'GET',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+'gestion/slider/obtener',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdSlider").val(data["id"]);
				                $("#HyperVinculo").val(data["nombre"]);
				                $("#FlagPrincipal").val(String(data["flagPrincipal"]));
				                if(data["flagActivo"] == true)
				                	$('#FlagActivo').prop('checked', data["flagActivo"]);
				                else
				                	$('#FlagActivoOff').prop('checked', true);
				                photoWeb = data["rutaImagenWeb"];
				                photoMobile = data["rutaImagenMobile"];				                
				            }
				        },
				        error: function (xhr, ajaxOptions, thrownError) {
				        	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        },
				        complete: function (data) {
				        	limpiarBusqueda();
				            irRegistro();
				            listarTipoImagenes();
				            cargarImagenes();
				        }
				    });
				}
				
				function actualizacionMargenes(id){
				    var params = new Object();
				    params.id = $('#hIdTipoImagen').val();
				    params.ancho = $('#inpAncho').val();
				    params.alto = $('#inpAlto').val();
				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'PUT',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+'gestion/tipo-imagen/actualizar-margenes',
				        dataType: "json",
				        data: params,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				            	if(data == "-9"){
				            		$.smallBox({
				                		 content: "Ha ocurrido un error. Por favor comunicarse con el administrador.",
										 color: "#8a6d3b",
					            		 iconSmall : "fa fa-warning fa-2x fadeInRight animated",
										 timeout: 5000,
									});
				            	}else{
				            		$.smallBox({
										content : "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
									});
				            		$('#myModalActualizacion').modal('hide');
				            	}
				            }
				        },
				        error: function (xhr, ajaxOptions, thrownError) {
				        	if(xhr["responseJSON"] == null)
				        		exception(xhr["status"], "Error...");
				        	else
				        		exception(xhr["status"], xhr["responseJSON"]["error"]);
				        },
				        complete: function (data) {
				            listarTipoImagenes();
				            cargarImagenes();
				            $('#inpAncho').val('');
				            $('#inpAlto').val('');
				        }
				    });
				}
				
				function activarDesactivar(id, ix) {
					$.smallBox({
						content : "¿Estás seguro que deseas cambiar el estado del registro seleccionado? <p class='text-align-right'><a href='javascript:confirmarActivarDesactivar("+id+","+ix+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 6000,
						icon : "fa fa-paper-plane swing animated",
						iconSmall: "",
					});
				}
				    
				function confirmarActivarDesactivar(id, ix){
					var param = new Object();
		            param.id = id;
		            
		            $("#load_pace").show();
		            $.ajax({
		                type: 'POST',
		    	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
		                url: _ctx+'gestion/slider/desactivar',
		                dataType: "json",
		                data: param,
		                success: function (data, textStatus) {
		                    if (textStatus == "success") {
		                    	$.smallBox({
		                        	content: "<i> El estado ha sido actualizado satisfactoriamente...</i>",
		                        	iconSmall: "fa fa-refresh fa-2x",
		                        });
		                    	actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[3].children[0].textContent;

		                        if (actualStatus == "Activo") {
		                        	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[3].innerHTML = '<span class="label label-danger">Inactivo</span>';
		    		            } else {
		    		            	document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[3].innerHTML = '<span class="label label-success">Activo</span>';
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
				}
				
				function hacerPrincipal(id, ix){
                    var actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[3].children[0].textContent;
			        if (actualStatus == "Activo") {
						$.smallBox({
							content : "¿Estás seguro que desea cambiar este registro como principal en el carrusel? <p class='text-align-right'><a href='javascript:confirmarHacerPrincipal("+id+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
							color : "#296191",
							timeout: 6000,
							icon : "fa fa-bell swing animated",
							iconSmall: "",
						});
			        }else{
			        	$.smallBox({
	                		 content: "Esta imagen no puede ser seleccionada como principal debido a que <strong>se encuentra inactiva</strong>, activarla primero y volver a intentarlo después",
							 color: "#8a6d3b",
		            		 iconSmall : "fa fa-warning fa-2x fadeInRight animated",
							 timeout: 6000,
						});
			        }
				        	
				}
				
				function confirmarHacerPrincipal(id){
					var param = new Object();
		            param.id = id;
		            
		            $("#load_pace").show();
		            $.ajax({
		                type: 'PUT',
		    	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
		                url: _ctx+'gestion/slider/principal',
		                dataType: "json",
		                data: param,
		                success: function (data, textStatus) {
		                    if (textStatus == "success") {
		                    	$.smallBox({
									content : "<i> El registro<strong> ha sido cambiado como principal para el carrousel...</strong></i>",
									color : "#c09853",
									timeout: 6000,
									icon : "fa fa-star swing animated",
									iconSmall: "",
								});
		                    }
		                },
		                error: function (xhr, ajaxOptions, thrownError) {
		                	exception(xhr["status"], xhr["responseJSON"]["error"]);
		                },
		                complete: function (data) {
		                	limpiarBusqueda();
		                    listarRegistros();
		                }
		            });
				}
				
				function baja(id, index) {
				    $.smallBox({
				        content: "¿Estás seguro de querer eliminar el registro? <p class='text-align-right'><a href='javascript:confirmarBaja(" + id + ");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
				        color: "#296191",
				        timeout: 10000,
				        icon: "fa fa-bell swing animated",
				        iconSmall: "",
				    });
				}
				
				function subirImagen(id, ancho, alto){
					$('#hIdTipoImagen').val(id);
					$('#hWImage').val(ancho);
					$('#hHImage').val(alto);
					$.smallBox({
						content : "¿Estás seguro de ingresar o actualizar la imagen? <p class='text-align-right'><a href='javascript:document.querySelector(\"#UploadImage\").click();' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 6000,
						icon : "fa fa-question fa-2x swing animated",
						iconSmall: "",
					});
				}
				
				function cargarImagenes(){
					
					var param = new Object();
				    param.id = $('#hIdSlider').val();
				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'GET',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+'gestion/slider-imagen/listarTodos',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				            	if(data == "-1"){}
				            	else{
				            		var prefixId = "preImagen";
				            		var i = 0;
				            		for(x in data){
				            			var tipoImagenId = data[x]["tipoImagen"];
				            			$('#'+prefixId+tipoImagenId).attr('onclick','javascript:previsualizar("'+data[x]["nombreMedia"]+'")');
				            			$('#'+prefixId+tipoImagenId).attr('title','Previsualizar imagen');
				            			$('#'+prefixId+tipoImagenId).attr('data-toggle','modal');
				            			$('#'+prefixId+tipoImagenId).attr('data-target','#myModalImage');
                                        $('#'+prefixId+tipoImagenId).children().removeClass('glyphicon glyphicon-eye-close');
				            			$('#'+prefixId+tipoImagenId).children().addClass('glyphicon glyphicon-eye-open');
				            			$('#'+prefixId+tipoImagenId).removeAttr('style');
				            			if(i == 0)
				            				photoWeb = data[x]["nombreMedia"];
				            			else
				            				photoMobile = data[x]["nombreMedia"]
				            			i++;
				            		}
				            	}
				            }
				        },
				        error: function (xhr) {
				        	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        },
				        complete: function (data) {
				        	
				        }
				    });
				}
				
				
				function previsualizar(nombreMedia){
					$('#Image').attr('src', _ctx+'media/image/slider/'+nombreMedia); 
				}
				
				function eventoSubirImagen(){
 					
					 var _URL = window.URL || window.webkitURL;
					 
					 $("#UploadImage").change(function(){
						 var w = parseFloat($('#hWImage').val());
						 var h = parseFloat($('#hHImage').val());
						 
				         //submit the form here
				         var file, img;
				         if ((file = this.files[0])) {
				             img = new Image();
				             img.onload = function() {
				                 if(parseFloat(this.width) <= w && parseFloat(this.height) <= h){
				                	 
								        var fileImagenSlider = $("#UploadImage").get(0).files[0];
								        var sliderId = $('#hIdSlider').val();
								        var tipoImagenId = $('#hIdTipoImagen').val();
										
								        var data = new FormData();
								        
										data.append("fileSliderImagen", fileImagenSlider);
						                data.append("sliderId", sliderId);
						                data.append("tipoImagenId", tipoImagenId);
						                
						                $.ajax({
						                    type: 'POST',
						                    url: _ctx+'gestion/slider-imagen/cargarImagen',
						                    data : data,
						                    contentType : false,
						                    processData : false,
						                    xhr: function() {
						                        var myXhr = $.ajaxSettings.xhr();
						                        if(myXhr.upload){
						                            myXhr.upload.addEventListener('progress', progress, false);
						                        }
						                        return myXhr;
						                	},
						                    success: function (data, textStatus) {
						                        if (textStatus == "success") {
						                            if(data == "-99"){
						                            	bootbox.alert("El archivo no ha podido ser guardado debido a que excede los límites permitidos de la aplicación, que son 5MB en caso sea un solo archivo y si son más de 2, como máximo en conjunto no deben exceder a 10MB. Para mayor información comunicarse con el administrador.");
						                            }else{
						                            	$.smallBox({
															content : "<i class='fa fa-image fa-2x'></i><i> La imagen ha sido guardada satisfactoriamente...!</i>",
								                        });
						                            }
						                        }
						                    },
							                error: function (xhr, ajaxOptions, thrownError) {
							                	if(xhr["responseJSON"] != null)
							                		exception(xhr["status"], xhr["responseJSON"]["error"]);
							                	else
							                		exception(xhr["status"], xhr["status"]);
							                },
						                    complete: function () {
						                    	cargarImagenes();
								            	$('#UploadImage').val('');
								            }
						                });
				                 }else{
				                	 $.smallBox({
				                		 content: "No se puede adjuntar. La imagen debe tener margenes<br/><strong> máximos de "+w+ "px de ancho y "+h+"px de alto.</strong>",
										 color: "#8a6d3b",
					            		 iconSmall : "fa fa-warning fa-2x fadeInRight animated",
										 timeout: 5000,
									});
				                	$('#UploadImage').val('');
				                 } 
				             };
				             img.onerror = function() {
				                 bootbox.alert( "No se ha seleccionado una imagen válida: <strong>" + file.type + "</strong>");
				             };
				             img.src = _URL.createObjectURL(file);
				         }    
				 	});
					
				}
				
				function confirmarBaja(id) {

				    $.ajax({
				        type: 'DELETE',
				        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				        url: _ctx+'gestion/slider/eliminar/' + id,
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
				
				function linkFormatter(value, row, index) {
			        return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id + ')" title="Editar">'+row.id+'</a>', value);
			    }
				
				function Opciones(value, row, index) {
		            opciones = 
		            		`<a href='#' onclick='javascript:activarDesactivar(${row.id},${index});' title='Actualizar estado'><i class='glyphicon glyphicon-refresh'></i></a> &nbsp &nbsp
		            		 <a href='#' onclick='javascript:hacerPrincipal(${row.id},${index});' title='Cambiar como principal'><i class='glyphicon glyphicon-star text-warning'></i></a> &nbsp
		            		 <a href='#' onclick='javascript:baja(${row.id});' title='Eliminar Slider'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
	            	return opciones;
		        }
				
				function OpcionesImagenes(value, row, index) {
					 var subir = '<a href="#" onclick="javascript:subirImagen('+row.id+','+row.ancho+','+row.alto+')" id="preImagen'+row.id+'" href="#" title="Subir imagen"><i class="glyphicon glyphicon-upload"></i></a>';
		             var previsualizar = "<a id=\"preImagen"+row.id+"\" href='#' title='No existe imagen de este tipo' style='color:#848484;'><i class='glyphicon glyphicon-eye-close'></i></a>";
					 var edt = '<a href="#" onclick="javascript:actualizarMargenes('+row.id+')" data-toggle="modal" data-target="#myModalActualizacion" id="preImagen'+row.id+'" href="#" title="Editar máximos"><i class="glyphicon glyphicon-edit"></i></a>';
		            return previsualizar + '  '+ subir + '  '+edt ;
		        }
				
				function wFormat(value, row, index) {
					 return String('<strong><i>'+row.ancho+' px</i></strong>', value);
		        }
				
				function hFormat(value, row, index) {
					 return String('<strong><i>'+row.alto+' px</i></strong>', value);
		        }
				
				function actualizarMargenes(id){
					$('#hIdTipoImagen').val(id);
				}
				
			    function isMainSlide(value, row, index) {
					
			    	if (row.flagPrincipal) {
		                return '<span class="label label-success">Si</span>';
		            } else {
		                return '<span class="label label-danger">No</span>';
		            }
				}
				
				function isActived(value, row, index) {
					if (row.flagActivo) {
		                return '<span class="label label-success">Activo</span>';
		            } else {
		                return '<span class="label label-danger">Inactivo</span>';
		            }
				}
				
				function validarRegistros()
				{
				    $("#frm_registro").validate({
				        ignore: ".ignore",
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
				        rules: {                    
				            HyperVinculo: {
								maxlength: $("#HyperVinculo").prop("maxlength"),
							},
							FlagPrincipal: {
								required: true,
							}
				        },
				        messages: {
				            HyperVinculo: {
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            FlagPrincipal: {
				                required: "La selección es obligatoria",
							}
				        },
				        submitHandler: function () {
				            registro();
				        }
				    });
				}
				
				function limpiarBusqueda(){
					$('#txtFiltro').val("");
					$('#Estado').val("-1");
				}
				