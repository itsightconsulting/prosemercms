var errorClass = 'invalid';
var errorElement = 'em';
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var _ctx = $("meta[name='_ctx']").attr("content");
var lang = '';
var xhrCs = "";
$('#view_register').attr('hidden','hidden');
//Uploader live
//var bar = $('.bar');
//var percent = $('.percent');
//var status = $('#status');

function identificandoIdioma(){
	lang = localStorage.getItem("idioma");
	
	if(lang == null || lang == undefined)
		lang = '';
	else if(lang == 'es')
		lang = '';
	else if(lang == 'us')
		lang = 'en/';
}
identificandoIdioma();

function limpiarFiltros() {
    $("#accordion input").val("");
    $("#accordion select").val(0);
    $("#accordion select").val("");
    $("#accordion #pddlEstado").val("");
}

function detail_show() {
        $(".content-modal").show().removeClass('slideOutLeft animated').addClass('fadeInLeftBig' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {

        });
}

function detail_hide() {
        $(".content-modal").removeClass('fadeInLeftBig animated').addClass('slideOutLeft' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
            $(".content-modal").hide();
        });
}

function irRegistro() {
        $('#view_list').removeClass().addClass('fadeOutRightBig' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){ 
            $('#view_list').hide();

            $("#view_register").show().removeClass().addClass('fadeInLeftBig' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
            });
        }); 
}

function irListado(x) {
	
		x[0]["defaultValue"] = 0;
        $('#view_register').removeClass().addClass('fadeOutRightBig' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){ 
            $('#view_register').hide();
            $("#view_list").show().removeClass().addClass('fadeInLeft' + ' animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
            });
        }); 
}

function exception(xhrStatus, xhrStatusText){
    xhrStatusText = xhrStatusText == "" || xhrStatusText == undefined ? "error" : xhrStatusText;
	$.smallBox({
		content : "<i> Comuníquese con el administrador <br/> Code error: "+xhrStatus+" <br/> Detail: "+xhrStatusText+"</i>",
		color: "#8a6d3b",
		iconSmall : "fa fa-warning fa-2x fadeInRight animated",
		timeout: 5000,
	}); 
}

$(document).ajaxSend(function(e, xhr, options) {
	xhr.setRequestHeader(header, token);
	if((options.type === 'POST' || options.type === 'PUT') && options.processData == false){
		spinnerUpload(xhr);
	}else if(options.type === 'POST' && options.url.includes("/agregar")){
		$("#btnGuardar").attr('disabled','disabled');
		$("#btnGuardar").text('Cargando... Por favor espere...');
	}
});

$(document).ajaxComplete(function (e, xhr, options){
	if((options.type === 'POST' || options.type === 'PUT') && options.processData == false){
		$('#bot1-Msg1').click();
	}else if(options.type === 'POST' && options.url.includes("/agregar")){
		$('#btnGuardar').removeAttr('disabled');
		$('#btnGuardar').text('Guardar');
	}
});

function spinnerUpload(xhr){
	$.SmartMessageBox({
		title : "<i class='fa fa-bullhorn'></i> Notificaciones Prosemer",
		content : "" +
				"<br/><i>La acción solicitada ha iniciado. Por favor espere...</i><br/>" +
				"<div class='progress' style='width:100%;'><div id='ProgressUpload' class='progress-bar bg-color-teal' data-transitiongoal='0'>0%</div></div>" +
				"<div class='row'><div class='row text-center'><i class='fa fa-spinner fa-spin fa-3x text-center'></i></div>",
		buttons : '[Cancelar]'
	}, function(ButtonPressed) {
		if (ButtonPressed === "Cancelar") {
			xhr.abort();
		}
	})
}

function progress(e){

    if(e.lengthComputable){
        var max = e.total;
        var current = e.loaded;

        var Percentage = (current * 100)/max;
        percentageParse = Percentage.toFixed(0);
		$('#ProgressUpload').css('width', percentageParse + '%');
		$('#ProgressUpload').text(percentageParse + '%');
        if(Percentage >= 100)
        {
           // process completed  
        }
    }  
}

