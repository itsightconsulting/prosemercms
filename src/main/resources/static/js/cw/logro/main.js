

			var pageNumber = 0;
			var pageSize = 0;
			var $btn = $("#btnGuardar");
			var $table = $('#tblEstudios');
			var checkedRows = [];
			var lstEstudios = [];
						
				$(function() {
					
					pageSetUp();
					//V A L I D A T E
					validarRegistros();
					setFechaActual();
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
					

					$("#btnRelacionar").click(function (e) {
						var options = $('#TipoEstudios').text();
						$.SmartMessageBox({
							title: "Alertas Prosemer",
							content: "Seleccione un tipo de estudio:",
							buttons: "[Continuar]",
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
							$('#hIdBeneficiario').val(Value);
							document.querySelector('.bs-checkbox input').style.display = 'none';
							document.querySelectorAll('.bs-checkbox')[0].textContent = "Relacionar";
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
								content : "<i> Se necesita una valor mayor o igual a 3 carácteres para<br> <strong>realizar la búsqueda.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
						}
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
					
					$('#tblEstudios').on('check.bs.table', function (e, row) {
						  checkedRows.push({id: row.id, titulo: row.tituloPrincipal});
						  console.log(checkedRows);
					});

					$('#tblEstudios').on('uncheck.bs.table', function (e, row) {
						$.each(checkedRows, function(index, value) {
							 if (value.id === row.id) {
								 checkedRows.splice(index,1);
							 }
						});
					});
					
					$('#myModalEstudio').on('hidden.bs.modal', function () {
						var rawInputs = "";
						$.each(checkedRows, function(index, value) {
							 lstEstudios.push(value.id);
							 rawInputs += `<li>${value.titulo}</li>`;
						});
						$('#EstudioSeleccionado').html(rawInputs);
						$('#hIdEstudio').val(lstEstudios.toString());
						$('#BeneficiarioId').val('');
						lstEstudios = [];checkedRows = [];
					});
					
				});
				
				function registro() {
				    if ($("#frm_registro").valid()) {
				    	iFrameBody = document.querySelectorAll('.cke_wysiwyg_frame')[0].contentDocument.querySelector('body');
				    	if(iFrameBody.textContent.length > 0){
					
					        var params = new Object();
							params.descripcion = iFrameBody.innerHTML;
							params.resumen = iFrameBody.textContent.substring(0,120).trim() +"...";
							var dt = $('#FechaLogro').val().split("-");
							params.fechaLogro = new Date(dt[0],Number(dt[1])-1,dt[2]);
							params.estudioId = $('#hIdEstudio').val()==''?'':$('#hIdEstudio').val();
							params.beneficiarioId = $('#hIdEstudio').val()==''?$('#BeneficiarioId').val():$('#hIdBeneficiario').val();
							
					        $.ajax({
					            type: 'POST',
					            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
					            url: _ctx+'gestion/logro/agregar',
					            dataType: "json",
					            data: params,
					            success: function (data, textStatus) {
					                if (textStatus == "success") {   
					                	if(data=="-9"){
					                		$.smallBox({
												content : "<i> Ha ocurrido un error interno en el sistema. Comunicarse con el administrador...</i>",
												timeout: 4500,
												color:	"alert",
											});
					                	}else{
					                		$.smallBox({});
					                	}
					                }
					            },
					            error: function (xhr, ajaxOptions, thrownError) {
					            	exception(xhr["status"], xhr["responseJSON"]["error"]);
					            },
					            complete: function (data) {
					            	window.location.href = _ctx+"gestion/logro"
					            }
					        });
				    	}else{
				    		$.smallBox({
								content : "<i> No se ha agregado <strong>la descripción del logro.</strong></i>",
								color: "#8a6d3b",
		            			iconSmall : "fa fa-warning fa-2x fadeInRight animated",
								timeout: 3500,
							});
				    	}
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

				function elegirEstudio(estudioId, beneficiarioId, tituloPrincipal){
					$('#EstudioSeleccionado').val(tituloPrincipal);
					$('#myModalEstudio').modal('hide');
					$('#hIdEstudio').val(estudioId);
					$.smallBox({
						content : "Ha seleccionado el estudio: <br/><strong>"+tituloPrincipal+"</strong>",
						color : "rgb(91, 175, 69)",
						timeout: 5000,
						icon : "fa fa-hand-o-up swing animated",
						iconSmall: "",
					});	
				}
				
				function setFechaActual(){
					const dateToString = d => `${d.getFullYear()}-${('00' + (d.getMonth() + 1)).slice(-2)}-${('00' + d.getDate()).slice(-2)}`; 
					$('#FechaLogro').val(dateToString(new Date()));
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
				            FechaLogro: {
								required: true,
							},
							hIdEstudio:{
								required: function(){
									if($('#hIdEstudio').val() == ''  && $('#BeneficiarioId').val() == ''){
										return true;
									}
									return false;
								},
								
							},
							BeneficiarioId:{
								required: function(){
									if($('#Beneficiario').val() != ''){
										return false;
									}
									return true;
								},
								number: true,
								min: 1,
							}
				        },
				        messages:{
				            FechaLogro: {
				                required: "El campo es obligatorio",
				                dateISO: "Seleccione una fecha válida",
				            },
							hIdEstudio:{
				                required: "Se requiere asociar el logro a un estudio",
							},BeneficiarioId:{
								required: "El campo es obligatorio en este caso",
								number: "No ha seleccionado un beneficiario válido",
								min: "No ha seleccionado un beneficiario válido"
							}
				        },
				        submitHandler: function () {
				            registro();
				        }
				    });
				}
				
				function getBodyCKEditor(){}
				