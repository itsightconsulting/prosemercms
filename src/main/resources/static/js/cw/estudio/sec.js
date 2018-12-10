			var $table = $('#tblRegistros');
			var $tableFiles = $('#tblArchivos');
			var $index = $('#hIdEstudio');
			
			let arrayArchivos = [];
			let arrayImagenes = [];
			var rawTags = "";
			var alias = "";
			
			let modalFileSort = $('#sortArchivos').get(0);
			let modalImageSort = $('#sortImagenes').get(0);
			//CK EDITOR BODY
			var iFrameBody = '';
			
				$(function() {
					
					$( "#sortArchivos" ).sortable();
					$( "#sortArchivos" ).disableSelection();
					
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
					instanciarTags();
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
				    	window.location.href = _ctx+'gestion/estudio/nuevo';
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
				    
				    $("#btnNuevosArchivos").click(function(){
				    	addFiles();
				    });
				    
					$("#UploadFile").change(function(){
						subirFileReemplazo(this);
					});
					
					$("#UploadOnlyFile").change(function(){
						subirOnlyFileReemplazo(this);
					});
					
					$("#UploadFiles").change(function(){ 
						subirArchivos(this);
					});
					
					$("#UploadResumenImage").change(function(){
						subirResumenImage(this);
					})
					
					$("#UploadPortadaImage").change(function(){
						subirPortadaImage(this);
					})
					
					$('#myModalVerificar').on('hidden.bs.modal', function () {
						reorganizarFileArrays();
					});
					
					$('.modal').on('hidden.bs.modal', function () {
						if(document.body.style.cssText.includes("padding-right"))
							document.body.style.cssText=null;
					});
					
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
					
					//Compare for reorder arrays
					compare = function compare(a,b) {
						if (a.orden < b.orden)
							return -1;
						if (a.orden > b.orden)
							return 1;
						return 0;
					}
				});
				
				function subirArchivos(fs){
						 arrayArchivos = [];
						 arrayImagenes = [];
						 arraySizes = [];
						 rawInputs = '';
						 modalFileSort.innerHTML = '';
						 modalImageSort.innerHTML = '';
						 $AliasList = $("#AliasList").get(0);
						 $AliasList.innerHTML = '';
						 
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
							        		
								            if(file.type.match('application') || file.type == ''){
									            file.sizeFormat = formatBytes(file.size);
									            arrayArchivos.push(file);
									            rawFiles += `<li class="dd-item"><span class="label label-default font-sm"><span class="label label-warning font-sm">${++iFile}.</span> ${file.name}</span></li>`;
									            filesSize+=file.size;
									            rawInputs += `<input type='text' placeholder='Ingresa el alias'></input>`;
								            }else{
								            	arrayImagenes.push(file);
								            	rawImages += `<li class="dd-item"><span class="label label-success font-sm"><span class="label label-warning font-sm">${++iImage}.</span> ${file.name}</span></li>`;
								            	filesSize+=file.size;
								            }
								        });
							        	
							        modalImageSort.innerHTML = rawImages;
						        	modalFileSort.innerHTML = rawFiles;
									$AliasList.innerHTML = rawInputs;
						     }
						     //Asignando el tamaño de los archivos
						     $('#FileSize').text(formatBytes(filesSize));
						     
				         }else{
				             modalFileSort.innerHTML = '';
				         }
				}
		
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
				        	url: _ctx+lang+'gestion/estudio/obtenerListado/'+comodin+'/'+estado+'/'+beneficiario,
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
						var tags = $("#Tags").val();
				        var params = new Object();
				        params.id = $("#hIdEstudio").val();
				        params.tituloPrincipal = $("#TituloPrincipal").val();
						params.tituloLargo = $("#TituloLargo").val();
						params.alcance = iFrameBody.innerHTML;
						var month = parseInt($("#Mes").val())+1;
						month = month < 10 ? '0' + month : month;
						var fechaEstudio = $("#Anio").val() + '/' + month +'/01';
						params.fechaEstudio = fechaEstudio;
						params.flagResumen = false,
						params.resumen = iFrameBody.textContent.substring(0,232).trim() + "...";
						params.tags = tags;
				                 
				        $.ajax({
				            type: 'POST',
				            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
				            url: _ctx+lang+'gestion/estudio/agregar',
				            dataType: "json",
				            data: params,
				            success: function (data, textStatus) {
				                if (textStatus == "success") {   
				                	if(data=="-9"){
				                		$.smallBox({
											content : "<i> El registro ha fallado debido a que el nombre de estudio ya existe en el sistema...</i>",
											timeout: 4500,
											color:	"alert",
										});
				                	}else{
				                		$.smallBox({
				                			content : "<i class='fa fa-child fa-2x'></i> <i>Actualización exitosa...!</i>",
				                		}); 
										registrandoNuevasTags(params.tags);
				                	}
				                }
				            },
				            error: function (xhr, ajaxOptions, thrownError) {
				            	exception(xhr["status"], xhr["responseJSON"]["error"]);
				            },
				            complete: function (data) {
				            	$('#hIdEstudio').val(0);
				            	limpiarBusqueda();
				            	irListado($index);
				            	listarRegistros();
				            }
				        });
				    	}else{
				    		$.smallBox({
								content : "<i> El <strong> alcance del estudio no puede actualizarse como vacío.</strong></i>",
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
				        url: _ctx+lang+'gestion/estudio/obtener',
				        dataType: "json",
				        data: param,
				        success: function (data, textStatus) {
				            if (textStatus == "success") {
				                $("#hIdEstudio").val(data["id"]);
				                $("#BeneficiarioId").val(beneficiario);
				                $("#TipoEstudioId").val(data["tipoEstudio"]["id"]);
				                $("#TituloPrincipal").val(data["tituloPrincipal"]);
				                $("#TituloLargo").val(data["tituloLargo"]);
								iFrameBody.innerHTML = data.alcance;
								
								$("#Consultor").val(data["consultor"]);
								$("#ResponsableEntidad").val(data["responsableEntidad"]);
								var fechaEstudio = data["fechaEstudio"].split("-");
								$("#Mes").val(parseInt(fechaEstudio[1])-1);
								$("#Anio").val(fechaEstudio[0]);
								$('#Tags').tokenfield('setTokens', data["contenidoWeb"]["tags"]);
				            }
				        },
				        error: function (xhr, ajaxOptions, thrownError) {
				        	exception(xhr["status"], xhr["responseJSON"]["error"]);
				        },
				        complete: function (data) {
				        	limpiarBusqueda();
				        	$('#Username-error').attr('hidden','hidden');
				        	$('#Username-error').attr('style','display:none');
				        	$('#Password-error').attr('hidden','hidden');
				        	$('#Password-error').attr('style','display:none');
				            irRegistro();
				            $("#load_pace").show();
				        }
				    });
				}
				
				function desactivar(id, ix) {
						$.SmartMessageBox({
							title : "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
							content : "<br/>¿Quieres activar o desactivar el registro?",
							buttons : '[No][Si]'
						}, function(ButtonPressed) {
							if (ButtonPressed === "Si") {
					        	var param = new Object();
					            param.id = id;
					            
					            $("#load_pace").show();
					            $.ajax({
					                type: 'PUT',
					    	        contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					                url: _ctx+'gestion/estudio/desactivar',
					                dataType: "json",
					                data: param,
					                success: function (data, textStatus) {
					                    if (textStatus == "success") {
					                    	if(data == "1"){
                                                $.smallBox({
                                                    content: "<i> Se actualizó el registro...</i>",
                                                });
                                                actualStatus = document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].children[0].textContent;

                                                if (actualStatus == "Activo") {
                                                    document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].innerHTML = '<span class="label label-danger">Inactivo</span>';
                                                } else {
                                                    document.querySelector('#tblRegistros tbody tr[data-index=\''+ix+'\']').children[4].innerHTML = '<span class="label label-success">Activo</span>';
                                                }
					                    	}else{
                                                $.smallBox({
                                                    color: "alert",
                                                    content : "<i class='fa fa-warning fa-2x fa-fw'></i><i>El presente estudio se encuentra activado <br/> " +
                                                        "como un contenido principal del portal público <br/> por ese motivo primero debe ir al menú:<br/>" +
                                                        "<b>Página Inicio>Main Estudios </b><br/>e inhabilitarlo como contenido prinicpal <bt/>para luego deshabilitarlo por completo desde aquí.</i>",
                                                    timeout: 15000,
                                                });
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
			        return String('<a class="editable editable-click" href="#" onclick="javascript:editar(' + row.id +','+ row.beneficiario.id +')" title="Editar">'+row.tituloPrincipal+'</a>', value);
			    }
				
				function Opciones(value, row, index) {
					opciones = `<a href='#' onclick='javascript:desactivar(${row.id}, ${index});' title='Actualizar status'><i class='glyphicon glyphicon-refresh'></i></a> &nbsp
								<a href='#' onclick='javascript:obtenerArchivos(${row.id});' title='Gestionar Archivos' data-toggle='modal' data-target='#myModal'><i class='glyphicon glyphicon-file'></i></a> &nbsp
								<a href='#' onclick='javascript:obtenerImagenes(${row.id});' title='Gestionar Galeria' data-toggle='modal' data-target='#myModal'><i class='glyphicon glyphicon-picture'></i></a> &nbsp
								<a href='#' onclick='javascript:reemplazarResumenImage(${row.id});' title='Actualizar imagen de resumen'><i class='glyphicon glyphicon-picture text-danger'></i></a> &nbsp;
								<a href='#' onclick='javascript:reemplazarPortadaImage(${row.id});' title='Actualizar imagen de portada'><i class='glyphicon glyphicon-picture text-warning'></i></a> &nbsp
								<a href='#' onclick='javascript:eliminar(${row.id}, ${index});' title='Eliminar estudio'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
		            return  opciones;
		        }
				
				function isActived(value, row, index) {
					if (row.flagActivo) {
		                return '<span class="label label-success">Activo</span>';
		            } else {
		                return '<span class="label label-danger">Inactivo</span>';
		            }
				}
				
				function instanciarTags(){
					var tags = [];
					$.ajax({
			            type: 'GET',
			            url: _ctx+'gestion/tag/obtenerListado',
			            dataType: 'json',
			            success: function (data, textStatus) {
			                if (textStatus == "success") {
			                	data.forEach((t, i) =>{
				                	tags[i] = t.nombre;
			                	});
			                	
			                	$('#Tags').tokenfield({
									  autocomplete: {
									    source: tags,
									    delay: 100
									  },
									  showAutocompleteOnFocus: false
								});
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            }, complete: function (data) {
			            	rawTags = tags.join();
			            }
			        });
				}
				
				function registrandoNuevasTags(tags){
					tags = tags.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase();
					rawTags = rawTags.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase();
					var arrayTags = tags.split(",");
						arrayTags.forEach((v,i) =>{
							v = v.trim();
							if(!rawTags.includes(v)){
								var params = new Object();
								params.nombre = v.charAt(0).toUpperCase() + v.slice(1);
								if(localStorage.getItem("idioma") == "es"){
									params.tipo = 1;
								}else{
									params.tipo = 2;
								}
								$.ajax({
						            type: 'POST',
						            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
						            url: _ctx+'gestion/tag/agregar',
						            dataType: "json",
						            data: params,
						            error: function (xhr, ajaxOptions, thrownError) {
						            	exception(xhr["status"], xhr["responseJSON"]["error"]);
						            },
						            complete: function (data) {
						            	$('#Tags').get(0).innerHTML = '';
						            }
						        });
							}
						});
						if($('#Tags').get(0).innerHTML == ""){
							$('#Tags').tokenfield('destroy');
							instanciarTags();
						}
				}
				
				function obtenerArchivos(id){
					document.querySelector('#filesInfo').innerHTML = `<label> <i class="fa fa-check-circle"></i> Info para archivos:<br/><strong> Los tipos de archivos permitidos a subir son word, excel, pdf, ppt, comprimidos zip o rar</strong></label>`;
					$('#hIdEstudio').val(id);
					$('#UploadFiles').attr('accept','.pdf,.docx,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.rar,.zip');
					$tableFiles.bootstrapTable('destroy');
					$tableFiles.bootstrapTable({
						url: _ctx+'gestion/estudio/obtenerArchivos',
						pagination: true,
						sidePagination: 'client',
						pageSize: 7,
						queryParams: p => { return {estudioId: id};},
						onLoadSuccess: function(data){},
						onLoadError: function (xhr, ajaxOptions, thrownError) {
							exception(xhr["status"], xhr["responseJSON"]["error"]);
						},
					});
				}
				
				function obtenerImagenes(id){
					console.log(id);
					document.querySelector('#filesInfo').innerHTML = `<label> <i class="fa fa-check-circle"></i> Info para imágenes:<br/><strong> Procurar subirlas con resolución mínima de 360px por 241px o proporcional mayor para una óptima visualización</strong></label>`;
					$('#hIdEstudio').val(id);
					$('#UploadFiles').attr('accept','.jpeg,.jpg,.png');
					$tableFiles.bootstrapTable('destroy');
					$tableFiles.bootstrapTable({
						url: _ctx+'gestion/estudio/obtenerImagenes',
						pagination: true,
						sidePagination: 'client',
						pageSize: 7,
						queryParams: p => { return {estudioId: id};},
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
						pagination: true,
						sidePagination: 'client',
						pageSize: 7,
						queryParams: p => { return {contenidoWebId: id};},
						onLoadSuccess: function(data){},
						onLoadError: function (xhr, ajaxOptions, thrownError) {
							exception(xhr["status"], xhr["responseJSON"]["error"]);
						},
					});
				}
				
				function obtenerArchivosDirecto(id){
					$tableFiles.bootstrapTable('destroy');
					$tableFiles.bootstrapTable({
						url: _ctx+'gestion/contenido-archivo/obtenerListado',
						pagination: true,
						sidePagination: 'client',
						pageSize: 7,
						queryParams: p => { return {contenidoWebId: id};},
						onLoadSuccess: function(data){},
						onLoadError: function (xhr, ajaxOptions, thrownError) {
							exception(xhr["status"], xhr["responseJSON"]["error"]);
						},
					});
				}
				
				function opcionesArchivos(value, row, index){
					var opciones = ""; 
					if(row.peso == null)
						opciones = `<a href='#' onclick='javascript:visualizar("${row.rutaMediaWeb}");' title='Visualizar imagen'><i class='glyphicon glyphicon-eye-open'></i></a> &nbsp
									<a href='#' onclick='javascript:reemplazar(${row.id}, 2);' title='Reemplazar'><i class='glyphicon glyphicon-cloud-upload'></i></a> &nbsp
									<a href='#' onclick='javascript:baja(${row.id}, 2, ${index});' title='Eliminar'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
					else
						
					opciones = `<a href='#' onclick='javascript:descargar("${row.rutaMediaWeb}");' title='Descargar'><i class='glyphicon glyphicon-cloud-download'></i></a> &nbsp
								<a href='#' onclick='javascript:reemplazar(${row.id}, 1);' title='Reemplazar'><i class='glyphicon glyphicon-cloud-upload'></i></a> &nbsp
								<a href='#' onclick='javascript:baja(${row.id}, 1, ${index});' title='Eliminar'><i class='glyphicon glyphicon-trash text-danger'></i></a> &nbsp`;
            		return  opciones;
				}
				
				function visualizar(ruta){
					$('#Image').attr('src', _ctx+'media/image/estudio'+ruta);
					$('#myModalImage').modal('show');
				}
				
				function descargar(ruta){
					window.location.href = _ctx+'media/file/estudio/gt/0'+ruta;
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
						content : "¿Estás seguro de querer reemplazar la imagen de portada del estudio? <p class='text-align-right'><a href='javascript:confirmarReemplazoPortadaImage("+id+")' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-paper-plane swing animated",
						iconSmall: "",
					});
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
			            url: _ctx+'gestion/'+tipoLiteral+'/eliminar/'+contenidoMediaId,
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
				
				function confirmarReemplazoResumenImage(id){
					$("#hIdEstudio").val(id);
	 	        	$('#UploadResumenImage')[0].click();
				}
				
				function confirmarReemplazoPortadaImage(id){
					$("#hIdEstudio").val(id);
	 	        	$('#UploadPortadaImage')[0].click();
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
					                    url: _ctx+'gestion/contenido-imagen/actualizar',
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
					                data.append("id", $("#hIdEstudio").val());
					                
					                $.ajax({
					                    type: 'PUT',
					                    url: _ctx+'gestion/estudio/imagen/resumen',
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
						                	exception(xhr["status"], xhr["responseJSON"]["error"]);
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
					                data.append("id", $("#hIdEstudio").val());
					                
					                $.ajax({
					                    type: 'PUT',
					                    url: _ctx+'gestion/estudio/imagen/portada',
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
						                	exception(xhr["status"], xhr["responseJSON"]["error"]);
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
				
				function subirOnlyFileReemplazo(f){
			         //submit the form here
			         
			         var file;
			         if ((file = f.files[0])) {
									
							        var data = new FormData();
							        
									data.append("file", file);
					                data.append("id", $("#hIdContenidoMedia").val());
					                data.append("peso", formatBytes(file.size));
					                if(alias.length > 0)
					                	data.append("alias", alias);
					                
					                $.ajax({
					                    type: 'PUT',
					                    url: _ctx+'gestion/contenido-archivo/actualizar',
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
							                        	content: "<i> El archivo se reemplazo satisfactoriamente...</i>",
							                        	iconSmall: "fa fa-refresh fa-2x"
							                        });
					                            }
					                            
					                        }
					                    },
						                error: function (xhr, ajaxOptions, thrownError) {
						                	exception(xhr["status"], xhr["responseJSON"]["error"]);
						                },
					                    complete: function (data) {
					                    	obtenerArchivosDirecto(parseInt(data.responseJSON));
							            	$('#UploadOnlyFile').val('');
							            	alias = "";
							            }
					                });
			         }
				}
				
				function reorganizarFileArrays(){
					var arrayOrdenFiles = [];
					var arrayOrdenImages = [];
					
					Array.from(modalFileSort.children).
						forEach((value, i) => {
							var obj = {nuevo: ++i, antiguo: parseInt(value.textContent.split('.')[0])};
							arrayOrdenFiles.push(obj);
						}
					);
					
					Array.from(modalImageSort.children).
						forEach((value, i) => {
							var obj = {nuevo: ++i, antiguo: parseInt(value.textContent.split('.')[0])};
							arrayOrdenImages.push(obj);
						}
					);
					
					//Archivos
					arrayArchivos.forEach((file,i) =>{
						arrayOrdenFiles.forEach((order,ii)=>{
							
							if((i+1)===order.antiguo){
								//Necesario para ejecutar el compare
								file.orden = order.nuevo;
								return false;
							}
						});
					});
					arrayArchivos.sort(compare);
					
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
				
				function addFiles() {
					$('#btnNuevosArchivos').button('loading');
					var aliasNames = [];
			        
			        var data = new FormData();
			        var id = $('#hIdEstudio').val();
			        			        
			        data.append("EstudioId", id);
			        
					for (var i = 0; i < arrayArchivos.length; ++i) {
						aliasNames = Array(arrayArchivos.length).fill(0);
						data.append("Documentos", arrayArchivos[i]);
						data.append("SizeList", arrayArchivos[i].sizeFormat);
                	}

					for (var i = 0; i < arrayImagenes.length; ++i) {
						data.append("Imagenes", arrayImagenes[i]);
                	}
					
					var alias = $('#AliasList').get(0);
					
					if(alias.innerHTML.length > 0){
						alias = $('#AliasList').get(0).querySelectorAll('input');
						Array.from(alias).forEach((input, i) => {
							aliasNames[i] = input.value;
						});
					}else{
						for(var i = 0; i < aliasNames.length; ++i){
							aliasNames[i] = "";
						}
					}
					//Guardando Alias para los archivos
					data.append("Alias", aliasNames);
					
			        $.ajax({
			            type: 'POST',
			            url: _ctx+'gestion/estudio/cargarArchivos/v2',
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
			                	$("#UploadFiles").val("");
			                	$.smallBox({content: "Se agregaron los registros satisfactoriamente..."});
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	if(xhr["responseJSON"] != null)
		                		exception(xhr["status"], xhr["responseJSON"]["error"]);
		                	else
		                		exception(xhr["status"], xhr["status"]);
			            },
			            complete: function (data) {
			            	if(aliasNames.length>0)
			            		obtenerArchivos(id);
			            	else
			            		obtenerImagenes(id);
			            	$('#btnNuevosArchivos').button('reset');
			            }
			        });
				}
				
				function eliminar(id, ix){
					$.smallBox({
						content : "¿Estás seguro de querer eliminar el registro? <p class='text-align-right'><a href='javascript:confirmarEliminacion("+id+","+ix+");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
						color : "#296191",
						timeout: 10000,
						icon : "fa fa-question swing animated",
						iconSmall: "",
					});
				}
				
				function confirmarEliminacion(id, ix){
					
					$.ajax({
			            type: 'GET',
			            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
			            url: _ctx+'gestion/estudio/verificar/relacion/'+id,
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
//			                		$.smallBox({content: "El registro se ha eliminado satisfactoriamente."});
			                		if(data == "1")
			                			$.smallBox({
			                				content : "<i> Este estudio esta relacionado con una o más capacitaciones. <br/>¿Desea proseguir y eliminarlo de todas formas?... <br/>** <strong>Las capacitaciones perderán la vinculación con el estudio. </strong></i><p class='text-align-left'><a href='javascript:preEliminacionAfterVerificario("+id+","+ix+","+data+");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
			                				color: "alert",
			                				iconSmall : "fa fa-warning fa-2x fadeInRight animated",
			                				timeout: 10000,
			                			}); 
			                		else if(data == "2")
			                			$.smallBox({
			                				content : "<i> Este estudio esta relacionado con una o más logros. <br/>¿Desea proseguir y eliminarlo de todas formas?... <br/>** <strong>Los logros perderán la vinculación con el estudio. </strong></i><p class='text-align-left'><a href='javascript:preEliminacionAfterVerificario("+id+","+ix+","+data+");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
			                				color: "alert",
			                				iconSmall : "fa fa-warning fa-2x fadeInRight animated",
			                				timeout: 10000,
			                			}); 
			                		else if(data == "3")
			                			$.smallBox({
			                				content : "<i> Este estudio esta relacionado con capacitaciones y logros. <br/>¿Desea proseguir y eliminarlo de todas formas?... <br/>** <strong>Los logros y capacitaciones perderán la vinculación con el estudio. </strong></i><p class='text-align-left'><a href='javascript:preEliminacionAfterVerificario("+id+","+ix+","+data+");' class='btn btn-primary btn-sm'>Si</a> <a href='javascript:void(0);' class='btn btn-danger btn-sm'>No</a></p>",
			                				color: "alert",
			                				iconSmall : "fa fa-warning fa-2x fadeInRight animated",
			                				timeout: 10000,
			                			}); 
			                		else
			                			confirmarEliminacionAfterVerificacion(id,ix);
			                	}
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            },
			            complete: function (data) {}
			        });
				}
				
				function preEliminacionAfterVerificario(id, ix, s){
					$.ajax({
			            type: 'PUT',
			            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
			            url: _ctx+'gestion/estudio/relacion/deshacer/'+id+'/'+s,
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
			                		confirmarEliminacionAfterVerificacion(id, ix);
			                	}
			                }
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	exception(xhr["status"], xhr["responseJSON"]["error"]);
			            },
			            complete: function (data) {}
			        });
				}
				
				function confirmarEliminacionAfterVerificacion(id, ix){
					
					$.ajax({
			            type: 'DELETE',
			            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
			            url: _ctx+'gestion/estudio/eliminar/'+id,
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
							Consultor: {
								required: true,
								maxlength: $("#Consultor").prop("maxlength"),
							},
				            BeneficiarioId:{
				                required: true,
				                digits: true,
							},
							ResponsableEntidad:{
								required: true,
								maxlength: $("#ResponsableEntidad").prop("maxlength"),
							},
							TipoEstudioId: {
								required: true,
								digits: true,
							},
							Anio:{
								required: true,
								min: 2000,
								max: function (){
									return new Date().getFullYear();
								},
							},
							Mes: {
								required: true,
								digits: true,
							}
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
				            Consultor: {
				            	required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            BeneficiarioId:{
				                required: "El campo es obligatorio",
				                digits: "Seleccione un beneficiario válido",
							},
							ResponsableEntidad: {
				            	required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
							},
				            TipoEstudioId: {
				            	required: "El campo es obligatorio",
				            	digits: "Seleccione un tipo de estudio válido",
				            },
							Anio:{
				            	required: "El año es obligatorio",
								min: $.validator.format("Este campo debe tener un valor mínimo de {0}."),
								max: $.validator.format("Este campo debe tener un valor máximo de {0}.")
							},
							Mes: {
				            	required: "El campo es obligatorio",
				            	digits: "Seleccione un periodo válido",
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