$(document).ajaxStart(function () {
})

$(document).ajaxStop(function () {
});

function formatBytes(a,b){if(0==a)return"0 Bytes";var c=1024,d=b||2,e=["Bytes","KB","MB","GB","TB","PB","EB","ZB","YB"],f=Math.floor(Math.log(a)/Math.log(c));return parseFloat((a/Math.pow(c,f)).toFixed(d))+" "+e[f]}

function cambiarIdioma(){

	$.ajax({
        type: 'GET',
        url: _ctx+'session/idioma',
        processData: false,
        contentType: false,
        success: function (data, textStatus) {
            if (textStatus == "success") {
            	localStorage.setItem("idioma", data);
            	try {
                	verficiarLocalStorage();
				} catch (e) {
					// TODO: handle exception
					console.log(e);
				}
            	$.smallBox({
            		content: "Algunas configuraciones se estan realizando,<br/> espere por favor...",
            		color: "#3276b1",
        			iconSmall : "fa fa-bullhorn fa-2x fadeInRight animated",
        			timeout: 3000,
            	});
            	
            	setTimeout(() => {
                	$.smallBox({content: "Usted ha elegido el idioma: "+ data.toUpperCase()});
				}, 3000);
            	
            	setTimeout(() => {
                	window.location.reload();
				}, 4500);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
        	exception(xhr["status"], xhr["responseJSON"]["error"]);
        },
        complete: function (data) {
        }
    });
	
}

function editor(){
	var editor = CKEDITOR.replace( 'ckeditor', { height: '380px', startupFocus : false, on: {'instanceReady': getBodyCKEditor}} );	
	try {
	editor.addCommand("insertFileCmd", {
        exec: function(edt) {
        	var modalArchivoLocal = document.createElement('div');
            $('#myModalEditor').modal('show');
        }
    });
	
	editor.addCommand("insertImgCmd", {
        exec: function(edt) {
        	
            $('#myModalEditorI').modal('show');
        }
    });
    editor.ui.addButton('InsertCustomFile', {
        label: "Obtener url de archivo del servidor",
        command: 'insertFileCmd',
        toolbar: 'insert',
        icon: 'img/ckeditor_folder.png'
    });
    editor.ui.addButton('InsertCustomImage', {
        label: "Obtener url de imagen del servidor",
        command: 'insertImgCmd',
        toolbar: 'insert',
        icon: 'img/ckeditor_images.png'
    });

	} catch (e) {
		// TODO: handle exception
		console.log(e);
	}
}

function cargarFile(f){
    //submit the form here
    
    var file;
    if ((file = f.files[0])) {
			        var data = new FormData();
			        
					data.append("file", file);
	                data.append("peso", formatBytes(file.size));
	                data.append("alias", file.name.split(".")[0]);
	                
	                xhrCs = $.ajax({
	                    type: 'POST',
	                    url: _ctx+'gestion/archivo/cargar',
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
	                            if(data == "-99" || data == "-9" || data == "-1"){
	                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
	                            } else{
	                            	$.smallBox({
			                        	content: "<i> El archivo ha sido subido satisfactoriamente...</i>",
			                        	iconSmall: "fa fa-hand-o-up fa-2x"
			                        });
	                            	//REF
		                        	$('#CKFileRoute').val(_ctx+'media/file/gt/0'+data);
	                            }
	                        }
	                    },
		                error: function (xhr) {
		                	if(xhr["responseJSON"] != null)
		                		exception(xhr["status"], xhr["responseJSON"]["error"]);
		                	else
		                		exception(xhr["status"], xhr["status"]);
		                },
	                    complete: function (data) {}
	                });
    }
}

	function cargarImagen(f){
		//submit the form here
         var _URL = window.URL || window.webkitURL;
         var file, img;
         if ((file = f.files[0])) {
             img = new Image();
             img.onload = function() {
				        var genericFile = $("#CKFileI").get(0).files[0];
						
				        var data = new FormData();
				        
						data.append("file", genericFile);
		                
		                $.ajax({
		                    type: 'POST',
		                    url: _ctx+'gestion/imagen/cargar',
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
		                            if(data == "-99" || data == "-9" || data == "-1"){
		                            	bootbox.alert("El archivo no ha podido ser guardado debido. Para mayor información comunicarse con el administrador.");
		                            } else{
		                            	$.smallBox({
				                        	content: "<i> La imagen ha sido subida satisfactoriamente...</i>",
				                        	iconSmall: "fa fa-hand-o-up fa-2x"
				                        });
		                            	//REF
			                        	$('#CKFileRouteI').val(_ctx+'media/image/libre'+data);
		                            }
		                        }
		                    },
			                error: function (xhr, ajaxOptions, thrownError) {
			                	if(xhr["responseJSON"] != null)
			                		exception(xhr["status"], xhr["responseJSON"]["error"]);
			                	else
			                		exception(xhr["status"], xhr["status"]);
			                },
		                    complete: function (data) {}
		                });
             };
             img.onerror = function() {
                 bootbox.alert( "No ha seleccionado una imagen válida: <strong>" + file.type + "");
             };
             img.src = _URL.createObjectURL(file);
         }
	}

