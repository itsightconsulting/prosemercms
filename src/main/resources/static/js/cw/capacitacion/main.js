			let arrayArchivos = [];
			let arrayImagenes = [];
			let rawTags = "";
			var $table = $('#tblEstudios');
			
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
					setFechaActual();
					
					$("#btnGuardar").click(function () {
				        registro();
				    });
					
					$("#btnRelacionar").click(function (e) {
						var options = $('#TipoEstudios').text();
						$.SmartMessageBox({
							title: "Alertas Prosemer",
							content: "Seleccione un tipo de estudio:",
							buttons: "[Seguir]",
							input: "select",
							options: options
						}, function (ButtonPress, Value) {
							preFiltroParaRelacionar(Value);
							$('#TipoEstudio').val(Value);
						});

						e.preventDefault();
					});
					
					function preFiltroParaRelacionar(tipoEstudio){
						var options = $('#Beneficiarios').text();
						$.SmartMessageBox({
							title: "Alertas Prosemer",
							content: "Ahora seleccione un beneficiario:",
							buttons: "[Continuar]",
							input: "select",
							options: options
						}, function (ButtonPress, Value) {
							$('#btnOcultoModal').click();
							$('#Beneficiario').val(Value);
							buscarPorTipoEstudio(tipoEstudio, Value, 0);
						});
						try{$('#Msg2').css('background-color','#41542b');}catch(e){}
					}
					
					$("#btnBuscaEstudio").click(function (e) {
						e.preventDefault();
						
						$estudio = $('#NombreEstudio').val();
						
						if($estudio.length > 2){
							buscarPorTipoEstudio($('#TipoEstudio').val(),$('#Beneficiario').val(), $estudio);
						}else{
							$.smallBox({
								content : "<i> Se necesita una valor mínimo de 3 carácteres para<br> <strong>realizar la búsqueda.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
						}
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
							params.tituloEstudioReferencia = $("#EstudioSeleccionado").val();
							params.descripcion = iFrameBody.innerHTML;
							var dt = $('#FechaCapacitacion').val().split("-");
							params.fechaCapacitacion = new Date(dt[0],Number(dt[1])-1,dt[2]);
							params.flagResumen = $('#ResumenYoN').is(':checked');
							if($('#ResumenYoN').is(':checked'))
								params.resumen = iFrameBody.textContent.substring(0,252).trim() + "...";
							params.tags = $('#Tags').val();
							if($('#hIdEstudio').val().length > 0){
								params.fkEstudio = $('#hIdEstudio').val();
							}
							if($('#hIdBeneficiario').val().length > 0){
								params.fkBeneficiario = $('#hIdBeneficiario').val();
							};
							
					        $.ajax({
					            type: 'POST',
					            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					            url: _ctx+'gestion/capacitacion/agregar',
					            dataType: "json",
					            data: params,
					            success: function (data, textStatus) {
					                if (textStatus == "success") {   
					                	if(data=="-9" && data =="-88"){
					                		$.smallBox({
												content : "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
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
					            complete: function (data) {}
					        });
				    	}else{
				    		$.smallBox({
								content : "<i> No se ha agregado <strong>la descripción de la capacitacion.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
				    	}
				    }
				}
				
				function subirArchivos(){
					
					 $("#ArchivosCapacitacion").change(function () {
						 arrayArchivos = [];
						 arrayImagenes = [];
						 arraySizes = [];
						 
						 let rawImages = "", rawFiles="", filesSize = 0;
						 
				         //Submit files
			             var archivos = $("#ArchivosCapacitacion").get(0);
				         
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
				             arrayArchivos = [];
				             arrayImagenes = [];
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
			        var imagenResumen = $("#ResumenImage").get(0);
			        
			        var data = new FormData();
			        
			        data.append("ContenidoWebId", contenidoWebId);
			        
			        if(imagenResumen.files[0] != null){
						data.append("fileImagenResumen", imagenResumen.files[0]);
			        }
			        
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
			            url: _ctx+'gestion/capacitacion/cargarArchivos',
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
				
				function buscarPorTipoEstudio(tipoEstudio, beneficiario, comodin){
						$table.bootstrapTable('destroy');
						$table.bootstrapTable({
							url: _ctx+'gestion/estudio/obtenerListado/top/' + tipoEstudio + '/' + beneficiario + '/' + comodin,
							pagination: true,
							sidePagination: 'client',
							pageSize: 8,
							onLoadSuccess: function(data){},
							onLoadError: function (xhr, ajaxOptions, thrownError) {
								exception(xhr["status"], xhr["responseJSON"]["error"]);
							},
						});
				}

				function linkFormatter(value, row, index) {
					return String('<a class="editable editable-click" href="#" onclick="javascript:elegirEstudio(' + row.id + ',' + row.beneficiario.id + ',\'' + row.tituloPrincipal + '\')" title="Seleccionar">' + row.tituloPrincipal + '</a>', value);
				}

				function elegirEstudio(estudioId, beneficiarioId, tituloPrincipal){
					$('#EstudioSeleccionado').val(tituloPrincipal);
					$('#myModalEstudio').modal('hide');
					$('#hIdEstudio').val(estudioId);
					$('#hIdBeneficiario').val(beneficiarioId);
					$.smallBox({
						content : "Ha seleccionado el estudio: <br/><strong>"+tituloPrincipal+"</strong>",
						color : "rgb(91, 175, 69)",
						timeout: 5000,
						icon : "fa fa-hand-o-up swing animated",
						iconSmall: "",
					});					
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
					window.location.href = _ctx+"gestion/capacitacion";
				}
				
				function setFechaActual(){
					const dateToString = d => `${d.getFullYear()}-${('00' + (d.getMonth() + 1)).slice(-2)}-${('00' + d.getDate()).slice(-2)}`; 
					$('#FechaCapacitacion').val(dateToString(new Date()));
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
			            	ResumenImage: {	
								required: function (){
									if($('#ResumenYoN').is(':checked')){
										return true;
									}
									return false;
								},
			            	},
							ArchivosCapacitacion: {
								required: false,
				            },
				            FechaCapacitacion:{
								required: true,
				            },
							hIdEstudio:{
								number: true,
								min: 1,
							},
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
				            ResumenImage:{
				            	required: "La subida de la imagen es obligatoria",
				            },
				            ArchivosCapacitacion:{
				            	required: "La subida de archivos es obligatoria",
				            },
				            FechaLogro: {
				                required: "El campo es obligatorio",
				                dateISO: "Seleccione una fecha válida",
				            },
							hIdEstudio:{
				                number: "No ha seleccionado un estudio válido",
								min: "No ha seleccionado un estudio válido"
							}
				        },
				        submitHandler: function () {
				            registro();
				        }
				    });
				}
				
				function getBodyCKEditor(){};
				