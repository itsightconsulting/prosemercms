			var $table = $('#tblRegistros');
			var $index = $('#hIdMemoria');
			var alias = "";
			
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
					
					$("#btnGuardar").click(function () {
				        registro();
				    });
					
				    $("#btnCancelar").click(function () {
				    	$('#Username').removeAttr('disabled');
				        irListado($index);
				    });
				    
				    $("#UploadOnlyFile").change(function(){
						subirOnlyFileReemplazo(this);
					});
				    
				    $("#UploadResumenImage").change(function(){
						subirResumenImage(this);
					})
					
					$("#UploadPortadaImage").change(function(){
						subirPortadaImage(this);
					})
					
					$('#myModal').on('hidden.bs.modal', function () {
						$("#UploadFiles").val("");
						modalFileSort.innerHTML = '';
						modalImageSort.innerHTML = '';
						$("#AliasList").get(0).innerHTML = '';
					});
					
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
				
				function Opciones(value, row, index){
					var opciones = ""; 
					if(row.nombreArchivo == null || row.nombreArchivo == ''){
						opciones = `<a href='#' title='Aún no se ha subido algún archivo'><i style='color:gray;' class='glyphicon glyphicon-cloud-download'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazar(${row.id});' title='Reemplazar o subir archivo'><i class='glyphicon glyphicon-cloud-upload'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazarResumenImage(${row.id});' title='Reemplazar o subir imagen de resumen'><i class='glyphicon glyphicon-picture text-danger'></i></a> &nbsp;
									<a href='#' onclick='javascript:reemplazarPortadaImage(${row.id});' title='Reemplazar o subir imagen portada'><i class='glyphicon glyphicon-picture text-warning'></i></a> &nbsp`;
					}else{
						opciones = `<a href='#' onclick='javascript:descargar("${row.rutaArchivo}");' title='Descargar archivo'><i class='glyphicon glyphicon-cloud-download'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazar(${row.id});' title='Reemplazar o subir archivo'><i class='glyphicon glyphicon-cloud-upload'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazarResumenImage(${row.id});' title='Reemplazar o subir imagen de resumen'><i class='glyphicon glyphicon-picture text-danger'></i></a> &nbsp;
									<a href='#' onclick='javascript:reemplazarPortadaImage(${row.id});' title='Reemplazar o subir imagen portada'><i class='glyphicon glyphicon-picture text-warning'></i></a> &nbsp`;
					}
            		return opciones;
				}
		
				function listarRegistros() {
										
					    $table.bootstrapTable('destroy');
				        
				        $table.bootstrapTable({
				        	url: _ctx+lang+'gestion/memoria/obtenerListado',
				        	pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: d => {},
				        	onLoadError: function (xhr) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        	},
				        });
					
				}
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				    	iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				    	if(iFrameBody.textContent.length > 0){
				        var params = new Object();
				        params.id = $("#hIdMemoria").val();
				        params.tituloPrincipal = $("#TituloPrincipal").val();
						params.contenido = iFrameBody.innerHTML;
						params.resumen = iFrameBody.textContent.substring(0,232).trim() + "...";
				                 
				        $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+lang+'gestion/memoria/guardar',
				            dataType: "json",
				            data: params,
				            success: function (data, textStatus) {
				                if (textStatus == "success") {   
				                	if(data=="-9"){
				                		$.smallBox({
											content : "<i> El registro ha fallado debido a que el nombre de evento ya existe en el sistema...</i>",
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
				            	$('#hIdMemoria').val(0);
				            	irListado($index);
				            	listarRegistros();
				            }
				        });
				    	}else{
				    		$.smallBox({
								content : "<i> El <strong>contenido no debe actualizarse como vacío.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
				    	}
				    }
				}
				
				function editar(id) {
				    var param = new Object();
				    param.id = id;
				    				    
				    $("#load_pace").show();
				    $.ajax({
				        type: 'GET',
				        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				        url: _ctx+lang+'gestion/memoria/obtener',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdMemoria").val(data["id"]);
				                $("#TituloPrincipal").val(data["tituloPrincipal"]);
								iFrameBody.innerHTML = data["contenido"];
				            }
				        },
				        error: function (xhr, ajaxOptions, thrownError) {
				        	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        },
				        complete: function (data) {
				            irRegistro();
				            $("#load_pace").show();
				        }
				    });
				}
				
				function descargar(ruta){
					window.location.href = _ctx+'media/file/memoria/gt/0'+ruta;
					$.smallBox({
						content: "La descarga ha iniciado...", 
						iconSmall: "fa fa-cloud fa-2x",
						color: "#296191"
					});
				}
				
				function reemplazarResumenImage(id){
					$.smallBox({
						content : "¿Estás seguro de querer reemplazar la imagen para el resumen? <p class='text-align-right'><a href='javascript:confirmarReemplazoResumenImage("+id+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-paper-plane swing animated",
						iconSmall: "",
					});
				}
				
				function reemplazarPortadaImage(id){
					$.smallBox({
						content : "¿Estás seguro de querer reemplazar la imagen de portada del contenido memoria del proyecto? <p class='text-align-right'><a href='javascript:confirmarReemplazoPortadaImage("+id+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-paper-plane swing animated",
						iconSmall: "",
					});
				}
				
				function reemplazar(id){
					$('#hIdMemoria').val(id);
					
					$.smallBox({
						content : "¿Estás seguro de querer reemplazar el archivo? <p class='text-align-right'><a href='javascript:confirmarReemplazo()' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-bell swing animated",
						iconSmall: "",
					});
				}
				
				function confirmarReemplazoResumenImage(id){
					$("#hIdMemoria").val(id);
	 	        	$('#UploadResumenImage')[0].click();
				}
				
				function confirmarReemplazoPortadaImage(id){
					$("#hIdMemoria").val(id);
	 	        	$('#UploadPortadaImage')[0].click();
				}
				
				function confirmarReemplazo(){
					//Tipo - Archivo: 1 | Tipo - Imagen:  2
						$.smallBox({
							content : "¿Desea <strong>agregar un alias</strong> al archivo? <p class='text-align-right'><a href='javascript:agregarAlias();' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:continuarSinAlias();' class='btn btn-danger btn-sm'>No</a></p>",
							color : "#296191",
							timeout: 10000,
							icon : "fa fa-question swing animated",
							iconSmall: "",
						});
				}
				
				function agregarAlias(){
					bootbox.prompt("Ingrese el alias:", function(result){ 
						if(result){
							alias = result;
							$('#UploadOnlyFile')[0].click();
						}
					});
				}
				
				function continuarSinAlias(){
					$('#UploadOnlyFile')[0].click();
				}
				
				function subirOnlyFileReemplazo(f){
			         //submit the form here
			         
			         var file;
			         if ((file = f.files[0])) {
									
							        var data = new FormData();
							        
									data.append("file", file);
					                data.append("id", $("#hIdMemoria").val());
					                data.append("peso", formatBytes(file.size));
					                if(alias.length > 0)
					                	data.append("alias", alias);
					                
					                $.ajax({
					                    type: 'POST',
					                    url: _ctx+lang+'gestion/memoria/archivo',
					                    data : data,
					                    contentType: false,
					                    processData: false,
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
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else if(data == "-1"){
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else{
					                            	$.smallBox({
							                        	content: "<i> El archivo se reemplazo satisfactoriamente...</i>",
							                        	iconSmall: "fa fa-refresh fa-2x"
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
					                    complete: function (data) {
					                    	listarRegistros();
							            	$('#UploadOnlyFile').val('');
							            	alias = "";
							            }
					                });
			         }
				}
				
				function subirResumenImage(f){
					//submit the form here
			         var _URL = window.URL || window.webkitURL;
			         var file, img;
			         if ((file = f.files[0])) {
			             img = new Image();
			             img.onload = function() {
							        var genericFile = $("#UploadResumenImage").get(0).files[0];
									
							        var data = new FormData();
							        
									data.append("file", genericFile);
					                data.append("id", $("#hIdMemoria").val());
					                
					                $.ajax({
					                    type: 'POST',
					                    url: _ctx+'gestion/memoria/imagen/resumen',
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
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else if(data == "-1"){
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else{
					                            	$.smallBox({
							                        	content: "<i> La imagen se reemplazo satisfactoriamente...</i>",
							                        	iconSmall: "fa fa-refresh fa-2x"
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
					                    complete: function (data) {
							            	$('#UploadResumenImage').val('');
							            }
					                });
			             };
			             img.onerror = function() {
			                 bootbox.alert( "No se ha seleccionado una imagen válida: <strong>" + file.type + "");
			             };
			             img.src = _URL.createObjectURL(file);
			         }
				}
				
				function subirPortadaImage(f){
					//submit the form here
			         var _URL = window.URL || window.webkitURL;
			         var file, img;
			         if ((file = f.files[0])) {
			             img = new Image();
			             img.onload = function() {
							        var genericFile = $("#UploadPortadaImage").get(0).files[0];
									
							        var data = new FormData();
							        
									data.append("file", genericFile);
					                data.append("id", $("#hIdMemoria").val());
					                
					                $.ajax({
					                    type: 'POST',
					                    url: _ctx+lang+'gestion/memoria/imagen/portada',
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
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else if(data == "-1"){
					                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
					                            } else{
					                            	$.smallBox({
							                        	content: "<i> La imagen se reemplazo satisfactoriamente...</i>",
							                        	iconSmall: "fa fa-refresh fa-2x"
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
					                    complete: function (data) {
							            	$('#UploadPortadaImage').val('');
							            }
					                });
			             };
			             img.onerror = function() {
			                 bootbox.alert( "No se ha seleccionado una imagen válida: <strong>" + file.type + "");
			             };
			             img.src = _URL.createObjectURL(file);
			         }
				}
				
				function getBodyCKEditor(){
					iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				}
				
				function linkFormatter(value, row, index) {
			        return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id +')" title="Editar">'+row.tituloPrincipal+'</a>', value);
			    }