$('#myModalEditor').on('hidden.bs.modal', function () {
	$('#CKFileRoute').val('');$('#CKFile').val('')
})

$('#myModalEditorI').on('hidden.bs.modal', function () {
	$('#CKFileRouteI').val('');$('#CKFileI').val('')
})

function cargarModalsParaCK(){
	var modalImagenServer = document.createElement('div');
	modalImagenServer.innerHTML = `<div class="modal fade" id="myModalEditorI" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h5 class="modal-title" id="myModalLabel"><strong>Subir Imagen</strong></h5>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-sm-12 col-md-12 col-lg-12 form-group" >
								<div class="form-group">
									<input id="CKFileI" name="CKFileI" class="btn btn-warning btn-sm" type="file" accept=".jpg,.jpeg,.png"/>
								</div>
								<div class="">
									<div class="input-group input-group-sm">
								        <input id="CKFileRouteI" type="text" placeholder="Enlace" class="form-control"/>
								        <div class="input-group-btn">
											<button id="CKFileCopyI" class="btn btn-default" type="button"><strong><i class="fa fa-clipboard text-warning"></i></strong></button>
										</div>
								    </div>										                
								</div>
							</div>
							<div class="text-align-center">
							</div>
						</div>
					</div>
				</div><!-- /.modal-content -->
			</div><!-- /.modal-dialog -->
		</div>`;
	document.body.append(modalImagenServer);
	var modalFileServer = document.createElement('div');
	modalFileServer.innerHTML = `<div class="modal fade" id="myModalEditor" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-sm">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
									&times;
								</button>
								<h5 class="modal-title" id="myModalLabel"><strong>Subir Archivo</strong></h5>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="col-sm-12 col-md-12 col-lg-12 form-group" >
										<div class="form-group">
											<input id="CKFile" name="CKFile" class="btn btn-warning btn-sm" type="file" accept=".pdf,.docx,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.zip,.rar"/>
										</div>
										<div class="">
											<div class="input-group input-group-sm">
										        <input id="CKFileRoute" type="text" placeholder="Enlace" class="form-control"/>
										        <div class="input-group-btn">
													<button id="CKFileCopy" class="btn btn-default" type="button"><strong><i class="fa fa-clipboard text-warning"></i></strong></button>
												</div>
										    </div>										                
										</div>
									</div>
									<div class="text-align-center">
									</div>
								</div>
							</div>
						</div><!-- /.modal-content -->
					</div><!-- /.modal-dialog -->
				</div>`;
	document.body.append(modalFileServer);
}
