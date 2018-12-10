			let arrayArchivos = [];
			let arrayImagenes = [];
			let rawTags = "";
			
			let modalImageSort = $('#sortImagenes').get(0);
			let modalFileSort = $('#sortArchivos').get(0);
			
				$(function() {
					
					$( "#sortArchivos" ).sortable();
					$( "#sortArchivos" ).disableSelection();
					
					$( "#sortImagenes" ).sortable();
					$( "#sortImagenes" ).disableSelection();
					
					pageSetUp();
					subirArchivos();
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
					
					$("#btnGuardar").click(function () {
				        registro();
				    });
					
					$("#EditorCancel").click(function () {
				        xhrCs.abort();
					    $('#myModalEditor').modal('hide');
				    });
					
					$('#myModal').on('hidden.bs.modal', function () {
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
					
					//Compare for reorder arrays
					compare = function compare(a,b) {
						if (a.orden < b.orden)
							return -1;
						if (a.orden > b.orden)
							return 1;
						return 0;
						}
					});
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				    	iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				    	
				    	if(iFrameBody.textContent.length > 0){
					        var params = new Object();
							params.tituloPrincipal = $("#TituloPrincipal").val();
							params.tituloLargo = $("#TituloLargo").val();
							params.descripcion = iFrameBody.innerHTML;
							var month = parseInt($("#Mes").val())+1;
							month = month < 10 ? '0' + month : month;
							var fechaEvento = $("#Anio").val() + '/' + month +'/01';
							params.fechaEvento = fechaEvento;
							params.flagResumen = $('#ResumenYoN').is(':checked');
							if($('#ResumenYoN').is(':checked'))
								params.resumen = iFrameBody.textContent.substring(0,252).trim() + "...";
							params.tags = $('#Tags').val();
					                 
					        $.ajax({
					            type: 'POST',
					            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					            url: _ctx+'gestion/evento/agregar',
					            dataType: "json",
					            data: params,
					            success: function (data, textStatus) {
					                if (textStatus == "success") {   
					                	if(data=="-9"){
					                		$.smallBox({
												content: "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
												timeout: 4500,
												color:	"alert",
											});
					                	}else{
					                		$.smallBox({});
					                		//Data = contenidoWebId
					                		addFiles(data, params.tags);
					                	}
					                }
					            },
					            error: function (xhr, ajaxOptions, thrownError) {
					            	exception(xhr["status"], xhr["responseJSON"]["error"]);
					            },
					            complete: function (data) {
					            }
					        });
				    	}else{
				    		$.smallBox({
								content : "<i> No se ha agregado <strong>una descripción del evento.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
				    	}
				        
				    }
				}
				
				function readURL(input) {
					  if (input.files && input.files[0]) {
					    var reader = new FileReader();

					    reader.onload = function(e) {
					      $('#ImagenPortada').attr('src', e.target.result);
					    }
					    reader.readAsDataURL(input.files[0]);
					  }
				}

				function subirArchivos(){
					
					 $("#ArchivosEvento").change(function(){
						 arrayArchivos = [];
						 arrayImagenes = [];
						 arraySizes = [];
						 
						 let rawImages = "", rawFiles="", filesSize = 0;
						 
				         //Submit files
			             var archivos = $("#ArchivosEvento").get(0);
				         
				         if ((file = this.files[0])) {
						        						        
						     archivos = archivos.files;
						     
						     //Cantidad de archivos
						     $('#FilesAmount').text(archivos.length);
						        
							//Dividiendo los archivos segun su tipo en 2 array: | images | files
						     if(archivos != null){
						    	 var nuevoOrden = [], iFile = 0,iImage = 0; 
						        	Array.from(archivos).forEach((file, i) => {
						        		if(file.type.match('application') || file.type == ''){
							            	file.sizeFormat = formatBytes(file.size);
							            	arrayArchivos.push(file);
							            	rawFiles += `<li class="dd-item"><span class="label label-default font-sm"><span class="label label-warning font-sm">${++iFile}.</span> ${file.name}</span></li>`;
							            	filesSize+=file.size;
							            }else{//type : Image
							            	arrayImagenes.push(file);
							            	rawImages += `<li class="dd-item"><span class="label label-success font-sm"><span class="label label-warning font-sm">${++iImage}.</span> ${file.name}</span></li>`;
							            	filesSize+=file.size;
							            }
							            
							        });		
						        	modalFileSort.innerHTML = rawFiles;
						        	modalImageSort.innerHTML = rawImages;
						     }
						     //Asignando el tamaño de los archivos
						     $('#FileSize').text(formatBytes(filesSize));
						     
				         }else{
				             modalFileSort.innerHTML = '';
				             modalImageSort.innerHTML = '';
				         }
				 	});
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
				
				function addFiles(contenidoWebId, tags) {
					var aliasNames = [];
			        var data = new FormData();
			        			        
			        data.append("ContenidoWebId", contenidoWebId);
			        
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
			            url: _ctx+'gestion/evento/cargarArchivos',
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
			                	$('#frm_registro')[0].reset();
			                	const labels = document.querySelectorAll('.state-success');
			                	labels.forEach((v,i) => v.classList.remove('state-success'));
			                	$('#ImagenPortada').attr('src', _ctx+'img/upload-empty.png');
			                }
			                
			                registrandoNuevasTags(tags);
			            },
			            error: function (xhr, ajaxOptions, thrownError) {
			            	if(xhr["responseJSON"] != null)
		                		exception(xhr["status"], xhr["responseJSON"]["error"]);
		                	else
		                		exception(xhr["status"], xhr["status"]);
			            },
			            complete: function (data) {}
			        });
			        
				}
				
				function ingresarAlias(){
					let basicNames = modalFileSort.children;
					let rawBasics = '';
					let rawInputs = '';
					if(basicNames.length > 0){
						rawBasics = modalFileSort.innerHTML;
						Array.from(basicNames).forEach((value, i) => {
							rawInputs += `<input type='text' placeholder='Ingresa el alias'></input>`;
						});
						var $listFile = $('#FileList').get(0);
						var $AliasList = $('#AliasList').get(0);
						$listFile.innerHTML = rawBasics;
						$AliasList.innerHTML = rawInputs;
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
								params.tipo = localStorage.getItem("idioma") == "es"? 1:2;//1: ES || 2: US
	
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
						window.location.href = _ctx+"gestion/evento";			
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
				        	Tags:{
				        		required: true,
				        		maxlength: 100,
				        	},
				            TituloPrincipal: {
								required: true,
								maxlength: $("#TituloPrincipal").prop("maxlength"),
							},
							TituloLargo: {
								required: true,
								maxlength: $("#TituloLargo").prop("maxlength"),
							},
			            	ArchivosEvento: {
								required: false,
							},
							Anio:{
								required: true,
								min: 2000,
								max: function (){
									return (new Date().getFullYear())+1;
								},
							},
							Mes: {
								required: true,
								digits: true,
							}
				        },
				        messages:{
				        	Tags: {
				                required: "Ingrese por lo menos un tag",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            TituloPrincipal: {
				                required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            TituloLargo: {
				                required: "El campo es obligatorio",
				                maxlength: $.validator.format("Este campo debe de tener como máximo {0} caracteres.")
				            },
				            ArchivosEvento:{
				            	required: "La subida de archivos es obligatoria",
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
				
				function getBodyCKEditor(){};