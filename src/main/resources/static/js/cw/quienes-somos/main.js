			var $table = $('#tblRegistros');
			var $tableFiles = $('#tblArchivos');
			var $index = $('#hIdQuienes');
			
			let arrayImagenes = [];
			let modalImageSort = $('#sortImagenes').get(0);
			
			//CK EDITOR BODY
			var iFrameBody = '';
			
				$(function() {
					
					$( "#sortImagenes" ).sortable();
					$( "#sortImagenes" ).disableSelection();
					
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
				    
				    $("#UploadFiles").change(function(){ 
						subirArchivos(this);
					});
				    
				    $("#UploadFile").change(function(){
				    	subirFileReemplazo(this);
				    })
				    
				    $("#btnNuevosArchivos").click(function(){
				    	addFiles();
				    });
				    
				  	//Compare for reorder arrays
					compare = function compare(a,b) {
						if (a.orden < b.orden)
							return -1;
						if (a.orden > b.orden)
							return 1;
						return 0;
					}
				  	
					$('#myModal').on('hidden.bs.modal', function () {
						$("#UploadFiles").val("");
						modalImageSort.innerHTML = '';
					});
					
					$('#myModalVerificar').on('hidden.bs.modal', function () {
						reorganizarFileArrays();
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
		
				function listarRegistros() {
										
					    $table.bootstrapTable('destroy');
				        
				        $table.bootstrapTable({
				        	url: _ctx+lang+'gestion/quienes-somos/obtenerListado',
				        	pagination: true,
				        	sidePagination: 'client',
				        	pageSize: 10,
				        	onLoadSuccess: d => {},
				        	onLoadError: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        	},
				        });
					
				}
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				    	iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				    	if(iFrameBody.textContent.length > 0){
				        var params = new Object();
				        params.id = $("#hIdQuienes").val();
						params.contenido = iFrameBody.innerHTML;
				                 
				        $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+lang+'gestion/quienes-somos/guardar',
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
				            	$('#hIdQuienes').val(0);
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
				        url: _ctx+lang+'gestion/quienes-somos/obtener',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdQuienes").val(data["id"]);
				                $("#Menu").val(data["nombreMenu"]);
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
				
				function obtenerImagenes(id){
					$('#hIdQuienes').val(id);
					$('#UploadFiles').attr('accept','.jpeg,.jpg,.png');
					$tableFiles.bootstrapTable('destroy');
					$tableFiles.bootstrapTable({
						url: _ctx+'gestion/quienes-somos/obtenerImagenes',
						pagination: false,
						sidePagination: 'client',
						pageSize: 10,
						queryParams: p => { return {quienesSomosId: id};},
						onLoadSuccess: function(data){},
						onLoadError: function (xhr, ajaxOptions, thrownError) {
							exception(xhr["status"], xhr["responseJSON"]["error"]);
						},
					});
				}
				
				function obtenerImagenesDirecto(id){
					$tableFiles.bootstrapTable('destroy');
					$tableFiles.bootstrapTable({
						url: _ctx+'gestion/contenido-imagen/obtenerListado',
						pagination: false,
						sidePagination: 'client',
						pageSize: 10,
						queryParams: p => { return {contenidoWebId: id};},
						onLoadSuccess: function(data){},
						onLoadError: function (xhr, ajaxOptions, thrownError) {
							exception(xhr["status"], xhr["responseJSON"]["error"]);
						},
					});
				}
				
				function subirArchivos(fs){
					 arrayImagenes = [];
					 arraySizes = [];
					 modalImageSort.innerHTML = '';
					 
					 let rawImages = "", rawFiles="", filesSize = 0;
					 
			         //Submit files
		             var archivos = $("#UploadFiles").get(0);
			         
			         if ((file = fs.files[0])) {
					        						        
					     archivos = archivos.files;
					     
					     //Cantidad de archivos
					     $('#FilesAmount').text(archivos.length);
					        
					     if(archivos != null){
					    	 var nuevoOrden = [], iFile = 0,iImage = 0; 
						        	Array.from(archivos).forEach((file, i) => {
							            	arrayImagenes.push(file);
							            	rawImages += `<li class="dd-item"><span class="label label-success font-sm"><span class="label label-warning font-sm">${++iImage}.</span> ${file.name}</span></li>`;
							            	filesSize+=file.size;
							        });
						        modalImageSort.innerHTML = rawImages;
					     }
					     //Asignando el tamaño de los archivos
					     $('#FileSize').text(formatBytes(filesSize));
					     
			         }
				}
				
				function reorganizarFileArrays(){
					var arrayOrdenImages = [];
					
					Array.from(modalImageSort.children).
						forEach((value, i) => {
							var obj = {nuevo: ++i, antiguo: parseInt(value.textContent.split('.')[0])};
							arrayOrdenImages.push(obj);
						}
					);
					
					//Imagenes
					arrayImagenes.forEach((file,i) =>{
						arrayOrdenImages.forEach((order,ii)=>{
							
							if((i+1)===order.antiguo){
								//Necesario para ejecutar el compare
								file.orden = order.nuevo;
								return false;
							}
						});
					});
					arrayImagenes.sort(compare);
				}
				
				function visualizar(ruta){
					$('#Image').attr('src', _ctx+'media/image/quienes-somos/historia'+ruta);
					$('#myModalImage').modal('show');
				}
				
				function reemplazar(id, tipo){
					$.smallBox({
						content : "¿Estás seguro de querer reemplazar el archivo o imagen? <p class='text-align-right'><a href='javascript:confirmarReemplazo("+id+","+tipo+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-bell swing animated",
						iconSmall: "",
					});
				}
				
				function confirmarReemplazo(contenidoMediaId, tipo){
					//Tipo - Archivo: 1 | Tipo - Imagen:  2
					$("#hIdContenidoMedia").val(contenidoMediaId);
					if(tipo == 1){
						$.smallBox({
							content : "¿Desea <strong>agregar un alias</strong> al archivo? <p class='text-align-right'><a href='javascript:agregarAlias();' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:continuarSinAlias();' class='btn btn-danger btn-sm'>No</a></p>",
							color : "#296191",
							timeout: 10000,
							icon : "fa fa-question swing animated",
							iconSmall: "",
						});
					}
					else if(tipo == 2)
	 	        		$('#UploadFile')[0].click();
				}
				
				function baja(id, tipo, index){
					$.smallBox({
						content : "¿Estás seguro de querer eliminar el registro? <p class='text-align-right'><a href='javascript:confirmarBaja("+id+","+tipo+","+index+");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-bell swing animated",
						iconSmall: "",
					});
				}
				
				function confirmarBaja(contenidoMediaId, tipo, index){
					//Tipo - Archivo: 1 | Tipo - Imagen:  2
					var tipoLiteral = '';
					switch (tipo) {
					case 1:
						tipoLiteral = 'contenido-archivo';
						break;
					case 2:
						tipoLiteral = 'contenido-imagen';
						break;
					}
					
					$.ajax({
			            type: 'DELETE',
			            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
			            url: '/gestion/'+tipoLiteral+'/eliminar/'+contenidoMediaId,
			            dataType: "json",
			            success: function (data, textStatus) {
			                if (textStatus == "success") {   
			                	if(data=="-9"){
			                		$.smallBox({
										content: "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
										timeout: 4500,
										color:	"alert",
									});
			                	}else{
			                		$.smallBox({content: "El registro se ha eliminado satisfactoriamente."});
			                		document.querySelector('#tblArchivos tbody tr[data-index=\''+index+'\']').remove();
			                	}
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            },
			            complete: function (data) {}
			        });
				}
				
				function subirFileReemplazo(f){
			         //submit the form here
			         var _URL = window.URL || window.webkitURL;
			         var file, img;
			         if ((file = f.files[0])) {
			             img = new Image();
			             img.onload = function() {
							        var genericFile = $("#UploadFile").get(0).files[0];
									
							        var data = new FormData();
							        
									data.append("image", genericFile);
					                data.append("id", $("#hIdContenidoMedia").val());
					                
					                $.ajax({
					                    type: 'PUT',
					                    url: '/gestion/contenido-imagen/actualizar',
					                    data : data,
					                    contentType : false,
					                    processData : false,
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
						                	exception(xhr["status"], xhr["responseJSON"]["error"]);
						                },
					                    complete: function (data) {
					                    	obtenerImagenesDirecto(parseInt(data.responseJSON));
							            	$('#UploadFile').val('');
							            }
					                });
			             };
			             img.onerror = function() {
			                 bootbox.alert( "No se ha seleccionado una imagen válida: <strong>" + file.type + "");
			             };
			             img.src = _URL.createObjectURL(file);
			         }
				}
				
				function addFiles() {
					$('#btnNuevosArchivos').button('loading');
					var aliasNames = [];
			        
			        var data = new FormData();
			        var id = $('#hIdQuienes').val();
			        			        
			        data.append("QuienesId", id);

					for (var i = 0; i < arrayImagenes.length; ++i) {
						data.append("Imagenes", arrayImagenes[i]);
                	}
					
			        $.ajax({
			            type: 'POST',
			            url: '/gestion/quienes-somos/cargarArchivos',
			            data : data,
			            contentType : false,
			            processData : false,
			            success: function (data, textStatus) {
			                if (textStatus == "success") {
			                	$("#UploadFiles").val("");
			                	$.smallBox({content: "Se agregaron las imágenes satisfactoriamente..."});
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            },
			            complete: function (data) {
			            	obtenerImagenes(id);
			            	$('#btnNuevosArchivos').button('reset');
			            }
			        });
				}
				
				function getBodyCKEditor(){
					iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				}
				
				function linkFormatter(value, row, index) {
			        return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id +')" title="Editar">'+row.nombreMenu+'</a>', value);
			    }
				
				function Opciones(value, row, index) {
					var opciones = '';
//					if(index == 0){
//						opciones = `<a href='#' onclick='javascript:obtenerImagenes(${row.id});' title='Gestionar Imagenes' data-toggle='modal' data-target='#myModal'><i class='glyphicon glyphicon-picture'></i></a> &nbsp`;
//					}else{
						opciones = `<a href='#' title='Inhabilitado para este contenido'><i style='color:gray;' class='glyphicon glyphicon-picture'></i></a> &nbsp`;
//					}
		            return  opciones;
		        }
				
				function opcionesArchivos(value, row, index){
					var opciones = ""; 
						opciones = `<a href='#' onclick='javascript:visualizar("${row.rutaMediaWeb}");' title='Visualizar imagen'><i class='glyphicon glyphicon-eye-open'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazar(${row.id}, 2);' title='Reemplazar'><i class='glyphicon glyphicon-cloud-upload'></i></a> &nbsp
									<a href='#' onclick='javascript:baja(${row.id}, 2, ${index});' title='Eliminar'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
            		return  opciones;
				}